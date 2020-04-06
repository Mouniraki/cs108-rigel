package ch.epfl.rigel.math;

/**
 * An interval.
 *
 * @author Mounir Raki (310287)
 */
public abstract class Interval {
    private final double low, high;
    protected Interval(double l, double h){
        low = l;
        high = h;
    }

    /**
     * Tests if a value is contained in the interval.
     *
     * @param v
     *          the value to test
     *
     * @return true if the value is contained in the interval, false otherwise
     */
    public abstract boolean contains(double v);

    /**
     * Redefines the equals method from java.lang.Object to throw an error.
     *
     * @param obj
     *          the interval to compare with this one (doesn't matter here)
     * @throws UnsupportedOperationException
     *          if the method is called
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * Redefines the hashCode method from java.lang.Object to throw an error.
     *
     * @throws UnsupportedOperationException
     *          if the method is called
     */
    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * Getter for the lower bound of the interval.
     *
     * @return the lower bound of the interval
     */
    public double low() {
        return low;
    }

    /**
     * Getter for the upper bound of the interval.
     *
     * @return the upper bound of the interval
     */
    public double high() {
        return high;
    }

    /**
     * Getter for the size of the interval.
     *
     * @return the size of the interval
     */
    public double size() {
        return high - low;
    }
}
