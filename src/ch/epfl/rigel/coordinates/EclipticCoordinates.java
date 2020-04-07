package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * A specific type of Spherical Coordinates : Ecliptic Coordinates.
 *
 * @author Mounir Raki (310287)
 */
public final class EclipticCoordinates extends SphericalCoordinates {
    private EclipticCoordinates(double lon, double lat){
        super(lon, lat);
    }

    /**
     * Constructs a set of ecliptic coordinates from a value of longitude (in radians)
     * and a value of latitude (in radians).
     *
     * @param lon
     *          the value of the longitude (in radians)
     * @param lat
     *          the value of the latitude (in radians)
     * @throws IllegalArgumentException
     *          if the longitude isn't in the interval [0°,360°[
     *          or if the latitude isn't in the interval [-90°,90°]
     *
     * @return a new set of ecliptic coordinates
     */
    public static EclipticCoordinates of(double lon, double lat) {
        Preconditions.checkInInterval(RightOpenInterval.of(0, 360), Angle.toDeg(lon));
        Preconditions.checkInInterval(ClosedInterval.symmetric(180), Angle.toDeg(lat));
        return new EclipticCoordinates(lon, lat);
    }

    /**
     * Getter for the value of the longitude (in radians).
     *
     * @return the value of the longitude (in radians)
     */
    public double lon() {
        return super.lon();
    }

    /**
     * Getter for the value of the longitude (in degrees).
     *
     * @return the value of the longitude (in degrees)
     */
    public double lonDeg() {
        return super.lonDeg();
    }

    /**
     * Getter for the value of the latitude (in radians).
     *
     * @return the value of the latitude (in radians)
     */
    public double lat() {
        return super.lat();
    }

    /**
     * Getter for the value of the latitude (in degrees).
     *
     * @return the value of the latitude (in degrees)
     */
    public double latDeg() {
        return super.latDeg();
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of a point
     * expressed in the Ecliptic Coordinates.
     *
     * @return the textual representation of the point in the Ecliptic Coordinates
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f°, β=%.4f°)", lonDeg(), latDeg());
    }
}
