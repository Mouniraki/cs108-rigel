package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * CLASSDESCRIPTION
 *
 * @author Mounir Raki (310287)
 */
public class BlackBodyColor {
    private final Map<Integer, Color> map;

    private BlackBodyColor(Map<Integer, Color> map){
        this.map = Map.copyOf(map);//CHECK IF IMMUABILITY IS NEEDED
    }

    public static BlackBodyColor of(){
        try(InputStream stream = BlackBodyColor.class.getResourceAsStream("/bbr_color.txt")){
            BufferedReader r = new BufferedReader(new InputStreamReader(stream));
            String line;
            Map<Integer, Color> comboTemperatureColor = new HashMap<>();

            while((line = r.readLine()) != null){
                if(line.charAt(0) != '#'){
                    if(line.substring(10, 15).contains("10deg")){
                        int colorTemperature = Integer.parseInt(line.substring(1, 6).trim());
                        Color color = Color.web(line.substring(80, 87).trim());
                        comboTemperatureColor.put(colorTemperature, color);
                    }
                }
            }
            return new BlackBodyColor(comboTemperatureColor);
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Color colorForTemperature(int colorTemperature) {
        Preconditions.checkInInterval(ClosedInterval.of(1000, 40000), colorTemperature);
        double index = Math.round(colorTemperature / 100.0);
        int approachedTemp = (int) (index * 100);
        return map.get(approachedTemp);
    }
}
