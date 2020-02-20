package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

import java.util.Locale;

/**
 * A specific type of interval : a right-open interval.
 *
 * @author Mounir Raki (310287)
 */
public final class RightOpenInterval extends Interval{
    private RightOpenInterval(double low, double high){super(low, high);}

    /**
     * Constructs a right-open interval from a lower bound and an upper bound.
     *
     * @param low
     *          lower bound of the interval to construct (must be lower than the upper bound)
     * @param high
     *          upper bound of the interval to construct (must be higher than the lower bound)
     * @throws IllegalArgumentException
     *          if the lower bound is greater than or equal to the upper bound
     *
     * @return a new right-open interval
     */
    public static RightOpenInterval of(double low, double high){
        checkArgument(low < high);
        return new RightOpenInterval(low, high);
    }

    /**
     * Constructs a right-open interval from the size of the desired interval.
     *
     * @param size
     *          the size that the interval has to be (must be greater than 0)
     * @throws IllegalArgumentException
     *          if the size of the interval is lower than or equal to 0
     *
     * @return a new right-open interval
     */
    public static RightOpenInterval symmetric(double size){
        checkArgument(size > 0);
        return new RightOpenInterval(-(size/2), size/2);
    }

    /**
     * Reduces an argument to the interval.
     *
     * @param v
     *          the argument to reduce
     *
     * @return the reduced argument to the interval
     */
    public double reduce(double v) {return low()+floorMod(v-low(), high()-low());}

    private double floorMod(double a, double b) {return a - b*Math.floor(a/b);}

    /**
     * Redefines the toString method from java.lang.Object to construct
     * the textual representation of the right-open interval.
     *
     * @return the textual representation of a closed interval
     */
    @Override
    public String toString() {return String.format(Locale.ROOT, "[%s,%s[", low(), high());}

    /**
     * Redefines the contains method from the Interval class to check if a value is contained in the interval.
     *
     * @return true if the value is contained in the interval, false otherwise
     */
    @Override
    public boolean contains(double v) {return (v<high() && v>=low());}

}
