package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyEpochTest {
    @Test
    void daysUntilTest(){
        var d1 = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);

        var d2 = ZonedDateTime.of(
                LocalDate.of(2010, Month.JANUARY, 2),
                LocalTime.of(6, 0),
                ZoneOffset.UTC);

        assertEquals(2.25, Epoch.J2000.daysUntil(d1));
        assertEquals(2.25, Epoch.J2010.daysUntil(d2));
    }

    void julianCenturiesUntilTest(){


    }
}