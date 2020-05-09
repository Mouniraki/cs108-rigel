package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyStarCatalogueBuilderTest {

    @Test
    void builderBuildsCorrectly() {
        EquatorialCoordinates equPos = EquatorialCoordinates.of(Angle.ofHr(21), Angle.TAU/8);
        List<Star> generalListOfStars = new ArrayList<>();
        List<Star> listOfStars0 = new ArrayList<>();
        List<Star> listOfStars1 = new ArrayList<>();
        List<Asterism> asterisms = new ArrayList<>();

        Star star0 = new Star(1, "star0", equPos, 1.f, 1.f);
        Star star1 = new Star(1, "star1", equPos, 1.f, 1.f);
        Star star2 = new Star(1, "star2", equPos, 1.f, 1.f);
        Star star3 = new Star(1, "star3", equPos, 1.f, 1.f);
        Star star4 = new Star(1, "star4", equPos, 1.f, 1.f);

        generalListOfStars.add(star0);
        generalListOfStars.add(star1);
        generalListOfStars.add(star2);
        generalListOfStars.add(star3);
        generalListOfStars.add(star4);

        listOfStars0.add(star0);
        listOfStars0.add(star1);
        listOfStars1.add(star2);
        listOfStars1.add(star3);
        listOfStars1.add(star4);

        Asterism asterism0 = new Asterism(listOfStars0);
        Asterism asterism1 = new Asterism(listOfStars1);
        asterisms.add(asterism0);
        asterisms.add(asterism1);

        StarCatalogue.Builder builder = new StarCatalogue.Builder();

        builder.addStar(star0);
        builder.addStar(star1);
        builder.addStar(star2);
        builder.addStar(star3);
        builder.addStar(star4);

        builder.addAsterism(asterism0);
        builder.addAsterism(asterism1);

        StarCatalogue catalogueFromBuilder = builder.build();
        StarCatalogue normalCatalogue = new StarCatalogue(generalListOfStars, asterisms);

        for(int i = 0; i < normalCatalogue.stars().size(); ++i){
            assertEquals(normalCatalogue.stars().get(i), catalogueFromBuilder.stars().get(i));
        }
        assertEquals(normalCatalogue.asterisms(), catalogueFromBuilder.asterisms());
    }

    @Test
    void gettersAreUnmodifiableAndImmutable() {
        EquatorialCoordinates equPos = EquatorialCoordinates.of(Angle.ofHr(21), Angle.TAU/8);
        List<Star> generalListOfStars = new ArrayList<>();
        List<Star> listOfStars0 = new ArrayList<>();
        List<Star> listOfStars1 = new ArrayList<>();
        List<Star> listOfStarsForException = new ArrayList<>();
        List<Asterism> asterisms = new ArrayList<>();

        Star star0 = new Star(1, "star0", equPos, 1.f, 1.f);
        Star star1 = new Star(1, "star1", equPos, 1.f, 1.f);
        Star star2 = new Star(1, "star2", equPos, 1.f, 1.f);
        Star star3 = new Star(1, "star3", equPos, 1.f, 1.f);
        Star star4 = new Star(1, "star4", equPos, 1.f, 1.f);

        generalListOfStars.add(star0);
        generalListOfStars.add(star1);
        generalListOfStars.add(star2);
        generalListOfStars.add(star3);
        generalListOfStars.add(star4);

        listOfStars0.add(star0);
        listOfStars0.add(star1);
        listOfStars1.add(star2);
        listOfStars1.add(star3);
        listOfStars1.add(star4);

        Asterism asterism0 = new Asterism(listOfStars0);
        Asterism asterism1 = new Asterism(listOfStars1);

        listOfStarsForException.add(star0);
        Star star5 = new Star(1, "star4", equPos, 1.f, 1.f);
        listOfStarsForException.add(star5);

        asterisms.add(asterism0);
        asterisms.add(asterism1);

        StarCatalogue.Builder builder = new StarCatalogue.Builder();

        builder.addStar(star0);
        builder.addStar(star1);
        builder.addStar(star2);
        builder.addStar(star3);
        builder.addStar(star4);

        builder.addAsterism(asterism0);
        builder.addAsterism(asterism1);

        assertThrows(UnsupportedOperationException.class, () -> {
            builder.stars().add(new Star(1, "star4", equPos, 1.f, 1.f));
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            builder.asterisms().add(asterism0);
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            builder.stars().remove(0);
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            builder.asterisms().remove(0);
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            builder.stars().clear();
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            builder.asterisms().clear();
        });
    }
}