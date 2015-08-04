package com.inigoillan.libanalytics.algorithms.hash;

import org.junit.Test;

import static org.junit.Assert.*;


public class Hash32BitsTest {

    //region GetLeastSignificantBits tests

    @Test
    public void GetLeastSignificantBits_GivenNumber255And3Bits_Return7() throws Exception {
        // Arrange
        int hashValue = 255;
        Hash32Bits hash = new Hash32Bits(hashValue);

        // Act
        int result = hash.getLeastSignificantBits(3);

        // Assert
        int expected = 7;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetLeastSignificantBits_GivenNumber255And0Bits_Return0() throws Exception {
        // Arrange
        int hashValue = 255;
        Hash32Bits hash = new Hash32Bits(hashValue);

        // Act
        int result = hash.getLeastSignificantBits(0);

        // Assert
        int expected = 0;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetLeastSignificantBits_GivenNegativeNumber255And5Bits_Return1() throws Exception {
        // Arrange
        int hashValue = -255;
        Hash32Bits hash = new Hash32Bits(hashValue);

        // Act
        int result = hash.getLeastSignificantBits(5);

        // Assert
        int expected = 1;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    //endregion

    //region GetMostSignificantBits tests

    @Test
    public void GetMostSignificantBits_Given256And30Bits_Return64() throws Exception {
        // Arrange
        int hashValue = 256;
        Hash32Bits hash = new Hash32Bits(hashValue);

        // Act
        int result = hash.getMostSignificantBits(30);

        // Assert
        int expected = 64;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetMostSignificantBits_GivenIntegerMaxAnd32Bits_Return255() throws Exception {
        // Arrange
        int hashValue = Integer.MIN_VALUE;
        Hash32Bits hash = new Hash32Bits(hashValue);

        // Act
        int result = hash.getMostSignificantBits(32);

        // Assert
        int expected = Integer.MIN_VALUE;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetMostSignificantBits_GivenIntegerMaxAnd5Bits_Return15() throws Exception {
        // Arrange
        int hashValue = Integer.MAX_VALUE;
        Hash32Bits hash = new Hash32Bits(hashValue);

        // Act
        int result = hash.getMostSignificantBits(5);

        // Assert
        int expected = 15; // Take into account sign bit is 0
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    @Test
    public void GetMostSignificantBits_GivenNegativeNumber1And5Bits_Return31() throws Exception {
        // Arrange
        int hashValue = -1; // -1 in complement 2 format is all 32 bits set to 1
        Hash32Bits hash = new Hash32Bits(hashValue);

        // Act
        int result = hash.getMostSignificantBits(5);

        // Assert
        int expected = 31;
        String msg = getErrorMessage(hashValue, expected, result);

        assertEquals(msg, expected, result);
    }

    //endregion


    //region Helper methods

    private String getErrorMessage(int hashValue, int expected, int result) {

        String hashInBinary = Integer.toBinaryString(hashValue);
        String expectedInBinary = Integer.toBinaryString(expected);
        String resultValueInBinary = Integer.toBinaryString(result);

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