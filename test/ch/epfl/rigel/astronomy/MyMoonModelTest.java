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
        double refEclLon = 214.862515;
        double refEclLat = 1.716257;
        var refEclCoords = EclipticCoordinates.of(Angle.ofDeg(refEclLon), Angle.ofDeg(refEclLat));

        var zdt = ZonedDateTime.of(
                LocalDate.of(2003, Month.SEPTEMBER, 1),
                LocalTime.of(0,0,0),
                ZoneOffset.UTC);
        var eclToEqu = new EclipticToEquatorialConversion(zdt);
        var refEquCoords = eclToEqu.apply(refEclCoords);

        double numberOfDays = Epoch.J2010.daysUntil(zdt);

        var moon = MoonModel.MOON.at(numberOfDays, eclToEqu);
        /*
        double calculatedRa = moon.equatorialPos().ra();
        double calculatedDec = moon.equatorialPos().dec();

         */

        double calcEclLon = Angle.toDeg(MoonModel.MOON.getEcl().lon());
        double calcEclLat = Angle.toDeg(MoonModel.MOON.getEcl().lat());

        assertEquals(refEclLon, calcEclLon);
        assertEquals(refEclLat, calcEclLat);
    }
}