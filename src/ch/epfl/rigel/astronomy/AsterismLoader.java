package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.US_ASCII;

public enum AsterismLoader implements StarCatalogue.Loader{
    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try(BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))){
            String line;
            while((line = r.readLine()) != null){
                String[] strArray = line.split(",");
                List<Star> stars = new ArrayList<>();
                for(String hipId : strArray){
                    int hipparcosId = Integer.parseInt(hipId);
                    for(Star star : builder.stars()){ //ISSUE HERE
                        if(star.hipparcosId() == hipparcosId)
                            stars.add(star);
                    }
                }
                Asterism a = new Asterism(stars);

                builder.addAsterism(a);
            }
        }
    }
}
