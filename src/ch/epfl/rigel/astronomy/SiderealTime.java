package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public final class SiderealTime {
    private SiderealTime(){}

    private final static double MIN_PER_HR = 60.0;
    private final static double SEC_PER_HR = 3600.0;

    static double greenwich(ZonedDateTime when){
        ZonedDateTime whenInGreenwich = when.withZoneSameInstant(ZoneOffset.UTC);
        double julianCenturies = Epoch.J2000.julianCenturiesUntil(whenInGreenwich.truncatedTo(ChronoUnit.DAYS));
        ZonedDateTime decHrInMs = whenInGreenwich.truncatedTo(ChronoUnit.MILLIS);

        double decimalHours = decHrInMs.getHour() + decHrInMs.getMinute()/MIN_PER_HR + decHrInMs.getSecond()/SEC_PER_HR;
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
