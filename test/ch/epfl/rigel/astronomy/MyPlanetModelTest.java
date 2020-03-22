package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MyPlanetModelTest {
    @Test
    void atWorksWithFramapadValues(){
        var equJupiter = PlanetModel.JUPITER
                .at(-2231.0, new EclipticToEquatorialConversion(
                        ZonedDateTime.of(
                                LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0), ZoneOffset.UTC)))
                .equatorialPos();

        assertEquals(11.187154934709682, equJupiter.raHr(), 1e-14); //THIS ONE
        assertEquals(11.187154934709678, equJupiter.raHr(), 1e-14);
        //assertEquals(11.18675858733765, equJupiter.raHr(), 1e-14);

        assertEquals(6.3566355066857465, equJupiter.decDeg()); //THIS ONE
        assertEquals(6.356635506685756, equJupiter.decDeg(), 1e-14);
        //assertEquals(6.3426361553333095, equJupiter.decDeg(), 1e-14);

        var angJupiter = Angle.toDeg(PlanetModel.JUPITER
                .at(-2231.0, new EclipticToEquatorialConversion(
                        ZonedDateTime.of(
                                LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0),
                                ZoneOffset.UTC)))
                .angularSize())*3600;
        assertEquals(35.11141185362771, angJupiter);

        var magnJupiter = PlanetModel.JUPITER
                .at(-2231.0, new EclipticToEquatorialConversion(
                        ZonedDateTime.of(
                                LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0),
                                ZoneOffset.UTC)))
                .magnitude();
        assertEquals(-1.9885659217834473, magnJupiter);


        var equMercury = PlanetModel.MERCURY
                .at(-2231.0, new EclipticToEquatorialConversion(
                        ZonedDateTime.of(
                                LocalDate.of(2003, Month.NOVEMBER, 22),
                                LocalTime.of(0, 0, 0, 0),
                                ZoneOffset.UTC)))
                .equatorialPos();

        assertEquals(16.820074565897148, equMercury.raHr()); //THIS ONE
        assertEquals(16.820074565897194, equMercury.raHr(), 1e-13);
        assertEquals(16.82007456589712, equMercury.raHr(), 1e-13);
        //assertEquals(16.8279572540347, equMercury.raHr(), 1e-13);

        assertEquals(-24.500872462861224, equMercury.decDeg()); //THIS ONE
        assertEquals(-24.500872462861274, equMercury.decDeg(), 1e-13);
        assertEquals(-24.500872462861143, equMercury.decDeg(), 1e-13);
        assertEquals(-24.500872462861228, equMercury.decDeg(), 1e-13);
        assertEquals(-24.500872462861217, equMercury.decDeg(), 1e-13);
        //assertEquals(-23.612022868326743, equMercury.decDeg(), 1e-13);
    }

    @Test
    void atWorksWithMercury(){
        double lonEclGeo = Angle.ofDeg(256.0213547);
        double latEclGeo = Angle.ofDeg(0.23851218);

        var localDate = LocalDate.of(2012, Month.DECEMBER, 21);
        var localTime = LocalTime.of(21, 30, 18);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var mercury = PlanetModel.MERCURY.at(numberOfDays, eclEqu);
        assertEquals(eclConverted.ra(), mercury.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), mercury.equatorialPos().dec(), 1e-8);
    }

    @Test
    void atWorksWithVenus(){
        double lonEclGeo = Angle.ofDeg(69.27122014);
        double latEclGeo = Angle.ofDeg(3.782883228);

        var localDate = LocalDate.of(1980, Month.APRIL, 13);
        var localTime = LocalTime.of(13, 47, 57);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var venus = PlanetModel.VENUS.at(numberOfDays, eclEqu);
        assertEquals(eclConverted.ra(), venus.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), venus.equatorialPos().dec(), 1e-8);
    }

    @Test
    void atWorksWithMars(){
        double lonEclGeo = Angle.ofDeg(304.0043146);
        double latEclGeo = Angle.ofDeg(-1.059272633);

        var localDate = LocalDate.of(2011, Month.JANUARY, 20);
        var localTime = LocalTime.of(15, 36, 42);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var mars = PlanetModel.MARS.at(numberOfDays, eclEqu);
        assertEquals(eclConverted.ra(), mars.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), mars.equatorialPos().dec(), 1e-8);
    }

    @Test
    void atWorksWithJupiter(){
        double lonEclGeo = Angle.ofDeg(232.4880543);
        double latEclGeo = Angle.ofDeg(1.156238158);

        var localDate = LocalDate.of(2018, Month.FEBRUARY, 18);
        var localTime = LocalTime.of(3, 25, 32);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var jupiter = PlanetModel.JUPITER.at(numberOfDays, eclEqu);
        assertEquals(eclConverted.ra(), jupiter.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), jupiter.equatorialPos().dec(), 1e-8);
    }
    @Test
    void atWorksWithSaturn(){
        double lonEclGeo = Angle.ofDeg(299.9711624);
        double latEclGeo = Angle.ofDeg(-0.05121356);

        var localDate = LocalDate.of(2020, Month.MARCH, 20);
        var localTime = LocalTime.of(13, 45, 58);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var saturn = PlanetModel.SATURN.at(numberOfDays, eclEqu);
        assertEquals(eclConverted.ra(), saturn.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), saturn.equatorialPos().dec(), 1e-8);
    }
    @Test
    void atWorksWithUranus(){
        double lonEclGeo = Angle.ofDeg(41.00960426);
        double latEclGeo = Angle.ofDeg(-0.460118998);

        var localDate = LocalDate.of(2040, Month.AUGUST, 29);
        var localTime = LocalTime.of(10, 18, 45);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var saturn = PlanetModel.URANUS.at(numberOfDays, eclEqu);
        assertEquals(eclConverted.ra(), saturn.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), saturn.equatorialPos().dec(), 1e-8);
    }

    @Test
    void atWorksWithNeptun(){
        double lonEclGeo = Angle.ofDeg(284.6986133);
        double latEclGeo = Angle.ofDeg(0.86736909);

        var localDate = LocalDate.of(1990, Month.MAY, 6);
        var localTime = LocalTime.of(2, 50, 27);
        var zonedTime = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC);
        var eclEqu = new EclipticToEquatorialConversion(zonedTime);

        double numberOfDays = Epoch.J2010.daysUntil(zonedTime);
        var eclCoords = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclConverted = eclEqu.apply(eclCoords);

        var neptune = PlanetModel.NEPTUNE.at(numberOfDays, eclEqu);
        assertEquals(eclConverted.ra(), neptune.equatorialPos().ra(), 1e-8);
        assertEquals(eclConverted.dec(), neptune.equatorialPos().dec(), 1e-8);
    }

    @Test
    void atWorksWithBookExampleJupiter(){
        int numberOfDays = -2231;
        double lonEclGeo = Angle.ofDeg(166.310510);
        double latEclGeo = Angle.ofDeg(1.036466);
        var date = LocalDate.of(2003, Month.NOVEMBER, 22);
        var time = LocalTime.of(0, 0, 0);
        var ecl = EclipticCoordinates.of(lonEclGeo, latEclGeo);

        var eclEqu = new EclipticToEquatorialConversion(ZonedDateTime.of(date, time, ZoneOffset.UTC));

        var j = PlanetModel.JUPITER.at(numberOfDays, eclEqu);
        var equJ = j.equatorialPos();

        assertEquals(eclEqu.apply(ecl).ra(), equJ.ra(), 1e-8);
        assertEquals(eclEqu.apply(ecl).dec(), equJ.dec(), 1e-8);
    }

    @Test
    void atWorksWithBookExampleMercury(){
        int numberOfDays = -2231;
        double lonEclGeo = Angle.ofDeg(253.929758);
        double latEclGeo = Angle.ofDeg(-2.044057);
        var date = LocalDate.of(2003, Month.NOVEMBER, 22);
        var time = LocalTime.of(0, 0, 0);
        var when = ZonedDateTime.of(date, time, ZoneOffset.UTC);
        var ecl = EclipticCoordinates.of(lonEclGeo, latEclGeo);
        var eclEqu = new EclipticToEquatorialConversion(when);

        var m = PlanetModel.MERCURY.at(numberOfDays, eclEqu);
        var equM = m.equatorialPos();

        assertEquals(eclEqu.apply(ecl).ra(), equM.ra(), 1e-8);
        assertEquals(eclEqu.apply(ecl).dec(), equM.dec(), 1e-8);
    }

}