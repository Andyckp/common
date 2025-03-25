package com.ac.common.aeron;

import io.aeron.Aeron;
import io.aeron.Publication;
import io.aeron.archive.Archive;
import io.aeron.archive.ArchivingMediaDriver;
import io.aeron.archive.client.AeronArchive;
import io.aeron.archive.codecs.SourceLocation;
import io.aeron.driver.MediaDriver;

import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.common.sbe.MessageHeaderEncoder;
import com.ac.common.sbe.TradeEncoder;

import java.io.File;
import java.nio.ByteBuffer;

public class TradePublisher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TradePublisher.class);
    private static final String CHANNEL = "aeron:udp?endpoint=localhost:40123";
    private static final int STREAM_ID = 1001;
    private static final int MESSAGE_INTERVAL_MS = 200;

    @Override
    public void run() {
        // Setup Aeron context and archive
        try (
            ArchivingMediaDriver mediaDriver = ArchivingMediaDriver.launch(
                new MediaDriver.Context()
                    .spiesSimulateConnection(true)
                    .dirDeleteOnStart(true),
                new Archive.Context()
                    .deleteArchiveOnStart(true)
                    .archiveDir(new File("./archive"))
            );
            Aeron aeron = Aeron.connect();
            AeronArchive aeronArchive = AeronArchive.connect(new AeronArchive.Context().aeron(aeron));
            
            ) {

            Publication publication = aeron.addPublication(CHANNEL, STREAM_ID);
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            UnsafeBuffer unsafeBuffer = new UnsafeBuffer(buffer);

            TradeEncoder tradeEncoder = new TradeEncoder();
            MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();

            long tradeId = 0;
            while (true) {
                // Encode SBE header
                headerEncoder.wrap(unsafeBuffer, 0)
                             .blockLength(tradeEncoder.sbeBlockLength())
                             .templateId(tradeEncoder.sbeTemplateId())
                             .schemaId(tradeEncoder.sbeSchemaId())
                             .version(tradeEncoder.sbeSchemaVersion());

                // Encode trade data
                tradeEncoder.wrap(unsafeBuffer, MessageHeaderEncoder.ENCODED_LENGTH)
                            .tradeId("TRADE-" + tradeId)              // Example trade ID
                            .instrumentId("INSTR-" + tradeId)         // Example instrument ID
                            .marketId(12345)                          // Example market ID
                            .portfolioId("PORT-" + tradeId)           // Example portfolio ID
                            // .side('B')                                // Example side (B for Buy, S for Sell)
                            // .price(1002500L + tradeId)                // Example price (scaled to avoid floating-point)
                            // .quantity(1000L + tradeId)                // Example quantity
                            .createTs(System.currentTimeMillis())     // Example timestamp
                            .isDelete((byte) 0);                      // Example delete flag (0 for false)

                // Calculate message length (header + trade)
                int messageLength = MessageHeaderEncoder.ENCODED_LENGTH + tradeEncoder.encodedLength();

                // Publish to Aeron
                long result = publication.offer(unsafeBuffer, 0, messageLength);
                if (result < 0) {
                    logger.error("Failed to publish trade: {}", result);
                } else {
                    logger.info("Published trade ID: {}", tradeId);
                }

                // Start recording to Aeron Archive (if not already started)
                aeronArchive.startRecording(CHANNEL, STREAM_ID, SourceLocation.LOCAL);

                // Sleep for the specified interval
                Thread.sleep(MESSAGE_INTERVAL_MS);

                tradeId++;
            }
        } catch (Exception e) {
            logger.error("An error occurred during publishing", e);
        }
    }

    public void test(String[] args) {
        TradePublisher tradePublisher = new TradePublisher();
        Thread thread = new Thread(tradePublisher);
        thread.start();
    }
}
