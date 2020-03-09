package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

public interface CelestialObjectModel<O> {
    /**
     * Abstract method. Modelises the number (can be negative) of days after given epoch J2010.
     * @param daysSinceJ2010 Number of days after the epoch J2010
     * @param eclipticToEquatorialConversion Conversion from ecliptic to equatorial coordinates
     * @return Modelised object by the model for the number of days after epoch J2010
     */
    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
