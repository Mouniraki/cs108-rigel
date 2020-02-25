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
    final private double ra;
    final private double dec;

    private EquatorialCoordinates(double ra, double dec) {
        super(ra, dec);
        this.ra = ra;
        this.dec = dec;
    }

    /**
     * Constructs equatorial coordinates.
     * @param ra the right ascension
     * @param dec the declination
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
        return ra;
    }

    /**
     * Returns the right ascension expressed in degrees.
     * @return the right ascension (in degrees)
     */
    public double raDeg(){
        return Angle.toDeg(ra);
    }

    /**
     * Returns the right ascension expressed in hours.
     * @return the right ascension (in hours)
     */
    public double raHr(){
        return Angle.toHr(ra);
    }

    /**
     * Returns the declination.
     * @return the declination
     */
    public double dec(){
        return dec;
    }

    /**
     * Returns the declination expressed in degrees.
     * @return the declination (in degrees)
     */
    public double decDeg(){
        return Angle.toDeg(dec);
    }

    /**
     * Prints the equatorial coordinates.
     * @return print of the equatorial coordinates
     */
    @Override
    public String toString(){
        return String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", Angle.toHr(ra), Angle.toDeg(dec));
    }
}
