package com.inigoillan.libanalytics.algorithms.minhash;

import com.google.common.base.Preconditions;
import com.inigoillan.libanalytics.hash.ComparableSignificantBits;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class bBitMinHashBuilder<K extends ComparableSignificantBits<K>> {
    private int minHashSize;

    private K[] minHash;

    public bBitMinHashBuilder(@Nonnegative int minHashSize) {
        Preconditions.checkArgument(minHashSize > 0, "MinHash size has to be greater than zero");

        this.minHashSize = minHashSize;

        this.minHash = (K[]) new ComparableSignificantBits[minHashSize];
    }

    public void addHashed(@Nonnull K... hashes) {
        Preconditions.checkArgument(hashes.length == this.getMinHashSize());

        for (int i = 0; i < minHashSize; i++) {
            if (hashes[i].compareTo(minHash[i]) < 0) {
                minHash[i] = hashes[i];
            }
        }
    }

    public bBitMinHash build(@Nonnegative int bitSize) {
        return null;
    }

    //region Getters and setters

    protected int getMinHashSize() {
        return minHashSize;
    }

    protected void setMinHashSize(int minHashSize) {
        this.minHashSize = minHashSize;
    }
}
