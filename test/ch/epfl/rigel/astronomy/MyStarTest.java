package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStarTest {
    @Test
    void constructorWorksWithValidValues(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i < 1000; ++i) {
            var hipparcosId = i;

            String starName = "TestedStar";

            var ra = rng.nextDouble(0, Angle.ofHr(24));
            var lat_dec = rng.nextDouble(-Angle.TAU/4, Angle.TAU/4);

            var equPos = EquatorialCoordinates.of(ra, lat_dec);

            float magnitude = 0.1f * i;
            float colorIndex = 5.5f;

            if(0.1 * i <= 5.5){
                colorIndex = 0.1f * i - 0.5f;
            }

            var star = new Star(hipparcosId, starName, equPos, magnitude, colorIndex);
            assertEquals(hipparcosId, star.hipparcosId());
            assertEquals("TestedStar", star.name());
            assertEquals(equPos.toString(), star.equatorialPos().toString());
            assertEquals(0, star.angularSize());
            assertEquals(magnitude, star.magnitude());
        }
    }


    @Test
    void constructorFailsOnWrongHipparcosID() {
        var rng = TestRandomizer.newRandom();
        for (int i = 1; i < 10; ++i) {
            var hipparcosId = -1 * i;
            String starName = "TestedStar";
            var ra = rng.nextDouble(0, Angle.ofHr(24));
            var lat_dec = rng.nextDouble(-Angle.TAU / 4, Angle.TAU / 4);
            var equPos = EquatorialCoordinates.of(ra, lat_dec);
            float magnitude = 0.1f * i;
            float colorIndex = 5.5f;
            if (0.1 * i <= 5.5) {
                colorIndex = 0.1f * i - 0.5f;
            }

            float finalColorIndex = colorIndex;
            assertThrows(IllegalArgumentException.class, () -> {
                new Star(hipparcosId, starName, equPos, magnitude, finalColorIndex);
            });
        }
    }

        @Test
        void constructorFailsOnWrongColorIndex(){
            var rng = TestRandomizer.newRandom();
            for(int i=1; i < 10; ++i) {
                var hipparcosId = i;
                String starName = "TestedStar";
                var ra = rng.nextDouble(0, Angle.ofHr(24));
                var lat_dec = rng.nextDouble(-Angle.TAU/4, Angle.TAU/4);
                var equPos = EquatorialCoordinates.of(ra, lat_dec);
                float magnitude = 0.1f * i;
                float colorIndex = 5.5f;

                if(i % 2 == 0){
                    colorIndex = -0.500001f - 0.0001f * i;
                }
                else{
                    colorIndex = 5.5f + 0.00001f * i;
                }


                float finalColorIndex = colorIndex;
                assertThrows(IllegalArgumentException.class, () -> {
                    new Star(hipparcosId, starName, equPos, magnitude, finalColorIndex);
                });
            }
    }

    @Test
    void constructorFailsOnNullEquatorialPosition(){
        var rng = TestRandomizer.newRandom();
        for(int i=0; i < 1000; ++i) {
            var hipparcosId = i;
            String starName = "TestedStar";
            var ra = rng.nextDouble(0, Angle.ofHr(24));
            var lat_dec = rng.nextDouble(-Angle.TAU/4, Angle.TAU/4);
            var equPos = EquatorialCoordinates.of(ra, lat_dec);
            float magnitude = 0.1f * i;
            float colorIndex = 5.5f;
            if(0.1 * i <= 5.5){
                colorIndex = 0.1f * i - 0.5f;
            }

            float finalColorIndex = colorIndex;
            assertThrows(NullPointerException.class, () -> {
                new Star(hipparcosId, starName, null, magnitude, finalColorIndex);
            });
        }
    }

    @Test
    void starTemperatureIsValid() {
        var rng = TestRandomizer.newRandom();
            var hipparcosId = 2;

            String starName = "TestedStar";

            var ra = rng.nextDouble(0, Angle.ofHr(24));
            var lat_dec = rng.nextDouble(-Angle.TAU/4, Angle.TAU/4);

            var equPos = EquatorialCoordinates.of(ra, lat_dec);

            float magnitude = 0.1f;
            float colorIndex = 5.5f;

            float rigelColorIndex = -0.03f;
            float betelgeuseColorIndex = 1.5f;
        var rigel = new Star(hipparcosId, starName, equPos, magnitude, rigelColorIndex);
        var betelgeuse = new Star(hipparcosId, starName, equPos, magnitude, betelgeuseColorIndex);


        assertEquals(10500, rigel.colorTemperature(), 18);
        assertEquals(3800, betelgeuse.colorTemperature(),18);
    }



}