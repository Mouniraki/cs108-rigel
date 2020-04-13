package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.Moon;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.util.List;

public class SkyCanvasPainter {
    Canvas canvas;
    GraphicsContext ctx;

    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
    }

    public void clear(){

    }

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform transform){
        CartesianCoordinates moonPosition = sky.moonPosition();
        Moon moon = sky.moon();
        double moonSize = moon.angularSize();
        double diameter = 2 * Math.tan(moonSize / 4);

        HorizontalCoordinates projectedCoordinates = projection.inverseApply(moonPosition);

        Point2D point2D = transform.transform(projectedCoordinates.az(), projectedCoordinates.alt());

        ctx.setFill(Color.WHITE);
        ctx.fillOval(point2D.getX(), point2D.getY(), diameter, diameter);
    }


    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform transform){
        List<Star> stars = sky.stars();

        for(Star star : stars){
            EquatorialCoordinates starPosition = star.equatorialPos();
            EquatorialToHorizontalConversion conversion = new EquatorialToHorizontalConversion(sky.observationInstant(), sky.observationPos());

            double starSize = star.angularSize();
            double diameter = 2 * Math.tan(starSize / 4);

//            HorizontalCoordinates projectedCoordinates = projection.inverseApply(moonPosition);
//            Point2D point2D = transform.transform(projectedCoordinates.az(), projectedCoordinates.alt());
        }

    }
    public void drawSun(){

    }





    public void drawSkyCanvas(ObservedSky sky, StereographicProjection projection, Transform transform){
        drawMoon(sky, projection, transform);//and so on
    }

}
