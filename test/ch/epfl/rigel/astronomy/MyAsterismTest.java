package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyAsterismTest {

    @Test
    void constructorFailsWhenListIsEmpty() {
        List<Star> stars = new ArrayList<Star>();
        assertThrows(IllegalArgumentException.class, () -> {
            new Asterism(stars);
        });
    }

    @Test
    void constructorWorksWithNonEmptyList() {
        List<Star> stars = new ArrayList<Star>();
        var equPos = EquatorialCoordinates.of(0, 0);
        Star star1 = new Star(1, "RandomStar1", equPos, 1.f, 1.f);
        Star star2 = new Star(2, "RandomStar2", equPos, 1.f, 1.f);

        stars.add(star1);
        stars.add(star2);
        Asterism myAsterism = new Asterism(stars);
    }

    @Test
    void asterismGetterWorksCorrectly() {
        List<Star> stars = new ArrayList<Star>();
        var equPos = EquatorialCoordinates.of(0, 0);
        Star star1 = new Star(1, "RandomStar1", equPos, 1.f, 1.f);
        Star star2 = new Star(2, "RandomStar2", equPos, 1.f, 1.f);

        stars.add(star1);
        stars.add(star2);
        Asterism myAsterism = new Asterism(stars);
        assertEquals(stars, myAsterism.stars());
    }

}