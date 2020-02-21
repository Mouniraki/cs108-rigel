package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.*;

final public class GeographicCoordinates extends SphericalCoordinates{
    final private double lon;
    final private double lat;

    private GeographicCoordinates(double lon, double lat) {
        super(lon, lat);
    }

    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){
        Preconditions.checkInInterval(RightOpenInterval.of(-180, 180), lonDeg);
        Preconditions.checkInInterval(ClosedInterval.of(-90, 90), latDeg);

        return new GeographicCoordinates(Angle.ofDeg(latDeg), Angle.ofDeg(latDeg));
    }

    public static boolean isValidLonDeg(double lonDeg){
        if(lonDeg >= -180 && lonDeg < 180){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isValidLatDeg(double latDeg){
        if(latDeg >= -90 && latDeg <= 90){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public double lon(){
        return lon;
    }

    @Override
    public double lonDeg(){
        return Angle.toDeg(lon);
    }

    @Override
    public double lat(){
        return lat;
    }

    @Override
    public double latDeg(){
        return Angle.toDeg(lat);
    }

    @Override
    public String toString(){
        return "(lon=" + String.format("%.4f", lon) + "°, lat=" + String.format("%.4f", lat) + "°)";
    }
}
