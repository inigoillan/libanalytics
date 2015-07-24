package com.inigoillan.libanalytics.algorithms.hashers;


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
