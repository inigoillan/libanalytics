package com.inigoillan.libanalytics.algorithms;

import com.google.common.annotations.Beta;

import javax.annotation.Nonnull;

/**
 * Specification for elements that can be merged together
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since  1.0
 */
@Beta
public interface Mergeable<K> {
    /**
     * Merges two elements into one
     * @param k
     * @return
     */
    @Nonnull K merge(@Nonnull K k);
}
