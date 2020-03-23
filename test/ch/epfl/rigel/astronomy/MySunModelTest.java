package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class MySunModelTest {
    private final static ZonedDateTime ZDT_BOOKEXAMPLE = ZonedDateTime.of(
            LocalDate.of(2003, Month.JULY, 27),
            LocalTime.of(0, 0, 0),
            ZoneOffset.UTC
            );

    private final static ZonedDateTime ZDT_STARTSEMESTER = ZonedDateTime.of(
            LocalDate.of(2020, Month.FEBRUARY, 17),
            LocalTime.of(8, 15, 47),
            ZoneOffset.UTC
    );

    private final static ZonedDateTime ZDT_ENDSEMESTER = ZonedDateTime.of(
            LocalDate.of(2020, Month.MAY, 29),
            LocalTime.of(16, 45, 59),
            ZoneOffset.UTC
    );

    private final static ZonedDateTime ZDT_BERLIN_WALL = ZonedDateTime.of(
            LocalDate.of(1989, Month.NOVEMBER, 9),
            LocalTime.of(19, 05, 36),
            ZoneOffset.UTC
    );

    private final static ZonedDateTime ZDT_NEW_CENTURY = ZonedDateTime.of(
            LocalDate.of(2000, Month.JANUARY, 1),
            LocalTime.of(3, 27, 18),
            ZoneOffset.UTC
    );

    @Test
    void atWorksWithFramapadTests(){
        assertEquals(SunModel.SUN
                .at(27 + 31, new EclipticToEquatorialConversion(
                ZonedDateTime.of(
                        LocalDate.of(2010,  Month.FEBRUARY, 27),
                        LocalTime.of(0,0),
                        ZoneOffset.UTC)))
                .equatorialPos()
                .ra(), 5.9325494700300885);


        assertEquals(SunModel.SUN
                        .at(-2349, new EclipticToEquatorialConversion(
                                ZonedDateTime.of(
                                        LocalDate.of(2003, Month.JULY, 27),
                                        LocalTime.of(0, 0, 0,0),
                                        ZoneOffset.UTC)))
                        .equatorialPos()
                        .raHr(), 8.392682808297807);

        assertEquals(SunModel.SUN
                .at(-2349, new EclipticToEquatorialConversion(
                        ZonedDateTime.of(
                                LocalDate.of(2003, Month.JULY,27),
                                LocalTime.of(0, 0, 0, 0),
                                ZoneOffset.UTC)))
                .equatorialPos()
                .decDeg(), 19.35288373097352);
    }

    @Test
    void atWorksWithKnownValues(){
        double lonEclGeo1 = Angle.ofDeg(123.5806048);
        double mA1 = Angle.ofDeg(201.1591307);
        double e1 = Angle.ofDeg(0.524936145);

        double lonEclGeo2 = Angle.ofDeg(328.1137963);
        double mA2 = Angle.ofDeg(43.67934189);
        double e2 = Angle.ofDeg(0.539591543);

        double lonEclGeo3 = Angle.ofDeg(68.78687837);
        double mA3 = Angle.ofDeg(144.5645926);
        double e3 = Angle.ofDeg(0.525938715);

        double lonEclGeo4 = Angle.ofDeg(227.3815892);
        double mA4 = Angle.ofDeg(305.8213071);
        double e4 = Angle.ofDeg(0.538338401);

        double lonEclGeo5 = Angle.ofDeg(280.0148554);
        double mA5 = Angle.ofDeg(357.0025174);
        double e5 = Angle.ofDeg(0.542175889);

        double latEclGeo = 0;

        double numberOfDays1 = Epoch.J2010.daysUntil(ZDT_BOOKEXAMPLE);
        double numberOfDays2 = Epoch.J2010.daysUntil(ZDT_STARTSEMESTER);
        double numberOfDays3 = Epoch.J2010.daysUntil(ZDT_ENDSEMESTER);
        double numberOfDays4 = Epoch.J2010.daysUntil(ZDT_BERLIN_WALL);
        double numberOfDays5 = Epoch.J2010.daysUntil(ZDT_NEW_CENTURY);

        var eclEqu1 = new EclipticToEquatorialConversion(ZDT_BOOKEXAMPLE);
        var eclEqu2 = new EclipticToEquatorialConversion(ZDT_STARTSEMESTER);
        var eclEqu3 = new EclipticToEquatorialConversion(ZDT_ENDSEMESTER);
        var eclEqu4 = new EclipticToEquatorialConversion(ZDT_BERLIN_WALL);
        var eclEqu5 = new EclipticToEquatorialConversion(ZDT_NEW_CENTURY);

        var eclCoords1 = EclipticCoordinates.of(lonEclGeo1, latEclGeo);
        var eclCoords2 = EclipticCoordinates.of(lonEclGeo2, latEclGeo);
        var eclCoords3 = EclipticCoordinates.of(lonEclGeo3, latEclGeo);
        var eclCoords4 = EclipticCoordinates.of(lonEclGeo4, latEclGeo);
        var eclCoords5 = EclipticCoordinates.of(lonEclGeo5, latEclGeo);

        var eclConverted1 = eclEqu1.apply(eclCoords1);
        var eclConverted2 = eclEqu1.apply(eclCoords2);
        var eclConverted3 = eclEqu1.apply(eclCoords3);
        var eclConverted4 = eclEqu1.apply(eclCoords4);
        var eclConverted5 = eclEqu1.apply(eclCoords5);

        var sun1 = SunModel.SUN.at(numberOfDays1, eclEqu1);
        var sun2 = SunModel.SUN.at(numberOfDays2, eclEqu2);
        var sun3 = SunModel.SUN.at(numberOfDays3, eclEqu3);
        var sun4 = SunModel.SUN.at(numberOfDays4, eclEqu4);
        var sun5 = SunModel.SUN.at(numberOfDays5, eclEqu5);

        assertEquals(eclConverted1.ra(), sun1.equatorialPos().ra(), 1e-7); //7 DECIMALS
        assertEquals(eclConverted1.dec(), sun1.equatorialPos().dec(), 1e-7); //7 DECIMALS
        assertEquals(mA1, Angle.normalizePositive(sun1.meanAnomaly()), 1e-6); //6 DECIMALS
        assertEquals(e1, sun1.angularSize(), 1e-6); //6 DECIMALS

        assertEquals(eclConverted2.ra(), sun2.equatorialPos().ra(), 1e-5); //5 DECIMALS
        assertEquals(eclConverted2.dec(), sun2.equatorialPos().dec(), 1e-4); //4 DECIMALS
        assertEquals(mA2, Angle.normalizePositive(sun2.meanAnomaly()), 1e-5); //5 DECIMALS
        assertEquals(e2, sun2.angularSize(), 1e-6); //6 DECIMALS

        assertEquals(eclConverted3.ra(), sun3.equatorialPos().ra(), 1e-5); //5 DECIMALS
        assertEquals(eclConverted3.dec(), sun3.equatorialPos().dec(), 1e-4); //4 DECIMALS
        assertEquals(mA3, Angle.normalizePositive(sun3.meanAnomaly()), 1e-6); //6 DECIMALS
        assertEquals(e3, sun3.angularSize(), 1e-6); //6 DECIMALS

        assertEquals(eclConverted4.ra(), sun4.equatorialPos().ra(), 1e-4); //4 DECIMALS
        assertEquals(eclConverted4.dec(), sun4.equatorialPos().dec(), 1e-4); //4 DECIMALS
        assertEquals(mA4, Angle.normalizePositive(sun4.meanAnomaly()), 1e-5); //5 DECIMALS
        assertEquals(e4, sun4.angularSize(), 1e-5); //5 DECIMALS

        assertEquals(eclConverted5.ra(), sun5.equatorialPos().ra(), 1e-6); //6 DECIMALS
        assertEquals(eclConverted5.dec(), sun5.equatorialPos().dec(), 1e-4); //4 DECIMALS
        assertEquals(mA5, Angle.normalizePositive(sun5.meanAnomaly()), 1e-5); //6 DECIMALS
        assertEquals(e5, sun5.angularSize(), 1e-6); //6 DECIMALS
    }
}