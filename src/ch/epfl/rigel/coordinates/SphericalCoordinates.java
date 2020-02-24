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

    double lon() {
        return longitude;
    }

    double lonDeg(){
        return Angle.toDeg(longitude);
    }

    double lat(){
        return latitude;
    }

    double latDeg(){
        return Angle.toDeg(latitude);
    }

    /**
     * Throws an error, is defined to prevent the user from using the equals() method.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public final boolean equals(Object obj) {throw new UnsupportedOperationException();}

    /**
     * Throws an error, is defined to prevent the user from using the hashCode() method.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public final int hashCode() {throw new UnsupportedOperationException();}
}
