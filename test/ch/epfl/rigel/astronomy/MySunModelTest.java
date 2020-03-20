package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MySunModelTest {
    void atWorksWithRandomValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var day = rng.nextInt(1, 31);
            var month = rng.nextInt(1, 12);
            var year = rng.nextInt(1970, 2050);

            var hour = rng.nextInt(0, 24);
            var min = rng.nextInt(0, 60);
            var sec = rng.nextInt(0, 60);

            var localDate = LocalDate.of(year, month, day);
            var localTime = LocalTime.of(hour, min, sec);
            var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);

            var numberOfDays = Epoch.J2010.daysUntil(zonedTime);
            var eclEqu = new EclipticToEquatorialConversion(zonedTime);

            var sun = SunModel.SUN.at(numberOfDays, eclEqu);
            /*
            assertEquals(eclConverted.ra(), sun.equatorialPos().ra(), 1e-8);
            assertEquals(eclConverted.dec(), sun.equatorialPos().dec(), 1e-8);

             */
        }
    }

    @Test
    void atWorksWithKnownValues(){
        double lonEclGeo = Angle.ofDeg(123.5806048);
        double latEclGeo = Angle.ofDeg(0);

        var localDate = LocalDate.of(2003, Month.JULY, 27);
        var localTime = LocalTime.of(0, 0, 0);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);

        var numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        System.out.println(Angle.toDeg(eclConverted.ra()));
        var sun = SunModel.SUN.at(numberOfDays, eclEqu);
        assertEquals(eclConverted.ra(), sun.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), sun.equatorialPos().dec(), 1e-8);
    }

}