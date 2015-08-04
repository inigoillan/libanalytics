package com.inigoillan.libanalytics.algorithms.hash;

/**
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public interface SignificantBits {
    Number getLeastSignificantBits(int bits);

    Number getMostSignificantBits(int bits);

    int getSize();
}
