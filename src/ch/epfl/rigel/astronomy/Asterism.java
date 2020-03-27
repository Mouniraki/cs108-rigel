package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;

/**
 * An asterism.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class Asterism {
    final private List<Star> stars;

    /**
     * Constructor of an Asterism.
     *
     * @param stars
     *          the list of stars of the asterism to construct
     */
    public Asterism(List<Star> stars){
        Preconditions.checkArgument(stars.size() != 0);
        this.stars = List.copyOf(stars);
    }

    /**
     * Getter for the list of stars.
     *
     * @return the list of stars.
     */
    public List<Star> stars() {
        return stars;
    }
}
