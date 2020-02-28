package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ch.epfl.rigel.coordinates.GeographicCoordinates.*;

class GeographicCoordinatesTest {
    @Test
    void testAngleOutOfBounds(){
        assertThrows(IllegalArgumentException.class, () -> {ofDeg(20, 215);});
        assertThrows(IllegalArgumentException.class, () -> {ofDeg(-200, 15);});
        assertThrows(IllegalArgumentException.class, () -> {ofDeg(-181, 90);});

    }

    @Test
    void ofDegWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double lonVal = Math.random() * 360.0 - 180;
            double latVal = Math.random() * 180.0 - 90.0;
            var h = ofDeg(lonVal, latVal);

            assertEquals(lonVal, h.lonDeg(), 1e-6);
            assertEquals(latVal, h.latDeg(), 1e-6);
        }
    }

    @Test
    void getterWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double lonVal = Math.random() * 360.0 - 180;
            double latVal = Math.random() * 180.0 - 90.0;
            var h = ofDeg(lonVal, latVal);

            assertEquals(Angle.ofDeg(lonVal), h.lon(), 1e-6);
            assertEquals(Angle.ofDeg(latVal), h.lat(), 1e-6);
        }
    }

    @Test
    void ofDegFailsWithInvalidValues(){
        for(int i=0; i<1000; ++i){
            double azValuePositive = 180 + Math.random()*i;
            double altValuePositive = 90.0001 + Math.random()*i;

            double azValueNegative = -0.0001-Math.random()*i;
            double altValueNegative = -90.0001-Math.random()*i;

            assertThrows(IllegalArgumentException.class, () -> {
                GeographicCoordinates.ofDeg(azValuePositive, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                GeographicCoordinates.ofDeg(azValueNegative, altValueNegative);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                GeographicCoordinates.ofDeg(azValueNegative, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                GeographicCoordinates.ofDeg(azValuePositive, altValueNegative);
            });
        }
    }
    @Test
    void toStringTest(){
        var h1 = GeographicCoordinates.ofDeg(90, 90); //(90°, 90°)
        var h2 = GeographicCoordinates.ofDeg(22.5, -60); //(22.5°, -60°)
        var h3 = GeographicCoordinates.ofDeg(2.67984205, 3.642014042);
        var h4 = GeographicCoordinates.ofDeg(35, 7.2);
        var h5 = GeographicCoordinates.ofDeg(6.57, 46.52);


        assertEquals("(lon=90.0000°, lat=90.0000°)", h1.toString());
        assertEquals("(lon=22.5000°, lat=-60.0000°)", h2.toString());
        assertEquals("(lon=2.6798°, lat=3.6420°)", h3.toString());
        assertEquals("(lon=35.0000°, lat=7.2000°)", h4.toString());
        assertEquals("(lon=6.5700°, lat=46.5200°)", h5.toString());

    }


    @Test
    void isValidLonDegWorksWithValidValues(){
        for(int i=0; i<100000; ++i) {
            double lonVal = Math.random() * 360.0 - 180;

            assertTrue(isValidLonDeg(lonVal));        }
    }

    @Test
    void isValidLatDegWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double latVal = Math.random() * 180.0 - 90.0;

            assertTrue(isValidLatDeg(latVal));
        }
    }

    @Test
    void isValidLonDegNotWorksWithInvalidValues(){
        for(int i=0; i<1000; ++i) {
            double lonVal = 180 + Math.random()*i;
            double lonVal2 = -1 * (180 + Math.random()*i);

            assertFalse(isValidLonDeg(lonVal));
            assertFalse(isValidLonDeg(lonVal));        }
    }
}
