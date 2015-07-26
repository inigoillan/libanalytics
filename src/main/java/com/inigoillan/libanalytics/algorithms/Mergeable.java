package com.inigoillan.libanalytics.algorithms;

import javax.annotation.Nonnull;

/**
 * Specification for elements that can be merged together
 *
 * @author: Inigo Illan <inigo.illan@gmail.com>
 * @since: 25/07/15
 */
public interface Mergeable<K> {
    /**
     * Merges two elements into one
     * @param k
     * @return
     */
    @Nonnull K merge(@Nonnull K k);
}
