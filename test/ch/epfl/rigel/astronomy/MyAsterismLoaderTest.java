package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/* DEBUG SCRIPT (IF NEEDED)
for(Asterism a : l){
                System.out.println(test.asterismIndices(a));
                for(Star s : a.stars())
                    System.out.printf(Locale.ROOT, "%d %s %s %.3f %d %n", s.hipparcosId(), s.name(), s.equatorialPos(), s.magnitude(), s.colorTemperature());
                System.out.println();
            }
 */
class MyAsterismLoaderTest {
    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";
    private static final String ASTERISM_CATALOGUE_NAME =
            "/asterisms.txt";

    @Test
    void asterismLoaderIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    @Test
    void asterismLoaderHasCorrectNumberOfAsterisms() throws IOException {
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)){

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            int numberOfAsterisms = 153; //FOUND WITH THE TERMINAL
            assertEquals(numberOfAsterisms, test.asterisms().size());
        }
    }

    @Test
    void asterismLoaderHasCorrectNbrOfKeys() throws IOException {
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)) {

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            for(Asterism as : test.asterisms()){
                int nbrOfStars = as.stars().size();
                int nbrOfKeys = test.asterismIndices(as).size();
                assertEquals(nbrOfStars, nbrOfKeys);
            }
        }
    }

    @Test
    void asterismLoaderContainsRigel() throws IOException {
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)){

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            List<Asterism> l = new ArrayList<>();

            for(Asterism as : test.asterisms()){
                for(Star star : as.stars()){
                    if((star.name()).equalsIgnoreCase("Rigel")){
                        l.add(as);
                    }
                }
            }
            assertNotNull(l);
        }
    }

    @Test
    void asterismLoaderOutputsCorrectIDForRigel() throws IOException {
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)){

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            int rigelID = 0;
            for(Asterism as : test.asterisms()){
                for(Star star : as.stars()){
                    if((star.name()).equalsIgnoreCase("rigel")) {
                        rigelID = test.asterismIndices(as).get(as.stars().indexOf(star));
                    }
                }
            }
            assertEquals(1019, rigelID);
        }
    }

    @Test
    void asterismLoaderContainsBetelgeuse() throws IOException {
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)){

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            List<Asterism> l = new ArrayList<>();

            for(Asterism as : test.asterisms()){
                for(Star star : as.stars()){
                    if((star.name()).equalsIgnoreCase("betelgeuse")){
                        l.add(as);
                    }
                }
            }
            assertNotNull(l);
        }
    }

    @Test
    void asterismLoaderOutputsCorrectIDForBetelgeuse() throws IOException {
        try(InputStream asterismStream = getClass()
                .getResourceAsStream(ASTERISM_CATALOGUE_NAME);
            InputStream hygStream = getClass()
                    .getResourceAsStream(HYG_CATALOGUE_NAME)){

            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                    .build();

            int betelID = 0;
            for(Asterism as : test.asterisms()){
                for(Star star : as.stars()){
                    if((star.name()).equalsIgnoreCase("betelgeuse")) {
                        betelID = test.asterismIndices(as).get(as.stars().indexOf(star));
                    }
                }
            }
            assertEquals(1213, betelID);
        }
    }

}