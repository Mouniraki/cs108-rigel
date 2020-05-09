package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

/**
 * One of the types of celestial object: a star.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class Star extends CelestialObject {
    private final int hipparcosId;
    private final double factorColorIndex;
    private final double colorTemperature;
    private final static ClosedInterval COLORINDEX_INTERVAL = ClosedInterval.of(-0.5, 5.5);

    /**
     * Constructs a new star.
     *
     * @param hipparcosId
     *          the specific Hipparcos ID of the star
     * @param name
     *          the name of the star
     * @param equatorialPos
     *          the position of the star (in equatorial coordinates)
     * @param magnitude
     *          the magnitude of the star
     * @param colorIndex
     *          the color index of the star, useful for computing the color temperature
     *
     * @throws IllegalArgumentException
     *          if either the Hipparcos ID is negative, or if the color index is not in the interval [-0.5, 5.5]
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex){
        super(name, equatorialPos, 0, magnitude);
        Preconditions.checkArgument(hipparcosId >= 0);
        Preconditions.checkInInterval(COLORINDEX_INTERVAL, colorIndex);
        this.hipparcosId = hipparcosId;
        this.factorColorIndex = 0.92*colorIndex;
        this.colorTemperature = 4600 * ((1 / (factorColorIndex + 1.7)) + (1 / (factorColorIndex + 0.62)));
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
        return (int) colorTemperature;
    }
}
