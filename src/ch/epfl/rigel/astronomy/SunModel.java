package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.math.Angle;

/**
 * Model of the Sun.
 *
 * @author Nicolas Szwajcok (315213)
 */
public enum SunModel implements CelestialObjectModel<Sun> {
    SUN;

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
        double sunLonAtJ2010 = Angle.ofDeg(279.557208);
        double sunLonAtPerigee = Angle.ofDeg(283.112438);
        double orbitalEccentricity = 0.016705;
        double daysInTropicalYear = 365.242191;
        double thetaZero = Angle.ofDeg(0.533128);

        double meanAnomaly = (Angle.TAU / daysInTropicalYear) * daysSinceJ2010 + sunLonAtJ2010 - sunLonAtPerigee;
        double realAnomaly = meanAnomaly + 2 * orbitalEccentricity * Math.sin(meanAnomaly);
        double eclipticLon = realAnomaly + sunLonAtPerigee;

        double angularSize = thetaZero * ((1 + orbitalEccentricity * Math.cos(realAnomaly) / 1 - orbitalEccentricity * orbitalEccentricity);
        EclipticCoordinates eclSun = EclipticCoordinates.of(eclipticLon, 0);

        return new Sun(eclSun, eclipticToEquatorialConversion.apply(eclSun), (float) angularSize, (float) meanAnomaly);
    }
}
