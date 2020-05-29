package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * A sidereal time.
 *
 * @author Mounir Raki (310287)
 */
public final class SiderealTime {
    private final static double MS_PER_HR = 3600.0 * 1000.0;
    private final static Polynomial P1 = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
    private final static double P2_COEFFICIENT = 1.002737909;

    private SiderealTime(){}

    /**
     * Calculates the greenwich sidereal time from a couple date/time in radians.
     *
     * @param when
     *          a couple date/time (in ZonedDateTime)
     *
     * @return the greenwich sidereal time, in radians and normalized to the interval [0, TAU[
     */
    public static double greenwich(ZonedDateTime when){
        ZonedDateTime whenInUTC = when.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime whenInDaysOnly = whenInUTC.truncatedTo(ChronoUnit.DAYS);

        double julianCenturiesUntilWhen = Epoch.J2000.julianCenturiesUntil(whenInDaysOnly);
        double hoursInWhen = whenInDaysOnly.until(whenInUTC, ChronoUnit.MILLIS) / MS_PER_HR;

        double S0 = P1.at(julianCenturiesUntilWhen);
        double S1 = P2_COEFFICIENT * hoursInWhen;

        double gst = Angle.ofHr(S0 + S1);
        return Angle.normalizePositive(gst);
    }

    /**
     * Calculates the local sidereal time in radians, from a couple date/time
     * and a geographic coordinate of the location.
     *
     * @param when
     *          a couple date/time (in ZonedDateTime)
     * @param where
     *          the coordinates of the location from where to find the sidereal time (in GeographicCoordinates)
     *
     * @return the local sidereal time, in radians and normalized to the interval [0, TAU[
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where){
        return Angle.normalizePositive(greenwich(when) + where.lon());
    }
}
