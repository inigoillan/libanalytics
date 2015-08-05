package com.inigoillan.libanalytics.algorithms.countminsketch;

import com.inigoillan.libanalytics.hash.Divisible;
import com.inigoillan.libanalytics.hash.Hash32Bits;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;


public class CountMinSketchTest {

    //region Query tests

    @Test
    public void Query_GivenAddedHash20Times_Return20() throws Exception {
        // Arrange
        CountMinSketch<Divisible> countMinSketch = new CountMinSketch<>(3, 10);
        addHashes(countMinSketch, 20, new int[] {1, 2, 3});

        // Act
        long result = countMinSketch.query(buildDivisibleArray(new int[]{1, 2, 3}));

        // Assert
        assertEquals(20, result);
    }

    @Test
    public void Query_GivenHashesNotInSketch_Return0() throws Exception {
        // Arrange
        CountMinSketch<Divisible> countMinSketch = new CountMinSketch<>(3, 10);
        addHashes(countMinSketch, 10, new int[] {1, 2, 3});

        // Act
        long result = countMinSketch.query(buildDivisibleArray(new int[]{1, 2, 4}));

        // Assert
        assertEquals(0, result);
    }

    //endregion

    //region Helper methods

    private void addHashes(CountMinSketch<Divisible> countMinSketch, int number, int[] hashes) {
        Divisible[] divisibleHashes = buildDivisibleArray(hashes);
        countMinSketch.addHashed(number, divisibleHashes);
    }

    private Divisible[] buildDivisibleArray(int[] hashes) {
        return Arrays.stream(hashes).mapToObj(i -> hash(i)).toArray(Divisible[]::new);
    }

    private Divisible hash(int i) {
        return new Hash32Bits(i);
    }

    //endregion
}