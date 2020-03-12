package ch.epfl.rigel.astronomy;

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
    public static List<PlanetModel> ALL = new ArrayList<>(List.of(
            MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE
    ));

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


    PlanetModel(String frenchName, double revPeriod, double lonAtJ2010, double lonAtPerigee, double orbitEccentricity,
                double orbitSMAxis, double orbitEclipticInclination, double lonOrbitalNode, double angularSizeAt1UA,
                double magnitudeAt1UA){

        this.frenchName = frenchName; //OK
        this.revPeriod = revPeriod; //TROPICAL YEARS
        this.lonAtJ2010 = Angle.ofDeg(lonAtJ2010); // DEG
        this.lonAtPerigee = Angle.ofDeg(lonAtPerigee); // DEG
        this.orbitEccentricity = orbitEccentricity; //NONE
        this.orbitSMAxis = orbitSMAxis; //UA
        this.orbitEclipticInclination = Angle.ofDeg(orbitEclipticInclination); // DEG
        this.lonOrbitalNode = Angle.ofDeg(lonOrbitalNode); // DEG/RAD
        this.angularSizeAt1UA = Angle.ofArcsec(angularSizeAt1UA); //ARCSEC
        this.magnitudeAt1UA = magnitudeAt1UA; //NONE

    }

    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        return null;
    }
}
