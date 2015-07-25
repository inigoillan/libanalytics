package com.inigoillan.libanalytics.algorithms.oddsketch;

import com.inigoillan.libanalytics.algorithms.hashers.Hash;
import com.inigoillan.libanalytics.algorithms.hashers.Hasher;

import javax.annotation.Nonnull;

/**
 * @author ignacius
 * @since  24/07/15
 */
public class GenericOddSketch<E, K extends Hash> extends OddSketch<K> {
    private Hasher<E, K> hasher;

    /**
     * Constructor
     *
     * @param size The size of the sketch
     */
    public GenericOddSketch(int size, @Nonnull Hasher<E, K> hasher) {
        super(size);

        this.hasher = hasher;
    }


    /**
     * Adds an element to the sketch
     *
     * @param element
     */
    public void addElement(@Nonnull E element) {
        this.addHashed(hash(element));
    }

    protected K hash(@Nonnull E element) {
        return this.hasher.hash(element);
    }

    protected Hasher<E, K> getHasher() {
        return this.hasher;
    }

    protected void setHasher(@Nonnull Hasher<E, K> hasher) {
        this.hasher = hasher;
    }
}
