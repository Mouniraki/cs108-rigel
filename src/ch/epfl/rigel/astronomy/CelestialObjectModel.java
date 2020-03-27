package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

/**
 * A model of a celestial object.
 *
 * @author Nicolas Szwajcok (315213)
 * @param <O>
 *     The type of the celestial object to model
 */
public interface CelestialObjectModel<O> {
    /**
     * Abstract method. Models the number (can be negative) of days after given epoch J2010.
     * @param daysSinceJ2010 Number of days after the epoch J2010
     * @param eclipticToEquatorialConversion Conversion from ecliptic to equatorial coordinates
     * @return Modelised object by the model for the number of days after epoch J2010
     */
    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
