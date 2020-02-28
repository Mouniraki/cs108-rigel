package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;

import java.time.ZonedDateTime;

public final class SiderealTime {
    private SiderealTime(){}
    static double greenwich(ZonedDateTime when){
        double T = Epoch.J2000.julianCenturiesUntil(when);
        //double t = ;
        //T = nbr de siècles juliens séparant l'époque J2000 et le début du jour contenant l'instant
        //t = nbr d'heures séparant le début du jour contenant l'instant et l'instant lui-même

    /*
        double sidereal1 = 0.000025862*(T*T)+2400.051336*T+6.697374558;
        double sidereal2 = 1.002737909*t;
        return sidereal1 + sidereal2;
    */
    return 0.0;
    }
    static double local(ZonedDateTime when, GeographicCoordinates where){return 0.0;}
}
