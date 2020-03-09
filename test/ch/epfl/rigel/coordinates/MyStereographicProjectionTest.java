package ch.epfl.rigel.coordinates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStereographicProjectionTest {

    @Test
    void equalsThrowsUOE(){
        var c1 = new StereographicProjection(HorizontalCoordinates.ofDeg(30, 0));
        var c2 = new StereographicProjection(HorizontalCoordinates.ofDeg(40, 60));
        assertThrows(UnsupportedOperationException.class, () -> {
            c1.equals(c2);
        });
    }

    @Test
    void hashCodeThrowsUOE(){
        var c1 = new StereographicProjection(HorizontalCoordinates.ofDeg(30, 0));
        assertThrows(UnsupportedOperationException.class, () -> {
            c1.hashCode();
        });
    }
}