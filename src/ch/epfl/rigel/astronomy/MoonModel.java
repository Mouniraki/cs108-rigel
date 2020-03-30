package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Model of the Moon.
 *
 * @author Mounir Raki (310287)
 */
public enum MoonModel implements CelestialObjectModel<Moon>{
    MOON;

    private final static double MEAN_LON = Angle.ofDeg(91.929336);
    private final static double MEAN_LON_AT_PERIGEE = Angle.ofDeg(130.143076);
    private final static double LON_ASC_NODE = Angle.ofDeg(291.682547);
    private final static double ORBIT_INCL = Angle.ofDeg(5.145396);
    private final static double ORBIT_ECC = 0.0549;
    private final static double THETA_ZERO = Angle.ofDeg(0.5181);

    /**
     * Creates the model of the Moon with specific parameters.
     *
     * @param daysSinceJ2010
     *          Number of days after the epoch J2010
     * @param eclipticToEquatorialConversion
     *          Conversion from ecliptic to equatorial coordinates
     * @return a new Moon with the corresponding model
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        Sun sun = SunModel.SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double sunLonEclGeo = sun.eclipticPos().lon();
        double sunMeanAnomaly = sun.meanAnomaly();

        double meanOrbitalLon = Angle.ofDeg(13.1763966) * daysSinceJ2010 + MEAN_LON;
        double moonMeanAnomaly = meanOrbitalLon - Angle.ofDeg(0.1114041) * daysSinceJ2010 - MEAN_LON_AT_PERIGEE;

        double evection = Angle.ofDeg(1.2739) * Math.sin(2 * (meanOrbitalLon - sunLonEclGeo) - moonMeanAnomaly);
        double annualEquCorr = Angle.ofDeg(0.1858) * Math.sin(sunMeanAnomaly);
        double correction3 = Angle.ofDeg(0.37) * Math.sin(sunMeanAnomaly);

        double corrAnomaly = moonMeanAnomaly + evection - annualEquCorr - correction3;
        double centerEquCorr = Angle.ofDeg(6.2886) * Math.sin(corrAnomaly);
        double correction4 = Angle.ofDeg(0.214) * Math.sin(2 * corrAnomaly);

        double orbitalLonCorr = meanOrbitalLon + evection + centerEquCorr - annualEquCorr + correction4;
        double variation = Angle.ofDeg(0.6583) * Math.sin(2 * (orbitalLonCorr - sunLonEclGeo));

        double realOrbitalLon = orbitalLonCorr + variation;

        double meanLonAscNode = LON_ASC_NODE - Angle.ofDeg(0.0529539) * daysSinceJ2010;
        double corrLonAscNode = meanLonAscNode - Angle.ofDeg(0.16) * Math.sin(sunMeanAnomaly);

        double eclLon = Math.atan2(
                Math.sin(realOrbitalLon - corrLonAscNode) * Math.cos(ORBIT_INCL),
                Math.cos(realOrbitalLon - corrLonAscNode)) + corrLonAscNode;

        double eclLat = Math.asin(Math.sin(realOrbitalLon - corrLonAscNode) * Math.sin(ORBIT_INCL));
        EclipticCoordinates moonEclCoords = EclipticCoordinates.of(Angle.normalizePositive(eclLon), eclLat);

        double phase = (1 - Math.cos(realOrbitalLon - sunLonEclGeo)) / 2;

        double earthMoonDist = (1 - ORBIT_ECC * ORBIT_ECC) / (1 + ORBIT_ECC * Math.cos(corrAnomaly + centerEquCorr));
        double angularSize = THETA_ZERO / earthMoonDist;

        return new Moon(eclipticToEquatorialConversion.apply(moonEclCoords), (float) angularSize, 0, (float) phase);
    }
}
