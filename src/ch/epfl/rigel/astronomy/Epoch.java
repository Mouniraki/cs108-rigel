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

    //A REVOIR
    public double daysUntil(ZonedDateTime when){
        when.until(this., ChronoUnit.MILLIS);
    }

    //A REVOIR
    public double julianCenturiesUntil(ZonedDateTime when){
        when.until(this., ChronoUnit.MILLIS);
    }

}
