package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.*;

import static java.nio.charset.StandardCharsets.US_ASCII;

public enum HygDatabaseLoader implements StarCatalogue.Loader{
    INSTANCE;


    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try(BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))){
            String line;
            int i=0;
            while((line = r.readLine()) != null){
               String[] strArray = line.split(",");
               if(i > 0) {
                   int hipparcosId = 0;
                   String proper;
                   double ra = Double.parseDouble(strArray[colIndexes.RARAD.ordinal()]);
                   double dec = Double.parseDouble(strArray[colIndexes.DECRAD.ordinal()]);
                   double magnitude = 0;
                   double colorIndex = 0;
                   String bayer = "?";
                   String con = strArray[colIndexes.CON.ordinal()];

                   if (!strArray[colIndexes.HIP.ordinal()].isEmpty())
                       hipparcosId = Integer.parseInt(strArray[colIndexes.HIP.ordinal()]);

                   if (!strArray[colIndexes.BAYER.ordinal()].isEmpty())
                       bayer = strArray[colIndexes.BAYER.ordinal()];

                   if (!strArray[colIndexes.PROPER.ordinal()].isEmpty())
                       proper = strArray[colIndexes.PROPER.ordinal()];

                   else
                       proper = bayer + " " + con;

                   if (!strArray[colIndexes.MAG.ordinal()].isEmpty())
                       magnitude = Double.parseDouble(strArray[colIndexes.MAG.ordinal()]);

                   if (!strArray[colIndexes.CI.ordinal()].isEmpty())
                       colorIndex = Double.parseDouble(strArray[colIndexes.CI.ordinal()]);

                   EquatorialCoordinates equatorialPos = EquatorialCoordinates.of(ra, dec);

                   builder.addStar(new Star(hipparcosId, proper, equatorialPos, (float) magnitude, (float) colorIndex));
               }
               ++i;
            }
        }
    }

    private enum colIndexes{
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
                RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
                RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
                COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }
}
