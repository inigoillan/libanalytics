package com.inigoillan.libanalytics.algorithms.hash;

import com.google.common.annotations.Beta;

/**
 * Represents a hashed element
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since  1.0
 */
@Beta
public interface Hash {
    /**
     * Divides the hash by the divisor
     *
     * @param divisor
     * @return
     */
    int divideBy(int divisor);

    /**
     * Computes the modulo of the hash by the divisor
     *
     * @param divisor
     * @return
     */
    int mod(int divisor);

    Number getLeastSignficantBits(int bits);

    Number getMostSignificantBits(int bits);

    int getSize();
}
