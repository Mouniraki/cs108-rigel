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
            while((line = r.readLine()) != null) {
               String[] strArray = line.split(",");
               if(i > 0) {
                   int hipparcosId = 0;
                   String proper;
                   double ra = Double.parseDouble(strArray[ColName.RARAD.ordinal()]);
                   double dec = Double.parseDouble(strArray[ColName.DECRAD.ordinal()]);
                   double magnitude = 0;
                   double colorIndex = 0;
                   String bayer = "?";
                   String con = strArray[ColName.CON.ordinal()];

                   if (!strArray[ColName.HIP.ordinal()].isEmpty())
                       hipparcosId = Integer.parseInt(strArray[ColName.HIP.ordinal()]);

                   if (!strArray[ColName.BAYER.ordinal()].isEmpty())
                       bayer = strArray[ColName.BAYER.ordinal()];

                   if (!strArray[ColName.PROPER.ordinal()].isEmpty())
                       proper = strArray[ColName.PROPER.ordinal()];

                   else
                       proper = bayer + " " + con;

                   if (!strArray[ColName.MAG.ordinal()].isEmpty())
                       magnitude = Double.parseDouble(strArray[ColName.MAG.ordinal()]);

                   if (!strArray[ColName.CI.ordinal()].isEmpty())
                       colorIndex = Double.parseDouble(strArray[ColName.CI.ordinal()]);

                   EquatorialCoordinates equatorialPos = EquatorialCoordinates.of(ra, dec);

                   builder.addStar(new Star(hipparcosId, proper, equatorialPos, (float) magnitude, (float) colorIndex));
               }
               ++i;
            }
        }
    }

    private enum ColName {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
                RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
                RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
                COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }
}
