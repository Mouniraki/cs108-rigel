package ch.epfl.rigel.coordinates;

import jdk.nashorn.api.scripting.NashornException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStereographicProjectionTest {

    @Test
    void circleCenterForParallelWorksForAllValues() {
        var h_center = HorizontalCoordinates.ofDeg(60, 40);

        for(int i=0; i<1000; ++i) {
            double azDeg = Math.random()*360.0;
            double altDeg = Math.random()*180.0 - 90.0;
            var h1 = HorizontalCoordinates.ofDeg(azDeg, altDeg);
            var s = new StereographicProjection(h_center);
            double yCenter = Math.cos(h_center.lat()) / (Math.sin(h1.lat()) + Math.sin(h_center.lat()));
            assertEquals(0.0, s.circleCenterForParallel(h1).x());
            assertEquals(yCenter, s.circleCenterForParallel(h1).y());
        }
    }

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