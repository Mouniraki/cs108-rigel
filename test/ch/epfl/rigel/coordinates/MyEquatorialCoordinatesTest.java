package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static ch.epfl.rigel.coordinates.EquatorialCoordinates.*;
import static org.junit.jupiter.api.Assertions.*;

class MyEquatorialCoordinatesTest {

    @Test
    void testAngleOutOfBounds(){
        assertThrows(IllegalArgumentException.class, () -> {of(20, 215);});
        assertThrows(IllegalArgumentException.class, () -> {of(-200, 15);});
        assertThrows(IllegalArgumentException.class, () -> {of(Angle.ofDeg(360), Angle.ofDeg(55));});
        assertThrows(IllegalArgumentException.class, () -> {of(Angle.ofDeg(222), Angle.ofDeg(90.01));});
        assertThrows(IllegalArgumentException.class, () -> {of(Angle.ofDeg(222), Angle.ofDeg(-90.01));});

    }

    @Test
    void ofDegWorksWithValidValues(){

        var hdd = EquatorialCoordinates.of(3.49066, 1.48353);
        assertEquals(3.49066, hdd.ra(), 1e-6);
        assertEquals(1.48353, hdd.dec(), 1e-6);

        for(int i=0; i < 100000; ++i) {
            double ra = (Math.random() * Angle.TAU);
            double dec = (Math.random() * Angle.TAU / 2 - Angle.TAU / 4);

            var h = EquatorialCoordinates.of(ra, dec);

            assertEquals(ra, h.ra(), 1e-6);
            assertEquals(dec, h.dec(), 1e-6);
        }
    }

    @Test
    void getterWorksWithValidValues(){
        var hdd = EquatorialCoordinates.of(Angle.TAU * 215 / 360, Angle.TAU * 45 / 360); // 215 , 45
        assertEquals(Angle.TAU * 215 / 360, hdd.ra(), 1e-6);
        assertEquals(Angle.TAU * 45 / 360, hdd.dec(), 1e-6);

        assertEquals(215, hdd.raDeg(), 1e-6);
        assertEquals(45, hdd.decDeg(), 1e-6);

        assertEquals(14.333333333, hdd.raHr(), 1e-6);
        assertEquals(45, hdd.decDeg(), 1e-6);

        for(int i=0; i<1000000; ++i) {
            double ra = (Math.random() * Angle.TAU);
            double dec = (Math.random() * Angle.TAU / 2 - Angle.TAU / 4);
            var h = of(ra, dec);

            assertEquals(ra, h.ra(), 1e-6);
            assertEquals(dec, h.dec(), 1e-6);

            assertEquals(Angle.toDeg(ra), h.raDeg(), 1e-6);
            assertEquals(Angle.toDeg(dec), h.decDeg(), 1e-6);

            assertEquals(Angle.toHr(ra), h.raHr(), 1e-6);
        }
    }

    @Test
    void ofDegFailsWithInvalidValues(){
        double azExtremeValuePositive = Angle.TAU + Math.random();
        assertThrows(IllegalArgumentException.class, () -> {
            EquatorialCoordinates.of(azExtremeValuePositive, 0.005);
        });
        for(int i=0; i<1000; ++i){
            double azValuePositive = Angle.TAU + Math.random();
            double altValuePositive = Angle.TAU / 4 + 0.000000001 + Math.random();

            double azValueNegative = -0.0001-Math.random();
            double altValueNegative = -90.0001-Math.random();

            assertThrows(IllegalArgumentException.class, () -> {
                EquatorialCoordinates.of(azValuePositive, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                EquatorialCoordinates.of(azValueNegative, altValueNegative);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                EquatorialCoordinates.of(azValueNegative, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                EquatorialCoordinates.of(azValuePositive, altValueNegative);
            });
        }
    }

    @Test
    void toStringTest2(){
        var ha1 = EquatorialCoordinates.of(Angle.TAU/4, Angle.TAU/4); //(90°, 90°)
        var h2 = EquatorialCoordinates.of(Angle.TAU/16, -Angle.TAU/6); //(22.5°, -60°)
        var h1 = EquatorialCoordinates.of(0.392699075, 0.785375); //(90°, 90°)

        assertEquals("(ra=1.5000h, dec=44.9987°)", h1.toString());
        assertEquals("(ra=6.0000h, dec=90.0000°)", ha1.toString());
        assertEquals("(ra=1.5000h, dec=-60.0000°)", h2.toString());
    }
}
