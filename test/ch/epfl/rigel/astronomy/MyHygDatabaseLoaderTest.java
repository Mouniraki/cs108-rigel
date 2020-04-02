package ch.epfl.rigel.astronomy;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class MyHygDatabaseLoaderTest {
    private static final String HYG_CATALOGUE_NAME =
            "/hygdata_v3.csv";

    @Test
    void hygDatabaseIsCorrectlyInstalled() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            assertNotNull(hygStream);
        }
    }

    @Test
    void hygDatabaseHasCorrectNumberOfStars() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();

            int numberOfStars = 5067; //FOUND WITH THE TERMINAL
            assertEquals(numberOfStars, test.stars().size());
        }
    }

    @Test
    void hygDatabaseWorksForEmptyNames() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue test = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();

            for(Star s : test.stars()) {
                if(s.name().charAt(0) == '?')
                    assertEquals(' ', s.name().charAt(1));
            }
        }
    }

    @Test
    void hygDatabaseContainsRigel() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();

            Star rigel = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("rigel")) {
                    rigel = s;
                }
            }
            assertNotNull(rigel);
        }
    }

    @Test
    void hygDatabaseContainsBetelgeuse() throws IOException {
        try (InputStream hygStream = getClass()
                .getResourceAsStream(HYG_CATALOGUE_NAME)) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hygStream, HygDatabaseLoader.INSTANCE)
                    .build();

            Star betelgeuse = null;
            for (Star s : catalogue.stars()) {
                if (s.name().equalsIgnoreCase("betelgeuse"))
                    betelgeuse = s;
            }
            assertNotNull(betelgeuse);
        }
    }
}