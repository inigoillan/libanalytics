package com.inigoillan.libanalytics.hash.hasher;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.inigoillan.libanalytics.hash.Hash32Bits;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class Guava32BitHasherTest {

    @Test
    public void Hash_GivenTestString_ReturnHash() throws Exception {
        // Arrange
        byte[] elementToHash = "test".getBytes();

        HashFunction hashFunction = mock(HashFunction.class, RETURNS_DEEP_STUBS);
        when(hashFunction.hashBytes(eq(elementToHash))).thenReturn(HashCode.fromInt(10));

        Guava32BitHasher hasher = new Guava32BitHasher(hashFunction);

        // Act
        Hash32Bits hash = hasher.hash(elementToHash);

        // Assert
        Hash32Bits expectedHash = new Hash32Bits(10);

        assertEquals(expectedHash, hash);
    }
}