package ch.epfl.rigel.astronomy;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public enum Epoch {
    J2000(LocalDate.of(2000, Month.JANUARY, 1)),
    J2010(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1));
    private LocalDate date;

    private Epoch(LocalDate epochDate){
        date = epochDate;
    }

    public double daysUntil(ZonedDateTime when){
       double msSplit =  when.until(this.date, ChronoUnit.MILLIS);
       return msSplit/(1000.0 * 60.0 * 60.0 * 24.0);
    }

    public double julianCenturiesUntil(ZonedDateTime when){
        double msSplit = when.until(this.date, ChronoUnit.MILLIS);
        return msSplit/(1000.0 * 60.0 * 60.0 * 24.0);
    }

}
