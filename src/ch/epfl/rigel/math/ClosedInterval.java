package ch.epfl.rigel.math;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * A specific type of interval : a closed interval.
 *
 * @author Mounir Raki (310287)
 */
public final class ClosedInterval extends Interval{

    private ClosedInterval(double low, double high) {super(low, high);}

    /**
     * Constructs a closed interval from a lower bound and an upper bound.
     *
     * @param low
     *          lower bound of the interval to construct (must be lower than the upper bound)
     * @param high
     *          upper bound of the interval to construct (must be higher than the lower bound)
     * @throws IllegalArgumentException
     *          if the lower bound is greater than or equal to the upper bound
     *
     * @return a new closed interval
     */
    public static ClosedInterval of(double low, double high){
        checkArgument(low < high);
        return new ClosedInterval(low, high);
    }

    /**
     * Constructs a closed interval from the size of the desired interval.
     *
     * @param size
     *          the size that the interval has to be (must be greater than 0)
     * @throws IllegalArgumentException
     *          if the size of the interval is lower than or equal to 0
     *
     * @return a new closed interval
     */
    public static ClosedInterval symmetric(double size){
        checkArgument(size > 0);
        return new ClosedInterval(-(size / 2), size / 2);
    }

    /**
     * Clips an argument to the interval.
     *
     * @param v
     *          the argument to clip
     *
     * @return the clipped argument to the interval
     */
    public double clip(double v){
        if(v <= low()) return low();
        else return Math.min(v, high());
    }

    /**
     * Redefines the toString method from java.lang.Object to construct the textual representation of the closed interval.
     *
     * @return the textual representation of a closed interval
     */
    @Override
    public String toString() {return String.format(Locale.ROOT, "[%s,%s]", low(), high());}

    /**
     * Redefines the contains method from the Interval class to check if a value is contained in the interval.
     *
     * @return true if the value is contained in the interval, false otherwise
     */
    @Override
    public boolean contains(double v) {return (v <= high() && v >= low());}

}
