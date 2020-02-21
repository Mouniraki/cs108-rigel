package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class HorizontalCoordinates extends SphericalCoordinates {
    private HorizontalCoordinates(double az, double alt){
        super(az, alt);
    }

    public static HorizontalCoordinates of(double az, double alt) {
        Preconditions.checkInInterval(RightOpenInterval.of(0, Angle.TAU), az);
        Preconditions.checkInInterval(ClosedInterval.symmetric(Angle.TAU/2), alt);
        return new HorizontalCoordinates(az, alt);
    }

    //A TESTER !!
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        Preconditions.checkInInterval(RightOpenInterval.of(0, 360), azDeg);
        Preconditions.checkInInterval(ClosedInterval.symmetric(180), altDeg);
        return new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg));
    }

    public double az() {return lon();}

    public double azDeg() {return lonDeg();}

    //A REVOIR
    public String azOctantName(String n, String e, String s, String w) {
        String cardinality = "";
        if(RightOpenInterval.of(22.5, 67.5).contains(azDeg())) {cardinality = n+e;}
        else if(RightOpenInterval.of(67.5, 112.5).contains(azDeg())) {cardinality = e;}
        else if(RightOpenInterval.of(112.5, 157.5).contains(azDeg())) {cardinality = s+e;}
        else if(RightOpenInterval.of(157.5, 202.5).contains(azDeg())) {cardinality = s;}
        else if(RightOpenInterval.of(202.5, 247.5).contains(azDeg())) {cardinality = s+w;}
        else if(RightOpenInterval.of(247.5, 292.5).contains(azDeg())) {cardinality = w;}
        else if(RightOpenInterval.of(292.5, 337.5).contains(azDeg())) {cardinality = n+w;}
        else cardinality = n;
        return cardinality;
    }

    public double alt() {return lat();}
    public double altDeg() {return latDeg();}
    public double angularDistanceTo(HorizontalCoordinates that) {
        return Math.acos(Math.sin(this.alt())*Math.sin(that.alt())+Math.cos(this.alt())*Math.cos(that.alt())*Math.cos(this.az() - that.az()));
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(), altDeg());
    }
}
