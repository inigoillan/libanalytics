package com.inigoillan.libanalytics.algorithms.hashers;

import javax.annotation.Nonnull;

public interface Hasher<E, K extends Hash> {
    K hash(@Nonnull E element);
}
