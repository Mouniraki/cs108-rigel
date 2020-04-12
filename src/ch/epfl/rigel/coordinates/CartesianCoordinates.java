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
     * Creates an instance of the cartesian coordinates.
     *
     * @param x x coordinate (abscissa)
     * @param y y coordinate (ordinate)
     *
     * @return A point expressed using the cartesian coordinates
     */
    public static CartesianCoordinates of(double x, double y){
        return new CartesianCoordinates(x, y);
    }

    /**
     * Getter of the x value (abscissa).
     *
     * @return the x value of the coordinates (abscissa)
     */
    public double x(){
        return x;
    }

    /**
     * Getter of the y value (ordinate).
     *
     * @return the y value of the coordinates (ordinate)
     */
    public double y(){
        return y;
    }


    /**
     * Calculates the distance between two points in Cartesian Coordinates.
     *
     * @param c
     *          the point to which the distance must be calculated.
     *
     * @return the calculated distance
     */
    public double distanceTo(CartesianCoordinates c){
        double xTerm = (c.x()-this.x()) * (c.x()-this.x());
        double yTerm = (c.y()-this.y()) * (c.y()-this.y());

        return Math.sqrt(xTerm + yTerm);
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the equals() method.
     *
     * @throws UnsupportedOperationException The use of the equals() method is not supported.
     */
    @Override
    public final boolean equals(Object obj){
        throw new UnsupportedOperationException();
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the hashCode() method.
     *
     * @throws UnsupportedOperationException The use of the hashCode() method is not supported.
     */
    @Override
    public final int hashCode(){
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
