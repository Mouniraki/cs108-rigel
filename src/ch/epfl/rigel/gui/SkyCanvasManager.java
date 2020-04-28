package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Transform;

/**
 * CLASSDESCRIPTION
 *
 * @author Mounir Raki (310287)
 */
public class SkyCanvasManager {
    private ObjectBinding<StereographicProjection> projection; //OK
    private ObjectBinding<Transform> planeToCanvas;
    private ObjectBinding<ObservedSky> observedSky; //OK
    private ObjectProperty<CartesianCoordinates> mousePosition;
    private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition; //OK

    public DoubleBinding mouseAzDeg; //OK
    public DoubleBinding mouseAltDeg; //OK
    public ObjectBinding<CelestialObject> objectUnderMouse; //OK


    public SkyCanvasManager(StarCatalogue catalogue,
                            DateTimeBean dateTime,
                            ViewingParametersBean viewingParameters,
                            ObserverLocationBean observerLocation){

        Canvas canvas = new Canvas();
        //canvas.setOnMouseMoved(MouseEvent.MOUSE_MOVED); //DONT KNOW WHY IT MARKS AS INCORRECT

        mousePosition = new SimpleObjectProperty<>();

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParameters.getCenter()),
                viewingParameters.centerProperty());

        //ISSUE WITH DECLARATION
        //planeToCanvas = Bindings.createObjectBinding(() -> ,
                //projection, canvas.widthProperty(), canvas.heightProperty(), viewingParameters.fieldOfViewDegProperty());

        //NOT SURE ABOUT THE DEPENDENCIES
        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTime.getZonedDateTime(), observerLocation.getCoordinates(), projection.get() ,catalogue),
                dateTime.dateProperty(), dateTime.timeProperty(), dateTime.zoneProperty(), observerLocation.coordinatesProperty(),
                projection);

        objectUnderMouse = Bindings.createObjectBinding(
                () -> observedSky.get().objectClosestTo(mousePosition.get(), 10).get(),
                observedSky, mousePosition, planeToCanvas); //MISSING PLANETOCANVAS IN THE LAMBDA

        mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> projection.get().inverseApply(mousePosition.get()),
                mousePosition, projection, planeToCanvas); //MISSING PLANETOCANVAS IN THE LAMBDA

        mouseAzDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.get().azDeg(),
                mouseHorizontalPosition);

        mouseAltDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.get().altDeg(),
                mouseHorizontalPosition);
    }
}
