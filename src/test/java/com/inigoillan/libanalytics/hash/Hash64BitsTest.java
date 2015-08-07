package com.inigoillan.libanalytics.hash;

import org.junit.Test;

import static org.junit.Assert.*;


public class Hash64BitsTest {

    //region GetLeastSignificantBits tests

    @Test
    public void GetLeastSignificantBits_GivenNumber255And3Bits_Return7() throws Exception {
        // Arrange
        long hashValue = 255;
        Hash64Bits hash = new Hash64Bits(hashValue);

        // Act
        long result = hash.getLeastSignificantBits(3);

        // Assert
        long expected = 7;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetLeastSignificantBits_GivenNumber255And0Bits_Return0() throws Exception {
        // Arrange
        long hashValue = 255;
        Hash64Bits hash = new Hash64Bits(hashValue);

        // Act
        long result = hash.getLeastSignificantBits(0);

        // Assert
        long expected = 0;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetLeastSignificantBits_GivenNegativeNumber255And5Bits_Return1() throws Exception {
        // Arrange
        long hashValue = -255;
        Hash64Bits hash = new Hash64Bits(hashValue);

        // Act
        long result = hash.getLeastSignificantBits(5);

        // Assert
        long expected = 1;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    //endregion

    //region GetMostSignificantBits tests

    @Test
    public void GetMostSignificantBits_Given256And62Bits_Return64() throws Exception {
        // Arrange
        long hashValue = 256L;
        Hash64Bits hash = new Hash64Bits(hashValue);

        // Act
        long result = hash.getMostSignificantBits(62);

        // Assert
        long expected = 64;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetMostSignificantBits_GivenLongMaxAnd64Bits_Return255() throws Exception {
        // Arrange
        long hashValue = Long.MIN_VALUE;
        Hash64Bits hash = new Hash64Bits(hashValue);

        // Act
        long result = hash.getMostSignificantBits(64);

        // Assert
        long expected = Long.MIN_VALUE;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetMostSignificantBits_GivenLongMaxAnd5Bits_Return15() throws Exception {
        // Arrange
        long hashValue = Long.MAX_VALUE;
        Hash64Bits hash = new Hash64Bits(hashValue);

        // Act
        long result = hash.getMostSignificantBits(5);

        // Assert
        long expected = 15; // Take into account sign bit is 0
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetMostSignificantBits_GivenNegativeNumber1And5Bits_Return31() throws Exception {
        // Arrange
        long hashValue = -1; // -1 in complement 2 format is all 64 bits set to 1
        Hash64Bits hash = new Hash64Bits(hashValue);

        // Act
        long result = hash.getMostSignificantBits(5);

        // Assert
        long expected = 31;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    //endregion


    //region Helper methods

    private String getErrorMessage(long hashValue, long expected, long result) {

        String hashInBinary = Long.toBinaryString(hashValue);
        String expectedInBinary = Long.toBinaryString(expected);
        String resultValueInBinary = Long.toBinaryString(result);

        return String.format(
                "\n" +
                        "Expected value was %d, but got %d\n" +
                        "Original value in binary format is  %s\n" +
                        "Expected value in binary format is  %s\n" +
                        "Result value in binary format is    %s",
                expected, result, hashInBinary, expectedInBinary, resultValueInBinary);
    }

    //endregion
}