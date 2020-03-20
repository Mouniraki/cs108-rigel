package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import java.util.ArrayList;
import java.util.List;

/**
 * Models of the eight planets of the solar system.
 *
 * @author Mounir Raki (310287)
 */

public enum PlanetModel implements CelestialObjectModel<Planet>{
    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 271.063148, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    /**
     * Immutable list of all of the planet models, in order of declaration.
     */
    public static List<PlanetModel> ALL = new ArrayList<>(List.of(values()));

    private final String frenchName;
    private final double revPeriod;
    private final double lonAtJ2010;
    private final double lonAtPerigee;
    private final double orbitEccentricity;
    private final double orbitSMAxis;
    private final double orbitEclipticInclination;
    private final double lonOrbitalNode;
    private final double angularSizeAt1UA;
    private final double magnitudeAt1UA;

    private final static double DAYS_IN_TROPICAL_YEAR = 365.242191;

    PlanetModel(String frenchName, double revPeriod, double lonAtJ2010, double lonAtPerigee, double orbitEccentricity,
                double orbitSMAxis, double orbitEclipticInclination, double lonOrbitalNode, double angularSizeAt1UA,
                double magnitudeAt1UA){
        this.frenchName = frenchName;
        this.revPeriod = revPeriod;
        this.lonAtJ2010 = Angle.ofDeg(lonAtJ2010);
        this.lonAtPerigee = Angle.ofDeg(lonAtPerigee);
        this.orbitEccentricity = orbitEccentricity;
        this.orbitSMAxis = orbitSMAxis;
        this.orbitEclipticInclination = Angle.ofDeg(orbitEclipticInclination);
        this.lonOrbitalNode = Angle.ofDeg(lonOrbitalNode);
        this.angularSizeAt1UA = Angle.ofArcsec(angularSizeAt1UA);
        this.magnitudeAt1UA = magnitudeAt1UA;
    }

    /**
     * Generates the models of the planets.
     * @param daysSinceJ2010
     *          Number of days after the epoch J2010
     * @param eclipticToEquatorialConversion
     *          Conversion from ecliptic to equatorial coordinates
     *
     * @return a new Planet with the corresponding model
     */
    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double meanAngularSpeed = (Angle.TAU / DAYS_IN_TROPICAL_YEAR);

        double meanAnomaly = meanAnomaly(meanAngularSpeed, daysSinceJ2010, this);
        double realAnomaly = realAnomaly(meanAnomaly, this);

        double radius = radius(realAnomaly, this);
        double lonPlanetHelio = lonHelio(realAnomaly, this);

        double latEclHelio = Math.asin(Math.sin(lonPlanetHelio - lonOrbitalNode) * Math.sin(orbitEclipticInclination));

        double eclRadius = radius * Math.cos(latEclHelio);
        double lonEclHelio = Math.atan2(Math.sin(lonPlanetHelio - lonOrbitalNode) * Math.cos(orbitEclipticInclination),
                Math.cos(lonPlanetHelio - lonOrbitalNode)) + lonOrbitalNode;

        double earthMeanAnomaly = meanAnomaly(meanAngularSpeed, daysSinceJ2010, EARTH);
        double earthRealAnomaly = realAnomaly(earthMeanAnomaly, EARTH);
        double lonEarthHelio = lonHelio(earthRealAnomaly, EARTH);
        double earthRadius = radius(earthRealAnomaly, EARTH);

        double lonEclGeo = Angle.normalizePositive(lonEclGeo(lonEarthHelio, earthRadius, lonEclHelio, eclRadius, this)); //MUST BE NORMALIZED ?
        double latEclGeo = Math.atan((eclRadius * Math.tan(latEclHelio) * Math.sin(lonEclGeo - lonEclHelio)) / (earthRadius * Math.sin(lonEclHelio - lonEarthHelio)));

        return new Planet(frenchName, eclipticToEquatorialConversion.apply(EclipticCoordinates.of(lonEclGeo, latEclGeo)), (float) angularSizeAt1UA, (float) magnitudeAt1UA);
    }


    private double meanAnomaly(double meanAngularSpeed, double daysSinceJ2010, PlanetModel p){
        return meanAngularSpeed * (daysSinceJ2010 / p.revPeriod) + p.lonAtJ2010 - p.lonAtPerigee;
    }

    private double realAnomaly(double meanAnomaly, PlanetModel p){
        return meanAnomaly + 2 * p.orbitEccentricity * Math.sin(meanAnomaly);
    }

    private double radius(double realAnomaly, PlanetModel p){
        return (p.orbitSMAxis * (1 - p.orbitEccentricity*p.orbitEccentricity)) / (1 + p.orbitEccentricity * Math.cos(realAnomaly));
    }

    private double lonHelio(double realAnomaly, PlanetModel p){
        return realAnomaly + p.lonAtPerigee;
    }

    private double lonEclGeo(double lonEarthHelio, double earthRadius, double lonEclHelio, double eclRadius, PlanetModel p){
        if(p == MERCURY || p == VENUS){
            return Angle.TAU/2 + lonEarthHelio + Math.atan2(eclRadius * Math.sin(lonEarthHelio - lonEclHelio), earthRadius - eclRadius * Math.cos(lonEarthHelio - lonEclHelio));
        }
        else {
            return lonEclHelio + Math.atan2(earthRadius * Math.sin(lonEclHelio - lonEarthHelio), eclRadius - earthRadius * Math.cos(lonEclHelio - lonEarthHelio));
        }
    }
}
