package ch.epfl.rigel.gui;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyNamedTimeAcceleratorTest {
    @Test
    void getAcceleratorReturnsCorrectAccelerator(){
        List<NamedTimeAccelerator> l = List.of(NamedTimeAccelerator.values());
        List<TimeAccelerator> accelerators = List.of(
                TimeAccelerator.continuous(1),
                TimeAccelerator.continuous(30),
                TimeAccelerator.continuous(300),
                TimeAccelerator.continuous(3000),
                TimeAccelerator.discrete(60, Duration.ofHours(24)),
                TimeAccelerator.discrete(60, Duration
                        .ofHours(23)
                        .plusMinutes(56)
                        .plusSeconds(4))
                );
        for(NamedTimeAccelerator na : l){
            int index = l.indexOf(na);
            TimeAccelerator expectedAccelerator = accelerators.get(index);
            ZonedDateTime zdt = ZonedDateTime.parse("2020-06-01T23:55:00+01:00");
            long elapsed = (long) (2.34*1e9);
            ZonedDateTime expectedResult = expectedAccelerator.adjust(zdt, elapsed);
            ZonedDateTime actualResult = na.getAccelerator().adjust(zdt, elapsed);
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    void toStringReturnsCorrectName(){
        List<NamedTimeAccelerator> l = List.of(NamedTimeAccelerator.values());
        List<String> correctNames = List.of("1×", "30×", "300×", "3000×", "jour", "jour sidéral");
        List<String> invalidNames = List.of("TIMES_1", "TIMES_30", "TIMES_300", "TIMES_3000", "DAY", "SIDEREAL_DAY");
        for(NamedTimeAccelerator na : l){
            int naIndex = l.indexOf(na);
            String correctName = correctNames.get(naIndex);
            String invalidName = invalidNames.get(naIndex);
            assertEquals(correctName, na.toString());
            assertNotEquals(invalidName, na.toString());
        }
    }

    @Test
    void getNameReturnsCorrectName(){
        List<NamedTimeAccelerator> l = List.of(NamedTimeAccelerator.values());
        List<String> correctNames = List.of("1×", "30×", "300×", "3000×", "jour", "jour sidéral");
        List<String> invalidNames = List.of("TIMES_1", "TIMES_30", "TIMES_300", "TIMES_3000", "DAY", "SIDEREAL_DAY");
        for(NamedTimeAccelerator na : l){
            int naIndex = l.indexOf(na);
            String correctName = correctNames.get(naIndex);
            String invalidName = invalidNames.get(naIndex);
            assertEquals(correctName, na.getName());
            assertNotEquals(invalidName, na.getName());
        }
    }
}