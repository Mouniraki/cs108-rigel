package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

public final class EclipticCoordinates extends SphericalCoordinates {
    private EclipticCoordinates(double lon, double lat){
        super(lon, lat);
    }

    public static EclipticCoordinates of(double lon, double lat){
        Preconditions.checkInInterval(RightOpenInterval.of(0, Angle.TAU), lon);
        Preconditions.checkInInterval(ClosedInterval.symmetric(Angle.TAU/2), lat);
        return new EclipticCoordinates(lon, lat);
    }

    public double lon() {return super.lon();}
    public double lonDeg() {return super.lonDeg();}
    public double lat() {return super.lat();}
    public double latDeg() {return super.latDeg();}

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(λ=%.4f, β=%.4f)", lonDeg(), latDeg());
    }
}
