package com.inigoillan.libanalytics.algorithms.minhash;

import com.google.common.base.Preconditions;
import com.inigoillan.libanalytics.hash.Hash;
import com.inigoillan.libanalytics.hash.Hash32Bits;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class bBitMinHashTest {
    @Test
    public void EstimateJaccardIndex_NoElementsInCommon_Returns0() {
        // Arrange
        bBitMinHash<Hash> minHash1 = buildMinHash(1, 2, new int[] {0, 1});
        bBitMinHash<Hash> minHash2 = buildMinHash(1, 2, new int[] {1, 0});

        // Act
        float result = minHash1.estimateJaccardIndex(minHash2);

        // Assert
        assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void EstimateJaccardIndex_HalfElementsInCommon_Returns0Point5() {
        // Arrange
        bBitMinHash<Hash> minHash1 = buildMinHash(1, 2, new int[] {0, 1});
        bBitMinHash<Hash> minHash2 = buildMinHash(1, 2, new int[] {1, 1});

        // Act
        float result = minHash1.estimateJaccardIndex(minHash2);

        // Assert
        assertEquals(0.5f, result, 0.0f);
    }

    private bBitMinHash<Hash> buildMinHash(int bitsSize, int setSize, int... elements) {
        Preconditions.checkArgument(elements.length >= setSize);

        bBitMinHash<Hash> minHash = new bBitMinHash<>(bitsSize, setSize);

        for (int i = 0; i < setSize; i++) {
            Hash hash = buildHash(elements[i]);

            minHash.setHashed(i, hash);
        }

        return minHash;
    }

    private Hash buildHash(int e) {
        return new Hash32Bits(e);
    }

}