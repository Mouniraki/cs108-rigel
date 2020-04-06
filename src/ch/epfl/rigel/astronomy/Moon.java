package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

/**
 * One of the types of celestial object: a moon.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class Moon extends CelestialObject {
    private final float phase;

    /**
     * Constructor of an instance of a moon.
     *
     * @param equatorialPos The equatorial position of the moon
     * @param angularSize The angular size of the moon
     * @param magnitude The magnitude of the moon
     * @param phase The phase of the moon
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase){
        super("Lune", equatorialPos, angularSize, magnitude);
        Preconditions.checkInInterval(ClosedInterval.of(0, 1), phase);
        this.phase = phase;
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of the Moon.
     *
     * @return the textual representation of the Moon
     */
    @Override
    public String info(){
        return String.format(Locale.ROOT,"Lune (%.1f%%)", phase * 100.0);
    }
}
