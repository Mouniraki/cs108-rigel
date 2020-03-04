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

        LocalDateTime ldt = LocalDateTime.of(1980, Month.APRIL, 22, 14, 37);
        ZonedDateTime klDateTime = ldt.atZone(ZoneId.of("America/Tortola"));
        GeographicCoordinates where = GeographicCoordinates.ofDeg(-64, 52);
        EquatorialToHorizontalConversion theConversion = new EquatorialToHorizontalConversion(klDateTime, where);

        EquatorialCoordinates ec1 = EquatorialCoordinates.of(Angle.ofHr(18.5391667), 0.9735253);

        HorizontalCoordinates result1 = theConversion.apply(ec1);

        assertEquals(341.4626034, result1.azDeg(), 1e-5);
        assertEquals(20.9485886, result1.altDeg(), 1e-5);
    }
}