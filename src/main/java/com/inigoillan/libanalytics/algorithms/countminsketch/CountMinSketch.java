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
        this.sketch = Lists.newArrayList();

        for (int i = 0; i < numRows; i++) {
            long[] array = new long[numCols];
            Arrays.fill(array, 0l);

            this.sketch.add(array);
        }
    }

    //endregion


    //region addHashed

    public void addHashed(@Nonnull K... hashes) {
        addHashed(1, hashes);
    }

    /**
     * Given a list of hashes, add them to the sketch t many times. Note that the number of hashes you provide
     * must be the same as the number of rows you specified in the constructor
     *
     * @param t      Number of times to add the hashes to the sketch
     * @param hashes The hashes used to
     */
    public void addHashed(long t, @Nonnull K... hashes) {
        Preconditions.checkArgument(hashes.length == this.getNumRows());

        int numRows = getNumRows();
        int numCols = getNumCols();

        for (int i = 0; i < numRows; i++) {
            K hash = hashes[i];

            Number j = hash.mod(numCols);
            long count = this.sketch.get(i)[j.intValue()];
            count += t;
            this.sketch.get(i)[j.intValue()] = count;
        }
    }

    //endregion


    //region Query

    public long query(@Nonnull K... hashes) {
        long min = Long.MAX_VALUE;

        int numCols = getNumCols();

        for (int i = 0; i < hashes.length; i++) {
            K hash = hashes[i];

            long val = this.sketch.get(i)[hash.mod(numCols)];

            if (min > val) {
                min = val;
            }
        }

        return min;
    }

    //endregion


    //region Getters and Setters

    protected int getNumRows() {
        return sketch.size();
    }

    protected void setNumRows(int numRows) {

    }

    protected int getNumCols() {
        return sketch.get(0).length;
    }

    protected void setNumCols(int numCols) {

    }

    //endregion

}
