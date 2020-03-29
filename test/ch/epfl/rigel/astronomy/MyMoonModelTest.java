package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyMoonModelTest {
    @Test
    void atWorksWithBookExample(){
        double refEclLon = Angle.ofDeg(214.862515);
        double refEclLat = Angle.ofDeg(1.716257);
        var refEclCoords = EclipticCoordinates.of(refEclLon, refEclLat);

        var zdt = ZonedDateTime.of(
                LocalDate.of(2003, Month.SEPTEMBER, 1),
                LocalTime.of(0,0,0),
                ZoneOffset.UTC);
        var eclToEqu = new EclipticToEquatorialConversion(zdt);
        var refEquCoords = eclToEqu.apply(refEclCoords);

        double numberOfDays = Epoch.J2010.daysUntil(zdt);

        var moon = MoonModel.MOON.at(numberOfDays, eclToEqu);

        double calculatedRa = moon.equatorialPos().ra();
        double calculatedDec = moon.equatorialPos().dec();

        assertEquals(refEquCoords.ra(), calculatedRa, 1e-7);
        assertEquals(refEquCoords.dec(), calculatedDec, 1e-7);
    }
}