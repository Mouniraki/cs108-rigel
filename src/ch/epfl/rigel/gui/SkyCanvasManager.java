package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import java.util.Optional;

/**
 * A canvas manager where the sky is drawn.
 *
 * @author Mounir Raki (310287)
 */
public class SkyCanvasManager {
    private final ObjectBinding<StereographicProjection> projection;
    private final ObjectBinding<Transform> planeToCanvas;
    private final ObjectBinding<ObservedSky> observedSky;
    private final ObjectBinding<HorizontalCoordinates> searchedObjectCoordinates;

    private final ObjectProperty<CartesianCoordinates> mousePosition;
    private final ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

    private final StringProperty objectName;
    private final DoubleBinding mouseAzDeg;
    private final DoubleBinding mouseAltDeg;
    private final ObjectBinding<CelestialObject> objectUnderMouse;

    private final Canvas canvas;

    private final static int MAX_DISTANCE_IN_CANVAS = 10;
    private final static int AZDEG_INCREMENT = 10;
    private final static int ALTDEG_INCREMENT = 5;
    private final static RightOpenInterval AZDEG_INTERVAL = RightOpenInterval.of(0, 360);
    private final static ClosedInterval ALTDEG_INTERVAL = ClosedInterval.of(5, 90);
    private final static ClosedInterval FOV_INTERVAL = ClosedInterval.of(30, 150);

    private double scaleFactor;

    /**
     * Constructs a canvas manager from a star catalogue and beans containing the time, location and viewing information.
     *
     * @param catalogue
     *          the catalogue of stars and asterisms
     * @param dateTimeBean
     *          the bean containing the ZonedDateTime of the observation
     * @param observerLocationBean
     *          the bean containing the information in regard to the location of the observation
     * @param viewingParametersBean
     *          the bean containing the information in regard to the field of view and center of projection
     */
    public SkyCanvasManager(StarCatalogue catalogue,
                            DateTimeBean dateTimeBean,
                            ObserverLocationBean observerLocationBean,
                            ViewingParametersBean viewingParametersBean){
        scaleFactor = 0;
        canvas = new Canvas();
        SkyCanvasPainter painter = new SkyCanvasPainter(canvas);

        mousePosition = new SimpleObjectProperty<>(CartesianCoordinates.of(0, 0));

        objectName = new SimpleStringProperty("");

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty()
        );

        planeToCanvas = Bindings.createObjectBinding(
                () -> setTransform(viewingParametersBean),
                projection, viewingParametersBean.fieldOfViewDegProperty(), canvas.widthProperty(), canvas.heightProperty()
        );

        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(
                        dateTimeBean.getZonedDateTime(),
                        observerLocationBean.getCoordinates(),
                        projection.get(),
                        catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneProperty(),
                observerLocationBean.coordinatesProperty(), projection
        );

        searchedObjectCoordinates = Bindings.createObjectBinding(
                () -> observedSky.get().searchObjectCoordinates(objectName.get()),
                observedSky, objectName
        );

        mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> projection.get().inverseApply(cartMousePos()),
                mousePosition, projection, planeToCanvas
        );

        mouseAzDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.get().azDeg(),
                mouseHorizontalPosition
        );

        mouseAltDeg = Bindings.createDoubleBinding(
                () -> mouseHorizontalPosition.get().altDeg(),
                mouseHorizontalPosition
        );

        objectUnderMouse = Bindings.createObjectBinding(
                () -> {
                    double maxDistanceInPlane = //planeToCanvas.get().inverseDeltaTransform(new Point2D(scaleFactor));
                            Math.abs(MAX_DISTANCE_IN_CANVAS / scaleFactor);
                    Optional<CelestialObject> object = observedSky.get()
                                .objectClosestTo(cartMousePos(), maxDistanceInPlane);
                    return object.orElse(null); },
                observedSky, mousePosition, planeToCanvas
        );


        canvas.setOnMouseMoved(m -> mousePosition.setValue(CartesianCoordinates.of(m.getX(), m.getY())));

        canvas.setOnScroll(m -> {
            double fov = viewingParametersBean.getfieldOfViewDeg();
            /*
            if())
                deltaFOV = m.getDeltaX();
            else
                deltaFOV = m.getDeltaY();

             */
            double deltaFOV = Math.abs(m.getDeltaX()) > Math.abs(m.getDeltaY()) ? m.getDeltaX() : m.getDeltaY();
            fov += deltaFOV;
            viewingParametersBean.setFieldOfViewDeg(FOV_INTERVAL.clip(fov));
        });

        canvas.setOnMousePressed(m -> {
            if(m.isPrimaryButtonDown()) canvas.requestFocus();
        });

        canvas.setOnKeyPressed(k -> {
            switch(k.getCode()){
                case LEFT:
                    k.consume();
                    viewingParametersBean.setCenter(azMod(viewingParametersBean, -AZDEG_INCREMENT));
                    break;
                case RIGHT:
                    k.consume();
                    viewingParametersBean.setCenter(azMod(viewingParametersBean, AZDEG_INCREMENT));
                    break;
                case DOWN:
                    k.consume();
                    viewingParametersBean.setCenter(altMod(viewingParametersBean, -ALTDEG_INCREMENT));
                    break;
                case UP:
                    k.consume();
                    viewingParametersBean.setCenter(altMod(viewingParametersBean, ALTDEG_INCREMENT));
                    break;
            }
        });

        objectName.addListener((p, o, n) -> {
            if(searchedObjectCoordinates.get() != null)
                viewingParametersBean.setCenter(searchedObjectCoordinates.get());
            else viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
        });

        observedSky.addListener((p, o, n) -> painter.paint(observedSky.get(), projection.get(), planeToCanvas.get()));
        planeToCanvas.addListener((p, o, n) -> painter.paint(observedSky.get(), projection.get(), planeToCanvas.get()));
    }

    /**
     * Getter for the objectUnderMouse binding.
     *
     * @return the objectUnderMouse binding
     */
    public ObjectBinding<CelestialObject> objectUnderMouseProperty(){
        return objectUnderMouse;
    }

    /**
     * Getter for the mouseAzDeg binding.
     *
     * @return the mouseAzDeg binding
     */
    public DoubleBinding mouseAzDegProperty(){
        return mouseAzDeg;
    }

    /**
     * Getter for the mouseAltDeg binding.
     *
     * @return the mouseAltDeg binding
     */
    public DoubleBinding mouseAltDegProperty(){
        return mouseAltDeg;
    }

    /**
     * Getter for the property containing the name of the found CelestialObject.
     *
     * @return the property holding the name of the found CelestialObject
     */
    public StringProperty objectNameProperty(){
        return objectName;
    }

    /**
     * Getter for the canvas on which the elements are drawn.
     *
     * @return the canvas on which the elements are drawn
     */
    public Canvas canvas(){
        return canvas;
    }

    private HorizontalCoordinates azMod(ViewingParametersBean viewingParametersBean, int azDegIncrement){
        double newAzDeg = AZDEG_INTERVAL.reduce(viewingParametersBean.getCenter().azDeg() + azDegIncrement);
        return HorizontalCoordinates.ofDeg(newAzDeg, viewingParametersBean.getCenter().altDeg());
    }

    private HorizontalCoordinates altMod(ViewingParametersBean viewingParametersBean, int altDegIncrement){
        double newAltDeg = ALTDEG_INTERVAL.clip(viewingParametersBean.getCenter().altDeg() + altDegIncrement);
        return HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg(), newAltDeg);
    }

    private Transform setTransform(ViewingParametersBean viewingParametersBean){
        double imageWidth = projection.get()
                .applyToAngle(Angle.ofDeg(viewingParametersBean.getfieldOfViewDeg()));
        scaleFactor = canvas.getWidth() / imageWidth;
        double translationXFactor = canvas.getWidth() / 2;
        double translationYFactor = canvas.getHeight() / 2;
        Transform translation = Transform.translate(translationXFactor, translationYFactor);
        return translation.createConcatenation(Transform.scale(scaleFactor, -scaleFactor));
    }

    private CartesianCoordinates cartMousePos() {
        try{
            Point2D mousePosInPlane = planeToCanvas.get().inverseTransform(mousePosition.get().x(), mousePosition.get().y());
            return CartesianCoordinates.of(mousePosInPlane.getX(), mousePosInPlane.getY());
        }
        catch (NonInvertibleTransformException exception){
            return CartesianCoordinates.of(0, 0);
        }
    }
}
