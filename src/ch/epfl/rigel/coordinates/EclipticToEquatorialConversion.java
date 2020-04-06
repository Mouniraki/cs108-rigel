package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Class allowing the conversion from ecliptic to equatorial coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    final private double cosEpsilon;
    final private double sinEpsilon;

    /**
     * Constructs a conversion from ecliptic to equatorial coordinates.
     * @param when The date and time of the conversion
     */
    public EclipticToEquatorialConversion(ZonedDateTime when) {
        Polynomial p = Polynomial.of(Angle.ofArcsec(0.00181), -Angle.ofArcsec(0.0006), -Angle.ofArcsec(46.815), Angle.ofDMS(23, 26, 21.45));
        double T = Epoch.J2000.julianCenturiesUntil(when);
        double epsilon = p.at(T);
        cosEpsilon = Math.cos(epsilon);
        sinEpsilon = Math.sin(epsilon);
    }

    /**
     * Applies the conversion from ecliptic to equatorial coordinates.
     * @param ecl The ecliptic coordinates to convert
     * @return The converted equatorial coordinates
     */
    public EquatorialCoordinates apply(EclipticCoordinates ecl){
        double lambda = ecl.lon();
        double beta = ecl.lat();
        double sinLambda = Math.sin(lambda);

        double alpha = Math.atan2(sinLambda*cosEpsilon - Math.tan(beta) * sinEpsilon, Math.cos(lambda));
        double delta = Math.asin(Math.sin(beta)*cosEpsilon + Math.cos(beta)*sinEpsilon*sinLambda);

        return EquatorialCoordinates.of(Angle.normalizePositive(alpha), delta);
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the equals() method.
     *
     * @throws UnsupportedOperationException
     */
    final public boolean equals(Object obj){
        throw new UnsupportedOperationException();
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the hashCode() method.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    final public int hashCode(){
        throw new UnsupportedOperationException();
    }
}
