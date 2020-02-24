package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Class allowing the creation of the geographic coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
final public class GeographicCoordinates extends SphericalCoordinates{

    private GeographicCoordinates(double lon, double lat) {
        super(lon, lat);
        Preconditions.checkInInterval(RightOpenInterval.of(-180, 180), Angle.toDeg(lon));
        Preconditions.checkInInterval(ClosedInterval.of(-90, 90), Angle.toDeg(lat));
    }

    /**
     * Constructs geographic coordinates.
     * @param lonDeg longitude expressed in degrees
     * @param latDeg latitude expressed in degrees
     * @return Geographic coordinates
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){
        Preconditions.checkInInterval(RightOpenInterval.symmetric(360), lonDeg);
        Preconditions.checkInInterval(ClosedInterval.symmetric(180), latDeg);

        return new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg));
    }

    /**
     * Verifies if the angle (expressed in degrees) represents a valid longitude.
     * @param lonDeg the longitude expressed in degrees
     * @return true if the longitude is valid, false otherwise
     */
    public static boolean isValidLonDeg(double lonDeg){
        return (lonDeg >= -180 && lonDeg < 180);
    }

    /**
     * Verifies if the angle (expressed in degrees) represents a valid latitude.
     * @param latDeg the latitude expressed in degrees
     * @return true if the latitude is valid, false otherwise
     */
    public static boolean isValidLatDeg(double latDeg){
        return (latDeg >= -90 && latDeg <= 90);
    }

    /**
     * Returns the longitude.
     * @return the longitude
     */
    @Override
    public double lon(){
        return super.lon();
    }

    /**
     * Returns the longitude expressed in degrees.
     * @return the longitude (in degrees)
     */
    @Override
    public double lonDeg(){
        return super.lonDeg();
    }

    /**
     * Returns the latitude.
     * @return the latitude
     */
    @Override
    public double lat(){
        return super.lat();
    }

    /**
     * Returns the latitude expressed in degrees.
     * @return the latitude in degrees
     */
    @Override
    public double latDeg(){
        return super.latDeg();
    }

    /**
     * Prints the geographic coordinates.
     * @return print of the geographic coordinates
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg());
    }
}
