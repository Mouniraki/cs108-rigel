package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;
import static ch.epfl.rigel.Preconditions.checkInInterval;

/**
 * Set of methods and constants used to manipulate angles in different units.
 *
 * @author Mounir Raki (310287)
 */
public final class Angle {
    private Angle(){}

    /**
     * Defines the constant TAU.
     */
    public final static double TAU = 2 * Math.PI;

    private static final double HR_PER_RAD = 24 / TAU;
    private static final double RAD_PER_HR = TAU / 24;

    private static final double DEG_PER_MIN = 1.0 / 60.0;
    private static final double DEG_PER_SEC = 1.0 / 3600.0;

    private static final double MIN_PER_HR_OR_SEC_PER_MIN = 60;

    private final static RightOpenInterval MIN_SEC_INTERVAL = RightOpenInterval.of(0, MIN_PER_HR_OR_SEC_PER_MIN);
    private final static RightOpenInterval RAD_INTERVAL = RightOpenInterval.of(0, TAU);
    /**
     * Normalizes an angle in the interval going from 0 (included) to TAU (excluded).
     *
     * @param rad
     *          the angle to normalize (in radians)
     *
     * @return the normalized angle (in radians)
     */
    public static double normalizePositive(double rad) {
        return RAD_INTERVAL.reduce(rad);
    }

    /**
     * Converts an angle from arc seconds to radians.
     *
     * @param sec
     *          the angle to convert (in arc seconds)
     *
     * @return the converted angle (in radians)
     */
    public static double ofArcsec(double sec) {
        return Math.toRadians(sec * DEG_PER_SEC);
    }

    /**
     * Converts an angle from degrees minutes seconds to radians.
     *
     * @param deg
     *          the degrees part of the angle expression
     * @param min
     *          the arc minutes part of the angle expression (must be between 0 (included) and 60 (excluded))
     * @param sec
     *          the arc seconds part of the angle expression (must be between 0 (included) and 60 (excluded))
     *
     * @throws IllegalArgumentException
     *          if the minutes or seconds are lower than 0 or greater than 60
     *
     * @return the converted angle (in radians)
     */
    public static double ofDMS(int deg, int min, double sec){
        checkArgument(deg >= 0);
        checkInInterval(MIN_SEC_INTERVAL, min);
        checkInInterval(MIN_SEC_INTERVAL, sec);
        return Math.toRadians(deg + min*DEG_PER_MIN + sec*DEG_PER_SEC);
    }

    /**
     * Converts an angle from degrees to radians.
     *
     * @param deg
     *          the angle to convert (in degrees)
     *
     * @return the converted angle (in radians)
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Converts an angle from radians to degrees.
     *
     * @param rad
     *          the angle to convert (in radians)
     *
     * @return the converted angle (in degrees)
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Converts an angle from hours to radians.
     *
     * @param hr
     *          the angle to convert (in hours)
     *
     * @return the converted angle (in radians)
     */
    public static double ofHr(double hr) {
        return hr * RAD_PER_HR;
    }

    /**
     * Converts an angle from radians to hours.
     *
     * @param rad
     *          the angle to convert (in radians)
     *
     * @return the converted angle (in hours)
     */
    public static double toHr(double rad) {
        return rad * HR_PER_RAD;
    }
}
