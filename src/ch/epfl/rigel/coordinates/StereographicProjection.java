package ch.epfl.rigel.coordinates;

import java.util.Locale;
import java.util.function.Function;

/**
 * Class allowing a construction of a stereographic projection.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    final private HorizontalCoordinates center;
    final private double sinLambda1;
    final private double cosLambda1;

    /**
     * Constructor of the stereographic projection.
     * @param center Center of the projection.
     */
    public StereographicProjection(HorizontalCoordinates center){
        this.center = center;

        this.sinLambda1 = Math.sin(center.az());
        this.cosLambda1 = Math.cos(center.az());
    }

    /**
     * Returns the coordinates of the center of the circle corresponding to the parallel projection passing by the point of coordinates hor.
     * @param hor The point by which the parallel projection passes by
     * @return The coordinates of the circle corresponding to the projection of the parallel passing by the point hor
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        double centerY = ( this.cosLambda1 / ( Math.sin(hor.lat()) + this.sinLambda1 ) );
        return CartesianCoordinates.of(0, centerY);
    }

    /**
     * Returns the radious of the circle corresponding to the projection of the parallel passing by the point of coordinates hor.
     * @param parallel The point by which the parallel projection passes by
     * @return The radious of the circle corresponding to the projection of the parallel passing by the point hor
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        return ( Math.cos(parallel.lat()) / ( ( Math.sin(parallel.lat()) + this.sinLambda1 ) )  );
    }

    /**
     * Returns the projected diameter of a sphere of angular size rad centered at the center of the projection (horizon).
     * @param rad The angular size
     * @return Projected diameter of the sphere
     */
    public double applyToAngle(double rad){
        return (2 * Math.tan(rad / 4));
    }

    /**
     * Returns the cartesian coordinates of the projection of the point of the given horizontal coordinates
     * @param azAlt The horizontal coordinates used to calculate the projected point
     * @return The cartesian coordinates of the projected point
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        //TODO Obviously the way of calculating by defining so many variable is bad, but for now we need to
        //have an easy view why things work/not work, and when they'll work we'll optimize the code

        final double lambda = azAlt.az();
        final double phi = azAlt.alt();
        final double deltaLambda = lambda - this.center.az();

        double d = 1 / (1 + Math.sin(phi) * Math.sin(this.center.alt()) + Math.cos(phi) * Math.cos(this.center.alt()) * Math.cos(deltaLambda) );

        double x = d * Math.cos(phi) * Math.sin(deltaLambda);
        double y = d * (Math.sin(phi) * Math.cos(this.center.alt()) - Math.cos(phi) * Math.sin(center.alt()) * Math.cos(deltaLambda));

        return CartesianCoordinates.of(x, y);
    }

    /**
     * Returns the horizontal coordinates of the point of which the projection is the point of cartesian coordinates xy
     * @param xy The cartesian coordinates about which the projection is done
     * @return The horizontal coordinates of the point of the projection
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){

        double x = xy.x();
        double y = xy.y();

        double rho = Math.sqrt(x * x + y * y);
        double sinc = 2 * rho / (rho * rho + 1);
        double cosc = (1 - rho * rho) / (rho * rho + 1);

        double lambda = Math.atan2( (x * sinc), ( (rho * Math.cos(this.center.alt()) * cosc) - (y * Math.sin(this.center.alt()) * sinc) ) ) + this.center.az();


        double phi = Math.asin( (cosc * Math.sin(this.center.alt()) + (y * sinc * Math.cos(this.center.alt())) / rho) );

        return HorizontalCoordinates.of(lambda, phi);
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
     * expressed using the stereographic projection.
     *
     * @return the textual representation of the point in the stereographic projection
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "StereographicProjection with center at (x=0, y=%.4f)", center.alt() );
    }
}
