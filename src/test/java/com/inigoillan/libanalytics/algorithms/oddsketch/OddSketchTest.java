package com.inigoillan.libanalytics.algorithms.oddsketch;

import com.inigoillan.libanalytics.algorithms.hashers.Hash;
import com.inigoillan.libanalytics.algorithms.hashers.Hash32Bits;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;


public class OddSketchTest {

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
        int setSize = estimateSetSizeFor(sketch, 1, 2, 3, 4, 5, 6);

        // Assert
        assertEquals(6, setSize, getSetSizeAccuracy());
    }

    @Test
    public void ComputeJaccardIndex_SameOddSketch_Returns1() {
        // Arrange
        OddSketch<Hash> sketch = buildOddSketch(10);

        // Act
        double jaccardIndex = sketch.computeJaccardIndex(sketch);

        // Assert
        assertEquals(1.0, jaccardIndex, 0.0);
    }

    @Test
    public void ComputeJaccardIndex_DifferentOddSketch_Returns0() {
        // Arrange
        OddSketch<Hash> sketch1 = buildOddSketch(10);
        OddSketch<Hash> sketch2 = buildOddSketch(10);

        // Act
        double jaccardIndex = getJaccardIndexFor(sketch1, new int[]{1, 2, 3, 4},
                                                sketch2, new int[]{6, 7, 8, 9});

        // Assert
        assertEquals(0.0, jaccardIndex, 0.0);
    }


    private double getJaccardIndexFor(OddSketch<Hash> sketch1, int[] elements1,
                                    OddSketch<Hash> sketch2, int[] elements2) {
        for (int element : elements1) {
            sketch1.addHashed(hash(element));
        }

        for (int element: elements2) {
            sketch2.addHashed(hash(element));
        }

        return sketch1.computeJaccardIndex(sketch2);
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
        OddSketch<Hash> sketch = mock(OddSketch.class, CALLS_REAL_METHODS);
        sketch.setSize(size);

        return sketch;
    }

    private float getSetSizeAccuracy() {
        return 1.0f;
    }
}