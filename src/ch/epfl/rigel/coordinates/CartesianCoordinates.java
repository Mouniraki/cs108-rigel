package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Class allowing the construction of a point the cartesian coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class CartesianCoordinates {
    private final double x;
    private final double y;

    private CartesianCoordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Allows to create cartesian coordinates.
     * @param x x coordinate (abscissa)
     * @param y y coordinate (ordinate)
     * @return A point expressed using the cartesian coordinates.
     */
    public static CartesianCoordinates of(double x, double y){
        return new CartesianCoordinates(x, y);
    }

    /**
     * Getter of the x value (abscissa).
     * @return the x value of the coordinates (abscissa)
     */
    public double x(){
        return this.x;
    }

    /**
     * Getter of the y value (ordinate)
     * @return the y value of the coordinates (ordinate)
     */
    public double y(){
        return this.y;
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the equals() method.
     *
     * @throws UnsupportedOperationException
     */
    final public boolean equals(Object obj){
        throw new UnsupportedOperationException();
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the hashCode() method.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    final public int hashCode(){
        throw new UnsupportedOperationException();
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of a point
     * expressed in the cartesian coordinates.
     *
     * @return the textual representation of the point in the horizontal coordinates
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Cartesian coordinates: (x=%.4f, y=%.4f)", x(), y());
    }

}
