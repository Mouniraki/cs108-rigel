package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

import static java.lang.Math.*;

/**
 * Allows the construction of a Stereographic Projection.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    private final HorizontalCoordinates center;
    private final double sinCenterLat;
    private final double cosCenterLat;

    /**
     * Constructs a Stereographic Projection using an instance of Horizontal Coordinates.
     *
     * @param center The center of the projection
     */
    public StereographicProjection(HorizontalCoordinates center){
        this.center = center;
        this.sinCenterLat = sin(center.lat());
        this.cosCenterLat = cos(center.lat());
    }

    /**
     * Returns the Cartesian Coordinates of the center of the circle corresponding to the projection of the parallel passing by the point of coordinates hor.
     *
     * @param hor The Horizontal Coordinates of a point by which the projection of the parallel passes by
     *
     * @return The coordinates of the circle corresponding to the projection of the parallel passing by the point hor
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        double centerY = cosCenterLat / (sin(hor.lat()) + sinCenterLat);
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
        return cos(parallel.lat()) / (sin(parallel.lat()) + sinCenterLat);
    }

    /**
     * Returns the projected diameter of a sphere of angular size rad centered at the center of the projection (horizon).
     *
     * @param rad The angular size of the sphere
     *
     * @return The projected diameter of the sphere
     */
    public double applyToAngle(double rad){
        return 2 * tan(rad / 4);
    }

    /**
     *
     * Converts a point expressed using Horizontal Coordinates into a point expressed using Cartesian Coordinates.
     *
     * @param azAlt The Horizontal Coordinates to be converted into Cartesian Coordinates
     *
     * @return The Cartesian Coordinates of the projected point
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double lonDifference = azAlt.lon() - center.lon();
        double cosAlt = cos(azAlt.alt());
        double sinAlt = sin(azAlt.lat());
        double cosLonDifference = cos(lonDifference);

        double d = 1 / (1 + sinAlt*sinCenterLat + cosAlt*cosCenterLat*cosLonDifference);

        double x = d*cosAlt* sin(lonDifference);
        double y = d * (sinAlt*cosCenterLat - cosAlt*sinCenterLat*cosLonDifference);

        return CartesianCoordinates.of(x, y);
    }

    /**
     * Converts a point expressed using Cartesian Coordinates into a point expressed using Horizontal Coordinates.
     *
     * @param xy The Cartesian Coordinates to be converted into Horizontal Coordinates
     *
     * @return The Horizontal Coordinates of the point of the projection
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        double x = xy.x();
        double y = xy.y();

        double rho = sqrt(x*x + y*y);
        double sinC = 2*rho/(rho*rho + 1);
        double cosC = (1 - rho*rho)/(rho*rho + 1);

        double az, alt;

        if(x == 0 && y == 0){
            az = center.lon();
            alt = center.lat();
        }

        else {
            az = atan2(x * sinC, rho*cosCenterLat*cosC - y*sinCenterLat*sinC) + center.lon();
            alt = asin(cosC * sinCenterLat + (y * sinC * cosCenterLat) / rho);
        }

        return HorizontalCoordinates.of(Angle.normalizePositive(az), alt);
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the equals() method.
     *
     * @throws UnsupportedOperationException The use of the equals() method is not supported.
     */
    @Override
    final public boolean equals(Object obj){
        throw new UnsupportedOperationException("You are not allowed to use the equals method in StereographicProjection.");
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the hashCode() method.
     *
     * @throws UnsupportedOperationException The use of the hashCode() method is not supported.
     */
    @Override
    final public int hashCode(){
        throw new UnsupportedOperationException("You are not allowed to use the equals method in StereographicProjection.");
    }

    /**
     * Redefines the toString method in java.lang.Object to construct the textual representation of a point expressed using the Stereographic Projection.
     *
     * @return The textual representation of the point in the Stereographic Projection
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "StereographicProjection with center at (x=0, y=%.4f)", center.lat());
    }
}
