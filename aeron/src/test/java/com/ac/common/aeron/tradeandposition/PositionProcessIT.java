package com.ac.common.aeron.tradeandposition;

import com.ac.common.aeron.Utils;
import io.aeron.archive.Archive;
import io.aeron.archive.ArchivingMediaDriver;
import io.aeron.driver.MediaDriver;
import org.agrona.CloseHelper;
import org.junit.jupiter.api.Test;

public class PositionProcessIT {
    public static final String REPLICATION_CHANNEL = "aeron:udp?endpoint=localhost:0";
    public static final String CONTROL_REQUEST_CHANNEL = "aeron:udp?endpoint=localhost:8010";
    private ArchivingMediaDriver mediaDriver;

    @Test
    public void test() {
        // set up
        setup();

        PositionProcessApplication app = new PositionProcessApplication();
        app.setup();

        TradeTestPublisher tradeTestPublisher = new TradeTestPublisher();

        // read and write
        tradeTestPublisher.write();
        app.read();

        // clean up
        tradeTestPublisher.cleanUp();
        app.cleanUp();
        cleanUp();
    }

    public void setup() {
        mediaDriver = ArchivingMediaDriver.launch(
            new MediaDriver.Context()
                .spiesSimulateConnection(true)
                .dirDeleteOnStart(true),
            new Archive.Context()
                .deleteArchiveOnStart(true)
                .controlChannel(CONTROL_REQUEST_CHANNEL)
                .replicationChannel(REPLICATION_CHANNEL)
                .archiveDir(Utils.createTempDir())
        );
    }

    private void cleanUp() {
        CloseHelper.quietClose(mediaDriver);
    }
}
