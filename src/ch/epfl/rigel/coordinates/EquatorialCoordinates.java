package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Class allowing the creation of the equatorial coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
final public class EquatorialCoordinates extends SphericalCoordinates{
    private EquatorialCoordinates(double ra, double dec) {
        super(ra, dec);
    }

    /**
     * Constructs equatorial coordinates.
     * @param ra the right ascension (in radians)
     * @param dec the declination (in radians)
     * @return Equatorial coordinates
     */
    public static EquatorialCoordinates of(double ra, double dec){
        Preconditions.checkInInterval(RightOpenInterval.of(0, 24), Angle.toHr(ra));
        Preconditions.checkInInterval(ClosedInterval.of(-90, 90), Angle.toDeg(dec));

        return new EquatorialCoordinates(ra, dec);
    }

    /**
     * Returns the right ascension.
     * @return the right ascension
     */
    public double ra(){
        return super.lon();
    }

    /**
     * Returns the right ascension expressed in degrees.
     * @return the right ascension (in degrees)
     */
    public double raDeg(){
        return super.lonDeg();
    }

    /**
     * Returns the right ascension expressed in hours.
     * @return the right ascension (in hours)
     */
    public double raHr(){return Angle.toHr(super.lon());
    }

    /**
     * Returns the declination.
     * @return the declination
     */
    public double dec(){
        return super.lat();
    }

    /**
     * Returns the declination expressed in degrees.
     * @return the declination (in degrees)
     */
    public double decDeg(){
        return super.latDeg();
    }

    /**
     * Prints the equatorial coordinates.
     * @return print of the equatorial coordinates
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", raHr(), decDeg());
    }
}
