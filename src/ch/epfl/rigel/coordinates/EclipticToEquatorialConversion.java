package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * The conversion from ecliptic to equatorial coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {
    private final double cosEclObl;
    private final double sinEclObl;

    /**
     * Constructs a conversion from ecliptic to equatorial coordinates.
     *
     * @param when The date and time at the moment of the conversion
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        Polynomial p = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006), -Angle.ofArcsec(46.815), Angle.ofDMS(23, 26, 21.45));
        double T = Epoch.J2000.julianCenturiesUntil(when);
        double eclObliquity = p.at(T);
        cosEclObl = Math.cos(eclObliquity);
        sinEclObl = Math.sin(eclObliquity);
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
        double sinEclLon = Math.sin(eclLon);

        double ra = Math.atan2(
                        sinEclLon*cosEclObl - Math.tan(eclLat)*sinEclObl,
                        Math.cos(eclLon));
        double dec = Math.asin(Math.sin(eclLat)*cosEclObl + Math.cos(eclLat)*sinEclObl*sinEclLon);

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
