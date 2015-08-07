package com.inigoillan.libanalytics.hash.hasher;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.inigoillan.libanalytics.hash.Hash32Bits;

import javax.annotation.Nonnull;

/**
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class Guava32BitHasher implements Hasher<byte[], Hash32Bits> {

    private final HashFunction hashFunction;

    public Guava32BitHasher(@Nonnull HashFunction hashFunction) {
        this.hashFunction = hashFunction;
    }

    @Override
    public Hash32Bits hash(@Nonnull byte[] element) {
        HashCode hash = hashFunction.hashBytes(element);

        return new Hash32Bits(hash.asInt());
    }
}
