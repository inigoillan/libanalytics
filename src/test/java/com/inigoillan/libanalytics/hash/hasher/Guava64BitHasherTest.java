package com.inigoillan.libanalytics.hash.hasher;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.inigoillan.libanalytics.hash.Hash64Bits;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class Guava64BitHasherTest {

    @Test
    public void HashElement_GivenTestString_ReturnHash() throws Exception {
        // Arrange
        byte[] elementToHash = "test".getBytes();

        HashFunction hashFunction = mock(HashFunction.class, RETURNS_DEEP_STUBS);
        when(hashFunction.hashBytes(eq(elementToHash))).thenReturn(HashCode.fromLong(10L));

        Guava64BitHasher hasher = new Guava64BitHasher(hashFunction);

        // Act
        Hash64Bits hash = hasher.hash(elementToHash);

        // Assert
        Hash64Bits expectedHash = new Hash64Bits(10L);

        assertEquals(expectedHash, hash);
    }
}