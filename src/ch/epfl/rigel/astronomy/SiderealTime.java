package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public final class SiderealTime {
    private SiderealTime(){}

    static double greenwich(ZonedDateTime when){
        ZonedDateTime whenInUTC = when.withZoneSameInstant(ZoneOffset.UTC);
        double julianCenturies = Epoch.J2000.julianCenturiesUntil(whenInUTC.truncatedTo(ChronoUnit.DAYS));
        double decimalMillis = whenInUTC.truncatedTo(ChronoUnit.DAYS)
                .until(whenInUTC, ChronoUnit.MILLIS);
        double decimalHours = decimalMillis/(3600.0*1000.0);

        Polynomial sidereal1 = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
        Polynomial sidereal2 = Polynomial.of(1.002737909);
        double siderealGreenwichInHours = sidereal1.at(julianCenturies) + sidereal2.at(decimalHours);
        return Angle.normalizePositive(Angle.ofHr(siderealGreenwichInHours));
    }

    static double local(ZonedDateTime when, GeographicCoordinates where){
        double siderealLocal = greenwich(when)+where.lon();
        return Angle.normalizePositive(siderealLocal);
    }
}
