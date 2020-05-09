package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Determines the color of a black body given its color temperature.
 *
 * @author Mounir Raki (310287)
 */
public class BlackBodyColor {
    private final static Map<Integer, Color> TEMPERATURE_COLOR = initTable();

    private BlackBodyColor(){}

    private static Map<Integer, Color> initTable(){
        try(InputStream stream = BlackBodyColor.class.getResourceAsStream("/bbr_color.txt")){
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            String line;
            Map<Integer, Color> map = new HashMap<>();

            while((line = r.readLine()) != null){
                if(line.charAt(0) != '#'){
                    if(line.substring(10, 15).contains("10deg")){
                        int colorTemperature = Integer.parseInt(line.substring(1, 6).trim());
                        Color color = Color.web(line.substring(80, 87).trim());
                        map.put(colorTemperature, color);
                    }
                }
            }
        return Map.copyOf(map);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Associates a color temperature in degrees Kelvin to its color representation.
     *
     * @param colorTemperature
     *          the color temperature for which a color association has to be found
     * @throws IllegalArgumentException
     *          if the color temperature is lower than 1000 or greater than 40000,
     *          or if the association temperature - color is empty or not properly initialized
     *
     * @return the color representation of a color temperature
     */
    public static Color colorForTemperature(int colorTemperature) {
        Preconditions.checkInInterval(ClosedInterval.of(1000, 40000), colorTemperature);
        Preconditions.checkArgument(!TEMPERATURE_COLOR.isEmpty());
        double index = Math.round(colorTemperature / 100.0);
        int approachedTemp = (int) (index * 100);
        return TEMPERATURE_COLOR.get(approachedTemp);
    }
}
