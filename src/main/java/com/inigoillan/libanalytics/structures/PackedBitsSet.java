package com.inigoillan.libanalytics.structures;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import sun.misc.Unsafe;

import javax.annotation.Nonnegative;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;

/**
 *
 * @author <a href="mailto:inigo.illan@gmail.com">Inigo Illan</a>
 * @since 1.0
 */
public class PackedBitsSet {
    private final int bitSize;
    private final int setSize;

    private final long[] set;

    public PackedBitsSet(@Nonnegative int bitSize, @Nonnegative int setSize) {
        Preconditions.checkArgument(bitSize > 0, "Bit size parameter should be a positive non-zero value");
        Preconditions.checkArgument(bitSize < 32, "Bit size can't be longer than 32 bits");
        Preconditions.checkArgument(setSize > 0, "Set size parameter should be a positive non-zero value");

        this.bitSize = bitSize;
        this.setSize = setSize;

        int size = (int) Math.ceil((double) bitSize * setSize / getLongSizeInBits());
        set = new long[size];
    }

    public void setIthBits(@Nonnegative int position, int value) {
        Preconditions.checkArgument((1 << bitSize) > value, "The value can't be represented by the bit size");

        int bucket = getBucket(position);
        int bucketLastBit = getLastBitBucket(position);

        if (bucket == bucketLastBit) {
            int offset = (position * bitSize) % getLongSizeInBits();

            setIthBitInBucket(bucket, offset, value);
        } else {
            int offset = (position * bitSize) % getLongSizeInBits();
            int remanent = getLongSizeInBits() - offset;


            int firstValue = value >> remanent;
            int secondValue = value & ((1 << remanent) - 1);
            setIthBitInBucket(bucket, offset, secondValue);
            setIthBitInBucket(bucketLastBit, 0, firstValue);
        }
    }

    private void setIthBitInBucket(@Nonnegative int bucket, @Nonnegative int offset, int value) {
        long longValue = value;
        long bucketValue = set[bucket];

        // We remove everything on that space so we can OR with the actual value
        long mask = ~(((1L << bitSize) - 1) << offset);
        bucketValue &= mask;

        // We write the new value by ORing
        bucketValue |= (longValue << offset);

        set[bucket] = bucketValue;
    }

    public int getIthBits(@Nonnegative int position) {
        Preconditions.checkArgument(this.setSize >= position, "Bounds check exception");

        int bucket = getBucket(position);
        int bucketLastBit = getLastBitBucket(position);

        if (bucket == bucketLastBit) {
            int offset = (position * bitSize) % getLongSizeInBits();

            return getIthBitsInBucket(bucket, offset, bitSize);
        } else {
            int offset = (position * bitSize) % getLongSizeInBits();
            int remanent = getLongSizeInBits() - offset;

            int secondValue = getIthBitsInBucket(bucket, offset, remanent);
            int firstValue = getIthBitsInBucket(bucketLastBit, 0, bitSize - remanent);

            return (firstValue << remanent) + secondValue;
        }

    }

    private int getIthBitsInBucket(int bucket, int offset, int bits) {
        long bucketValue = set[bucket];
        long mask = (((1L << bits) - 1) << offset);
        bucketValue &= mask;
        long result = bucketValue >>> offset;

        return (int) result;
    }

    private int getBucket(int position) {
        return (int) Math.floor((double) position * bitSize / getLongSizeInBits());
    }

    private int getLastBitBucket(int position) {
        return (int) Math.floor((double) (((position + 1) * bitSize) - 1) / getLongSizeInBits());
    }

    private int getLongSizeInBits() {
        return 8 * Long.BYTES;
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper stringHelper = MoreObjects.toStringHelper(this.getClass())
                .add("bit size", bitSize)
                .add("set size", setSize)
                .add("set", setArrayToBinaryString());

        return stringHelper.toString();
    }

    private String setArrayToBinaryString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ ");

        for (int i = set.length - 1; i > 0; i--) {
            builder.append(Long.toBinaryString(set[i]).length() + ":");
            builder.append(Long.toBinaryString(set[i]));
            builder.append(", ");
        }

        builder.append(Long.toBinaryString(set[0]).length() + ":");
        builder.append(Long.toBinaryString(set[0]));

        builder.append(" }");
        return builder.toString();
    }
}
