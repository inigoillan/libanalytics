package com.inigoillan.libanalytics.algorithms.minhash;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.inigoillan.libanalytics.algorithms.Mergeable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.TreeSet;

/**
 * MinHash implementation
 *
 * For details on the implementation, please refer to the <a href="http://www.cs.princeton.edu/courses/archive/spring04/cos598B/bib/BroderCFM-minwise.pdf">Minhash paper</a>
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since  1.0
 */
public class MinHash<K extends Comparable> implements Mergeable<MinHash<K>> {
    private static Predicate<MinHash> SIZE_NOT_ZERO = new Predicate<MinHash>() {
        @Override
        public boolean apply(MinHash minHash) {
            return minHash.getTreeSet().size() <= 0;
        }
    };

    //region Variables

    private TreeSet<K> treeSet = new TreeSet<K>();
    private int maxSize;

    //endregion


    //region ctors

    public MinHash(int maxSize) {
        Preconditions.checkArgument(maxSize > 0);

        this.setMaxSize(maxSize);
    }

    //endregion


    //region addHashed

    /**
     * Adds the element to the set
     *
     * @param hash The Hash to add to the set
     */

    public void addHashed(@Nonnull K hash) {
        getTreeSet().add(hash);

        removeExtra();
    }

    //endregion


    //region merge

    @Nonnull
    @Override
    public MinHash<K> merge(@Nonnull MinHash<K> minHash) {
        int minNumHash = Math.min(minHash.getMaxSize(), this.getMaxSize());

        MinHash<K> newMinHash = new MinHash<K>(minNumHash);

        newMinHash.mergeHelper(minHash);
        newMinHash.mergeHelper(this);

        return newMinHash;

    }

    /**
     * Merge the given MinHash with this one. Precision will be set to the minimum of both MinHashes
     *
     * @param minHash
     */
    public void mergeHelper(@Nonnull MinHash<K> minHash) {
        this.getTreeSet().addAll(minHash.getTreeSet());

        this.setMaxSize(Math.min(minHash.getMaxSize(), this.getMaxSize()));

        removeExtra();
    }

    private void removeExtra() {
        while (getTreeSet().size() > getMaxSize()) {
            getTreeSet().pollLast();
        }
    }

    //endregion


    //region estimateJaccardIndex

    /**
     * Estimates the Jaccard Index between this minhash and the one provided in the parameter
     *
     * @param minHash The MinHash against which to estimate the Jaccard Index
     *
     * @return the Jaccard Index as a [0..1] value. Note this method doesn't cause any changes in the internal state of the class
     */
    public float estimateJaccardIndex(@Nonnull MinHash<K> minHash) {
        if (this.getTreeSet().size() == 0 || minHash.getTreeSet().size() == 0) {
            return 0;
        }

        MinHash<K> smallerMinHash = minHash.getTreeSet().size() > this.getTreeSet().size() ? this : minHash;
        MinHash<K> otherMinHash = this == smallerMinHash ? minHash : this;

        int intersectionCounter = 0;

        for (Comparable hash : smallerMinHash.getTreeSet()) {
            if (otherMinHash.getTreeSet().contains(hash)) {
                intersectionCounter++;
            }
        }

        return (float) intersectionCounter / smallerMinHash.getTreeSet().size();
    }


    /**
     * Gets the estimated Jaccard Index
     *
     * @param hashes The MinHashes against to which estimate the Jaccard Index.
     * @return the Jaccard Index as a [0..1] value
     */
    @Beta
    public static <T extends Comparable> float estimateJaccardIndex(@Nonnull Iterable<MinHash<T>> hashes) {
        Preconditions.checkArgument(Iterables.size(hashes) > 0);

        if (Iterables.any(hashes, SIZE_NOT_ZERO)) {
            return 0;
        }

        TreeSet<T> all = new TreeSet<T>();
        int mink = Integer.MAX_VALUE;
        int maxs = Integer.MIN_VALUE;

        for(MinHash hash :hashes) {
            all.addAll(hash.getTreeSet());
            mink = Math.min(mink, hash.getMaxSize());
            maxs = Math.max(maxs, hash.getTreeSet().size());
        }

        mink = maxs < mink ? maxs : mink;
        int result = 0;

        for(int i = 0; i < mink; i++) {
            Comparable l;

            try {
                l = all.pollFirst();
            } catch(NullPointerException e) {
                //This can happen if k is larger than
                //the number of insertions.
                break;
            }

            boolean allContain = true;
            for(MinHash h : hashes) {
                if(!h.getTreeSet().contains(l)) {
                    allContain = false;
                    break;
                }
            }
            if(allContain) {
                result += 1;
            }
        }
        return ((float)result)/mink;
    }

    /**
     * Gets the estimated Jaccard Index
     *
     * @param hashes The MinHashes against to which estimate the Jaccard Index.
     * @return the Jaccard Index as a [0..1] value
     */
    @Beta
    public static float estimateJaccardIndex(MinHash... hashes) {
        return estimateJaccardIndex(Lists.newArrayList(hashes));
    }

    //endregion


    //region Getters and Setters

    protected TreeSet<K> getTreeSet() {
        return treeSet;
    }

    protected void setTreeSet(TreeSet<K> treeSet) {
        this.treeSet = treeSet;
    }

    protected int getMaxSize() {
        return maxSize;
    }

    protected void setMaxSize(int size) {
        this.maxSize = size;
    }


    //endregion


    //region toString, HashCode and Equals

    @Override
    public String toString() {
        return toStringHelper().toString();
    }

    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("sketch max size", this.getMaxSize())
                .add("sketch elements", this.getTreeSet().toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getTreeSet(), this.getMaxSize());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MinHash))
            return false;

        if (o == this)
            return true;

        MinHash minHash = ((MinHash) o);

        return this.getTreeSet().equals(minHash.getTreeSet()) &&
                this.getMaxSize() == minHash.getMaxSize();
    }

    //endregion

}

