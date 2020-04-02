package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyStarCatalogueTest {
    EquatorialCoordinates equPos = EquatorialCoordinates.of(Angle.ofHr(21), Angle.TAU/8);
    private List<Star> stars = new ArrayList<>(Arrays.asList(new Star(1, "star0", equPos, 1.f, 1.f), new Star(1, "star1", equPos, 1.f, 1.f), new Star(1, "star2", equPos, 1.f, 1.f), new Star(1, "star3", equPos, 1.f, 1.f)));
    private List<Asterism> asterisms = new ArrayList<>();

    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";
    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";

//        @Test
//        void fileLoadingTest(){
//            List<Star> stars = new ArrayList<>();
//            var ra = Angle.ofHr(21);
//            var lat_dec = Angle.TAU/8;
//            var equPos = EquatorialCoordinates.of(ra, lat_dec);
//
//            stars.add(new Star(1, "abc", equPos, 1, 2));
//
//            List<Asterism> asterisms = new ArrayList<>();
//            asterisms.add(new Asterism(stars));
//
//            StarCatalogue starCatalogue = new StarCatalogue(stars, asterisms);
//
//            starCatalogue.asterismIndices(asterisms.get(0));
//        }

    @Test
    void constructorFailsWithAsterismStarNotInListOfStars() {
        List<Star> starsForTheAsterism = new ArrayList<>(stars);

        starsForTheAsterism.add(new Star(1, "star5", equPos, 1.f, 1.f));
        asterisms.add(new Asterism(starsForTheAsterism));

        assertThrows(IllegalArgumentException.class, () -> {
            new StarCatalogue(stars, asterisms);
        });
    }

    @Test
    void starsGetterWorksCorrectly() {
        StarCatalogue catalogue = new StarCatalogue(stars, asterisms);
        assertEquals(stars, catalogue.stars());
    }

    @Test
    void asterismsGetterWorksCorrectly() {

        List<Star> starsForTheAsterism = new ArrayList<>(stars);
//        asterisms.add(new Asterism(starsForTheAsterism));
        Asterism testAsterism = new Asterism(starsForTheAsterism);
        List<Asterism> testAsterisms = new ArrayList<>();
        testAsterisms.add(testAsterism);

        StarCatalogue catalogue = new StarCatalogue(stars, testAsterisms);
        assertEquals(new HashSet<>(testAsterisms), catalogue.asterisms());
    }

//    @Test
//    void numberOfIndexIsEqualToNumberOfStartInAsterism(){
//        for (Asterism asterism : catalogue.asterisms()) {
//            int nbIndices = catalogue.asterismIndices(asterism).size();
//            int nbStars = asterism.stars().size();
//            assertEquals(nbIndices, nbStars);
//        }
//    }

    @Test
    void variousTestsAndReadablePrintfOnCompletelyFinishedStarCatalogue() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel")){
                    rigel = s;
                }
            }

            assertNotNull(rigel);

            assertEquals(5067, catalogue.stars().size());
            assertEquals(153, catalogue.asterisms().size());

            for (Asterism asterism : catalogue.asterisms()) {
                int nbIndices = catalogue.asterismIndices(asterism).size();
                int nbStars = asterism.stars().size();

                assertEquals(nbIndices, nbStars);

                System.out.println("This case works correctly, number of stars : " + nbStars);
            }

            List<Star> allStar = new ArrayList<>(catalogue.stars());

            System.out.println("LIST OF STARS :");
            for(Star s : allStar){
                System.out.print(s.hipparcosId() + " ");
            } //should print out the same star IDS as in the fichier (check visually)
            System.out.println();
            System.out.println();


            System.out.println("ASTERISMS : ");
            int i;

            //vérifier visuellement en utilisant CTRL-F que les astérismes contenu dans ASTERISMS sont bien les memes
            //flemme de coder une méthode qui vérifie automatiquement
            for(Asterism asterism : catalogue.asterisms()){
                List<Integer> cAstInd = catalogue.asterismIndices(asterism);
                i = 0;
                for(Star star : asterism.stars()){
                    System.out.print("Hip : ");
                    System.out.print(star.hipparcosId());
                    System.out.print("  foundHipparcos : ");
                    System.out.print(allStar.get(cAstInd.get(i)).hipparcosId());

                /*TEST : l'index stoqué dans asterismIndices renvoie le meme hipparcosId que
                l'index stoqué dans l'astérisme voulu : */
                    assertEquals(allStar.get(cAstInd.get(i)).hipparcosId(), star.hipparcosId());
                    System.out.print(" ||| ");
                    i++;
                }
                System.out.println();
            }
        }
    }
}