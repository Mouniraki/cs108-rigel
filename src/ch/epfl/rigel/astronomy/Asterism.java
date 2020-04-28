package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;

/**
 * The class describing an asterism.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class Asterism {
    private final List<Star> stars;

    /**
     * Constructor of an asterism.
     *
     * @param stars the list of stars inside of the asterism
     */
    public Asterism(List<Star> stars){
        Preconditions.checkArgument(!stars.isEmpty());
        this.stars = List.copyOf(stars);
    }

    /**
     * Getter for the list of the stars of the asterism.
     *
     * @return the list of the stars of the asterism
     */
    public List<Star> stars() {
        return stars;
    }
}
