package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyEpochTest {
    @Test
    void daysUntilTest(){
        var jan3_2000 = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(18, 0),
                ZoneOffset.UTC);

        var march1_2020 = ZonedDateTime.of(
                LocalDate.of(2020, Month.MARCH, 1),
                LocalTime.of(15, 26, 42),
                ZoneOffset.UTC);

        var june22_2009 = ZonedDateTime.of(
                LocalDate.of(2009, Month.JUNE, 22),
                LocalTime.of(12, 35, 59),
                ZoneOffset.UTC);

        assertEquals(2.25, Epoch.J2000.daysUntil(jan3_2000));
        assertEquals(7365.1435416667, Epoch.J2000.daysUntil(march1_2020), 1e-10);
        assertEquals(-191.475011574, Epoch.J2010.daysUntil(june22_2009), 1e-10);
    }

    void julianCenturiesUntilTest(){


    }
}