package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * A specific type of Celestial Object : a Star.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class Star extends CelestialObject { //immuable
    final private int hipparcosId;
    final private float colorIndex;

    /**
     * Constructs a new Star.
     *
     * @param hipparcosId
     *          the specific Hipparcos ID of the Star
     * @param name
     *          the name of the Star
     * @param equatorialPos
     *          the position of the Star (in EquatorialCoordinates)
     * @param magnitude
     *          the magnitude of the Star
     * @param colorIndex
     *          the color index of the Star, useful for computing the color temperature
     *
     * @throws IllegalArgumentException
     *          if either the Hipparcos ID is negative, or if the color index is not in the interval [-0.5, 5.5]
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex){
        super(name, equatorialPos, 0, magnitude);
        Preconditions.checkArgument(hipparcosId >= 0);
        Preconditions.checkInInterval(ClosedInterval.of(-0.5, 5.5), colorIndex);
        this.hipparcosId = hipparcosId;
        this.colorIndex = colorIndex;
    }

    /**
     * Getter for the Hipparcos ID.
     *
     * @return the specific Hipparcos ID of the Star
     */
    public int hipparcosId(){
        return hipparcosId;
    }


    /**
     * Calculates the color temperature of the Star.
     *
     * @return the color temperature of the Star
     */
    public int colorTemperature(){
        double temperature = 4600 * ((1 / (0.92 * colorIndex + 1.7)) + (1 / (0.92 * colorIndex + 0.62)));
        return (int) Math.floor(temperature);
    }
}
