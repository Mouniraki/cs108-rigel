package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

/**
 * Class allowing a construction of a planet.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class Planet extends CelestialObject {
    /**
     * Constructor of an instance of planet.
     * @param name The name of the planet
     * @param equatorialPos The equatorial position of the planet
     * @param angularSize The angular size of the planet
     * @param magnitude The magnitude of the planet
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);
    }
}
