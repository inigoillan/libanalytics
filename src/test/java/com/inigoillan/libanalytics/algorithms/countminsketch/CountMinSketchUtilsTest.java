package com.inigoillan.libanalytics.algorithms.countminsketch;

import com.google.common.collect.Lists;
import com.inigoillan.libanalytics.hash.Divisible;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class CountMinSketchUtilsTest {

    //region GetEstimationError tests

    @Test
    public void GetEstimationError_GivenCountMinSketch_Return20() {
        // Arrange
        CountMinSketch<Divisible> sketch = mock(CountMinSketch.class);
        when(sketch.getSketch()).thenReturn(Lists.newArrayList(new long[]{10L, 10L, 10L}));
        when(sketch.getNumCols()).thenReturn(3);

        // Act
        float result = CountMinSketchUtils.getEstimationError(sketch);

        // Assert
        assertEquals(20.0f, result, 0.0f);
    }

    @Test
    public void GetEstimationError_GivenCountMinSketch0Elements_Return0() {
        // Arrange
        CountMinSketch<Divisible> sketch = mock(CountMinSketch.class);
        when(sketch.getSketch()).thenReturn(Lists.newArrayList(new long[]{0L, 0L, 0L}));
        when(sketch.getNumCols()).thenReturn(3);

        // Act
        float result = CountMinSketchUtils.getEstimationError(sketch);

        // Assert
        assertEquals(0.0f, result, 0.0f);
    }

    //endregion

    //region GetProbabilityError tests

    @Test
    public void GetProbabilityError_GivenCountMinSketch10Rows_ReturnPoint999() {
        // Arrange
        CountMinSketch<Divisible> sketch = mock(CountMinSketch.class);
        when(sketch.getNumRows()).thenReturn(10);

        // Act
        float result = CountMinSketchUtils.getProbabilityError(sketch);

        // Assert
        assertEquals(0.999f, result, 0.0001f);
    }

    @Test
    public void GetProbabilityError_GivenCountMinSketch0Elements_Return0() {
        // Arrange
        CountMinSketch<Divisible> sketch = mock(CountMinSketch.class);
        when(sketch.getNumRows()).thenReturn(0);

        // Act
        float result = CountMinSketchUtils.getProbabilityError(sketch);

        // Assert
        assertEquals(0.0f, result, 0.0f);
    }

    //endregion

}