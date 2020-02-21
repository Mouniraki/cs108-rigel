package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Mather class for all types of Spherical Coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
abstract class SphericalCoordinates {
    private double lon;
    private double lat;

    SphericalCoordinates(double longitude, double latitude) {
        this.lon = longitude;
        this.lat = latitude;
    }

    double lon() {
        return lon;
    }

    double lonDeg(){
        return Angle.toDeg(lon);
    }

    double lat(){
        return lat;
    }

    double latDeg(){
        return Angle.toDeg(lat);
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
    @Override
    final public int hashCode(){
        throw new UnsupportedOperationException("This operation is not supported.");
    }
}
