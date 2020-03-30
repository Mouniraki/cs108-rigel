package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class MyMoonModelTest {
    private static ZonedDateTime ZDT_BOOKEXAMPLE = ZonedDateTime.of(
            LocalDate.of(2003, Month.SEPTEMBER, 1),
            LocalTime.of(0,0,0),
            ZoneOffset.UTC);

    private static ZonedDateTime ZDT_JAN30_2014 = ZonedDateTime.of(
            LocalDate.of(2014, Month.JANUARY, 30),
            LocalTime.of(13,45,50),
            ZoneOffset.UTC);

    private static ZonedDateTime ZDT_1SEPT_1979 = ZonedDateTime.of(
            LocalDate.of(1979, Month.SEPTEMBER, 1),
            LocalTime.of(0,0,0),
            ZoneOffset.UTC);

    @Test
    void atWorksWithBookExample(){
        double refEclLon = Angle.ofDeg(214.862515);
        double refEclLat = Angle.ofDeg(1.716257);
        double refPhase = 0.224717717;
        double refAngularDiameter = Angle.ofDeg(0.541242956);

        var refEclCoords = EclipticCoordinates.of(refEclLon, refEclLat);
        var eclToEqu = new EclipticToEquatorialConversion(ZDT_BOOKEXAMPLE);
        var refEquCoords = eclToEqu.apply(refEclCoords);

        double numberOfDays = Epoch.J2010.daysUntil(ZDT_BOOKEXAMPLE);

        var moon = MoonModel.MOON.at(numberOfDays, eclToEqu);

        double calculatedRa = moon.equatorialPos().ra();
        double calculatedDec = moon.equatorialPos().dec();

        String refName = String.format(Locale.ROOT, "Lune (%.1f%%)", refPhase * 100);

        assertEquals(refEquCoords.ra(), calculatedRa, 1e-7);
        assertEquals(refEquCoords.dec(), calculatedDec, 1e-7);
        assertEquals(refName, moon.info());
        assertEquals(0, moon.magnitude());
        assertEquals(refAngularDiameter, moon.angularSize(), 1e-3); //ISSUES WITH CALCULATION IN THE EXCEL
    }

    @Test
    void atWorksOn30Jan2014(){
        double refEclLon = Angle.ofDeg(305.8808742);
        double refEclLat = Angle.ofDeg(5.137117611);
        double refPhase = 0.001692235;
        double refAngDiameter = Angle.ofDeg(0.557671816);

        var refEclCoords = EclipticCoordinates.of(refEclLon, refEclLat);
        var eclToEqu = new EclipticToEquatorialConversion(ZDT_JAN30_2014);
        var refEquCoords = eclToEqu.apply(refEclCoords);

        double numberOfDays = Epoch.J2010.daysUntil(ZDT_JAN30_2014);

        var moon = MoonModel.MOON.at(numberOfDays, eclToEqu);

        double calculatedRa = moon.equatorialPos().ra();
        double calculatedDec = moon.equatorialPos().dec();

        String refName = String.format(Locale.ROOT, "Lune (%.1f%%)", refPhase * 100);

        assertEquals(refEquCoords.ra(), calculatedRa, 1e-3); //ISSUES WITH CALCULATION IN THE EXCEL
        assertEquals(refEquCoords.dec(), calculatedDec, 1e-4); //ISSUES WITH CALCULATION IN THE EXCEL
        assertEquals(refName, moon.info());
        assertEquals(0, moon.magnitude());
        assertEquals(refAngDiameter, moon.angularSize(), 1e-3);
    }

    @Test
    void atWorksOn1Sept1979(){
        double refEclLon = Angle.ofDeg(263.4077039);
        double refEclLat = Angle.ofDeg(4.972978358);
        double refPhase = 0.632782693;
        double refAngDiameter = Angle.ofDeg(0.527361777);

        var refEclCoords = EclipticCoordinates.of(refEclLon, refEclLat);
        var eclToEqu = new EclipticToEquatorialConversion(ZDT_1SEPT_1979);
        var refEquCoords = eclToEqu.apply(refEclCoords);

        double numberOfDays = Epoch.J2010.daysUntil(ZDT_1SEPT_1979);

        var moon = MoonModel.MOON.at(numberOfDays, eclToEqu);

        double calculatedRa = moon.equatorialPos().ra();
        double calculatedDec = moon.equatorialPos().dec();

        String refName = String.format(Locale.ROOT, "Lune (%.1f%%)", refPhase * 100);

        assertEquals(refEquCoords.ra(), calculatedRa, 1e-4); //ISSUES WITH CALCULATION IN THE EXCEL
        assertEquals(refEquCoords.dec(), calculatedDec, 1e-5); //ISSUES WITH CALCULATION IN THE EXCEL
        assertEquals(refName, moon.info());
        assertEquals(0, moon.magnitude());
        assertEquals(refAngDiameter, moon.angularSize(), 1e-4); //ISSUES WITH CALCULATION IN THE EXCEL
    }

    @Test
    void angularSizeOn1Sept1979IsCoherentWithFramapad(){
        double refAngDiameter = 0.009225908666849136;

        var eclToEqu = new EclipticToEquatorialConversion(ZDT_1SEPT_1979);
        double numberOfDays = Epoch.J2010.daysUntil(ZDT_1SEPT_1979);

        var moon = MoonModel.MOON.at(numberOfDays, eclToEqu);
        assertEquals(0, moon.magnitude());
        assertEquals(refAngDiameter, moon.angularSize());
    }
}