package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyEclipticCoordinatesTest {

    @Test
    void ofWorksWithValidValues(){
        for(int i=0; i<1000; ++i) {
            double lonValue = Math.random() * Angle.TAU;
            double latValue = Math.random() * Angle.TAU/2 - Angle.TAU/4;
            var h = EclipticCoordinates.of(lonValue, latValue);

            assertEquals(lonValue, h.lon(), 1e-6);
            assertEquals(latValue, h.lat(), 1e-6);
        }
    }

    @Test
    void ofFailsWithInvalidValues(){
        for(int i=0; i<1000; ++i){
            double lonValuePositive = Angle.TAU + Math.random()*i;
            double latValuePositive = Angle.TAU/4 + Math.random()*i;

            double lonValueNegative = -0.0001-Math.random()*i;
            double latValueNegative = -Angle.TAU/4-Math.random()*i;

            assertThrows(IllegalArgumentException.class, () -> {
                EclipticCoordinates.of(lonValuePositive, latValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                EclipticCoordinates.of(lonValueNegative, latValueNegative);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                EclipticCoordinates.of(lonValueNegative, latValuePositive);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                EclipticCoordinates.of(lonValuePositive, latValueNegative);
            });
        }
    }

    @Test
    void toStringTest(){
        var h1 = EclipticCoordinates.of(Angle.TAU/4, Angle.TAU/4); //(90°, 90°)
        var h2 = EclipticCoordinates.of(Angle.TAU/16, -Angle.TAU/6); //(22.5°, -60°)

        assertEquals("(λ=90.0000°, β=90.0000°)", h1.toString());
        assertEquals("(λ=22.5000°, β=-60.0000°)", h2.toString());
    }

    @Test
    void equalsThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var coordinates = EclipticCoordinates.of(Angle.TAU/4, Angle.TAU/6);
            coordinates.equals(coordinates);
        });
    }

    @Test
    void hashCodeThrowsUOE() {
        assertThrows(UnsupportedOperationException.class, () -> {
            EclipticCoordinates.of(Angle.TAU/4, Angle.TAU/6).hashCode();
        });
    }
}