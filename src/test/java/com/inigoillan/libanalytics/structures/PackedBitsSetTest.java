package com.inigoillan.libanalytics.structures;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class PackedBitsSetTest {
    @Test
    public void GetIthBits_1BitSize64SetSizeAndPosition32_Returns0() {
        // Arrange
        PackedBitsSet set = new PackedBitsSet(1, 64);

        // Act
        int result = set.getIthBits(32);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void GetIthBits_1BitSize64SetSizeAndPosition32_Returns1AfterBeingSet() {
        // Arrange
        PackedBitsSet set = new PackedBitsSet(1, 64);
        int value = 1;

        // Act
        int result = setAndGetIthBits(set, 32, value);

        // Assert
        assertEquals(value, result);
    }

    @Test
    public void GetIthBits_20BitSize128SetSizeAndPosition63_ReturnsValueAfterBeingSet() {
        // Arrange
        PackedBitsSet set = new PackedBitsSet(20, 10);
        int value = (1 << 20) - 19;

        // Act
        int result = setAndGetIthBits(set, 3, value);

        // Assert
        assertEquals(value, result);
    }

    @Test
    public void GetIthBits_RandomInsertions_ReturnsCorrectValues() {
        // Arrange
        int setSize = 128;
        int bits = 10;
        int mask = (int) Math.pow(2, bits) - 1;

        PackedBitsSet set = new PackedBitsSet(bits, setSize);
        Random random = new Random();
        ArrayList<Integer> insertions = Lists.newArrayList();

        // Act
        for (int i = 0; i < setSize; i++) {
            int value = random.nextInt();
            value &= mask;
            set.setIthBits(i, value);
            insertions.add(i, value);
        }

        System.out.println(set);
        // Assert
        for (int i = 0; i < setSize; i++) {
            int result = set.getIthBits(i);
            assertEquals(String.format("Expected and got:\n%s\n%s", Integer.toBinaryString(insertions.get(i)), Integer.toBinaryString(result) ), (int) insertions.get(i), result);
        }
    }


    private int setAndGetIthBits(PackedBitsSet set, int position, int value) {
        set.setIthBits(position, value);
        System.out.println(set);
        return set.getIthBits(position);
    }


}