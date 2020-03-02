package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Class allowing the conversion from equatorial to horizontal coordinates.
 *alpha
 * @author Nicolas Szwajcok (315213)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    final private double localSideralTime;
    final private GeographicCoordinates place;

    /**
     * Constructs a conversion from equatorial to horizontal coordinates
     * @param when the date and time
     * @param where the place
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        this.place = where;
        this.localSideralTime = SiderealTime.local(when, where);
    }

    /**
     * Applies the conversion from equatorial to horizontal coordinates
     * @param equ The equatorial coordinates to convert
     * @return Converted horizontal coordinates
     */
    public HorizontalCoordinates apply(EquatorialCoordinates equ){
        double hrAngle = this.localSideralTime - equ.ra();

        System.out.println("HourAngle : " + Angle.ofDeg(hrAngle));

        double sinDelta = Math.sin(equ.dec());
        double cosDelta = Math.cos(equ.dec());

        double sinPhi = Math.sin(place.lat());
        double cosPhi = Math.cos(place.lat());

        double height = Math.asin( (sinDelta * sinPhi) + ( cosDelta * cosPhi * Math.cos(hrAngle)) );

//        height = Angle.normalizePositive(height);
        System.out.println("Height deg : " + Angle.toDeg(height));

        double azimuth = Math.atan2( (-1 *cosDelta * cosPhi * Math.sin(hrAngle) ), (sinDelta - ( sinPhi * Math.sin(height))) );

        System.out.println("Azimuth : " + Angle.toDeg(azimuth));

        azimuth = Angle.normalizePositive(azimuth);

        return HorizontalCoordinates.of(azimuth, height);
    }

    /**
     * Throws an error. This is defined to prevent the programmer from using the equals() method.
     *
     * @throws UnsupportedOperationException
     */
    final public boolean equals(Object obj){ throw new UnsupportedOperationException(); }

    /**
     * Throws an error. This is defined to prevent the programmer from using the hashCode() method.
     *
     * @throws UnsupportedOperationException
     */
    @Override
    final public int hashCode(){
        throw new UnsupportedOperationException();
    }
}
