package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * A specific type of Spherical Coordinates : Geographic Coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class GeographicCoordinates extends SphericalCoordinates{
    private final static RightOpenInterval LONDEG_INTERVAL = RightOpenInterval.symmetric(360);
    private final static ClosedInterval LATDEG_INTERVAL = ClosedInterval.symmetric(180);

    private GeographicCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    /**
     * Constructs new Geographic Coordinates.
     *
     * @param lonDeg longitude expressed in degrees
     * @param latDeg latitude expressed in degrees
     *
     * @return a new instance of Geographic Coordinates
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){
        Preconditions.checkInInterval(LONDEG_INTERVAL, lonDeg);
        Preconditions.checkInInterval(LATDEG_INTERVAL, latDeg);

        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    /**
     * Checks if the angle (expressed in degrees) represents a valid longitude.
     *
     * @param lonDeg the longitude expressed in degrees
     *
     * @return true if the longitude is valid, false otherwise
     */
    public static boolean isValidLonDeg(double lonDeg){
        return (lonDeg >= -180 && lonDeg < 180);
    }

    /**
     * Checks if the angle (expressed in degrees) represents a valid latitude.
     *
     * @param latDeg the latitude expressed in degrees
     *
     * @return true if the latitude is valid, false otherwise
     */
    public static boolean isValidLatDeg(double latDeg){
        return (latDeg >= -90 && latDeg <= 90);
    }

    /**
     * Returns the longitude.
     *
     * @return the longitude
     */
    @Override
    public double lon(){
        return super.lon();
    }

    /**
     * Returns the longitude expressed in degrees.
     *
     * @return the longitude (in degrees)
     */
    @Override
    public double lonDeg(){
        return super.lonDeg();
    }

    /**
     * Returns the latitude.
     *
     * @return the latitude
     */
    @Override
    public double lat(){
        return super.lat();
    }

    /**
     * Returns the latitude expressed in degrees.
     *
     * @return the latitude (in degrees)
     */
    @Override
    public double latDeg(){
        return super.latDeg();
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of a point
     * expressed in the Geographic Coordinates.
     *
     * @return the textual representation of the point in the Geographic Coordinates
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
    }
}
