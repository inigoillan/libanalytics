package com.inigoillan.libanalytics.hash;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnegative;
import java.util.Objects;

/**
 * Represents a hashed element into a 32 bits key
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since  1.0
 */
public class Hash32Bits implements Hash, Comparable<Hash32Bits> {
    private final int hash;

    public Hash32Bits(int hash) {
        this.hash = hash;
    }

    @Override
    public int divideBy(int divisor) {
        return hash / divisor;
    }

    @Override
    public int mod(int divisor) {
        return hash % divisor;
    }

    @Override
    public Integer getLeastSignificantBits(@Nonnegative int bits) {
        Preconditions.checkArgument(bits < 32);

        int hex = (1 << bits) - 1;

        return this.hash & hex;
    }

    @Override
    public Integer getMostSignificantBits(int bits) {
        Preconditions.checkArgument(bits <= 32);

        return (this.hash >>> (32 - bits));
    }

    @Override
    public final int getSize() {
        return 32;
    }


    //region Equals, HashCode, ToString and CompareTo

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Hash32Bits)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        Hash32Bits other = (Hash32Bits) o;

        return this.hash == other.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hash);
    }

    @Override
    public String toString() {
        return Integer.toString(hash);
    }

    @Override
    public int compareTo(Hash32Bits hash32Bits) {
        return hash32Bits.hash == this.hash ? 0 : this.hash - hash32Bits.hash;
    }

    //endregion
}
