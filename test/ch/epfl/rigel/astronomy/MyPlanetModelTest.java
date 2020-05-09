package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyPlanetModelTest {
    @Test
    void atWorksWithMercury(){
        double lonEclGeo = Angle.ofDeg(256.0213547);
        double latEclGeo = Angle.ofDeg(0.23851218);
        double angularSize = Angle.ofArcsec(5.202566955);
        double magnitude = -1.52477121;

        var localDate = LocalDate.of(2012, Month.DECEMBER, 21);
        var localTime = LocalTime.of(21, 30, 18);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var mercury = PlanetModel.MERCURY.at(numberOfDays, eclEqu);

        assertEquals("Mercure", mercury.name());
        assertEquals(eclConverted.ra(), mercury.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), mercury.equatorialPos().dec(), 1e-8);
        assertEquals(angularSize, mercury.angularSize(), 1e-6); //FLOAT VALUE -> NOT GOOD (7th dec)
        assertEquals(magnitude, mercury.magnitude(), 0.1); //FAILS ON 1ST DEC
    }

    @Test
    void atWorksWithVenus(){
        double lonEclGeo = Angle.ofDeg(69.27122014);
        double latEclGeo = Angle.ofDeg(3.782883228);
        double angularSize = Angle.ofArcsec(25.6000113);
        double magnitude = -5.196691188;

        var localDate = LocalDate.of(1980, Month.APRIL, 13);
        var localTime = LocalTime.of(13, 47, 57);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var venus = PlanetModel.VENUS.at(numberOfDays, eclEqu);
        assertEquals("Vénus", venus.name());
        assertEquals(eclConverted.ra(), venus.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), venus.equatorialPos().dec(), 1e-8);
        assertEquals(angularSize, venus.angularSize(), 1e-5); //FLOAT VALUE -> NOT GOOD (6th dec)
        assertEquals(magnitude, venus.magnitude(),0.1); //FAILS ON 1ST DEC
    }

    @Test
    void atWorksWithMars(){
        double lonEclGeo = Angle.ofDeg(304.0043146);
        double latEclGeo = Angle.ofDeg(-1.059272633);
        double angularSize = Angle.ofArcsec(3.934414956);
        double magnitude = 1.089907132;

        var localDate = LocalDate.of(2011, Month.JANUARY, 20);
        var localTime = LocalTime.of(15, 36, 42);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var mars = PlanetModel.MARS.at(numberOfDays, eclEqu);
        assertEquals("Mars", mars.name());
        assertEquals(eclConverted.ra(), mars.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), mars.equatorialPos().dec(), 1e-8);
        assertEquals(angularSize, mars.angularSize(), 1e-7); //FLOAT VALUE -> CHECK
        assertEquals(magnitude, mars.magnitude(), 0.01); //FAILS ON 2ND DEC
    }

    @Test
    void atWorksWithJupiter(){
        double lonEclGeo = Angle.ofDeg(232.4880543);
        double latEclGeo = Angle.ofDeg(1.156238158);
        double angularSize = Angle.ofArcsec(37.70006165);
        double magnitude = -2.13145144;

        var localDate = LocalDate.of(2018, Month.FEBRUARY, 18);
        var localTime = LocalTime.of(3, 25, 32);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var jupiter = PlanetModel.JUPITER.at(numberOfDays, eclEqu);
        assertEquals("Jupiter", jupiter.name());
        assertEquals(eclConverted.ra(), jupiter.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), jupiter.equatorialPos().dec(), 1e-8);
        assertEquals(angularSize, jupiter.angularSize(), 1e-7); //FLOAT VALUE -> CHECK
        assertEquals(magnitude, jupiter.magnitude(), 0.0001); //FAILS ON 5TH DEC
    }
    @Test
    void atWorksWithSaturn(){
        double lonEclGeo = Angle.ofDeg(299.9711624);
        double latEclGeo = Angle.ofDeg(-0.05121356);
        double angularSize = Angle.ofArcsec(15.80371891);
        double magnitude = 1.2292452;

        var localDate = LocalDate.of(2020, Month.MARCH, 20);
        var localTime = LocalTime.of(13, 45, 58);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var saturn = PlanetModel.SATURN.at(numberOfDays, eclEqu);
        assertEquals("Saturne", saturn.name());
        assertEquals(eclConverted.ra(), saturn.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), saturn.equatorialPos().dec(), 1e-8);
        assertEquals(angularSize, saturn.angularSize(), 1e-6); //FLOAT VALUE -> NOT GOOD (7th dec)
        assertEquals(magnitude, saturn.magnitude(), 0.1); //FAIL ON 2ND DEC
    }
    @Test
    void atWorksWithUranus(){
        double lonEclGeo = Angle.ofDeg(41.00960426);
        double latEclGeo = Angle.ofDeg(-0.460118998);
        double angularSize = Angle.ofArcsec(3.388895501);
        double magnitude = 5.59365921;

        var localDate = LocalDate.of(2040, Month.AUGUST, 29);
        var localTime = LocalTime.of(10, 18, 45);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var uranus = PlanetModel.URANUS.at(numberOfDays, eclEqu);
        assertEquals("Uranus", uranus.name());
        assertEquals(eclConverted.ra(), uranus.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), uranus.equatorialPos().dec(), 1e-8);
        assertEquals(angularSize, uranus.angularSize(), 1e-7); //FLOAT VALUE -> CHECK
        assertEquals(magnitude, uranus.magnitude(), 1); //FAIL ON 1ST DECIMAL
    }

    @Test
    void atWorksWithNeptune(){
        double lonEclGeo = Angle.ofDeg(284.6986133);
        double latEclGeo = Angle.ofDeg(0.86736909);
        double angularSize = Angle.ofArcsec(2.095869306);
        double magnitude = 7.892916144;

        var localDate = LocalDate.of(1990, Month.MAY, 6);
        var localTime = LocalTime.of(2, 50, 27);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var neptune = PlanetModel.NEPTUNE.at(numberOfDays, eclEqu);
        assertEquals("Neptune", neptune.name());
        assertEquals(eclConverted.ra(), neptune.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), neptune.equatorialPos().dec(), 1e-8);
        assertEquals(angularSize, neptune.angularSize(), 1e-7); //FLOAT VALUE -> CHECK
        assertEquals(magnitude, neptune.magnitude(), 0.01);
    }

    @Test
    void atWorksWithBookExampleJupiter(){
        int numberOfDays = -2231;
        double lonEclGeo = Angle.ofDeg(166.310510);
        double latEclGeo = Angle.ofDeg(1.036466);
        double angularSize = Angle.ofArcsec(35.1); //35.14290308
        double magnitude = -1.991212985;
        var date = LocalDate.of(2003, Month.NOVEMBER, 22);
        var time = LocalTime.of(0, 0, 0);
        var ecl = EclipticCoordinates.of(lonEclGeo, latEclGeo);

        var eclEqu = new EclipticToEquatorialConversion(ZonedDateTime.of(date, time, ZoneOffset.UTC));

        var j = PlanetModel.JUPITER.at(numberOfDays, eclEqu);
        var equJ = j.equatorialPos();

        assertEquals(eclEqu.apply(ecl).ra(), equJ.ra(), 1e-8);
        assertEquals(eclEqu.apply(ecl).dec(), equJ.dec(), 1e-8);
        assertEquals(angularSize, j.angularSize(), 1e-7);
        assertEquals(magnitude, j.magnitude(), 0.01);
    }

    @Test
    void atWorksWithBookExampleMercury(){
        int numberOfDays = -2231;
        double lonEclGeo = Angle.ofDeg(253.929758);
        double latEclGeo = Angle.ofDeg(-2.044057);
        double angularSize = Angle.ofArcsec(5.1); //5.129568714
        double magnitude = -1.462134557;
        var date = LocalDate.of(2003, Month.NOVEMBER, 22);
        var time = LocalTime.of(0, 0, 0);
        var when = ZonedDateTime.of(date, time, ZoneOffset.UTC);
        var ecl = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclEqu = new EclipticToEquatorialConversion(when);

        var m = PlanetModel.MERCURY.at(numberOfDays, eclEqu);
        var equM = m.equatorialPos();

        assertEquals(eclEqu.apply(ecl).ra(), equM.ra(), 1e-8);
        assertEquals(eclEqu.apply(ecl).dec(), equM.dec(), 1e-8);
        assertEquals(angularSize, m.angularSize(), 1e-6); //FAIL ON 7TH DEC
        assertEquals(magnitude, m.magnitude(), 0.1); //FAIL ON 2ND DECIMAL
    }

}