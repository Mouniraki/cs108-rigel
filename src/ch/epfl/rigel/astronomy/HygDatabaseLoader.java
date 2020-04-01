package ch.epfl.rigel.astronomy;

import java.io.*;
import java.util.List;

import static java.nio.charset.StandardCharsets.US_ASCII;

public enum HygDatabaseLoader implements StarCatalogue.Loader{
    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try(BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))){
            String line;
            while((line = r.readLine()) != null){
               String[] strArray = line.split(",");
               System.out.println(strArray);
            }
        }
    }

    private enum columnIndexes{
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
                RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
                RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
                COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }
}
