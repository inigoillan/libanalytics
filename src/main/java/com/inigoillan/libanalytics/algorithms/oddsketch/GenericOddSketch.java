package com.inigoillan.libanalytics.algorithms.oddsketch;

import com.google.common.base.MoreObjects;
import com.inigoillan.libanalytics.algorithms.hashers.Hash;
import com.inigoillan.libanalytics.algorithms.hashers.Hasher;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Adds support for adding a generic element to the Odd sketch using a given {@link Hasher}
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since  1.0
 */
public class GenericOddSketch<E, K extends Hash> extends OddSketch<K> {
    private Hasher<E, K> hasher;

    //region constructor

    /**
     * Constructor
     *
     * @param size The size of the sketch
     */
    public GenericOddSketch(int size, @Nonnull Hasher<E, K> hasher) {
        super(size);

        this.hasher = hasher;
    }

    //endregion


    //region addElement

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

    //endregion


    //region Getters and setters

    protected Hasher<E, K> getHasher() {
        return this.hasher;
    }

    protected void setHasher(@Nonnull Hasher<E, K> hasher) {
        this.hasher = hasher;
    }

    //endregion


    //region Equals, hashcode and toString

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GenericOddSketch))
            return false;

        if (o == this)
            return true;

        GenericOddSketch sketch = (GenericOddSketch) o;

        return this.getHasher().equals(((GenericOddSketch) o).getHasher()) && super.equals(o);
    }


    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("hashed object", this.getHasher());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getHasher(), super.hashCode());
    }
}
