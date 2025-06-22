package com.ac.common.exchange2;

import com.lmax.disruptor.EventFactory;

public class InstrumentEvent {
    char[] instrumentId = new char[16];
    char[] instrumentDetail = new char[32];

    public void set(String instrumentId, String instrumentDetail) {
        System.arraycopy(instrumentId.toCharArray(), 0, this.instrumentId, 0, Math.min(instrumentId.length(), 16));
        System.arraycopy(instrumentDetail.toCharArray(), 0, this.instrumentDetail, 0, Math.min(instrumentDetail.length(), 32));
    }

    static class InstrumentEventFactory implements EventFactory<InstrumentEvent> {
        @Override
        public InstrumentEvent newInstance() {
            return new InstrumentEvent();
        }
    }
}
