package ch.epfl.rigel.gui;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyTimeAcceleratorTest {
    private final static ZonedDateTime ZDT = ZonedDateTime.of(
            LocalDate.of(2020, Month.APRIL, 20),
            LocalTime.of(21, 0, 0),
            ZoneOffset.UTC);

    @Test
    void continuousWorksWithWebsiteExample(){
        long realElapsedTime = (long) (2.34 * 1e9);
        int speedFactor = 300;

        var expectedZDT = ZonedDateTime.of(
                LocalDate.of(2020, Month.APRIL, 20),
                LocalTime.of(21, 11, 42),
                ZoneOffset.UTC);

        assertEquals(expectedZDT, TimeAccelerator.continuous(speedFactor).adjust(ZDT, realElapsedTime));
    }

    @Test
    void discreteWorksWithWebsiteExample(){
        long realElapsedTime = (long) (2.34 * 1e9);
        int runningFrequency = 10;
        var step = Duration.ofHours(23)
                .plusMinutes(56)
                .plusSeconds(4);

        var expectedZDT = ZonedDateTime.of(
                LocalDate.of(2020, Month.MAY, 13),
                LocalTime.of(19, 29, 32),
                ZoneOffset.UTC);

        assertEquals(expectedZDT, TimeAccelerator.discrete(runningFrequency, step).adjust(ZDT, realElapsedTime));
    }
}