package com.inigoillan.libanalytics.algorithms.oddsketch;

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
        Preconditions.checkArgument(size >= 0);

        this.setSize(size);

        BitSet sketch = new BitSet(size);
        sketch.clear();

        this.setSketch(sketch);
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
     * Estimates the set size of this Odd sketch.
     * Uses the Poisson Approximation by default to be consistent with the Jaccard Index estimation
     *
     * @return Returns the estimated set size
     */
    public int estimateSetSize() {
        return this.estimateSetSizeMarkovApproximation();
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
    protected int estimateSetSizeMarkovApproximation() {
        int z = sketch.cardinality();

        LOG.debug(String.format("Sketch cardinality is %d", z));

        double numerator = Math.log(1.0 - (2.0 * z / size));
        double denominator = Math.log(1.0 - (2.0 / size));

        double sizeEstimation = numerator / denominator;

        LOG.debug(String.format("When computing set size, the numerator is %f, denominator is %f and estimation is %f", numerator, denominator, sizeEstimation));

        return (int) Math.round(sizeEstimation);
    }

    /**
     * Estimates the set size for this Odd sketch based on the Poisson approximation:
     *
     * <math xmlns="http://www.w3.org/1998/Math/MathML"><mover><mi>m</mi><mo>^</mo></mover><mo>=</mo><mo>-</mo><mfrac><mi>n</mi><mn>2</mn></mfrac><mi>ln</mi><mfenced><mrow><mn>1</mn><mo>-</mo><mn>2</mn><mi>z</mi><mo>/</mo><mi>n</mi></mrow></mfenced></math>
     * <br>
     * where <math><mi>m</mi></math> is the size of the set,
     * <math><mi>z</mi></math> is the number of odd bins in the sketch,
     * and <math><mi>n</mi></math> is the number of bins in the sketch
     * <br><br>
     *
     * Refer to the <a href="http://www.itu.dk/people/pagh/papers/oddsketch.pdf">OddSketch paper</a> for details.
     *
     * @return The set size estimated by the Odd sketch using the Markov Chain Model
     */
    protected int estimateSetSizePoissonApproximation() {
        int z = sketch.cardinality();

        LOG.debug(String.format("Sketch cardinality is %d", z));

        double ln = Math.log(1.0 - (2.0 * z / size));

        double sizeEstimation = -size * ln / 2;

        LOG.debug(String.format("Set size estimation is %f", sizeEstimation));

        return (int) Math.round(sizeEstimation);
    }

    /**
     * Estimates the Jaccard Index for this Odd sketch:
     *
     * <math xmlns="http://www.w3.org/1998/Math/MathML"><mover><mi>J</mi><mo>^</mo></mover><mo>=</mo><mn>1</mn><mo>+</mo><mstyle displaystyle="false"><mfrac><mi>n</mi><mrow><mn>4</mn><mi>k</mi></mrow></mfrac></mstyle><mi>ln</mi><mfenced><mrow><mn>1</mn><mo>-</mo><mstyle displaystyle="false"><mfrac><mrow><mn>2</mn><mfenced open="|" close="|"><mrow><mi>o</mi><mi>d</mi><mi>d</mi><mo>(</mo><msub><mi>S</mi><mn>1</mn></msub><mo>)</mo><mo>&#8710;</mo><mi>o</mi><mi>d</mi><mi>d</mi><mo>(</mo><msub><mi>S</mi><mn>2</mn></msub><mo>)</mo></mrow></mfenced></mrow><mi>n</mi></mfrac></mstyle></mrow></mfenced><mspace linebreak="newline"/></math>
     * <br>
     * where <math><mi>J</mi></math> is the estimated Jaccard Index,
     * <math><mi>k</mi></math> is the number of elements added to the sketch,
     * <math><mfenced open="|" close="|"><mrow><mi>o</mi><mi>d</mi><mi>d</mi><mo>(</mo><msub><mi>S</mi><mn>1</mn></msub><mo>)</mo><mo>&#8710;</mo><mi>o</mi><mi>d</mi><mi>d</mi><mo>(</mo><msub><mi>S</mi><mn>2</mn></msub><mo>)</mo></mrow></mfenced><mspace linebreak="newline"/></math> is the symmetric difference (number of 1's in the xor-ed sketch)
     * and <math><mi>n</mi></math> is the number of bins in the sketch
     * <br><br>
     *
     * Refer to the <a href="http://www.itu.dk/people/pagh/papers/oddsketch.pdf">OddSketch paper</a> for details.
     *
     * @param other
     * @return
     */
    public double estimateJaccardIndex(@Nonnull OddSketch<Hash> other) {
        Preconditions.checkArgument(other.getSize() == this.getSize());
        Preconditions.checkArgument(other.elementsAdded == this.elementsAdded);

        if (this.elementsAdded == 0)
            return 1.0;

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

    /**
     * Flips (XOR) the bit in the ith position of the sketch
     *
     * @param index The posistion in the sketch to be flipped
     */
    protected void xorIthBit(int index) {
        Preconditions.checkArgument(index >= 0, "The index needs to be positive");
        Preconditions.checkArgument(index < size, "Index has to be in the bounds set in the size parameter");

        sketch.flip(index);
    }


    /**
     * Gets the size of the sketch.
     *
     * @return The size of the sketch
     */
    protected int getSize() {
        return size;
    }

    /**
     * Sets the size of the sketch. Take into account, you should instantiate a new sketch with the
     * {@link #setSketch(BitSet)} method in case the new size is bigger than the old one.
     * <br>
     * Tipically, you would use this method in case you are building a (de)serialization mechanism for this class
     *
     * @param size New size of the sketch
     */
    protected void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets the sketch in the form of a {@link BitSet}
     *
     * @return The sketch representation
     */
    protected BitSet getSketch() {
        return this.sketch;
    }

    /**
     * Sets the underlying sketch
     * <br>
     * Tipically, you would use this method in case you are building a (de)serialization mechanism for this class
     *
     * @param sketch The representation of the underlying sketch
     */
    protected void setSketch(BitSet sketch) {
        this.sketch = sketch;
    }
}
