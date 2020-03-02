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

        var march3_2020 = ZonedDateTime.of(
                LocalDate.of(2020, Month.MARCH, 3),
                LocalTime.of(23, 48, 32),
                ZoneOffset.UTC);

        var jan1_2000 = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0, 0),
                ZoneOffset.UTC);

        var dec31_2009 = ZonedDateTime.of(
                LocalDate.of(2009, Month.DECEMBER, 31),
                LocalTime.of(0, 0, 0),
                ZoneOffset.UTC);

        assertEquals(2.25, Epoch.J2000.daysUntil(jan3_2000));
        assertEquals(7365.1435416667, Epoch.J2000.daysUntil(march1_2020), 1e-10);
        assertEquals(-191.475011574, Epoch.J2010.daysUntil(june22_2009), 1e-10);
        assertEquals(3715.992037037, Epoch.J2010.daysUntil(march3_2020), 1e-10);

        assertEquals(0, Epoch.J2000.daysUntil(jan1_2000));
        assertEquals(0, Epoch.J2010.daysUntil(dec31_2009));
    }
    @Test
    void julianCenturiesUntilTest(){
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

        var march3_2020 = ZonedDateTime.of(
                LocalDate.of(2020, Month.MARCH, 3),
                LocalTime.of(23, 48, 32),
                ZoneOffset.UTC);

        var jan1_2000 = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0, 0),
                ZoneOffset.UTC);

        var dec31_2009 = ZonedDateTime.of(
                LocalDate.of(2009, Month.DECEMBER, 31),
                LocalTime.of(0, 0, 0),
                ZoneOffset.UTC);

        assertEquals(0.00006160164271, Epoch.J2000.julianCenturiesUntil(jan3_2000), 1e-10);
        assertEquals(0.2016466404, Epoch.J2000.julianCenturiesUntil(march1_2020), 1e-10);
        assertEquals(-0.005242300112, Epoch.J2010.julianCenturiesUntil(june22_2009), 1e-10);
        assertEquals(0.1017383172, Epoch.J2010.julianCenturiesUntil(march3_2020), 1e-10);
        assertEquals(0, Epoch.J2000.julianCenturiesUntil(jan1_2000));
        assertEquals(0, Epoch.J2010.julianCenturiesUntil(dec31_2009));
    }
}