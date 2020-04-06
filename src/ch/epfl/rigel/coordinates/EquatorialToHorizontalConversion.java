package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * The conversion from equatorial to horizontal coordinates.
 *
 * @author Nicolas Szwajcok (315213)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {
    private final double localSiderealTime;
    private final GeographicCoordinates place;

    /**
     * Constructs a conversion from equatorial to horizontal coordinates.
     *
     * @param when The date and time at the moment of the conversion
     * @param where The geographic coordinates of the place of the conversion
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        localSiderealTime = SiderealTime.local(when, where);
        place = where;
    }

    /**
     * Applies the conversion from equatorial to horizontal coordinates.
     *
     * @param equ The equatorial coordinates to convert into horizontal coordinates
     * @return The horizontal coordinates obtained from a conversion of the equatorial coordinates
     */
    public HorizontalCoordinates apply(EquatorialCoordinates equ){
        double H = localSiderealTime - equ.ra();

        double sinDec = Math.sin(equ.dec());
        double cosDec = Math.cos(equ.dec());
        double sinLat = Math.sin(place.lat());
        double cosLat = Math.cos(place.lat());

        double h = Math.asin(sinDec * sinLat + cosDec * cosLat * Math.cos(H));
        double A = Angle.normalizePositive(Math.atan2(-cosDec * cosLat * Math.sin(H), sinDec - sinLat * Math.sin(h)));

        return HorizontalCoordinates.of(A, h);
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the equals() method.
     *
     * @throws UnsupportedOperationException The use of the equals() method is not supported.
     */
    @Override
    public final boolean equals(Object obj){ throw new UnsupportedOperationException(); }

    /**
     * Throws an error. This is defined to prevent the programmer from using the hashCode() method.
     *
     * @throws UnsupportedOperationException The use of the hashCode() method is not supported.
     */
    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }
}
