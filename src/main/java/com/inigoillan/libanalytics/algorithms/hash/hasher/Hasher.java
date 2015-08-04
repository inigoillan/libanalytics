package com.inigoillan.libanalytics.algorithms.hash.hasher;

import com.inigoillan.libanalytics.algorithms.hash.Hash;

import javax.annotation.Nonnull;

/**
 * Represents a hashing algorithm
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since  1.0
 */
public interface Hasher<E, K extends Hash> {
    K hash(@Nonnull E element);
}
