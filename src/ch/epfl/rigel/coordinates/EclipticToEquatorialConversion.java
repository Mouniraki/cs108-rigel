package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static java.lang.Math.*;

/**
 * The conversion from ecliptic to equatorial coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {
    private final double cosEclObl;
    private final double sinEclObl;
    private final static Polynomial P = Polynomial.of(
            Angle.ofArcsec(0.00181),
            -Angle.ofArcsec(0.0006),
            -Angle.ofArcsec(46.815),
            Angle.ofDMS(23, 26, 21.45));

    /**
     * Constructs a conversion from ecliptic to equatorial coordinates.
     *
     * @param when The date and time at the moment of the conversion
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double julianCenturiesUntilJ2000 = Epoch.J2000.julianCenturiesUntil(when);
        double eclObliquity = P.at(julianCenturiesUntilJ2000);
        cosEclObl = cos(eclObliquity);
        sinEclObl = sin(eclObliquity);
    }

    /**
     * Applies the conversion from ecliptic to equatorial coordinates.
     *
     * @param ecl The ecliptic coordinates to convert into equatorial coordinates
     *
     * @return The equatorial coordinates obtained from a conversion of the ecliptic coordinates
     */
    public EquatorialCoordinates apply(EclipticCoordinates ecl){
        double eclLon = ecl.lon();
        double eclLat = ecl.lat();
        double sinEclLon = sin(eclLon);

        double ra = atan2(
                        sinEclLon*cosEclObl - tan(eclLat)*sinEclObl,
                        cos(eclLon));
        double dec = asin(sin(eclLat)*cosEclObl + cos(eclLat)*sinEclObl*sinEclLon);

        return EquatorialCoordinates.of(Angle.normalizePositive(ra), dec);
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the equals() method.
     *
     * @throws UnsupportedOperationException The use of the equals() method is not supported.
     */
    @Override
    final public boolean equals(Object obj){
        throw new UnsupportedOperationException();
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the hashCode() method.
     *
     * @throws UnsupportedOperationException The use of the hashCode() method is not supported.
     */
    @Override
    final public int hashCode(){
        throw new UnsupportedOperationException();
    }
}
