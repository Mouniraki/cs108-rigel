package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.List;

public final class Asterism {
    final private List<Star> stars;

    Asterism(List<Star> stars){
        Preconditions.checkArgument(stars.size() != 0);
        this.stars = stars;
    }

    public List<Star> stars() {
        return stars;
    }
}
