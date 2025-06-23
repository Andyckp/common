package com.ac.common.exchange2;

import com.lmax.disruptor.EventFactory;

public class InstrumentEvent {
    private final char[] instrumentId = new char[16];
    private final char[] instrumentDetail = new char[32];

    public void set(char[] instrumentId, char[] instrumentDetail) {
        System.arraycopy(instrumentId, 0, this.instrumentId, 0, Math.min(instrumentId.length, 16));
        System.arraycopy(instrumentDetail, 0, this.instrumentDetail, 0, Math.min(instrumentDetail.length, 32));
    }

    public char[] getInstrumentId() {
        return instrumentId;
    }

    public char[] getInstrumentDetail() {
        return instrumentDetail;
    }

    static class InstrumentEventFactory implements EventFactory<InstrumentEvent> {
        @Override
        public InstrumentEvent newInstance() {
            return new InstrumentEvent();
        }
    }
}
