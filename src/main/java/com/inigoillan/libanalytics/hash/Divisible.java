package com.inigoillan.libanalytics.hash;

/**
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public interface Divisible {
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
}
