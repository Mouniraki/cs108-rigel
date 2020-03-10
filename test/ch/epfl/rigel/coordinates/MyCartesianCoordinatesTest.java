package ch.epfl.rigel.coordinates;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MyCartesianCoordinatesTest {
    @Test
    void ofWorksWithAllValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i){
            var x = rng.nextDouble(-1000000, 1000000);
            var y = rng.nextDouble(-1000000, 1000000);
            var c = CartesianCoordinates.of(x, y);
            assertEquals(x, c.x());
            assertEquals(y, c.y());
        }
    }

    @Test
    void toStringWorksProperly(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i){
            var x = rng.nextDouble(-1000000, 1000000);
            var y = rng.nextDouble(-1000000, 1000000);
            var c = CartesianCoordinates.of(x, y);
            var s = String.format(Locale.ROOT, "Cartesian coordinates: (x=%.4f, y=%.4f)", x, y);
            assertEquals(s, c.toString());
        }
    }

    @Test
    void equalsThrowsUOE(){
        var c1 = CartesianCoordinates.of(0, 0);
        var c2 = CartesianCoordinates.of(0, 0);
        assertThrows(UnsupportedOperationException.class, () -> {
            c1.equals(c2);
        });
    }

    @Test
    void hashCodeThrowsUOE(){
        var c1 = CartesianCoordinates.of(0, 0);
        assertThrows(UnsupportedOperationException.class, () -> {
            c1.hashCode();
        });
    }

}