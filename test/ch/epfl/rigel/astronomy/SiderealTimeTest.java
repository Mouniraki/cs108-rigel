package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class SiderealTimeTest {
    @Test
    void greenwichTest(){
        var time = SiderealTime.greenwich(ZonedDateTime.of(
                LocalDate.of(2000, Month.JANUARY, 3),
                LocalTime.of(23, 59, 59),
                ZoneOffset.UTC));

        assertEquals(23.9997, time);
    }
}