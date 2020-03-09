package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MyCartesianCoordinatesTest {
    @Test
    void ofWorksWithAllValues(){
        for(int i=0; i<1000; ++i){
            double x = Math.random()*1000;
            double y = Math.random()*i;
            var c = CartesianCoordinates.of(x, y);
            assertEquals(x, c.x());
            assertEquals(y, c.y());
        }
    }

    @Test
    void toStringWorksProperly(){
        for(int i=0; i<1000; ++i){
            double x = Math.random()*1000;
            double y = Math.random()*i;
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