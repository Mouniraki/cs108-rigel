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
    private final static ClosedInterval PHASE_INTERVAL = ClosedInterval.of(0, 1);

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
        Preconditions.checkInInterval(PHASE_INTERVAL, phase);
        this.phase = phase;
    }

    /**
     * Getter for the moon phase.
     *
     * @return the moon phase
     */
    public float getPhase(){
        return this.phase;
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of the Moon.
     *
     * @return the textual representation of the Moon
     */
    @Override
    public String info(){
        return String.format(Locale.ROOT,super.name()+" (%.1f%%)", phase * 100.0);
    }
}
