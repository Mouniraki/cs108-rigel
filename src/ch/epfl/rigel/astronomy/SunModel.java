package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Model of the Sun.
 *
 * @author Nicolas Szwajcok (315213)
 * @author Mounir Raki (310287)
 */
public enum SunModel implements CelestialObjectModel<Sun> {
    SUN;

    private static final double SUN_LON_AT_J2010 = Angle.ofDeg(279.557208);
    private static final double SUN_LON_AT_PERIGEE = Angle.ofDeg(283.112438);
    private static final double ORBITAL_ECCENTRICITY = 0.016705;
    private static final double DAYS_IN_TROPICAL_YEAR = 365.242191;
    private static final double THETA_ZERO = Angle.ofDeg(0.533128);

    /**
     * Creates the model of the Sun with specific parameters.
     *
     * @param daysSinceJ2010
     *          Number of days after the epoch J2010
     * @param eclipticToEquatorialConversion
     *          Conversion from ecliptic to equatorial coordinates
     * @return a new Sun with the corresponding model.
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double meanAngularSpeed = Angle.TAU / DAYS_IN_TROPICAL_YEAR;
        double meanAnomaly = meanAngularSpeed*daysSinceJ2010 + SUN_LON_AT_J2010 - SUN_LON_AT_PERIGEE;
        double trueAnomaly = meanAnomaly + 2*ORBITAL_ECCENTRICITY*Math.sin(meanAnomaly);
        double eclipticLon = Angle.normalizePositive(trueAnomaly + SUN_LON_AT_PERIGEE);

        EclipticCoordinates eclCoords = EclipticCoordinates.of(eclipticLon, 0);

        double angularSize = THETA_ZERO * ((1 + ORBITAL_ECCENTRICITY*Math.cos(trueAnomaly)) / (1 - ORBITAL_ECCENTRICITY*ORBITAL_ECCENTRICITY));

        return new Sun(eclCoords, eclipticToEquatorialConversion.apply(eclCoords), (float) angularSize, (float) meanAnomaly);

    }
}
