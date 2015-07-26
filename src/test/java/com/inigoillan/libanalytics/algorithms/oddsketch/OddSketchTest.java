package com.inigoillan.libanalytics.algorithms.oddsketch;

import com.inigoillan.libanalytics.algorithms.hashers.Hash;
import com.inigoillan.libanalytics.algorithms.hashers.Hash32Bits;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;


public class OddSketchTest {

    //region estimateSetSize tests
    @Test
    public void estimateSetSize_1Element_ReturnsSetSize1() {
        // Arrange
        OddSketch<Hash> sketch = buildOddSketch(10);

        // Act
        int setSize = estimateSetSizeFor(sketch, 1);

        // Assert
        assertEquals(1, setSize);
    }

    @Test
    public void estimateSetSize_6Elements_ReturnsSetSize6() {
        // Arrange
        OddSketch<Hash> sketch = buildOddSketch(30);

        // Act
        int setSize = estimateSetSizeFor(sketch, 1, 5, 10, 15, 20, 25);

        // Assert
        assertEquals(6, setSize, getSetSizeAccuracy());
    }

    //endregion


    //region estimateJaccardIndex tests
    @Test
    public void EstimateJaccardIndex_SameOddSketch_Returns1() {
        // Arrange
        OddSketch<Hash> sketch = buildOddSketch(10);
        sketch.addHashed(hash(1));

        // Act
        double jaccardIndex = sketch.estimateJaccardIndex(sketch);

        // Assert
        assertEquals(1.0, jaccardIndex, 0.0);
    }

    @Test
    public void EstimateJaccardIndex_NoElementsAdded_Returns1() {
        // Arrange
        OddSketch<Hash> sketch1 = buildOddSketch(10);
        OddSketch<Hash> sketch2 = buildOddSketch(10);

        // Act
        double jaccardIndex = sketch1.estimateJaccardIndex(sketch2);

        // Assert
        assertEquals(1.0, jaccardIndex, 0.0);
    }

    @Test
    public void EstimateJaccardIndex_DifferentOddSketch_Returns0() {
        // Arrange
        OddSketch<Hash> sketch1 = buildOddSketch(10);
        OddSketch<Hash> sketch2 = buildOddSketch(10);

        // Act
        double jaccardIndex = getJaccardIndexFor(sketch1, new int[]{1, 2, 3, 4},
                sketch2, new int[]{6, 7, 8, 9});

        // Assert
        assertEquals(0.0, jaccardIndex, 0.0);
    }


    @Test
    public void EstimateJaccardIndex_VerySimilarOddSketches_ReturnsRatio() {
        // Arrange
        OddSketch<Hash> sketch1 = buildOddSketch(10);
        OddSketch<Hash> sketch2 = buildOddSketch(10);

        // Act
        double jaccardIndex = getJaccardIndexFor(sketch1, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                sketch2, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 11});

        // Assert
        assertEquals(0.9, jaccardIndex, 0.05);
    }

    //endregion


    //region Merge tests
    @Test
    public void Merge_SameSizedSketches_ResultCorrect() {
        // Arrange
        OddSketch<Hash> sketch1 = buildOddSketch(10);
        sketch1.addHashed(hash(1));
        OddSketch<Hash> sketch2 = buildOddSketch(10);
        sketch2.addHashed(hash(2));

        OddSketch<Hash> expectedResult = buildOddSketch(10);
        expectedResult.addHashed(hash(1));
        expectedResult.addHashed(hash(2));

        // Act
        OddSketch<Hash> result = sketch1.merge(sketch2);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void Merge_DifferentSizedSketches_ResultCorrect() {
        // Arrange
        OddSketch<Hash> sketch1 = buildOddSketch(10);
        sketch1.addHashed(hash(1));
        OddSketch<Hash> sketch2 = buildOddSketch(30);
        sketch2.addHashed(hash(25));

        OddSketch<Hash> expectedResult = buildOddSketch(10);
        expectedResult.addHashed(hash(1));
        expectedResult.addHashed(hash(25));

        // Act
        OddSketch<Hash> result = sketch1.merge(sketch2);

        // Assert
        assertEquals(expectedResult, result);
    }

    //endregion


    //region Helper methods
    private double getJaccardIndexFor(OddSketch<Hash> sketch1, int[] elements1,
                                      OddSketch<Hash> sketch2, int[] elements2) {
        for (int element : elements1) {
            sketch1.addHashed(hash(element));
        }

        for (int element : elements2) {
            sketch2.addHashed(hash(element));
        }

        return sketch1.estimateJaccardIndex(sketch2);
    }


    private int estimateSetSizeFor(OddSketch<Hash> sketch, int... elements) {
        for (Integer element : elements) {
            sketch.addHashed(hash(element));
        }

        return sketch.estimateSetSize();
    }

    private Hash hash(int hash) {
        return new Hash32Bits(hash);
    }

    private OddSketch<Hash> buildOddSketch(int size) {
        OddSketch<Hash> sketch = new OddSketch<>(size);

        return sketch;
    }

    private float getSetSizeAccuracy() {
        return 1.0f;
    }

    //endregion
}