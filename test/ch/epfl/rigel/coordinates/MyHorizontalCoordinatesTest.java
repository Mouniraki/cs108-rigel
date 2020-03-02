package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.*;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

class MyHorizontalCoordinatesTest {
    @Test
    void ofWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double azValue = Math.random() * Angle.TAU;
            double altValue = Math.random() * Angle.TAU/2 - Angle.TAU/4;
            var h = HorizontalCoordinates.of(azValue, altValue);

            assertEquals(azValue, h.az(), 1e-6);
            assertEquals(altValue, h.alt(), 1e-6);
        }
    }

    @Test
    void ofFailsWithInvalidValues(){
        for(int i=0; i<1000; ++i){
            double azValuePositive = Angle.TAU + Math.random()*i;
            double altValuePositive = Angle.TAU/4 + Math.random()*i;

            double azValueNegative = -0.0001-Math.random()*i;
            double altValueNegative = -Angle.TAU/4-Math.random()*i;

            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.of(azValuePositive, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.of(azValueNegative, altValueNegative);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.of(azValueNegative, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.of(azValuePositive, altValueNegative);
            });
        }
    }

    @Test
    void ofDegWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double azValue = Math.random() * 360.0;
            double altValue = Math.random() * 180.0 - 90.0;
            var h = HorizontalCoordinates.ofDeg(azValue, altValue);

            assertEquals(azValue, h.azDeg(), 1e-6);
            assertEquals(altValue, h.altDeg(), 1e-6);
        }
    }

    @Test
    void ofDegFailsWithInvalidValues(){
        for(int i=0; i<1000; ++i){
            double azValuePositive = 360.0 + Math.random()*i;
            double altValuePositive = 90.0001 + Math.random()*i;

            double azValueNegative = -0.0001-Math.random()*i;
            double altValueNegative = -90.0001-Math.random()*i;

            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.ofDeg(azValuePositive, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.ofDeg(azValueNegative, altValueNegative);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.ofDeg(azValueNegative, altValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                HorizontalCoordinates.ofDeg(azValuePositive, altValueNegative);
            });
        }
    }

    @Test
    void azOctantNameTest(){
        var h1 = HorizontalCoordinates.ofDeg(337.5, 90); //NORD
        var h11 = HorizontalCoordinates.ofDeg(0, 90); //NORD
        var h111 = HorizontalCoordinates.ofDeg(22.4, 90); //NORD

        var h2 = HorizontalCoordinates.ofDeg(22.5, 90); //NORD-EST
        var h22 = HorizontalCoordinates.ofDeg(45, 90); //NORD-EST
        var h222 = HorizontalCoordinates.ofDeg(67.4, 90); //NORD-EST

        var h3 = HorizontalCoordinates.ofDeg(67.5, 90); //EST
        var h33 = HorizontalCoordinates.ofDeg(90, 90); //EST
        var h333 = HorizontalCoordinates.ofDeg(112.4, 90); //EST

        var h4 = HorizontalCoordinates.ofDeg(112.5, 90); //SUD-EST
        var h44 = HorizontalCoordinates.ofDeg(135, 90); //SUD-EST
        var h444 = HorizontalCoordinates.ofDeg(157.4, 90); //SUD-EST

        var h5 = HorizontalCoordinates.ofDeg(157.5, 90); //SUD
        var h55 = HorizontalCoordinates.ofDeg(180, 90); //SUD
        var h555 = HorizontalCoordinates.ofDeg(202.4, 90); //SUD

        var h6 = HorizontalCoordinates.ofDeg(202.5, 90); //SUD-OUEST
        var h66 = HorizontalCoordinates.ofDeg(225, 90); //SUD-OUEST
        var h666 = HorizontalCoordinates.ofDeg(247.4, 90); //SUD-OUEST

        var h7 = HorizontalCoordinates.ofDeg(247.5, 90); //OUEST
        var h77 = HorizontalCoordinates.ofDeg(270, 90); //OUEST
        var h777 = HorizontalCoordinates.ofDeg(292.4, 90); //OUEST

        var h8 = HorizontalCoordinates.ofDeg(292.5, 90); //NORD-OUEST
        var h88 = HorizontalCoordinates.ofDeg(315, 90); //NORD-OUEST
        var h888 = HorizontalCoordinates.ofDeg(337.4, 90); //NORD-OUEST

        assertEquals("N", h1.azOctantName("N", "E", "S", "O"));
        assertEquals("N", h11.azOctantName("N", "E", "S", "O"));
        assertEquals("N", h111.azOctantName("N", "E", "S", "O"));

        assertEquals("NE", h2.azOctantName("N", "E", "S", "O"));
        assertEquals("NE", h22.azOctantName("N", "E", "S", "O"));
        assertEquals("NE", h222.azOctantName("N", "E", "S", "O"));

        assertEquals("E", h3.azOctantName("N", "E", "S", "O"));
        assertEquals("E", h33.azOctantName("N", "E", "S", "O"));
        assertEquals("E", h333.azOctantName("N", "E", "S", "O"));

        assertEquals("SE", h4.azOctantName("N", "E", "S", "O"));
        assertEquals("SE", h44.azOctantName("N", "E", "S", "O"));
        assertEquals("SE", h444.azOctantName("N", "E", "S", "O"));

        assertEquals("S", h5.azOctantName("N", "E", "S", "O"));
        assertEquals("S", h55.azOctantName("N", "E", "S", "O"));
        assertEquals("S", h555.azOctantName("N", "E", "S", "O"));

        assertEquals("SO", h6.azOctantName("N", "E", "S", "O"));
        assertEquals("SO", h66.azOctantName("N", "E", "S", "O"));
        assertEquals("SO", h666.azOctantName("N", "E", "S", "O"));

        assertEquals("O", h7.azOctantName("N", "E", "S", "O"));
        assertEquals("O", h77.azOctantName("N", "E", "S", "O"));
        assertEquals("O", h777.azOctantName("N", "E", "S", "O"));

        assertEquals("NO", h8.azOctantName("N", "E", "S", "O"));
        assertEquals("NO", h88.azOctantName("N", "E", "S", "O"));
        assertEquals("NO", h888.azOctantName("N", "E", "S", "O"));

    }

    @Test
    void angularDistanceToTest(){
        var first1 = HorizontalCoordinates.ofDeg(6.5682, 46.5183);
        var second1 = HorizontalCoordinates.ofDeg(8.5476, 47.3763);

        var first2 = HorizontalCoordinates.ofDeg(6.07675, 46.177101);
        var second2 = HorizontalCoordinates.ofDeg(8.54169, 47.376888);

        var first3 = HorizontalCoordinates.ofDeg(6.07675, 46.177101);
        var second3 = HorizontalCoordinates.ofDeg(6.632273, 46.519653);

        assertEquals(0.02793574, first1.angularDistanceTo(second1), 1e-6);
        assertEquals(0.0361440235, first2.angularDistanceTo(second2), 1e-6);
        assertEquals(0.00897448301, first3.angularDistanceTo(second3), 1e-6);
    }

    @Test
    void toStringTest(){
        var h1 = HorizontalCoordinates.of(Angle.TAU/4, Angle.TAU/4); //(90°, 90°)
        var h2 = HorizontalCoordinates.of(Angle.TAU/16, -Angle.TAU/6); //(22.5°, -60°)
        var h3 = HorizontalCoordinates.ofDeg(2.67984205, 3.642014042);
        var h4 = HorizontalCoordinates.ofDeg(350, 7.2);

        assertEquals("(az=90.0000°, alt=90.0000°)", h1.toString());
        assertEquals("(az=22.5000°, alt=-60.0000°)", h2.toString());
        assertEquals("(az=2.6798°, alt=3.6420°)", h3.toString());
        assertEquals("(az=350.0000°, alt=7.2000°)", h4.toString());
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var coordinates = HorizontalCoordinates.of(Angle.TAU/4, Angle.TAU/6);
            coordinates.equals(coordinates);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            HorizontalCoordinates.of(Angle.TAU/4, Angle.TAU/6).hashCode();
        });
    }

}