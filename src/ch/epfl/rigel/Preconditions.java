package ch.epfl.rigel;
import ch.epfl.rigel.math.Interval;


/**
 * Set of preconditions to use in the program.
 *
 * @author Mounir Raki (310287)
 */
public final class Preconditions {
    private Preconditions(){}

    /**
     * Checks if a condition given in parameters is true, throws an error otherwise.
     *
     * @param isTrue
     *          a boolean condition to check (must be true)
     * @throws IllegalArgumentException
     *          if the condition is false
     */
    public static void checkArgument(boolean isTrue){
        if(!isTrue){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if a value is in an interval.
     *
     * @param interval
     *          the interval where to check if the value is contained or not
     * @param value
     *          the value to test (must be in the interval)
     * @throws IllegalArgumentException
     *          if the value is not contained in the interval
     *
     * @return the value if it is contained in the interval
     */
    public static double checkInInterval(Interval interval, double value){
        if(!interval.contains(value))
            throw new IllegalArgumentException("The value is not contained in the given interval !");
        return value;
    }
}
