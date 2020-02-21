package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

public final class HorizontalCoordinates extends SphericalCoordinates {
    private double azimut, altitude;
    private HorizontalCoordinates(double az, double alt){
        azimut = az;
        altitude = alt;
    }

    public static HorizontalCoordinates of(double az, double alt){
        Preconditions.checkInInterval(RightOpenInterval.of(0, Angle.TAU), az);
        Preconditions.checkInInterval(ClosedInterval.symmetric(Angle.TAU/2), alt);
        return new HorizontalCoordinates(az, alt);
    }

    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg){
        Preconditions.checkInInterval(RightOpenInterval.of())
    }

    public double az(){}
    public double azDeg(){}
    public String azOctantName(String n, String e, String s, String w){}
    public double alt(){}
    public double altDeg(){}
    public double angularDistanceTo(HorizontalCoordinates that){}

    @Override
    public String toString() {}
}
