package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Transform;

import java.time.ZonedDateTime;
import java.util.Arrays;

/**
 * CLASSDESCRIPTION
 *
 * @author Mounir Raki (310287)
 */
public class SkyCanvasManager {
    private ObjectBinding<StereographicProjection> projection;
    private ObjectBinding<Transform> planeToCanvas;
    private ObjectBinding<ObservedSky> observedSky;
    private ObjectProperty<CartesianCoordinates> mousePosition;
    private ObjectBinding<HorizontalCoordinates> mouseHorizontalPosition;

    public DoubleBinding mouseAzDeg;
    public DoubleBinding MouseAltDeg;
    public ObjectBinding<CelestialObject> objectUnderMouse;

    public SkyCanvasManager(StarCatalogue catalogue,
                            DateTimeBean dateTimeBean,
                            ObserverLocationBean observerLocationBean,
                            ViewingParametersBean viewingParametersBean){
        SkyCanvasPainter painter = new SkyCanvasPainter(canvas());

        projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(viewingParametersBean.getCenter()),
                        viewingParametersBean.centerProperty());

        observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dateTimeBean.getZonedDateTime(), observerLocationBean.getCoordinates(), projection.get(), catalogue),
                dateTimeBean.dateProperty(), dateTimeBean.timeProperty(), dateTimeBean.zoneProperty(),
                observerLocationBean.coordinatesProperty(), projection
        );

        planeToCanvas = Bindings.createObjectBinding(
                () -> {
                    double imageWidth = projection.get().applyToAngle(Angle.ofDeg(viewingParametersBean.getfieldOfViewDeg()));
                    double scaleFactor = canvas().getWidth() / imageWidth;
                    double translationXFactor = canvas().getWidth() / 2;
                    double translationYFactor = canvas().getHeight() / 2;
                    Transform planeToCanvas = Transform.scale(scaleFactor, -scaleFactor);
                    return planeToCanvas.createConcatenation(Transform.translate(translationXFactor, translationYFactor));
                },
                projection, viewingParametersBean.fieldOfViewDegProperty(), canvas().widthProperty(), canvas().heightProperty()
        );

        //TESTING ONLY
        ZonedDateTime when =
                ZonedDateTime.parse("2020-02-17T20:15:00+01:00");
        GeographicCoordinates where =
                GeographicCoordinates.ofDeg(6.57, 46.52);
        HorizontalCoordinates projCenter =              //Professor's
                HorizontalCoordinates.ofDeg(180, 45);   //data
        StereographicProjection projection =
                new StereographicProjection(projCenter);
        ObservedSky sky =
                new ObservedSky(when, where, projection, catalogue);
        Transform planeToCanvas =                             //Professor's
                Transform.affine(1300, 0, 0, -1300, 400, 300); //data

        painter.paint(sky, projection, planeToCanvas);
        System.out.println(projection.toString());
        //painter.paint(observedSky.get(), projection.get(), planeToCanvas.get());
    }

    public ObjectBinding<CelestialObject> objectUnderMouseProperty(){
        return objectUnderMouse;
    }

    public Canvas canvas(){
        return new Canvas(800, 600);
    }
}
