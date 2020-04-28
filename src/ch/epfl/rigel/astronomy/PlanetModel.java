package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

/**
 * Models of the eight planets of the solar system.
 *
 * @author Mounir Raki (310287)
 */

public enum PlanetModel implements CelestialObjectModel<Planet> {
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
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    /**
     * Immutable list of all of the planet models, in order of declaration.
     */
    public final static List<PlanetModel> ALL = List.of(values());

    private final String frenchName;
    private final double lonAtJ2010;
    private final double lonAtPerigee;
    private final double orbitEccentricity;
    private final double orbitSMAxis;
    private final double orbitEclipticInclination;
    private final double lonOrbitalNode;
    private final double angularSizeAt1UA;
    private final double magnitudeAt1UA;

    private final static double DAYS_IN_TROPICAL_YEAR = 365.242191;
    private final double meanRevRatio;
    private double radiusTimesLonDiff;

    PlanetModel(String frenchName, double revPeriod, double lonAtJ2010, double lonAtPerigee, double orbitEccentricity,
                double orbitSMAxis, double orbitEclipticInclination, double lonOrbitalNode, double angularSizeAt1UA,
                double magnitudeAt1UA) {
        this.frenchName = frenchName;
        this.lonAtJ2010 = Angle.ofDeg(lonAtJ2010);
        this.lonAtPerigee = Angle.ofDeg(lonAtPerigee);
        this.orbitEccentricity = orbitEccentricity;
        this.orbitSMAxis = orbitSMAxis;
        this.orbitEclipticInclination = Angle.ofDeg(orbitEclipticInclination);
        this.lonOrbitalNode = Angle.ofDeg(lonOrbitalNode);
        this.angularSizeAt1UA = Angle.ofArcsec(angularSizeAt1UA);
        this.magnitudeAt1UA = magnitudeAt1UA;

        this.meanRevRatio = (Angle.TAU / DAYS_IN_TROPICAL_YEAR) / revPeriod;
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
        double cosOrbitEclipticInclination = cos(this.orbitEclipticInclination);
        double sinOrbitEclipticInclination = sin(this.orbitEclipticInclination);


        double meanAnomaly = this.meanAnomaly(daysSinceJ2010);
        double trueAnomaly = this.trueAnomaly(meanAnomaly);
        double radius = this.radius(trueAnomaly);
        double lonPlanetHelio = this.lonHelio(trueAnomaly);

        double cosLonDifference = cos(lonPlanetHelio - lonOrbitalNode);
        double sinLonDifference = sin(lonPlanetHelio - lonOrbitalNode);

        double latEclHelio = asin(sinLonDifference * sinOrbitEclipticInclination);
        double eclRadius = radius * cos(latEclHelio);

        double lonEclHelio = atan2(
                sinLonDifference * cosOrbitEclipticInclination,
                cosLonDifference) + lonOrbitalNode;

        double earthMeanAnomaly = EARTH.meanAnomaly(daysSinceJ2010);
        double earthTrueAnomaly = EARTH.trueAnomaly(earthMeanAnomaly);
        double lonEarthHelio = EARTH.lonHelio(earthTrueAnomaly);
        double earthRadius = EARTH.radius(earthTrueAnomaly);
        radiusTimesLonDiff = earthRadius * sin(lonEclHelio - lonEarthHelio);


        double lonEclGeo = this.lonEclGeo(lonEarthHelio, earthRadius, lonEclHelio, eclRadius);
        double latEclGeo = atan((eclRadius * tan(latEclHelio) * sin(lonEclGeo - lonEclHelio)) / radiusTimesLonDiff);
        EclipticCoordinates eclCoords = EclipticCoordinates.of(Angle.normalizePositive(lonEclGeo), latEclGeo);

        double distanceToEarth = sqrt(earthRadius*earthRadius + radius*radius - 2*radius*earthRadius*cos(lonPlanetHelio - lonEarthHelio)*cos(latEclHelio));
        double angularSize = angularSizeAt1UA / distanceToEarth;

        double phase = (1 + cos(lonEclGeo - lonPlanetHelio)) / 2;
        double magnitude = magnitudeAt1UA + 5* log10((radius * distanceToEarth) / sqrt(phase));

        return new Planet(frenchName, eclipticToEquatorialConversion.apply(eclCoords), (float) angularSize, (float) magnitude);
    }


    private double meanAnomaly(double daysSinceJ2010) {
        return meanRevRatio*daysSinceJ2010 + lonAtJ2010 - lonAtPerigee;
    }

    private double trueAnomaly(double meanAnomaly) {
        return meanAnomaly + 2*orbitEccentricity* sin(meanAnomaly);
    }

    private double radius(double trueAnomaly) {
        return (orbitSMAxis*(1 - orbitEccentricity*orbitEccentricity)) / (1 + orbitEccentricity* cos(trueAnomaly));
    }

    private double lonHelio(double trueAnomaly){
        return trueAnomaly + lonAtPerigee;
    }

    private double lonEclGeo(double lonEarthHelio, double earthRadius, double lonEclHelio, double eclRadius) {
        if(orbitSMAxis < EARTH.orbitSMAxis) {
            return Angle.TAU / 2 + lonEarthHelio + atan2(
                    eclRadius* sin(lonEarthHelio - lonEclHelio),
                    earthRadius - eclRadius*cos(lonEarthHelio - lonEclHelio));
        }
        else {
            return lonEclHelio + atan2(
                    radiusTimesLonDiff,
                    eclRadius - earthRadius*cos(lonEclHelio - lonEarthHelio));
        }
    }
}
