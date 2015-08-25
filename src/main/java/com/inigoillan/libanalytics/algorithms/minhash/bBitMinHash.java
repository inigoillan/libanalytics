package com.inigoillan.libanalytics.algorithms.minhash;

import com.google.common.base.Preconditions;
import com.inigoillan.libanalytics.collections.PackedBitsSet;
import com.inigoillan.libanalytics.hash.Hash;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Implementation of b Bit Minwise Hashing algorithm. This
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class bBitMinHash<K extends Hash> {
    private PackedBitsSet bitsSet;
    private int bitsSize;

    public bBitMinHash(@Nonnegative int bitsSize, @Nonnegative int setSize) {
        this.bitsSet = new PackedBitsSet(bitsSize, setSize);
        this.bitsSize = bitsSize;
    }

    public void setHashed(@Nonnegative int position, @Nonnull K hash) {
        int bits = hash.getLeastSignificantBits(bitsSize).intValue();

        bitsSet.setIthBits(position, bits);
    }

    public float estimateJaccardIndex(bBitMinHash<K> minHash) {
        Preconditions.checkArgument(minHash.bitsSet.getBitSize() == this.bitsSet.getBitSize(),
                "You can't estimate the Jaccard Index for bBitMinHashes of different bit sizes");
        Preconditions.checkArgument(minHash.bitsSet.getSetSize() == this.bitsSet.getSetSize(),
                "You can't estimate the Jaccard Index for bBitMinHashes of different set sizes");

        int size = this.bitsSet.getSetSize();
        int equalsFound = 0;

        for (int i = 0; i < size; i++) {
            if (this.bitsSet.getIthBits(i) == minHash.bitsSet.getIthBits(i)) {
                equalsFound++;
            }
        }

        return (float) equalsFound / size;
    }
}
