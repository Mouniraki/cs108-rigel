package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Mother class for all types of spherical coordinates.
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

    /**
     * Returns the longitude.
     *
     * @return the longitude
     */
    double lon() {
        return lon;
    }

    /**
     * Returns the longitude in degrees.
     *
     * @return the longitude in degrees
     */
    double lonDeg(){
        return Angle.toDeg(lon);
    }

    /**
     * Returns the latitude.
     *
     * @return the latitude
     */
    double lat(){
        return lat;
    }

    /**
     * Returns the latitude in degrees.
     *
     * @return the latitude in degrees
     */
    double latDeg(){
        return Angle.toDeg(lat);
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the equals() method.
     *
     * @throws UnsupportedOperationException
     */
    final public boolean equals(){
        throw new UnsupportedOperationException("The equals operation is not supported.");
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the hashCode() method.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    final public int hashCode(){
        throw new UnsupportedOperationException("The hashCode operation is not supported.");
    }
}
