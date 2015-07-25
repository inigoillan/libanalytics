package com.inigoillan.libanalytics.algorithms.oddsketch;

import com.inigoillan.libanalytics.algorithms.hashers.Hash;
import com.inigoillan.libanalytics.algorithms.hashers.Hasher;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class GenericOddSketchTest {

    @Test
    public void AddElement_Given1_HashMethodCalled() throws Exception {
        // Arrange
        Object element = new Object();
        Hasher hasher = mock(Hasher.class, RETURNS_DEEP_STUBS);

        GenericOddSketch sketch = buildOddSketch(10, hasher);

        // Act
        sketch.addElement(element);

        // Assert
        Mockito.verify(hasher, only()).hash(eq(element));
    }

    private GenericOddSketch<Object, Hash> buildOddSketch(int size, Hasher hasher) {
        GenericOddSketch<Object, Hash> sketch = new GenericOddSketch<>(size, hasher);

        return sketch;
    }
}