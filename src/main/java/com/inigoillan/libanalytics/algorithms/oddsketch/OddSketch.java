package com.inigoillan.libanalytics.algorithms.oddsketch;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.inigoillan.libanalytics.algorithms.hashers.Hash;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.BitSet;


/**
 * For details, please refer to the <a href="http://www.itu.dk/people/pagh/papers/oddsketch.pdf">OddSketch paper</a>
 *
 * @param <K> The type of {@link Hash} elements this sketch accepts
 */
public class OddSketch<K extends Hash> {
    private static final Logger LOG = Logger.getLogger(OddSketch.class);

    private BitSet sketch;

    private int size;
    private int elementsAdded = 0;


    /**
     * Constructor
     *
     * @param size The size of the sketch
     */
    public OddSketch(int size) {
        Preconditions.checkArgument(size > 0);

        sketch = new BitSet(size);
        sketch.clear();

        this.size = size;
    }

    /**
     * Adds a hashed element to the sketch
     */
    public void addHashed(@Nonnull K hashed) {
        int bucket = getBucket(hashed);

        xorIthBit(bucket);
        elementsAdded++;
    }

    /**
     * Estimates the set size for this Odd sketch based on the Markov Chain Model:
     *
     * <math xmlns="http://www.w3.org/1998/Math/MathML"><mover><mi>m</mi><mo>^</mo></mover><mo>=</mo><mfrac><mrow><mi>ln</mi><mfenced><mrow><mn>1</mn><mo>-</mo><mn>2</mn><mi>z</mi><mo>/</mo><mi>n</mi></mrow></mfenced></mrow><mrow><mi>ln</mi><mfenced><mrow><mn>1</mn><mo>-</mo><mn>2</mn><mo>/</mo><mi>n</mi></mrow></mfenced></mrow></mfrac></math>
     * <br>
     * where <math><mi>m</mi></math> is the size of the set,
     * <math><mi>z</mi></math> is the number of odd bins in the sketch,
     * and <math><mi>n</mi></math> is the total number of bins in the sketch
     * <br><br>
     *
     * Refer to the <a href="http://www.itu.dk/people/pagh/papers/oddsketch.pdf">OddSketch paper</a> for details.
     *
     * @return The set size estimated by the Odd sketch using the Markov Chain Model
     */
    public int estimateSetSize() {
        int z = sketch.cardinality();

        LOG.debug(String.format("Sketch cardinality is %d", z));

        double numerator = Math.log(1.0 - (2.0 * z / size));
        double denominator = Math.log(1.0 - (2.0 / size));

        double sizeEstimation = numerator / denominator;

        LOG.debug(String.format("When computing set size, the numerator is %f, denominator is %f and estimation is %f", numerator, denominator, sizeEstimation));

        return (int) Math.round(sizeEstimation);
    }

    /**
     *
     *
     * @param other
     * @return
     */
    public double computeJaccardIndex(@Nonnull OddSketch<Hash> other) {
        Preconditions.checkArgument(other.getSize() == this.getSize());
        Preconditions.checkArgument(other.elementsAdded == this.elementsAdded);

        int symmetricDifference = computeSymmetricDifference(other);

        int k = elementsAdded;
        int n = this.getSize();

        double inner = 1.0 - (2.0 * symmetricDifference / n);

        if (inner <= 0.0)
            return 0.0;

        double ln = Math.log(inner);
        return 1.0 + (n / (4.0 * k)) * ln;
    }

    protected int computeSymmetricDifference(@Nonnull OddSketch<Hash> other) {
        BitSet sketch = (BitSet) this.getSketch().clone();

        sketch.xor(other.getSketch());

        return sketch.cardinality();
    }

    protected int getBucket(Hash hash) {
        return hash.mod(size);
    }

    protected void xorIthBit(int i) {
        Preconditions.checkArgument(i >= 0, "The index needs to be positive");
        Preconditions.checkArgument(i < size, "Index has to be in the bounds set in the size parameter");

        sketch.flip(i);
    }


    protected int getSize() {
        return size;
    }

    @VisibleForTesting
    public void setSize(int size) {
        this.size = size;

        sketch = new BitSet(size);
        sketch.clear();
    }


    protected BitSet getSketch() {
        return this.sketch;
    }
}
