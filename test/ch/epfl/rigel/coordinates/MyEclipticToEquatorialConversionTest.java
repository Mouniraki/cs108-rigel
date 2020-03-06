package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static ch.epfl.rigel.coordinates.EquatorialCoordinates.of;
import static org.junit.jupiter.api.Assertions.*;

class MyEclipticToEquatorialConversionTest {

    @Test
    void isConversionCorrect(){

        LocalDateTime ldt = LocalDateTime.of(2020, Month.FEBRUARY, 28, 20, 33);
        ZonedDateTime klDateTime = ldt.atZone(ZoneId.of("Europe/Paris"));
        EclipticToEquatorialConversion theConversion = new EclipticToEquatorialConversion(klDateTime);

        LocalDateTime ldtBook = LocalDateTime.of(2009, Month.JULY, 6, 20, 33);
        ZonedDateTime klDateTimeBook = ldtBook.atZone(ZoneId.of("Europe/Paris"));
        EclipticToEquatorialConversion theConversionBook = new EclipticToEquatorialConversion(klDateTimeBook);

        EclipticCoordinates ecl1Book = EclipticCoordinates.of(Angle.ofDeg(139.6861), Angle.ofDeg(4.875278));

        EclipticCoordinates ecl1 = EclipticCoordinates.of(Angle.ofDeg(48.8566254), Angle.ofDeg(2.3522369));
        EclipticCoordinates ecl2 = EclipticCoordinates.of(Angle.ofDeg(45.855536), Angle.ofDeg(58.381592));
        EclipticCoordinates ecl3 = EclipticCoordinates.of(Angle.ofDeg(0), Angle.ofDeg(55.87654));

        EquatorialCoordinates result1Book = theConversion.apply(ecl1Book);

        EquatorialCoordinates result1 = theConversion.apply(ecl1);
        EquatorialCoordinates result2 = theConversion.apply(ecl2);
        EquatorialCoordinates result3 = theConversion.apply(ecl3);

        assertEquals(9.581477778, result1Book.raHr(), 0.0000001);     // 9h 34m 53.32s
        assertEquals(19.535002778, result1Book.decDeg(), 1e-7);//     19 ◦ 32 ' 6. 01''

        assertEquals(45.71539997, result1.raDeg(), 1e-4);
        assertEquals(19.69151561, result1.decDeg(), 1e-2); //19.00631°

        assertEquals(1.00836393, result2.raDeg(), 1e-2);
        assertEquals(68.58082816, result2.decDeg(), 1e-2);

        assertEquals(329.58721040, result3.raDeg(), 1e-2);
        assertEquals(49.42188763, result3.decDeg(), 1e-2);

    }

}