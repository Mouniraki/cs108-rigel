package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyStarCatalogueTest {

    EquatorialCoordinates equPos = EquatorialCoordinates.of(Angle.ofHr(21), Angle.TAU/8);
    private List<Star> stars = new ArrayList<>(Arrays.asList(new Star(1, "star0", equPos, 1.f, 1.f), new Star(1, "star1", equPos, 1.f, 1.f), new Star(1, "star2", equPos, 1.f, 1.f), new Star(1, "star3", equPos, 1.f, 1.f)));
    private List<Asterism> asterisms = new ArrayList<>();

    private static final String CATALOGUE_NAME =
            "/hygdata_v3.csv";
    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";

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
        Asterism testAsterism = new Asterism(starsForTheAsterism);
        List<Asterism> testAsterisms = new ArrayList<>();
        testAsterisms.add(testAsterism);

        StarCatalogue catalogue = new StarCatalogue(stars, testAsterisms);
        assertEquals(new HashSet<>(testAsterisms), catalogue.asterisms());
    }

    @Test
    void rigelIsContainedInTheCatalogueAndItsSizeIsCorrect() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(CATALOGUE_NAME)) {
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
        }

    }

    @Test
    void theCatalogueContainsAllStars() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();
            for (Asterism asterism : catalogue.asterisms()) {
                int nbIndices = catalogue.asterismIndices(asterism).size();
                int nbStars = asterism.stars().size();

                assertEquals(nbIndices, nbStars);
            }
        }
    }

        @Test
    void catalogueHasTheCorrectOrder() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(CATALOGUE_NAME)) {
            InputStream asterismStream = getClass()
                    .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE).loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            List<Star> allStar = new ArrayList<>(catalogue.stars());

            int index;
            for(Asterism asterism : catalogue.asterisms()){
                List<Integer> cAstInd = catalogue.asterismIndices(asterism);
                index = 0;
                for(Star star : asterism.stars()){
                    assertEquals(allStar.get(cAstInd.get(index)).hipparcosId(), star.hipparcosId());
                    index++;
                }
            }
        }
    }
}