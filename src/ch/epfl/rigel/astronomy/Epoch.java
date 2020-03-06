package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * An astronomical epoch.
 *
 * @author Mounir Raki (310287)
 */

public enum Epoch {
    J2000(ZonedDateTime.of(
            LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.of(12, 0),
            ZoneOffset.UTC)),

    J2010(ZonedDateTime.of(
            LocalDate.of(2010, Month.JANUARY, 1).minusDays(1),
            LocalTime.of(0, 0),
            ZoneOffset.UTC));

    private ZonedDateTime date;

    private static final double MS_PER_DAY = 1000 * 60 * 60 * 24;
    private static final double MS_PER_CENTURY = MS_PER_DAY * 365.25 * 100;

    private Epoch(ZonedDateTime epochDate){
        date = epochDate;
    }

    /**
     * Calculates the number of days between an epoch and an instant.
     *
     * @param when
     *          an instant (in ZonedDateTime)
     *
     * @return the number of days between the epoch and the instant
     */
    public double daysUntil(ZonedDateTime when){
       double msSplit = date.until(when, ChronoUnit.MILLIS);
       return msSplit/MS_PER_DAY;
    }

    /**
     * Calculates the number of julian centuries between an epoch and an instant.
     *
     * @param when
     *          an instant (in ZonedDateTime)
     *
     * @return the number of julian centuries between the epoch and the instant
     */
    public double julianCenturiesUntil(ZonedDateTime when){
        double msSplit = date.until(when, ChronoUnit.MILLIS);
        return msSplit/MS_PER_CENTURY;
    }

}
