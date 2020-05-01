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
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
 * CLASSDESCRIPTION
 *
 * @author Mounir Raki (310287)
 */
public class SkyCanvasManager {
    private ObjectBinding<StereographicProjection> projection; //OK
    private ObjectBinding<Transform> planeToCanvas; //OK
    private ObjectBinding<ObservedSky> observedSky; //OK
    private ObjectProperty<CartesianCoordinates> mousePosition; //OK
    private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition; //OK

    private DoubleBinding mouseAzDeg;
    private DoubleBinding mouseAltDeg;
    private ObjectBinding<CelestialObject> objectUnderMouse; //OK

    private Canvas canvas;

    private final static int AZDEG_INCREMENT = 10;
    private final static int ALTDEG_INCREMENT = 5;
    private final static RightOpenInterval AZDEG_INTERVAL = RightOpenInterval.of(0, 360);
    private final static ClosedInterval ALTDEG_INTERVAL = ClosedInterval.of(5, 90);

    public SkyCanvasManager(StarCatalogue catalogue,
                            DateTimeBean dateTimeBean,
                            ObserverLocationBean observerLocationBean,
                            ViewingParametersBean viewingParametersBean){
        canvas = new Canvas();
        SkyCanvasPainter painter = new SkyCanvasPainter(canvas);

        mousePosition = new SimpleObjectProperty<>();

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParametersBean.getCenter()),
                viewingParametersBean.centerProperty());

        planeToCanvas = Bindings.createObjectBinding(
                () -> applyTransform(viewingParametersBean),
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
                    double maxDistance = 10;
                    return observedSky.get().objectClosestTo(cartMousePos(), maxDistance).get();
                }, observedSky, mousePosition, planeToCanvas
        );

        canvas.setOnMouseMoved(m -> mousePosition.setValue(CartesianCoordinates.of(m.getX(), m.getY())));

        canvas.setOnScroll(m -> {
            double deltaFOV;
            double fov = viewingParametersBean.getfieldOfViewDeg();
            if(Math.abs(m.getDeltaX()) > Math.abs(m.getDeltaY()))
                deltaFOV = m.getDeltaX();
            else
                deltaFOV = m.getDeltaY();
            viewingParametersBean.setFieldOfViewDeg(fov + deltaFOV);
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

        observedSky.addListener((p, o, n) -> painter.paint(observedSky.get(), projection.get(), planeToCanvas.get()));
        projection.addListener((p, o, n) -> painter.paint(observedSky.get(), projection.get(), planeToCanvas.get()));
        planeToCanvas.addListener((p, o, n) -> painter.paint(observedSky.get(), projection.get(), planeToCanvas.get()));
    }

    public ObjectBinding<CelestialObject> objectUnderMouseProperty(){
        return objectUnderMouse;
    }

    public DoubleBinding mouseAzDegProperty(){
        return mouseAzDeg;
    }

    public DoubleBinding mouseAltDegProperty(){
        return mouseAltDeg;
    }

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

    private Transform applyTransform(ViewingParametersBean viewingParametersBean){
        double imageWidth = projection.get()
                .applyToAngle(Angle.ofDeg(viewingParametersBean.getfieldOfViewDeg()));
        double scaleFactor = canvas.getWidth() / imageWidth;
        double translationXFactor = canvas.getWidth() / 2;
        double translationYFactor = canvas.getHeight() / 2;
        Transform translation = Transform.translate(translationXFactor, translationYFactor);
        return translation.createConcatenation(Transform.scale(scaleFactor, -scaleFactor));
    }

    private CartesianCoordinates cartMousePos() throws NonInvertibleTransformException { //TO CHECK FOR THE EXCEPTION
        Point2D mousePosInPlane = planeToCanvas.get().inverseTransform(mousePosition.get().x(), mousePosition.get().y());
        return CartesianCoordinates.of(mousePosInPlane.getX(), mousePosInPlane.getY());
    }
}
