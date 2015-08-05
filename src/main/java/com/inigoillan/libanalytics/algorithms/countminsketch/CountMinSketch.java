package com.inigoillan.libanalytics.algorithms.countminsketch;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.inigoillan.libanalytics.hash.Divisible;
import com.inigoillan.libanalytics.hash.SignificantBits;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A Count-Min sketch implementation
 * <p>
 * For details, please refer to the <a href="https://7797b024-a-62cb3a1a-s-sites.googlegroups.com/site/countminsketch/cm-latin.pdf">Count-Min Sketch paper</a>
 *
 * @param <K> The type of {@link SignificantBits} elements this sketch accepts
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class CountMinSketch<K extends Divisible> {
    //region Variables

    private ArrayList<long[]> sketch;

    //endregion


    //region ctors

    public CountMinSketch(@Nonnegative int numRows, @Nonnegative int numCols) {
        this.setSketch(Lists.newArrayList());

        for (int i = 0; i < numRows; i++) {
            long[] array = new long[numCols];
            Arrays.fill(array, 0l);

            this.getSketch().add(array);
        }
    }

    //endregion


    //region addHashed

    /**
     * Given a list of hashes, increments the count in the sketch.
     *
     * @param hashes The hashes used to select the buckets to increment the count in the sketch
     *
     * @see CountMinSketch#addHashed(long, Divisible[])
     */
    public void addHashed(@Nonnull K... hashes) {
        addHashed(1, hashes);
    }

    /**
     * Given a list of hashes, adds them to the sketch <i>count</i> times. Note that the number of hashes you provide
     * must be the same as the number of rows you specified in the constructor
     *
     * @param count  Number of times to add the hashes to the sketch
     * @param hashes The hashes used to select the buckets to add the count in the sketch
     */
    public void addHashed(long count, @Nonnull K... hashes) {
        Preconditions.checkArgument(hashes.length == this.getNumRows(),
                "The number of hashes has to be of the same size than the number of columns in the sketch");

        int numRows = getNumRows();
        int numCols = getNumCols();

        for (int i = 0; i < numRows; i++) {
            K hash = hashes[i];

            Number j = hash.mod(numCols);
            long localCount = this.getSketch().get(i)[j.intValue()];
            localCount += localCount;
            this.getSketch().get(i)[j.intValue()] = localCount;
        }
    }

    //endregion


    //region estimatePointQuery

    /**
     * Queries the sketch to estimate the frequency of the given element represented by the hashes
     *
     * @param hashes The hashes to use
     * @return Returns the estimation for the point query
     */
    public long estimatePointQuery(@Nonnull K... hashes) {
        Preconditions.checkArgument(hashes.length == this.getSketch().size(),
                "The number of hashes has to be of the same size than the number of columns in the sketch");

        long min = Long.MAX_VALUE;

        int numCols = getNumCols();

        for (int i = 0; i < hashes.length; i++) {
            K hash = hashes[i];

            long val = this.getSketch().get(i)[hash.mod(numCols)];

            if (min > val) {
                min = val;
            }
        }

        return min;
    }

    //endregion


    //region



    //endregion



    //region Getters and Setters

    protected int getNumRows() {
        return getSketch().size();
    }

    protected void setNumRows(int numRows) {

    }

    protected int getNumCols() {
        return getSketch().get(0).length;
    }

    protected void setNumCols(int numCols) {

    }

    protected ArrayList<long[]> getSketch() {
        return sketch;
    }

    protected void setSketch(ArrayList<long[]> sketch) {
        this.sketch = sketch;
    }

    //endregion

}
