package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.time.*;

import static ch.epfl.rigel.astronomy.SiderealTime.greenwich;
import static org.junit.jupiter.api.Assertions.*;

class MySiderealTimeTest {
    @Test
    void greenwichTest(){
        var time = ZonedDateTime.of(
                LocalDate.of(1980, Month.APRIL, 22),
                LocalTime.of(14, 36, 51).plusNanos((long) 6.7e+8),
                ZoneOffset.UTC);
        assertEquals(1.2221107819819774, greenwich(time), 1e-10);
    }
}