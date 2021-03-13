package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;

/**
 * A bean containing the position of the observation.
 *
 * @author Nicolas Szwajcok (315213)
 */
public class ObserverLocationBean {
    private final DoubleProperty lonDeg;
    private final DoubleProperty latDeg;
    private final ObjectBinding<GeographicCoordinates> coordinates;

    /**
     * Creates an instance of an observation location bean by initializing its internal parameters.
     */
    public ObserverLocationBean(){
        this.lonDeg = new SimpleDoubleProperty();
        this.latDeg = new SimpleDoubleProperty();
        this.coordinates = Bindings.createObjectBinding(
                () -> GeographicCoordinates.ofDeg(getLonDeg(), getLatDeg()),
                lonDeg, latDeg
        );
    }

    /**
     * Returns the object property of the longitude (in degrees).
     *
     * @return The object property of the longitude (in degrees)
     */
    public DoubleProperty lonDegProperty(){
        return lonDeg;
    }

    /**
     * Returns the longitude (in degrees).
     *
     * @return The longitude (in degrees)
     */
    public double getLonDeg(){
        return lonDeg.get();
    }

    /**
     * Sets the value of the longitude (in degrees).
     *
     * @param lonDeg The longitude to be set (in degrees)
     */
    public void setLonDeg(double lonDeg){
        this.lonDeg.set(lonDeg);
    }

    /**
     * Returns the object property of the latitude (in degrees).
     *
     * @return The object property of the latitude (in degrees)
     */
    public DoubleProperty latDegProperty(){
        return latDeg;
    }

    /**
     * Returns the latitude (in degrees).
     *
     * @return The latitude (in degrees)
     */
    public double getLatDeg(){
        return latDeg.get();
    }

    /**
     * Sets the value of the latitude (in degrees).
     *
     * @param latDeg The latitude to be set (in degrees)
     */
    public void setLatDeg(double latDeg){
        this.latDeg.set(latDeg);
    }

    /**
     * Returns the object binding of the coordinates property (in degrees).
     *
     * @return The object binding of the coordinates property (in degrees)
     */
    public ObjectBinding<GeographicCoordinates> coordinatesProperty(){
        return coordinates;
    }

    /**
     * Returns the geographic coordinates of the observation.
     *
     * @return The geographic coordinates of the observation
     */
    public GeographicCoordinates getCoordinates(){
        return coordinates.get();
    }

    /**
     * Sets the geographic coordinates of the observation.
     *
     * @param coordinates The geographic coordinates of the observation to set
     */
    public void setCoordinates(GeographicCoordinates coordinates){
        setLonDeg(coordinates.lonDeg());
        setLatDeg(coordinates.latDeg());
    }
}
