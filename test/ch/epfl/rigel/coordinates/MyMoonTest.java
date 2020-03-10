package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Moon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyMoonTest {

    @Test
    void constructorFailsWithIncorrectValues(){
        for(float i = 1; i < 10; ++i) {
            EquatorialCoordinates ec = EquatorialCoordinates.of(1, 1);
            float finalI = i;
            assertThrows(IllegalArgumentException.class, () -> {
                Moon testMoon1 = new Moon(ec, 1.2f, 1.3f, 1.0f + 0.0000001f * finalI);
            });
        }
    }

    @Test
    void constructorWorksWithIncorrectValues(){
        for(float i = 1; i < 1001; ++i){
        EquatorialCoordinates ec = EquatorialCoordinates.of(1, 1);
        Moon testMoon1 = new Moon(ec, 1.2f, 1.3f,0.001f * i);
    }
}

    @Test
    void toStringTest(){
        EquatorialCoordinates ec = EquatorialCoordinates.of(1, 1);
        var h1 = new Moon(ec, 1.2f, 1.3f, 0.3752f);
        var h2 = new Moon(ec, 1.2f, 1.3f, 0.3758f);
        var h3 = new Moon(ec, 1.2f, 1.3f, 1f);
        var h4 = new Moon(ec, 1.2f, 1.3f, 0.9999f);
        var h5 = new Moon(ec, 1.2f, 1.3f, 0.2137f);


        assertEquals("Lune (37.5%)", h1.toString());
        assertEquals("Lune (37.6%)", h2.toString());
        assertEquals("Lune (100.0%)", h3.toString());
        assertEquals("Lune (100.0%)", h4.toString());
        assertEquals("Lune (21.4%)", h5.toString());

    }
}