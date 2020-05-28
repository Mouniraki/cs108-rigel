package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.*;

/**
 * The bean containing the part of sky displayed on the generated image.
 *
 * @author Nicolas Szwajcok (315213)
 */
public class ViewingParametersBean {
    private final DoubleProperty fieldOfViewDeg;
    private final ObjectProperty<HorizontalCoordinates> center;

    /**
     * Creates an instance of the viewing parameters bean.
     */
    public ViewingParametersBean(){
        this.fieldOfViewDeg = new SimpleDoubleProperty();
        this.center = new SimpleObjectProperty<>();
    }

    /**
     * Returns the object property of the field of view (in degrees).
     *
     * @return The object property of the field of view (in degrees)
     */
    public DoubleProperty fieldOfViewDegProperty(){
        return fieldOfViewDeg;
    }

    /**
     * Returns the field of view (in degrees).
     *
     * @return The field of view (in degrees)
     */
    public double getfieldOfViewDeg(){
        return fieldOfViewDeg.get();
    }

    /**
     * Sets the value of the field of view (in degrees).
     *
     * @param fieldOfViewDeg The field of view value to be set (in degrees)
     */
    public void setFieldOfViewDeg(double fieldOfViewDeg){
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    /**
     * Returns the object property of the center of the generated image of the sky.
     *
     * @return The object property of the center of the generated image of the sky
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty(){
        return center;
    }

    /**
     * Returns the horizontal coordinates of the center the generated image of the sky.
     *
     * @return The horizontal coordinates of the center the generated image of the sky
     */
    public HorizontalCoordinates getCenter(){
        return center.get();
    }

    /**
     * Sets the value of the center of the generated image of the sky.
     *
     * @param center The horizontal coordinates of the center of the generated image of the sky to be set
     */
    public void setCenter(HorizontalCoordinates center){
        this.center.set(center);
    }
}
