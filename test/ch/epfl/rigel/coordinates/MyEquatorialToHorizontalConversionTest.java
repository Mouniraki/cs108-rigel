package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MyEquatorialToHorizontalConversionTest {
    @Test
    void isConversionCorrect(){
        LocalDateTime ldt1 = LocalDateTime.of(1980, Month.APRIL, 22, 14, 37);
        ZonedDateTime klDateTime1 = ldt1.atZone(ZoneId.of("America/Tortola"));
        GeographicCoordinates where1 = GeographicCoordinates.ofDeg(-64, 52);
        EquatorialToHorizontalConversion theConversion1 = new EquatorialToHorizontalConversion(klDateTime1, where1);
        EquatorialCoordinates ec1 = EquatorialCoordinates.of(Angle.ofHr(18.5391667), 0.9735253);
        HorizontalCoordinates result1 = theConversion1.apply(ec1);

        LocalDateTime ldt2 = LocalDateTime.of(2000, Month.JANUARY, 1, 21, 37);
        ZonedDateTime klDateTime2 = ldt2.atZone(ZoneId.of("Europe/Madrid"));
        GeographicCoordinates where2 = GeographicCoordinates.ofDeg(0, 89);
        EquatorialToHorizontalConversion theConversion2 = new EquatorialToHorizontalConversion(klDateTime2, where2);
        EquatorialCoordinates ec2 = EquatorialCoordinates.of(Angle.ofHr(21.762222222), 0.4052558); //Hr: 21h, 45m, 44s
        HorizontalCoordinates result2 = theConversion2.apply(ec2);

        LocalDateTime ldt4 = LocalDateTime.of(1980, Month.APRIL, 22, 14, 36);
        ZonedDateTime klDateTime4 = ldt4.atZone(ZoneId.of("America/Tortola"));
        GeographicCoordinates where4 = GeographicCoordinates.ofDeg(-64, 47);
        EquatorialToHorizontalConversion theConversion4 = new EquatorialToHorizontalConversion(klDateTime4, where4);
        EquatorialCoordinates ec4 = EquatorialCoordinates.of(Angle.ofHr(18.5391667), 0.4052558); //Hr: 4, 32m, 04s
        HorizontalCoordinates result4 = theConversion4.apply(ec4);

        assertEquals(341.4626034, result1.azDeg(), 1e-5);
        assertEquals(20.9485886, result1.altDeg(), 1e-5);

        assertEquals(264.0588098, result2.azDeg(), 1e-5);
        assertEquals(23.3266666, result2.altDeg(), 1e-5);

        assertEquals(329.7599372, result4.azDeg(), 1e-4);
        assertEquals(-14.0341582, result4.altDeg(), 1e-5);
    }
}
