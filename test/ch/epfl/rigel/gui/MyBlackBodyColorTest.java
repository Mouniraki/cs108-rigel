package ch.epfl.rigel.gui;

import ch.epfl.test.TestRandomizer;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyBlackBodyColorTest {

    @Test
    void colorForTemperatureWorksWithFramapadValues(){
        assertEquals(Color.web("#ff3800"), BlackBodyColor.colorForTemperature(1000));
        assertEquals(Color.web("#ff8912"), BlackBodyColor.colorForTemperature(2000));
        assertEquals(Color.web("#ffdbba"), BlackBodyColor.colorForTemperature(4500));
        assertEquals(Color.web("#ccdbff"), BlackBodyColor.colorForTemperature(10000));
        assertEquals(Color.web("#9bbcff"), BlackBodyColor.colorForTemperature(40000));

        assertEquals(Color.web("#ffcc99"), BlackBodyColor.colorForTemperature(3798));
        assertEquals(Color.web("#ffcc99"), BlackBodyColor.colorForTemperature(3802));

        assertEquals(Color.web("#ff3800"), BlackBodyColor.colorForTemperature(1049));
        assertEquals(Color.web("#ff8912"), BlackBodyColor.colorForTemperature(1951));
    }

    @Test
    void colorForTemperatureWorksWithValidValues(){
        Color color = Color.web("#c8d9ff");
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i){
            int randomTemp = rng.nextInt(10450, 10500);
            assertEquals(color, BlackBodyColor.colorForTemperature(randomTemp));
        }
    }

    @Test
    void colorForTemperatureFailsOnInvalidValues(){
        Color color = Color.web("#c8d9ff");
        var rng = TestRandomizer.newRandom();
        for(int i=0; i<1000; ++i){
            int randomNegativeTemp = rng.nextInt(40001, 100000);
            int randomPositiveTemp = rng.nextInt(0, 1000);

            assertThrows(IllegalArgumentException.class, () -> {
                BlackBodyColor.colorForTemperature(randomNegativeTemp);
                BlackBodyColor.colorForTemperature(randomPositiveTemp);
            });
        }
    }
}