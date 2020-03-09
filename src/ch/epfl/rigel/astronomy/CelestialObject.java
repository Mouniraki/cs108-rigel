package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Locale;

public abstract class CelestialObject {
    final private String name;
    final private EquatorialCoordinates equatorialPos;
    final private float angularSize;
    final private float magnitude;

    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        if(angularSize < 0){ throw new IllegalArgumentException("A negative angular size is not allowed."); }
        if(name == null || equatorialPos == null){ throw new NullPointerException("The name or the equatorial position is null."); }

        this.name = name;
        this.equatorialPos = equatorialPos;
        this.angularSize = angularSize;
        this.magnitude = magnitude;
    }

    /**
     * Returns the name of the celestial object.
     * @return The name of the celestial object
     */
    public String name(){
        return this.name;
    }

    /**
     * Returns the angular size of the celestial object.
     * @return The size of the celestial object
     */
    public double angularSize(){
        return this.angularSize;
    }

    /**
     * Returns the magnitude of the celesial object.
     * @return The magnitude of the celestial object
     */
    public double magnitude(){
        return this.magnitude;
    }

    /**
     * Returns the equatorial position of the celestial object.
     * @return The equatorial position of the celestial object
     */
    public EquatorialCoordinates equatorialPos(){
        return this.equatorialPos;
    }

    //TODO PROBABLY IT IS WRONG, TO BE TESTED

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of a celestial object.
     * @return the textual representation of a celestial object
     */
    public String info(){
        return String.format(Locale.ROOT, "A celestial object of name: %., of the equatorial position: %., of angular size: %., and of magnitude: %..", name(), equatorialPos(), magnitude(), angularSize());
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of a celestial object.
     * @return the textual representation of a celestial object
     */
    public String toString(){
        return info();
    }
}
