package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Mother class for all types of celestial objects.
 *
 * @author Nicolas Szwajcok (315213)
 */
public abstract class CelestialObject {
    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;

    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        Preconditions.checkArgument(angularSize >= 0);
        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     * Returns the name of the celestial object.
     *
     * @return The name of the celestial object
     */
    public String name(){
        return name;
    }

    /**
     * Returns the angular size of the celestial object.
     *
     * @return The size of the celestial object
     */
    public double angularSize(){
        return angularSize;
    }

    /**
     * Returns the magnitude of the celestial object.
     *
     * @return The magnitude of the celestial object
     */
    public double magnitude(){
        return magnitude;
    }

    /**
     * Returns the equatorial position of the celestial object.
     *
     * @return The equatorial position of the celestial object
     */
    public EquatorialCoordinates equatorialPos(){
        return equatorialPos;
    }

    /**
     * Defines the textual representation of a celestial object.
     *
     * @return The textual representation of a celestial object
     */
    public String info(){
        return name();
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of a celestial object.
     *
     * @return the textual representation of a celestial object
     */
    @Override
    public String toString(){
        return info();
    }
}
