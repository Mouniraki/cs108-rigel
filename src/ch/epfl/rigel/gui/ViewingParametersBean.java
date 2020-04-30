package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The bean containing the part of sky visible on the generated image.
 *
 * @author Nicolas Szwajcok (315213)
 */
public class ViewingParametersBean {
    private IntegerProperty fieldOfViewDeg;
    private ObjectProperty<HorizontalCoordinates> center;

    /**
     * Creates an instance of the viewing parameters bean by initializing its internal parameters.
     */
    public ViewingParametersBean(){
        this.fieldOfViewDeg = new SimpleIntegerProperty();
        this.center = new SimpleObjectProperty<>();
    }

    /**
     * Returns the object property of the field of view (in degrees).
     *
     * @return The object property of the field of view (in degrees)
     */
    public IntegerProperty fieldOfViewDegProperty(){
        return fieldOfViewDeg;
    }

    /**
     * Returns the field of view (in degrees).
     *
     * @return The field of view (in degrees)
     */
    public Integer getfieldOfViewDeg(){
        return fieldOfViewDeg.get();
    }

    /**
     * Sets the value of the field of view (in degrees).
     *
     * @param fieldOfViewDeg The field of view value (in degrees) to be set
     */
    public void setFieldOfViewDeg(Integer fieldOfViewDeg){
        this.fieldOfViewDeg.setValue(fieldOfViewDeg);
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
        this.center.setValue(center);
    }
}
