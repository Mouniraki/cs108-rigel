package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Mather class for all types of Spherical Coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
abstract class SphericalCoordinates {
    private double longitude;
    private double latitude;

    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    private double lon() {
        return longitude;
    }

    private double lonDeg(){
        return Angle.toDeg(longitude);
    }

    private double lat(){
        return latitude;
    }

    private double latDeg(){
        return Angle.toDeg(latitude);
    }

    /**
     * Throws an error, is defined to prevent the user from using the equals() method.
     *
     * @throws UnsupportedOperationException
     */
    final public boolean equals(){
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    /**
     * Throws an error, is defined to prevent the user from using the hashCode() method.
     *
     * @throws UnsupportedOperationException
     */
    final public int hashCode(){
        throw new UnsupportedOperationException("This operation is not supported.");
    }
}
