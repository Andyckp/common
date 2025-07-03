package com.ac.derivativepricer.adaptor;

import static java.util.Objects.requireNonNull;

import static org.agrona.CloseHelper.quietClose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ac.derivativepricer.process.Util;

import io.aeron.Aeron;
import io.aeron.archive.Archive;
import io.aeron.archive.ArchivingMediaDriver;
import io.aeron.archive.client.AeronArchive;
import io.aeron.driver.MediaDriver;

public class AeronManager {

    private static final Logger logger = LoggerFactory.getLogger(AeronManager.class);

    // configuration
    public static final String CONTROL_REQUEST_CHANNEL_URI = "aeron:udp?endpoint=localhost:8012";
    public static final String CONTROL_RESPONSE_CHANNEL_URI = "aeron:udp?endpoint=localhost:0";
    public static final String REPLICATION_CHANNEL = "aeron:udp?endpoint=localhost:0";
    public static final String CHANNEL_URI = "aeron:udp?endpoint=localhost:9010";
    public static final String CHANNEL_URI_APP = "aeron:udp?endpoint=localhost:9020";
    // public static final String CHANNEL_URI = "aeron:ipc";
    // public static final String CHANNEL_URI = "aeron:udp?control-mode=manual"; // for multi-destination

    public static final int STREAM_STRATEGY_CAPTURE = 111;
    public static final int STREAM_STRATEGY_REPLAY = 112;
    public static final int STREAM_INSTRUMENT_CAPTURE = 121;
    public static final int STREAM_INSTRUMENT_REPLAY = 122;
    public static final int STREAM_VOLATILITY_CAPTURE = 131;
    public static final int STREAM_VOLATILITY_REPLAY = 132;
    public static final int STREAM_MARKET_DATA_CAPTURE = 141;
    public static final int STREAM_MARKET_DATA_REPLAY = 142;
    public static final int STREAM_PRIMARY_GREEK_CAPTURE = 211;
    public static final int STREAM_PRIMARY_GREEK_REPLAY = 212;
    public static final int STREAM_SECONDARY_GREEK_CAPTURE = 221;
    public static final int STREAM_SECONDARY_GREEK_REPLAY = 222;
    public static final int STREAM_ADJUSTED_GREEK_CAPTURE = 231;
    public static final int STREAM_ADJUSTED_GREEK_REPLAY = 232;
    
    private AeronArchive aeronArchive;
    private Aeron aeron;
    private ArchivingMediaDriver mediaDriver;

    public void start() {
        mediaDriver = ArchivingMediaDriver.launch(
            new MediaDriver.Context()
                .spiesSimulateConnection(true)
                .dirDeleteOnStart(true),
            new Archive.Context()
                .deleteArchiveOnStart(true)
                .controlChannel(CONTROL_REQUEST_CHANNEL_URI)
                .replicationChannel(REPLICATION_CHANNEL)
                .archiveDir(Util.createTempDir())
        );
        this.aeron = Aeron.connect();
        this.aeronArchive = AeronArchive.connect(new AeronArchive.Context()
                .controlRequestChannel(CONTROL_REQUEST_CHANNEL_URI)
                .controlResponseChannel(CONTROL_RESPONSE_CHANNEL_URI)
                .aeron(aeron));
    }

    public void stop() {
        quietClose(aeronArchive);
        quietClose(aeron);
        quietClose(mediaDriver);
    }

    public AeronArchive getAeronArchive() {
        requireNonNull(aeronArchive, "not started yet");
        return aeronArchive;
    }
    
    public Aeron getAeron() {
        requireNonNull(aeronArchive, "not started yet");
        return aeron;
    }
}
