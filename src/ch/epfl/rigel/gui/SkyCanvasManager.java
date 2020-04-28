package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Transform;

import java.awt.event.MouseEvent;

/**
 * CLASSDESCRIPTION
 *
 * @author Mounir Raki (310287)
 */
public class SkyCanvasManager {
    private ObjectBinding<StereographicProjection> projection; //OK
    private ObjectBinding<Transform> planeToCanvas;
    private ObjectBinding<ObservedSky> observedSky; //OK
    private ObjectProperty<CartesianCoordinates> mousePosition; //OK
    private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition; //OK

    public DoubleBinding mouseAzDeg; //OK
    public DoubleBinding mouseAltDeg; //OK
    public ObjectBinding<CelestialObject> objectUnderMouse; //OK

    public SkyCanvasManager(StarCatalogue catalogue,
                            DateTimeBean dateTime,
                            ViewingParametersBean viewingParameters,
                            ObserverLocationBean observerLocation){

        mousePosition = new SimpleObjectProperty<>();
        Canvas canvas = new Canvas();
        canvas.setOnMouseMoved(m -> mousePosition.set(CartesianCoordinates.of(m.getX(), m.getY())));

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParameters.getCenter()),
                viewingParameters.centerProperty());
/*
        //ISSUE WITH DECLARATION
        planeToCanvas = Bindings.createObjectBinding(
                () -> {
                    double fieldOfView = Angle.ofDeg(viewingParameters.getfieldOfViewDeg());
                    double imageWidth = projection.get()
                            .applyToAngle(fieldOfView);
                    double canvasWidth = canvas.getWidth();
                    double scaleFactor = canvasWidth / imageWidth;
                    double scaleX =

                    Transform.scale();
                    Transform.translate();
                },
                projection, canvas.widthProperty(), canvas.heightProperty(), viewingParameters.fieldOfViewDegProperty());


 */
        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTime.getZonedDateTime(), observerLocation.getCoordinates(), projection.get() ,catalogue),
                dateTime.dateProperty(), dateTime.timeProperty(), dateTime.zoneProperty(), observerLocation.coordinatesProperty(),
                projection);

        objectUnderMouse = Bindings.createObjectBinding(
                () -> {
                    double mouseX = mousePosition.get()
                            .x();
                    double mouseY = mousePosition.get()
                            .y();
                    Point2D cartMousePos = planeToCanvas.get().inverseTransform(mouseX, mouseY);
                    CartesianCoordinates outCanvas = CartesianCoordinates.of(cartMousePos.getX(), cartMousePos.getY());
                    return observedSky.get()
                            .objectClosestTo(outCanvas, 10)
                            .get();
                },
                observedSky, mousePosition, planeToCanvas);

        mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> {
                    double mouseX = mousePosition.get()
                            .x();
                    double mouseY = mousePosition.get()
                            .y();
                    Point2D cartMousePos = planeToCanvas.get().inverseTransform(mouseX, mouseY);
                    CartesianCoordinates outCanvas = CartesianCoordinates.of(cartMousePos.getX(), cartMousePos.getY());
                    return projection.get().inverseApply(outCanvas);
                },
                mousePosition, projection, planeToCanvas);

        mouseAzDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.get().azDeg(),
                mouseHorizontalPosition);

        mouseAltDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.get().altDeg(),
                mouseHorizontalPosition);
    }
}
