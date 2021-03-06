package com.inigoillan.libanalytics.algorithms.minhash;

import com.inigoillan.libanalytics.hash.Divisible;
import com.inigoillan.libanalytics.hash.Hash32Bits;
import org.junit.Test;

import static org.junit.Assert.*;


public class MinHashTest {

    //region EstimateJaccardIndex tests

    @Test
    public void EstimateJaccardIndex_FirstMinHashSizeIs0_ReturnZero() throws Exception {
        // Arrange
        MinHash<Comparable> minHash = buildMinHash(10, new int[0]);
        MinHash<Comparable> minHash2 = buildMinHash(10, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});

        // Act
        float result = minHash.estimateJaccardIndex(minHash2);

        // Assert
        assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void EstimateJaccardIndex_SecondMinHashSizeIs0_ReturnZero() throws Exception {
        // Arrange
        MinHash<Comparable> minHash = buildMinHash(10, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        MinHash<Comparable> minHash2 = buildMinHash(10, new int[0]);

        // Act
        float result = minHash.estimateJaccardIndex(minHash2);

        // Assert
        assertEquals(0.0f, result, 0.0f);
    }


    @Test
    public void EstimateJaccardIndex_0ElementsIncommon_JaccardIndexIs0() throws Exception {
        // Arrange
        MinHash<Comparable> minHash = buildMinHash(10, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        MinHash<Comparable> minHash2 = buildMinHash(10, new int[]{10, 11, 12, 13, 14, 15, 16, 17, 18, 19});

        // Act
        float result = minHash.estimateJaccardIndex(minHash2);

        // Assert
        assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void EstimateJaccardIndex_5OutOf10ElementsIncommon_JaccardIndexIsZeroPointFive() throws Exception {
        // Arrange
        MinHash<Comparable> minHash = buildMinHash(10, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        MinHash<Comparable> minHash2 = buildMinHash(10, new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14});

        // Act
        float result = minHash.estimateJaccardIndex(minHash2);

        // Assert
        assertEquals(0.5f, result, 0.0f);
    }

    @Test
    public void EstimateJaccardIndex_DifferentSizedMinHash_JaccardIndexIsZeroPointFive() throws Exception {
        // Arrange
        MinHash<Comparable> minHash = buildMinHash(10, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        MinHash<Comparable> minHash2 = buildMinHash(20, new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25});

        // Act
        float result = minHash.estimateJaccardIndex(minHash2);

        // Assert
        assertEquals(0.5f, result, 0.0f);
    }

    @Test
    public void EstimateJaccardIndex_GivenThreeMinHash_ReturnRepeatingPointThree() throws Exception {
        // Arrangee
        MinHash<Comparable> minHash1 = buildMinHash(9, new int[]{1, 2, 3, 4, 11, 12, 13, 14, 15});
        MinHash<Comparable> minHash2 = buildMinHash(9, new int[]{1, 2, 3, 5, 15, 16, 17, 18, 19});
        MinHash<Comparable> minHash3 = buildMinHash(9, new int[]{1, 2, 3, 6, 17, 18, 19, 20, 21});

        // Act
        float result = MinHash.estimateJaccardIndex(minHash1, minHash2, minHash3);

        // Assert
        assertEquals(3.0f / 9.0f, result, 0.0f);
    }

    @Test
    public void EstimateJaccardIndex_GivenThreeMinHashOneIsZero_ReturnZero() throws Exception {
        // Arrangee
        MinHash<Comparable> minHash1 = buildMinHash(9, new int[]{1, 2, 3, 4, 11, 12, 13, 14, 15});
        MinHash<Comparable> minHash2 = buildMinHash(9, new int[]{1, 2, 3, 5, 15, 16, 17, 18, 19});
        MinHash<Comparable> minHash3 = buildMinHash(9, new int[0]);

        // Act
        float result = MinHash.estimateJaccardIndex(minHash1, minHash2, minHash3);

        // Assert
        assertEquals(0.0f, result, 0.0f);
    }


    //endregion

    //region Merge tests

    @Test
    public void Merge_GivenTwoMinHash_ReturnCorrectMinHash() {
        // Arrange
        MinHash<Comparable> minHash1 = buildMinHash(5, new int[]{1, 2, 3, 9, 10});
        MinHash<Comparable> minHash2 = buildMinHash(5, new int[]{4, 5, 6, 7, 8});

        // Act
        MinHash<Comparable> result = minHash1.merge(minHash2);

        // Assert
        MinHash<Comparable> expected = buildMinHash(5, new int[]{1, 2, 3, 4, 5});
        assertEquals(expected, result);
    }


    @Test
    public void Merge_GivenDifferentSizedMinHashes_ReturnCorrectMinHash() {
        // Arrange
        MinHash<Comparable> minHash1 = buildMinHash(5, new int[]{1, 2, 3, 9, 10});
        MinHash<Comparable> minHash2 = buildMinHash(10, new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14});

        // Act
        MinHash<Comparable> result = minHash1.merge(minHash2);

        // Assert
        MinHash<Comparable> expected = buildMinHash(5, new int[]{1, 2, 3, 4, 5});
        assertEquals(expected, result);
    }

    //endregion


    //region Helper methods

    private MinHash<Comparable> buildMinHash(int size, int[] hashes) {
        MinHash<Comparable> minHash = new MinHash<>(size);

        for (int i = 0; i < hashes.length; i++) {
            minHash.addHashed(hash(hashes[i]));
        }

        return minHash;
    }

    private Comparable hash(int hash) {
        return new Hash32Bits(hash);
    }

    //endregion
}