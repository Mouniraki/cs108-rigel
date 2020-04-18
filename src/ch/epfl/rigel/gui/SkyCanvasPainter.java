package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Transform;

import java.util.Iterator;

/**
 * Generates an image of the sky.
 *
 * @author Nicolas Szwajcok (315213)
 */
public class SkyCanvasPainter {
    final private ClosedInterval interval;
    final private Canvas canvas;
    final private GraphicsContext ctx;

    /**
     * Initializes the process of generating an image of the sky.
     *
     * @param canvas Canvas on which an image is going to be painted.
     */
    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        this.interval = ClosedInterval.of(-2, 5);
    }

    /**
     * Clears the canvas by filling it with the black color.
     */
    public void clear(){
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Generates an image of the moon.
     *
     * @param sky The observed sky at the moment of generation of an image of the sky.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void drawMoon(ObservedSky sky, Transform transform){
        CartesianCoordinates moonCoords = sky.moonPosition();
        Point2D transformedCoord = transform.transform(moonCoords.x(), moonCoords.y());

        double moonAngularSize = sky.moon().angularSize();
        double diameter = 2 * Math.tan(moonAngularSize/4);
        Point2D diameterVector = transform.deltaTransform(0, diameter);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(transformedCoord.getX() - diameterVector.magnitude()/2,
                transformedCoord.getY() - diameterVector.magnitude()/2,
                diameterVector.magnitude(),
                diameterVector.magnitude());
    }

    /**
     * Generates line of horizon and names the cardinal points.
     *
     * @param projection The projection used to project given coordinates into a two-dimensional plane.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void drawHorizon(StereographicProjection projection, Transform transform){
        HorizontalCoordinates horizonCoord = HorizontalCoordinates.ofDeg(0, 0);
        CartesianCoordinates centerCoord = projection.circleCenterForParallel(horizonCoord);
        Point2D transformedCenterCoord = transform.transform(centerCoord.x(), centerCoord.y());

        double circleRadious = projection.circleRadiusForParallel(horizonCoord);
        Point2D transformedCircleRadious = transform.deltaTransform(circleRadious, circleRadious);
        double moveFactor = Math.abs(transformedCircleRadious.getY())*2;

        ctx.setLineWidth(2.0);
        ctx.setStroke(Color.RED);
        ctx.setFill(Color.RED);
        ctx.setTextBaseline(VPos.TOP);
        ctx.strokeOval(transformedCenterCoord.getX() - moveFactor/2, transformedCenterCoord.getY() - moveFactor/2, moveFactor, moveFactor);

        String n = "N";
        String e = "E";
        String s = "S";
        String o = "O";
        for(int i = 0; i < 8; ++i){
            HorizontalCoordinates cardinalHorCoord = HorizontalCoordinates.ofDeg(i * 45, -0.5);
            CartesianCoordinates projectedCoord = projection.apply(cardinalHorCoord);
            Point2D transformedCardinalPoint = transform.transform(projectedCoord.x(), projectedCoord.y());

            String cardinalPointName = cardinalHorCoord.azOctantName(n, e, s, o);
            ctx.fillText(cardinalPointName, transformedCardinalPoint.getX(), transformedCardinalPoint.getY());
        }
    }

    /**
     * Generates images of the stars and the asterisms.
     *
     * @param sky The observed sky at the moment of generation of an image of the sky.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void drawStars(ObservedSky sky, Transform transform){
        Iterator<Asterism> asterismsIterator = sky.asterisms().iterator();
        double[] transformedPoints = new double[sky.starPositions().length];
        transform.transform2DPoints(sky.starPositions(), 0, transformedPoints, 0, transformedPoints.length/2);

        ctx.setLineWidth(1.0);
        ctx.setStroke(Color.BLUE);
        ctx.setLineJoin(StrokeLineJoin.ROUND);

        while(asterismsIterator.hasNext()){
            Asterism asterism = asterismsIterator.next(); //take it into account if there are some random lines
            ctx.beginPath();
            for(int starIndex : sky.asterismsIndices(asterism)){
                double x = transformedPoints[2*starIndex];
                double y = transformedPoints[2*starIndex + 1];
                ctx.lineTo(x, y);
                ctx.moveTo(x, y);
            }
            ctx.stroke();
            ctx.closePath();
        }

        for(Star star : sky.stars()) {
            int starIndex = sky.stars().indexOf(star);
            double x = transformedPoints[2*starIndex];
            double y = transformedPoints[2*starIndex + 1];
            Point2D diameterVector = transformedSizeBasedOnMagnitude(star.magnitude(), transform);
            Color color = BlackBodyColor.colorForTemperature(star.colorTemperature());

            ctx.setFill(color);
            ctx.fillOval(x - diameterVector.magnitude()/2,
                    y - diameterVector.magnitude()/2,
                    diameterVector.magnitude(),
                    diameterVector.magnitude());
        }
    }

    /**
     * Generates an image of the sun.
     *
     * @param sky The observed sky at the moment of generation of an image of the sky.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void drawSun(ObservedSky sky, Transform transform){
        CartesianCoordinates sunCoords = sky.sunPosition();
        Point2D transformedCoord = transform.transform(sunCoords.x(), sunCoords.y());

        double diameter = 2*Math.tan(Angle.ofDeg(0.5)/4);
        Point2D diameterVector = transform.deltaTransform(0, diameter);

        ctx.setFill(Color.YELLOW);
        ctx.fillOval(transformedCoord.getX() - (diameterVector.magnitude() + 2)/2,
                transformedCoord.getY() - (diameterVector.magnitude() + 2)/2,
                diameterVector.magnitude()+2,
                diameterVector.magnitude()+2);

        ctx.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.25));
        ctx.fillOval(transformedCoord.getX() - (diameterVector.magnitude() * 2.2)/2,
                transformedCoord.getY() - (diameterVector.magnitude() * 2.2)/2,
                diameterVector.magnitude()*2.2,
                diameterVector.magnitude()*2.2);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(transformedCoord.getX() - diameterVector.magnitude()/2,
                transformedCoord.getY() - diameterVector.magnitude()/2,
                diameterVector.magnitude(),
                diameterVector.magnitude());
    }

    /**
     * Generates images of the planets.
     *
     * @param sky The observed sky at the moment of generation of an image of the sky.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void drawPlanets(ObservedSky sky, Transform transform) {
        double[] transformedPoints = new double[sky.planetPositions().length];
        transform.transform2DPoints(sky.planetPositions(), 0, transformedPoints, 0, transformedPoints.length/2);

        for(Planet planet : sky.planets()){
            int planetIndex = sky.planets().indexOf(planet);
            double x = transformedPoints[2*planetIndex];
            double y = transformedPoints[2*planetIndex + 1];
            Point2D diameterVector = transformedSizeBasedOnMagnitude(planet.magnitude(), transform);

            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillOval(x, y, diameterVector.magnitude(), diameterVector.magnitude());
        }
    }

    /**
     * Generates an image of the sky by calling all of the necessary methods in the correct order.
     *
     * @param sky The observed sky at the moment of generation of an image of the sky.
     * @param projection The projection used to project given coordinates into a two-dimensional plane.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void skyCanvasPaint(ObservedSky sky, StereographicProjection projection, Transform transform){
        clear();
        drawStars(sky, transform);
        drawPlanets(sky, transform);
        drawSun(sky, transform);
        drawMoon(sky, transform);
        drawHorizon(projection, transform);
    }

    private Point2D transformedSizeBasedOnMagnitude(double celestialObjectMagnitude, Transform transform){
        double planetSize = interval.clip(celestialObjectMagnitude);
        double sizeFactor = (99 - 17*planetSize) / 140;
        double diameter = sizeFactor * 2*Math.tan(Angle.ofDeg(0.5)/4);
        return transform.deltaTransform(0, diameter);
    }
}
