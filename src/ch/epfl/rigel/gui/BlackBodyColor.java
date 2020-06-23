package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Determines the color of a black body given its color temperature.
 *
 * @author Mounir Raki (310287)
 */
public final class BlackBodyColor {
    private final static Map<Integer, Color> TEMPERATURE_COLOR = initTable();
    private final static ClosedInterval TEMPERATURE_INTERVAL = ClosedInterval.of(1000, 40000);

    private BlackBodyColor(){}

    private static Map<Integer, Color> initTable(){
        try(InputStream stream = BlackBodyColor.class.getResourceAsStream("/bbr_color.txt");
            BufferedReader r = new BufferedReader(new InputStreamReader(stream, StandardCharsets.US_ASCII))){
            String line;
            Map<Integer, Color> map = new HashMap<>();

            int tempStartIndex = 1;
            int tempEndIndex = 6;
            int unitStartIndex = 10;
            int unitEndIndex = 15;
            int rgbStartIndex = 80;
            int rgbEndIndex = 87;

            while((line = r.readLine()) != null){
                if(line.charAt(0) != '#'){
                    if(line.substring(unitStartIndex, unitEndIndex).contains("10deg")){
                        int colorTemperature = Integer.parseInt(line.substring(tempStartIndex, tempEndIndex).trim());
                        Color color = Color.web(line.substring(rgbStartIndex, rgbEndIndex).trim());
                        map.put(colorTemperature, color);
                    }
                }
            }
        return Collections.unmodifiableMap(map);
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
        Preconditions.checkInInterval(TEMPERATURE_INTERVAL, colorTemperature);
        Preconditions.checkArgument(!TEMPERATURE_COLOR.isEmpty());
        double factor = 100.0;
        double index = Math.round(colorTemperature / factor);
        int approachedTemp = (int) (index * factor);
        return TEMPERATURE_COLOR.get(approachedTemp);
    }
}
