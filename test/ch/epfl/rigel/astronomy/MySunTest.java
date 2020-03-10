package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySunTest {
    @Test
    void constructorWorksWithValidValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var lon = rng.nextDouble(0, Angle.TAU);
            var ra = rng.nextDouble(0, Angle.ofHr(24));
            var lat_dec = rng.nextDouble(-Angle.TAU/4, Angle.TAU/4);

            var eclPos = EclipticCoordinates.of(lon, lat_dec);
            var equPos = EquatorialCoordinates.of(ra, lat_dec);

            var angularSize = (float) rng.nextDouble(0, 100000);
            var meanAnomaly = (float) rng.nextDouble(1000);

            var sun = new Sun(eclPos, equPos, angularSize, meanAnomaly);
            assertEquals("Sun", sun.name());
            assertEquals(-26.7, sun.magnitude());
            assertEquals(eclPos.toString(), sun.eclipticPos().toString());
            assertEquals(equPos.toString(), sun.equatorialPos().toString());
            assertEquals(angularSize, sun.angularSize());
            assertEquals(meanAnomaly, sun.meanAnomaly());
            assertEquals("Sun", sun.toString());
        }
    }


    @Test
    void constructorFailsOnNegativeAngularSize(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var eclPos = EclipticCoordinates.of(0, 0);
            var equPos = EquatorialCoordinates.of(0, 0);
            var angularSize = (float) rng.nextDouble(-100000.0, 0);
            var meanAnomaly = 0.f;
            assertThrows(IllegalArgumentException.class, () -> {
                new Sun(eclPos, equPos, angularSize, meanAnomaly);
            });
        }
    }

    @Test
    void constructorFailsOnNullEquatorialPosition(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var eclPos = EclipticCoordinates.of(0, 0);
            var angularSize = (float) rng.nextDouble(-100000.0, 0);
            var meanAnomaly = 0.f;

            assertThrows(IllegalArgumentException.class, () -> {
                new Sun(eclPos, null, angularSize, meanAnomaly);
            });
        }
    }

    @Test
    void constructorFailsOnNullEclipticPosition(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var equPos = EquatorialCoordinates.of(0, 0);
            var angularSize = (float) rng.nextDouble(-100000.0, 0);
            var meanAnomaly = 0.f;

            assertThrows(IllegalArgumentException.class, () -> {
                new Sun(null, equPos, angularSize, meanAnomaly);
            });
        }
    }
}