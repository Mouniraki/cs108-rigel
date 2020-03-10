package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyPlanetTest {
    @Test
    void constructorWorksWithValidValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var ra = rng.nextDouble(0, Angle.ofHr(24));
            var dec = rng.nextDouble(-Angle.TAU/4, Angle.TAU/4);

            var name = "Planet";
            var equPos = EquatorialCoordinates.of(ra, dec);
            var angularSize = (float) rng.nextDouble(0, 100000);
            var magnitude = (float) rng.nextDouble(1000);

            var planet = new Planet(name, equPos, angularSize, magnitude);
            assertEquals(name, planet.name());
            assertEquals(equPos.toString(), planet.equatorialPos().toString());
            assertEquals(angularSize, planet.angularSize());
            assertEquals(magnitude, planet.magnitude());

            assertEquals(name, planet.toString());
        }
    }

    @Test
    void constructorFailsOnNegativeAngularSize(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var name = "Planet";
            var equPos = EquatorialCoordinates.of(0, 0);
            var angularSize = (float) rng.nextDouble(-100000.0, 0);
            var magnitude = 0.f;

            assertThrows(IllegalArgumentException.class, () -> {
                new Planet(name, equPos, angularSize, magnitude);
            });
        }
    }

    @Test
    void constructorFailsOnNullName(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var equPos = EquatorialCoordinates.of(0, 0);
            var angularSize = (float) rng.nextDouble(-100000.0, 0);
            var magnitude = 0.f;

            assertThrows(IllegalArgumentException.class, () -> {
                new Planet(null, equPos, angularSize, magnitude);
            });
        }
    }

    @Test
    void constructorFailsOnNullEquatorialPosition(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var name = "Planet";
            var angularSize = (float) rng.nextDouble(-100000.0, 0);
            var magnitude = 0.f;

            assertThrows(IllegalArgumentException.class, () -> {
                new Planet(name, null, angularSize, magnitude);
            });
        }
    }
}