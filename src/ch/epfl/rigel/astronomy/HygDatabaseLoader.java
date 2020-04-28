package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.*;

import static java.nio.charset.StandardCharsets.US_ASCII;
/**
 * Loader for HYG Database.
 *
 * @author Mounir Raki (310287)
 */
public enum HygDatabaseLoader implements StarCatalogue.Loader{
    INSTANCE;

    /**
     * Loads a file containing the information of the stars from a HYG catalog (CSV file),
     * and builds the list of stars in a StarCatalogue.
     *
     * @param inputStream
     *          the content of the file to read, obtained by an InputStream
     * @param builder
     *          the type of builder, used to build the list of stars
     * @throws IOException
     *          if the file doesn't exist or the path is incorrect
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try(BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, US_ASCII))){
            r.readLine();
            String line;
            while((line = r.readLine()) != null) {
                String[] strArray = line.split(",");

                String hip = strArray[ColName.HIP.ordinal()];
                String mag = strArray[ColName.MAG.ordinal()];
                String ci = strArray[ColName.CI.ordinal()];
                String bay = strArray[ColName.BAYER.ordinal()];
                String pro = strArray[ColName.PROPER.ordinal()];
                String con = strArray[ColName.CON.ordinal()];

                double ra = Double.parseDouble(strArray[ColName.RARAD.ordinal()]);
                double dec = Double.parseDouble(strArray[ColName.DECRAD.ordinal()]);

                EquatorialCoordinates equatorialPos = EquatorialCoordinates.of(ra, dec);

                int hipparcosId = !(hip.isEmpty()) ? Integer.parseInt(hip) : 0;

                double magnitude = !(mag.isEmpty()) ? Double.parseDouble(mag) : 0;

                double colorIndex = !(ci.isEmpty()) ? Double.parseDouble(ci) : 0;

                String bayer = !(bay.isEmpty()) ? bay : "?";

                String proper = !(pro.isEmpty()) ? pro : bayer + " " + con;

                builder.addStar(new Star(hipparcosId, proper, equatorialPos, (float) magnitude, (float) colorIndex));
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
