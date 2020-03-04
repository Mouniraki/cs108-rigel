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

        LocalDateTime ldt = LocalDateTime.of(2020, Month.FEBRUARY, 28, 20, 33);
        ZonedDateTime klDateTime = ldt.atZone(ZoneId.of("Europe/Paris"));
        GeographicCoordinates where = GeographicCoordinates.ofDeg(48.8566762, 52);
        EquatorialToHorizontalConversion theConversion = new EquatorialToHorizontalConversion(klDateTime, where);

//        EquatorialCoordinates ec1 = EquatorialCoordinates.of(Angle.ofDeg(6), Angle.ofDeg(55));
        EquatorialCoordinates ec2 = EquatorialCoordinates.of(Angle.ofDeg(5.862222), Angle.ofDeg(23.219444)); //from Cambridge sheet
//        EquatorialCoordinates ec3 = EquatorialCoordinates.of(Angle.ofDeg(345), Angle.ofDeg(-54));

//        HorizontalCoordinates result1 = theConversion.apply(ec1);
        HorizontalCoordinates result2 = theConversion.apply(ec2);
//        HorizontalCoordinates result3 = theConversion.apply(ec3);

//        assertEquals(272.70, result1.azDeg(), 0.01);
//        assertEquals(-22.11, result1.altDeg(), 0.01);
//Altitude 	-22.11° 	-22.15°
//Azimuth 	272.70° 	272.70°

        assertEquals(357.4127249, result2.azDeg(), 0.0001);
        assertEquals(19.334345, result2.altDeg(), 0.00001);
//        Altitude 	-45.07° 	-45.09°
//        Azimuth 	304.21° 	304.21°

//        assertEquals(329.58721040, result3.azDeg(), 0.01);
//        assertEquals(49.42188763, result3.altDeg(), 0.01);

    }
}