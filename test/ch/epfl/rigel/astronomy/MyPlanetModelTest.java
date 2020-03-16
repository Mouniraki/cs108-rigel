package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyPlanetModelTest {
    @Test
    void atWorksWithBookExampleJupiter(){
        int numberOfDays = -2231;
        double lonEclGeo = Angle.ofDeg(166.310510);
        double latEclGeo = Angle.ofDeg(1.036466);
        var date = LocalDate.of(2003, Month.DECEMBER, 23);
        var time = LocalTime.of(0, 0, 0);
        var when = ZonedDateTime.of(date, time, ZoneOffset.UTC);
        var ecl = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclEqu = new EclipticToEquatorialConversion(when);

        var j = PlanetModel.JUPITER.at(numberOfDays, eclEqu);
        var equJ = j.equatorialPos();

        System.out.println(166.310510);
        assertEquals(eclEqu.apply(ecl).ra(), equJ.ra());
        assertEquals(eclEqu.apply(ecl).dec(), equJ.dec());
    }

    @Test
    void atWorksWithBookExampleMercury(){
        int numberOfDays = -2231;
        double lonEclGeo = Angle.ofDeg(253.929758);
        double latEclGeo = Angle.ofDeg(-2.044057);
        var date = LocalDate.of(2003, Month.DECEMBER, 23);
        var time = LocalTime.of(0, 0, 0);
        var when = ZonedDateTime.of(date, time, ZoneOffset.UTC);
        var ecl = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclEqu = new EclipticToEquatorialConversion(when);

        var m = PlanetModel.MERCURY.at(numberOfDays, eclEqu);
        var equM = m.equatorialPos();

        assertEquals(eclEqu.apply(ecl).ra(), equM.ra());
        assertEquals(eclEqu.apply(ecl).dec(), equM.dec());
    }

}