package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.SiderealTime.greenwich;
import static ch.epfl.rigel.astronomy.SiderealTime.local;
import static org.junit.jupiter.api.Assertions.*;

class MySiderealTimeTest {

    @Test
    void greenwichTest(){
        var april22_1980 = ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51).plusNanos((long) 6.7e+8),
                ZoneOffset.UTC);

        var jan1_1970 = ZonedDateTime.of(
                LocalDate.of(1970, Month.JANUARY, 1),
                LocalTime.of(16, 35, 47).plusNanos((long) 5.8e+8),
                ZoneOffset.UTC);

        var september11_2001 = ZonedDateTime.of(
                LocalDate.of(2001, Month.SEPTEMBER, 11),
                LocalTime.of(10, 51, 26).plusNanos((long) 7.2e+8),
                ZoneOffset.UTC);

        var may14_1999 = ZonedDateTime.of(
                LocalDate.of(1999, Month.MAY, 14),
                LocalTime.of(7, 35, 48).plusNanos((long) 1.4e+8),
                ZoneOffset.UTC);

        var jan24_2000 = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 24),
                LocalTime.of(8, 45, 01).plusNanos((long) 4.9e+8),
                ZoneOffset.UTC);

        var june18_2020 = ZonedDateTime.of(
                LocalDate.of(2020, Month.JUNE, 18),
                LocalTime.of(17, 55, 48).plusNanos((long) 7.4e+8),
                ZoneOffset.UTC);

        var march8_2017 = ZonedDateTime.of(
                LocalDate.of(2017, Month.MARCH, 8),
                LocalTime.of(12, 0, 0),
                ZoneOffset.UTC);

        var jan1_2000 = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0, 0),
                ZoneOffset.UTC);

        var dec31_2009 = ZonedDateTime.of(
                LocalDate.of(2009, Month.DECEMBER, 31),
                LocalTime.of(0, 0, 0),
                ZoneOffset.UTC);

        assertEquals(1.2221107819819774, greenwich(april22_1980), 1e-8);
        assertEquals(6.106200001522218, greenwich(jan1_1970), 1e-8);
        assertEquals(2.677173579861608, greenwich(september11_2001), 1e-8);
        assertEquals(6.031162825371777, greenwich(may14_1999), 1e-8);
        assertEquals(4.437556520056533, greenwich(jan24_2000), 1e-8);
        assertEquals(3.0785108030466, greenwich(june18_2020), 1e-8);

        assertEquals(4.894961210641396, greenwich(jan1_2000), 1e-8);
        assertEquals(1.7375098083318568, greenwich(dec31_2009), 1e-8);
        assertEquals(6.045532514675193, greenwich(march8_2017), 1e-8);
    }

    @Test
    void localTest(){
        var april22_1980 = ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51).plusNanos((long) 6.7e+8),
                ZoneOffset.UTC);

        var jan1_1970 = ZonedDateTime.of(
                LocalDate.of(1970, Month.JANUARY, 1),
                LocalTime.of(16, 35, 47).plusNanos((long) 5.8e+8),
                ZoneOffset.UTC);

        var september11_2001 = ZonedDateTime.of(
                LocalDate.of(2001, Month.SEPTEMBER, 11),
                LocalTime.of(10, 51, 26).plusNanos((long) 7.2e+8),
                ZoneOffset.UTC);

        var may5_1999 = ZonedDateTime.of(
                LocalDate.of(1999, Month.MAY, 14),
                LocalTime.of(7, 35, 48).plusNanos((long) 1.4e+8),
                ZoneOffset.UTC);

        var jan24_2000 = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 24),
                LocalTime.of(8, 45, 01).plusNanos((long) 4.9e+8),
                ZoneOffset.UTC);

        var june18_2020 = ZonedDateTime.of(
                LocalDate.of(2020, Month.JUNE, 18),
                LocalTime.of(17, 55, 48).plusNanos((long) 7.4e+8),
                ZoneOffset.UTC);

        var jan1_2000 = ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 1),
                LocalTime.of(12, 0, 0),
                ZoneOffset.UTC);

        var dec31_2009 = ZonedDateTime.of(
                LocalDate.of(2009, Month.DECEMBER, 31),
                LocalTime.of(0, 0, 0),
                ZoneOffset.UTC);

        assertEquals(Angle.normalizePositive(1.2221107819819774+Angle.ofDeg(-64)), local(april22_1980, GeographicCoordinates.ofDeg(-64, 0)), 1e-8);
        assertEquals(Angle.normalizePositive(6.106200001522218+Angle.ofDeg(30)), local(jan1_1970, GeographicCoordinates.ofDeg(30, 0)), 1e-8);
        assertEquals(Angle.normalizePositive(2.677173579861608+Angle.ofDeg(90)), local(september11_2001, GeographicCoordinates.ofDeg(90, 0)), 1e-8);
        assertEquals(Angle.normalizePositive(6.031162825371777+Angle.ofDeg(0)), local(may5_1999, GeographicCoordinates.ofDeg(0, 0)), 1e-8);
        assertEquals(Angle.normalizePositive(4.437556520056533+Angle.ofDeg(-130)), local(jan24_2000, GeographicCoordinates.ofDeg(-130, 0)), 1e-8);
        assertEquals(Angle.normalizePositive(3.0785108030466+Angle.ofDeg(-20)), local(june18_2020, GeographicCoordinates.ofDeg(-20, 0)), 1e-8);

        assertEquals(Angle.normalizePositive(4.894961210641396+Angle.ofDeg(-179)), local(jan1_2000, GeographicCoordinates.ofDeg(-179, 0)), 1e-8);
        assertEquals(Angle.normalizePositive(1.7375098083318568+Angle.ofDeg(179)), local(dec31_2009, GeographicCoordinates.ofDeg(179, 0)), 1e-8);
    }
}