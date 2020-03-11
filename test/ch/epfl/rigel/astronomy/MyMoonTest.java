package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

class MyMoonTest {
    @Test
    void constructorWorksWithValidValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var name = "Lune";
            var ra = rng.nextDouble(0, 2d * PI);
            var dec = rng.nextDouble(-PI / 2d, PI / 2d);
            var equPos = EquatorialCoordinates.of(ra, dec);
            var angularSize = (float)rng.nextDouble(0, 1000000);
            var magnitude = (float)rng.nextDouble(-1000000, 1000000);
            var phase = (float)rng.nextDouble(0, 1);
            var printer = String.format(Locale.ROOT,"Lune (%.1f%%)", phase * 100.0);
            var moon = new Moon(equPos, angularSize, magnitude, phase);

            assertEquals(name, moon.name());
            assertEquals(equPos.toString(), moon.equatorialPos().toString());
            assertEquals(angularSize, moon.angularSize());
            assertEquals(magnitude, moon.magnitude());
            assertEquals(printer, moon.toString());
        }
    }

    @Test
    void constructorFailsOnIllegalPhaseValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var equPos = EquatorialCoordinates.of(0, 0);
            var angularSize = 0.f;
            var magnitude = 0.f;
            float phaseNegative = (float) rng.nextDouble(-100000.0, 0);
            float phasePositive = (float) rng.nextDouble(1.1, 100000.0);

            assertThrows(IllegalArgumentException.class, () -> {
                new Moon(equPos, angularSize, magnitude, phaseNegative);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                new Moon(equPos, angularSize, magnitude, phasePositive);
            });
        }
    }


    @Test
    void constructorFailsOnNegativeAngularSize(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var equPos = EquatorialCoordinates.of(0, 0);
            var angularSize = (float) rng.nextDouble(-100000.0, 0);
            var magnitude = 0.f;
            var phase = 0.f;
            assertThrows(IllegalArgumentException.class, () -> {
                new Moon(equPos, angularSize, magnitude, phase);
            });
        }
    }

    @Test
    void constructorFailsOnNullEquatorialPosition(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i) {
            var angularSize = (float) rng.nextDouble(-100000.0, 0);
            var magnitude = 0.f;
            var phase = 0.f;

            assertThrows(IllegalArgumentException.class, () -> {
                new Moon(null, angularSize, magnitude, phase);
            });
        }
    }


    @Test
    void toStringTest(){
        EquatorialCoordinates ec = EquatorialCoordinates.of(1, 1);
        var h1 = new Moon(ec, 1.2f, 1.3f, 0.3752f);
        var h2 = new Moon(ec, 1.2f, 1.3f, 0.3758f);
        var h3 = new Moon(ec, 1.2f, 1.3f, 1f);
        var h4 = new Moon(ec, 1.2f, 1.3f, 0.9999f);
        var h5 = new Moon(ec, 1.2f, 1.3f, 0.2137f);


        assertEquals("Lune (37.5%)", h1.toString());
        assertEquals("Lune (37.6%)", h2.toString());
        assertEquals("Lune (100.0%)", h3.toString());
        assertEquals("Lune (100.0%)", h4.toString());
        assertEquals("Lune (21.4%)", h5.toString());

    }
}