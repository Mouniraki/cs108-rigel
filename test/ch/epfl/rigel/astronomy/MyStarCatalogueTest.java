package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyStarCatalogueTest {
        @Test
        void fileLoadingTest(){
            List<Star> stars = new ArrayList<>();
            var ra = Angle.ofHr(21);
            var lat_dec = Angle.TAU/8;
            var equPos = EquatorialCoordinates.of(ra, lat_dec);

            stars.add(new Star(1, "abc", equPos, 1, 2));

            List<Asterism> asterisms = new ArrayList<>();
            asterisms.add(new Asterism(stars));

            StarCatalogue starCatalogue = new StarCatalogue(stars, asterisms);

            starCatalogue.asterismIndices(asterisms.get(0));



        }
//            var rng = TestRandomizer.newRandom();
//            for(int i=0; i < 1000; ++i) {
//                String starName = "TestedStar";
//
//
//
//                float magnitude = 0.1f * i;
//                float colorIndex = (float) rng.nextDouble(-0.5, 5.5);
//
//                var star = new Star(i, starName, equPos, magnitude, colorIndex);
//                assertEquals(i, star.hipparcosId());
//                assertEquals("TestedStar", star.name());
//                assertEquals(equPos.toString(), star.equatorialPos().toString());
//                assertEquals(0, star.angularSize());
//                assertEquals(magnitude, star.magnitude());



}