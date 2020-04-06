package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * One of the types of celestial object: a planet.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class Planet extends CelestialObject {
    /**
     * Constructs a planet.
     *
     * @param name The name of the planet
     * @param equatorialPos The equatorial position of the planet
     * @param angularSize The angular size of the planet
     * @param magnitude The magnitude of the planet
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }
}
