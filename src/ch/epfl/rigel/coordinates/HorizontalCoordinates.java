package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * A specific type of Spherical Coordinates : Horizontal Coordinates.
 *
 * @author Mounir Raki (310287)
 */
public final class HorizontalCoordinates extends SphericalCoordinates {
    private HorizontalCoordinates(double az, double alt){
        super(az, alt);
    }

    /**
     * Constructs a set of Horizontal Coordinates from a value of azimuth (in radians)
     * and a value of altitude (in radians).
     *
     * @param az
     *          the value of the azimuth (in radians)
     * @param alt
     *          the value of the altitude (in radians)
     * @throws IllegalArgumentException
     *          if the azimuth isn't in the interval [0°,360°[
     *          or if the altitude isn't in the interval [-90°,90°]
     *
     * @return a new set of Horizontal Coordinates
     */
    public static HorizontalCoordinates of(double az, double alt) {
        Preconditions.checkInInterval(RightOpenInterval.of(0, 360), Angle.toDeg(az));
        Preconditions.checkInInterval(ClosedInterval.symmetric(180), Angle.toDeg(alt));
        return new HorizontalCoordinates(az, alt);
    }

    /**
     * Constructs a set of Horizontal Coordinates from a value of azimuth (in degrees)
     * and a value of altitude (in degrees).
     *
     * @param azDeg
     *          the value of the azimuth (in degrees)
     * @param altDeg
     *          the value of the altitude (in degrees)
     * @throws IllegalArgumentException
     *          if the azimuth isn't in the interval [0°,360°[
     *          or if the altitude isn't in the interval [-90°,90°]
     *
     * @return a new set of Horizontal Coordinates
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        Preconditions.checkInInterval(RightOpenInterval.of(0, 360), azDeg);
        Preconditions.checkInInterval(ClosedInterval.symmetric(180), altDeg);
        return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }

    /**
     * Getter for the azimuth in radians.
     *
     * @return the value of the azimuth (in radians).
     */
    public double az() {
        return super.lon();
    }

    /**
     * Getter for the azimuth in degrees.
     *
     * @return the value of the azimuth (in degrees).
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * Getter for the altitude in radians.
     *
     * @return the value of the altitude (in radians).
     */
    public double alt() {
        return super.lat();
    }

    /**
     * Getter for the altitude in degrees.
     *
     * @return the value of the altitude (in degrees).
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
     * Constructs the textual representation of the octant in which the azimuth value is localized.
     *
     * @param n
     *          the string value to assign to the northern coordinate
     * @param e
     *          the string value to assign to the eastern coordinate
     * @param s
     *          the string value to assign to the southern coordinate
     * @param w
     *          the string value to assign to the western coordinate
     *
     * @return the textual representation of the octant in which the azimuth value is localized.
     */
    public String azOctantName(String n, String e, String s, String w) {
        if(RightOpenInterval.of(22.5, 67.5).contains(azDeg())) return n+e;
        else if(RightOpenInterval.of(67.5, 112.5).contains(azDeg())) return e;
        else if(RightOpenInterval.of(112.5, 157.5).contains(azDeg())) return s+e;
        else if(RightOpenInterval.of(157.5, 202.5).contains(azDeg())) return s;
        else if(RightOpenInterval.of(202.5, 247.5).contains(azDeg())) return s+w;
        else if(RightOpenInterval.of(247.5, 292.5).contains(azDeg())) return w;
        else if(RightOpenInterval.of(292.5, 337.5).contains(azDeg())) return n+w;
        else return n;
    }

    /**
     * Calculates the angular distance between two points expressed in the Horizontal Coordinates.
     *
     * @param that
     *          the point to compare with (in horizontal coordinates)
     *
     * @return the angular distance between the two points (in radians)
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        return Math.acos(Math.sin(this.alt())*Math.sin(that.alt()) + Math.cos(this.alt())*Math.cos(that.alt())*Math.cos(this.az() - that.az()));
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of a point
     * expressed in the Horizontal Coordinates.
     *
     * @return the textual representation of the point in the Horizontal Coordinates
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(), altDeg());
    }
}
