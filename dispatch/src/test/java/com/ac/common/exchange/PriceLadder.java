package com.ac.common.exchange;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class PriceLadder {
    private final int capacity;
    private final int baseTick;
    private final Deque<Order>[] ring;
    private int bestIdx = -1;
    private int worstIdx = -1;

    @SuppressWarnings("unchecked")
    public PriceLadder(int baseTick, int capacity) {
        this.capacity = capacity;
        this.baseTick = baseTick;
        this.ring = new ArrayDeque[capacity];
        for (int i = 0; i < capacity; i++) {
            ring[i] = new ArrayDeque<>();
        }
    }

    private int toIdx(int priceTick) {
        return Math.floorMod(priceTick - baseTick, capacity);
    }

    public void addOrder(Order o) {
        int idx = toIdx(o.priceTick);
        ring[idx].add(o);
        if (bestIdx == -1 || idx < bestIdx) bestIdx = idx;
        if (worstIdx == -1 || idx > worstIdx) worstIdx = idx;
    }

    public Deque<Order> getLevel(int priceTick) {
        return ring[toIdx(priceTick)];
    }

    public boolean hasOrders(int idx) {
        return !ring[idx].isEmpty();
    }

    public void updateBest() {
        for (int i = bestIdx + 1; i <= worstIdx; i++) {
            if (hasOrders(i)) {
                bestIdx = i;
                return;
            }
        }
        bestIdx = -1;
        worstIdx = -1;
    }

    public int getBestTick() {
        return bestIdx == -1 ? -1 : (baseTick + bestIdx) % capacity;
    }

    public Iterable<Integer> iterTicks(boolean reverse) {
        List<Integer> ticks = new ArrayList<>();
        if (bestIdx == -1 || worstIdx == -1) return ticks; // Nothing to iterate

        for (int i = bestIdx; i <= worstIdx; i++) {
            if (!ring[i].isEmpty()) ticks.add((baseTick + i) % capacity);
        }

        if (reverse) Collections.reverse(ticks);
        return ticks;
    }

}

