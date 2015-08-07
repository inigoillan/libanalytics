package com.inigoillan.libanalytics.hash;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnegative;
import java.util.Objects;

/**
 * Represents a hashed element into a 64 bits key
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class Hash64Bits implements Hash, Comparable<Hash64Bits> {
    private final long hash;

    public Hash64Bits(long hash) {
        this.hash = hash;
    }

    @Override
    public int divideBy(int divisor) {
        return (int) hash / divisor;
    }

    @Override
    public int mod(int divisor) {
        return (int) hash % divisor;
    }

    @Override
    public Long getLeastSignificantBits(@Nonnegative int bits) {
        Preconditions.checkArgument(bits < 64);

        int hex = (1 << bits) - 1;

        return this.hash & hex;
    }

    @Override
    public Long getMostSignificantBits(int bits) {
        Preconditions.checkArgument(bits <= 64);

        return (this.hash >>> (64 - bits));
    }

    @Override
    public final int getSize() {
        return 64;
    }


    //region Equals, HashCode, ToString, CompareTo

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Hash64Bits)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        Hash64Bits other = (Hash64Bits) o;

        return this.hash == other.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hash);
    }

    @Override
    public String toString() {
        return Long.toString(hash);
    }

    @Override
    public int compareTo(Hash64Bits hash64Bits) {
        return hash64Bits.hash == this.hash ? 0 : (int) (this.hash - hash64Bits.hash);
    }

    //endregion

}
