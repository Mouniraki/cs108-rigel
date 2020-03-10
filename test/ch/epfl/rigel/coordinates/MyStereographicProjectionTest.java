package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStereographicProjectionTest {
    @Test
    void circleCenterForParallelWorksForValidValues() {
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<100; ++i) {
            var azCenter = rng.nextDouble(0, 360);
            var altCenter = rng.nextDouble(-90, 90);
            var h_center = HorizontalCoordinates.ofDeg(azCenter, altCenter);
            var s = new StereographicProjection(h_center);

            for (int j = 0; j < 1000; ++j) {
                var azDeg = rng.nextDouble(0, 360);
                var altDeg = rng.nextDouble(-90, 90);
                var h1 = HorizontalCoordinates.ofDeg(azDeg, altDeg);
                double yCenter = Math.cos(h_center.lat()) / (Math.sin(h1.lat()) + Math.sin(h_center.lat()));
                assertEquals(0.0, s.circleCenterForParallel(h1).x());
                assertEquals(yCenter, s.circleCenterForParallel(h1).y());
            }
        }
    }

    @Test
    void circleCenterForParallelOutputsInfinity(){
        var h_center = HorizontalCoordinates.ofDeg(0, 0);
        var h = HorizontalCoordinates.ofDeg(0, 0);
        var s = new StereographicProjection(h_center);
        double y = Math.cos(h_center.lat()) / (Math.sin(h.lat()) + Math.sin(h_center.lat()));
        assertEquals(y, s.circleCenterForParallel(h).y());
    }

    @Test
    void circleRadiusForParallelWorksForValidValues() {
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<100; ++i) {
            var azCenter = rng.nextDouble(0, 360);
            var altCenter = rng.nextDouble(-90, 90);
            var h_center = HorizontalCoordinates.ofDeg(azCenter, altCenter);
            var s = new StereographicProjection(h_center);

            for (int j = 0; j < 1000; ++j) {
                var azDeg = rng.nextDouble(0, 360);
                var altDeg = rng.nextDouble(-90, 90);
                var h1 = HorizontalCoordinates.ofDeg(azDeg, altDeg);
                double radius = Math.cos(h1.lat()) / (Math.sin(h1.lat()) + Math.sin(h_center.lat()));
                assertEquals(radius, s.circleRadiusForParallel(h1));
            }
        }
    }

    @Test
    void circleRadiusForParallelOutputsInfinity(){
        var h_center = HorizontalCoordinates.ofDeg(0, 0);
        var h = HorizontalCoordinates.ofDeg(0, 0);
        var s = new StereographicProjection(h_center);
        double radius = Math.cos(h.lat()) / (Math.sin(h.lat()) + Math.sin(h_center.lat()));
        assertEquals(radius, s.circleCenterForParallel(h).y());
    }

    @Test
    void applyToAngleWorksWithAllValues(){
        var s = new StereographicProjection(HorizontalCoordinates.ofDeg(0, 50));

        for(int i=0; i<1000; ++i) {
            var angle = Math.random() * Angle.TAU;
            double equation = 2 * Math.tan(angle / 4);
            assertEquals(equation, s.applyToAngle(angle));
        }
    }

    @Test
    void applyWorksWithAllValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<100; ++i) {
            var azCenter = rng.nextDouble(0, 360);
            var altCenter = rng.nextDouble(-90, 90);
            var h_center = HorizontalCoordinates.ofDeg(azCenter, altCenter);
            var s = new StereographicProjection(h_center);

            for (int j = 0; j < 1000; ++j) {
                var azDeg = rng.nextDouble(0, 360);
                var altDeg = rng.nextDouble(-90, 90);

                var h1 = HorizontalCoordinates.ofDeg(azDeg, altDeg);

                double lambdaDelta = h1.lon() - h_center.lon();
                double d = 1 / (1 + Math.sin(h1.lat()) * Math.sin(h_center.lat()) + Math.cos(h1.lat()) * Math.cos(h_center.lat()) * Math.cos(lambdaDelta));

                double x = d * Math.cos(h1.lat()) * Math.sin(lambdaDelta);
                double y = d * (Math.sin(h1.lat()) * Math.cos(h_center.lat()) - Math.cos(h1.lat()) * Math.sin(h_center.lat()) * Math.cos(lambdaDelta));

                assertEquals(x, s.apply(h1).x());
                assertEquals(y, s.apply(h1).y());
            }
        }
    }

    @Test
     void inverseApplyWorksWithAllValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<100; ++i) {
            var azDeg = rng.nextDouble(0, 360);
            var altDeg = rng.nextDouble(-90, 90);
            var h_center = HorizontalCoordinates.ofDeg(azDeg, altDeg);
            var s = new StereographicProjection(h_center);

            for (int j = 0; j < 1000; ++j) {
                var x = rng.nextDouble(-1000, 1000);
                var y = rng.nextDouble(-1000, 1000);

                var c = CartesianCoordinates.of(x, y);

                double rho = Math.sqrt(c.x() * c.x() + c.y() * c.y());

                double sinc = (2 * rho) / (rho * rho + 1);
                double cosc = (1 - rho * rho) / (rho * rho + 1);

                double lambda = Angle.normalizePositive(Math.atan2(c.x() * sinc, rho * Math.cos(h_center.lat()) * cosc - c.y() * Math.sin(h_center.lat()) * sinc) + h_center.lon());
                double fi = Math.asin(cosc * Math.sin(h_center.lat()) + ((c.y() * sinc * Math.cos(h_center.lat())) / rho));

                //System.out.println(lambda + " " + fi);

                var result = s.inverseApply(c);
                assertEquals(lambda, result.lon());
                assertEquals(fi, result.lat());
            }
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