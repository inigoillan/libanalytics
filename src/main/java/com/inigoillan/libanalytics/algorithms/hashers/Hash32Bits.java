package com.inigoillan.libanalytics.algorithms.hashers;

public class Hash32Bits implements Hash {
    private final int hash;

    public Hash32Bits(int hash) {
        this.hash = hash;
    }

    @Override
    public int divideBy(int divisor) {
        return hash / divisor;
    }

    @Override
    public int mod(int divisor) {
        return hash % divisor;
    }

    @Override
    public Integer getLeastSignficantBits(int bits) {
        return 0;
    }

    @Override
    public Integer getMostSignificantBits(int bits) {
        return 0;
    }

    @Override
    public final int getSize() {
        return 32;
    }
}
