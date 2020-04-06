package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

/**
 * Allows the construction of a stereographic projection.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    private final HorizontalCoordinates center;
    private final double sinCenterLat;
    private final double cosCenterLat;

    /**
     * Constructs a stereographic projection using an instance of horizontal coordinates.
     *
     * @param center The center of the projection
     */
    public StereographicProjection(HorizontalCoordinates center){
        this.center = center;
        this.sinCenterLat = Math.sin(center.lat());
        this.cosCenterLat = Math.cos(center.lat());
    }

    /**
     * Returns the cartesian coordinates of the center of the circle corresponding to the projection of the parallel passing by the point of coordinates hor.
     *
     * @param hor The horizontal coordinates of a point by which the projection of the parallel passes by
     *
     * @return The coordinates of the circle corresponding to the projection of the parallel passing by the point hor
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        double centerY = cosCenterLat / (Math.sin(hor.lat()) + sinCenterLat);
        return CartesianCoordinates.of(0, centerY);
    }

    /**
     * Returns the radius of the circle corresponding to the projection of the parallel passing by the point of coordinates hor.
     *
     * @param parallel The point by which the projection of the parallel passes by
     *
     * @return The radius of the circle corresponding to the projection of the parallel passing by the point hor
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        return Math.cos(parallel.lat()) / (Math.sin(parallel.lat()) + sinCenterLat);
    }

    /**
     * Returns the projected diameter of a sphere of angular size rad centered at the center of the projection (horizon).
     *
     * @param rad The angular size of the sphere
     *
     * @return The projected diameter of the sphere
     */
    public double applyToAngle(double rad){
        return 2 * Math.tan(rad / 4);
    }

    /**
     *
     * Converts a point expressed using horizontal coordinates into a point expressed using cartesian coordinates.
     *
     * @param azAlt The horizontal coordinates to be converted into cartesian coordinates
     *
     * @return The cartesian coordinates of the projected point
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double azAltLat = azAlt.lat();
        double centerLat = center.lat();
        double lonDifference = azAlt.lon() - center.lon();

        double d = 1 / (1 + Math.sin(azAltLat)*Math.sin(centerLat) + Math.cos(azAltLat)*Math.cos(centerLat)*Math.cos(lonDifference));

        double x = d*Math.cos(azAltLat)*Math.sin(lonDifference);
        double y = d*(Math.sin(azAltLat)*Math.cos(centerLat) - Math.cos(azAltLat)*Math.sin(centerLat)*Math.cos(lonDifference));

        return CartesianCoordinates.of(x, y);
    }

    /**
     * Converts a point expressed using cartesian coordinates into a point expressed using horizontal coordinates.
     *
     * @param xy The cartesian coordinates to be converted into horizontal coordinates
     *
     * @return The horizontal coordinates of the point of the projection
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        double x = xy.x();
        double y = xy.y();

        double rho = Math.sqrt(x*x + y*y);
        double sinC = 2*rho/(rho*rho + 1);
        double cosC = (1 - rho*rho)/(rho*rho + 1);

        double lambda = Angle.normalizePositive(Math.atan2(x*sinC, rho*Math.cos(center.lat())*cosC - y*Math.sin(center.lat())*sinC) + center.lon());

        double phi = Math.asin(cosC*Math.sin(center.lat()) + (y*sinC*Math.cos(center.lat()))/rho);

        return HorizontalCoordinates.of(lambda, phi);
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the equals() method.
     *
     * @throws UnsupportedOperationException The use of the equals() method is not supported.
     */
    @Override
    final public boolean equals(Object obj){
        throw new UnsupportedOperationException();
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the hashCode() method.
     *
     * @throws UnsupportedOperationException The use of the hashCode() method is not supported.
     */
    @Override
    final public int hashCode(){
        throw new UnsupportedOperationException();
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of a point expressed using the stereographic projection.
     *
     * @return The textual representation of the point in the stereographic projection
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "StereographicProjection with center at (x=0, y=%.4f)", center.lat());
    }
}
