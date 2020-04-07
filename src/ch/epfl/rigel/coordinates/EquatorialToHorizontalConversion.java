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
        double hourAngle = localSiderealTime - equ.ra();

        double sinEquDec = Math.sin(equ.dec());
        double cosEquDec = Math.cos(equ.dec());
        double sinGeoLat = Math.sin(place.lat());
        double cosGeoLat = Math.cos(place.lat());

        double alt = Math.asin(sinEquDec*sinGeoLat + cosEquDec*cosGeoLat*Math.cos(hourAngle));
        double az = Math.atan2(
                -cosEquDec * cosGeoLat * Math.sin(hourAngle),
                sinEquDec - sinGeoLat*Math.sin(alt));

        return HorizontalCoordinates.of(Angle.normalizePositive(az), alt);
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
