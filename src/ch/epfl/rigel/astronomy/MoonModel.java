package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import static java.lang.Math.*;

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

    private final static double SIN_ORBIT_INCL = sin(ORBIT_INCL);
    private final static double COS_ORBIT_INCL = cos(ORBIT_INCL);

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
        double sinSunMeanAnomaly = sin(sun.meanAnomaly());

        double meanOrbitalLon = Angle.ofDeg(13.1763966)*daysSinceJ2010 + MEAN_LON;
        double moonMeanAnomaly = meanOrbitalLon - Angle.ofDeg(0.1114041)*daysSinceJ2010 - MEAN_LON_AT_PERIGEE;
        double evection = Angle.ofDeg(1.2739) * sin(2*(meanOrbitalLon - sunLonEclGeo) - moonMeanAnomaly);
        double annualEquCorr = Angle.ofDeg(0.1858) * sinSunMeanAnomaly;
        double correction3 = Angle.ofDeg(0.37) * sinSunMeanAnomaly;
        double corrAnomaly = moonMeanAnomaly + evection - annualEquCorr - correction3;
        double centerEquCorr = Angle.ofDeg(6.2886) * sin(corrAnomaly);
        double correction4 = Angle.ofDeg(0.214) * sin(2 * corrAnomaly);
        double orbitalLonCorr = meanOrbitalLon + evection + centerEquCorr - annualEquCorr + correction4;
        double variation = Angle.ofDeg(0.6583) * sin(2 * (orbitalLonCorr - sunLonEclGeo));
        double moonOrbitalLon = orbitalLonCorr + variation;

        EquatorialCoordinates moonEquatorialPos = eclipticToEquatorialConversion.apply(
                moonEclipticPos(daysSinceJ2010, moonOrbitalLon, sinSunMeanAnomaly)
        );
        float moonAngularSize = moonAngularSize(corrAnomaly, centerEquCorr);
        float moonPhase = moonPhase(moonOrbitalLon, sunLonEclGeo);

        return new Moon(moonEquatorialPos, moonAngularSize, 0, moonPhase);
    }


    private EclipticCoordinates moonEclipticPos(double daysSinceJ2010, double moonOrbitalLon, double sinSunMeanAnomaly){
        double meanLonAscNode = LON_ASC_NODE - Angle.ofDeg(0.0529539)*daysSinceJ2010;
        double corrLonAscNode = meanLonAscNode - Angle.ofDeg(0.16)*sinSunMeanAnomaly;
        double sinLonDiff = sin(moonOrbitalLon - corrLonAscNode);
        double cosLonDiff = cos(moonOrbitalLon - corrLonAscNode);
        double eclLon = atan2(
                sinLonDiff * COS_ORBIT_INCL,
                cosLonDiff) + corrLonAscNode;
        double eclLat = asin(sinLonDiff * SIN_ORBIT_INCL);

        return EclipticCoordinates.of(Angle.normalizePositive(eclLon), eclLat);
    }


    private float moonPhase(double moonOrbitalLon, double sunLonEclGeo){
        return (float) ((1 - cos(moonOrbitalLon - sunLonEclGeo)) / 2);
    }


    private float moonAngularSize(double corrAnomaly, double centerEquCorr){
        double earthMoonDist = (1 - ORBIT_ECC*ORBIT_ECC) / (1 + ORBIT_ECC* cos(corrAnomaly + centerEquCorr));
        return (float) (THETA_ZERO / earthMoonDist);
    }
}
