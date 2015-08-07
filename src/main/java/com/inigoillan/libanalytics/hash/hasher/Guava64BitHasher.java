package com.inigoillan.libanalytics.hash.hasher;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.inigoillan.libanalytics.hash.Hash64Bits;

import javax.annotation.Nonnull;

/**
 * {@link Hasher} adapter for Guava {@link HashFunction}
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 *
 * @see HashFunction
 */
public class Guava64BitHasher implements Hasher<byte[], Hash64Bits>{
    private final HashFunction hashFunction;

    public Guava64BitHasher(@Nonnull HashFunction hashFunction) {
        this.hashFunction = hashFunction;
    }


    @Override
    public Hash64Bits hash(@Nonnull byte[] element) {
        HashCode hash = hashFunction.hashBytes(element);

        return new Hash64Bits(hash.asLong());
    }
}
