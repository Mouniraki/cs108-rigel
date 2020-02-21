package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

final public class EquatorialCoordinates extends SphericalCoordinates{
    final private double ra;
    final private double dec;

    private EquatorialCoordinates(double ra, double dec) {
        this.ra = ra;
        this.dec = dec;
    }

    public static EquatorialCoordinates of(double ra, double dec){
        Preconditions.checkInInterval(RightOpenInterval.of(0, 24), Angle.toHr(ra));
        Preconditions.checkInInterval(ClosedInterval.of(-90, 90), dec);
        return new EquatorialCoordinates(Angle.toHr(Angle.toDeg(ra)), dec);
    }

    @Override
    public double ra(){
        return ra;
    }

    @Override
    public double raDeg(){
        return Angle.toDeg(ra);
    }

    @Override
    public double raHr(){
        return Angle.toHr(ra));
    }

    @Override
    public double dec(){
        return dec;
    }

    @Override
    public double decDeg(){
        return Angle.toDeg(dec);
    }

    @Override
    public String toString(){
        return String.format(Locale.ROOT, "(ra=%.4f, dec=%.4f°)", Angle.toHr(ra), Angle.toDeg(dec));
        //return "(ra=" + String.format("%.4f", )) + "h, dec=" + String.format("%.4f", ) + "°)";
    }
}
