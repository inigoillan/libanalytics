package com.inigoillan.libanalytics.algorithms.countminsketch;

import com.inigoillan.libanalytics.hash.Divisible;

import java.util.Arrays;

/**
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class CountMinSketchUtils {
    
    private CountMinSketchUtils() {}

    //region Estimation Error

    /**
     * Estimation error for the point query
     *
     * @param sketch
     * @return
     *
     * @see CountMinSketch#estimatePointQuery(Divisible[])
     */
    public static float getEstimationError(CountMinSketch sketch) {
        long[] row0 = (long[]) sketch.getSketch().get(0);

        long numEvents = registeredEvents(row0);
        int width = sketch.getNumCols();

        return 2.0f * numEvents / width;
    }

    private static long registeredEvents(long[] row) {
        return Arrays.stream(row).sum();
    }

    //endregion


    //region Probability Error

    /**
     * Probability error for the point query
     *
     * @param sketch
     * @return
     *
     * @see CountMinSketch#estimatePointQuery(Divisible[])
     */
    public static float getProbabilityError(CountMinSketch sketch) {
        int sketchHeight = sketch.getNumRows();

        return 1.0f - (1.0f / (float) Math.pow(2, sketchHeight));
    }

    //endregion
}
