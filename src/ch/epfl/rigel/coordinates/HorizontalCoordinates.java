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
    private final static RightOpenInterval LONDEG_INTERVAL = RightOpenInterval.of(0, 360);
    private final static ClosedInterval LATDEG_INTERVAL = ClosedInterval.symmetric(180);
    private final static int OCTANT_VALUE = 45;
    private final static float OCTANT_OFFSET = 22.5f;

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
        Preconditions.checkInInterval(LONDEG_INTERVAL, Angle.toDeg(az));
        Preconditions.checkInInterval(LATDEG_INTERVAL, Angle.toDeg(alt));
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
        Preconditions.checkInInterval(LONDEG_INTERVAL, azDeg);
        Preconditions.checkInInterval(LATDEG_INTERVAL, altDeg);
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
        int index = azDeg() >= OCTANT_OFFSET ? (int) ((azDeg() - OCTANT_OFFSET) / OCTANT_VALUE) : 7;
        switch(index){
            case 0:
                return n+e;
            case 1:
                return e;
            case 2:
                return s+e;
            case 3:
                return s;
            case 4:
                return s+w;
            case 5 :
                return w;
            case 6 :
                return n+w;
            default:
                return n;
        }
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
