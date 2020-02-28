package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Comment to this one
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

    public double daysUntil(ZonedDateTime when){
       double msSplit = date.until(when, ChronoUnit.MILLIS);
       return msSplit/MS_PER_DAY;
    }

    //A REVOIR
    public double julianCenturiesUntil(ZonedDateTime when){
        double msSplit = date.until(when, ChronoUnit.MILLIS);
        return msSplit/MS_PER_CENTURY;
    }

}
