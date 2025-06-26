package com.ac.common.exchange2;

public class CircularBuffer {
    public static final int BUFFER_SIZE_1 = 4;
    public static final int BUFFER_SIZE_2 = 8;
    public final OrderEvent[][] buffer;
    public int lvl1MinSeq = Integer.MAX_VALUE;
    public int lvl1MaxSeq = Integer.MIN_VALUE;
    public final int[] lvl2MinSeqs = new int[BUFFER_SIZE_1];
    public final int[] lvl2MaxSeqs = new int[BUFFER_SIZE_1];
    public final boolean[] occupied = new boolean[BUFFER_SIZE_1];

    public CircularBuffer() {
        buffer = new OrderEvent[BUFFER_SIZE_1][];
        for (int i = 0; i < BUFFER_SIZE_1; i++) {
            buffer[i] = new OrderEvent[BUFFER_SIZE_2];
            for (int j = 0; j < BUFFER_SIZE_2; j++) {
                buffer[i][j] = new OrderEvent(); 
            }
        } 

        for (int i = 0; i < BUFFER_SIZE_1; i++) {
            lvl2MinSeqs[i] = Integer.MAX_VALUE;
            lvl2MaxSeqs[i] = Integer.MIN_VALUE;
            occupied[i] = false;
        }
    }

    public void add(int seq, int price, int volume, char[] userId, char[] instrumentId, Side side) { 
        if (lvl1MinSeq == Integer.MAX_VALUE || lvl1MaxSeq == Integer.MIN_VALUE) {
            lvl1MinSeq = seq;
            lvl1MaxSeq = seq;
            addLevel2(seq, price, volume, userId, instrumentId, side);
            return;
        }

        if (seq < lvl1MinSeq) {
            if (lvl1MaxSeq - seq >= BUFFER_SIZE_1) {
                throw new IllegalArgumentException("Seq too small: " + seq);
            }
            lvl1MinSeq = seq;
            addLevel2(seq, price, volume, userId, instrumentId, side);
            return;
        }

        if (seq > lvl1MaxSeq) {
            if (seq - lvl1MinSeq >= BUFFER_SIZE_1) {
                throw new IllegalArgumentException("Seq too big: " + seq);
            }
            lvl1MaxSeq = seq;
            addLevel2(seq, price, volume, userId, instrumentId, side);
            return;
        }

        addLevel2(seq, price, volume, userId, instrumentId, side);
    }
    
    private void addLevel2(int seq, int price, int volume, char[] userId, char[] instrumentId, Side side) {
        int lvl1Idx = seq % BUFFER_SIZE_1;
        int lvl2MinSeq = lvl2MinSeqs[lvl1Idx];
        int lvl2MaxSeq = lvl2MaxSeqs[lvl1Idx];
        
        if (lvl2MinSeq == Integer.MAX_VALUE || lvl2MaxSeq == Integer.MIN_VALUE) {
            occupied[seq % BUFFER_SIZE_1] = true;
            lvl2MinSeqs[lvl1Idx] = 0;
            lvl2MaxSeqs[lvl1Idx] = 0;
            buffer[lvl1Idx][0].set(price, volume, userId, instrumentId, side);
            return;
        }
        
        if (lvl2MaxSeq - lvl2MinSeq + 1 >= BUFFER_SIZE_2) {
            throw new IllegalArgumentException("Level 2 buffer full for seq: " + seq);
        }
            
        occupied[seq % BUFFER_SIZE_1] = true;
        buffer[lvl1Idx][lvl2MaxSeq++ % BUFFER_SIZE_2].set(price, volume, userId, instrumentId, side);
    }

    public void removeLevel1(int seq) {
        if (lvl1MinSeq == Integer.MAX_VALUE || lvl1MaxSeq == Integer.MIN_VALUE) {
            throw new IllegalStateException("Seq not found: " + seq);
        }

        if (seq < lvl1MinSeq || seq > lvl1MaxSeq) {
            throw new IllegalArgumentException("Seq out of range: " + seq);
        }

        if (seq == lvl1MinSeq && seq == lvl1MaxSeq) {
            occupied[seq % BUFFER_SIZE_1] = false;
            lvl2MaxSeqs[seq % BUFFER_SIZE_1] = Integer.MIN_VALUE;
            lvl2MinSeqs[seq % BUFFER_SIZE_1] = Integer.MAX_VALUE;
            lvl1MinSeq = Integer.MAX_VALUE;
            lvl1MaxSeq = Integer.MIN_VALUE;
            return;
        } 
        
        if (seq == lvl1MinSeq) {
            occupied[seq % BUFFER_SIZE_1] = false;
            for (int i = lvl1MinSeq + 1; i <= lvl1MaxSeq; i++) {
                if (occupied[i % BUFFER_SIZE_1]) {
                    lvl1MinSeq = i;
                    break;
                }
            }
            return;
        } 
        
        if (seq == lvl1MaxSeq) {
            occupied[seq % BUFFER_SIZE_1] = false;
            for (int i = lvl1MaxSeq - 1; i >= lvl1MinSeq; i--) {
                if (occupied[i % BUFFER_SIZE_1]) {
                    lvl1MaxSeq = i;
                    break;
                }
            }
            return;
        }

        occupied[seq % BUFFER_SIZE_1] = false;
        lvl2MaxSeqs[seq % BUFFER_SIZE_1] = Integer.MIN_VALUE;
        lvl2MinSeqs[seq % BUFFER_SIZE_1] = Integer.MAX_VALUE;
    }
}
