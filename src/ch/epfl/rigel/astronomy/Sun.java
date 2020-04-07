package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * One of the types of celestial object: a Sun.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class Sun extends CelestialObject {
    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    /**
     * Constructor of an instance of a Sun.
     *
     * @param eclipticPos The ecliptic position of the Sun
     * @param equatorialPos The equatorial position of the Sun
     * @param angularSize The angular size of the Sun
     * @param meanAnomaly The mean anomaly of the Sun
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly){
        super("Soleil", equatorialPos, angularSize,-26.7f);
        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * Returns the ecliptic position of the Sun.
     *
     * @return The ecliptic position of the Sun
     */
    public EclipticCoordinates eclipticPos(){
        return eclipticPos;
    }

    /**
     * Returns the mean anomaly of the Sun.
     *
     * @return The mean anomaly of the Sun
     */
    public double meanAnomaly(){
        return meanAnomaly;
    }
}
