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
 * @author Mounir Raki (310287)
 */
public class SkyCanvasPainter {
    private final Canvas canvas;
    private final GraphicsContext ctx;
    private final static ClosedInterval MAGNITUDE_INTERVAL = ClosedInterval.of(-2, 5);
    private final static double RAD_DIAMETER = Angle.ofDeg(0.5);
    private final static double HALF_MOON_PHASE = 0.5;

    /**
     * Initializes the process of generating an image of the sky.
     *
     * @param canvas Canvas on which an image is going to be painted.
     */
    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
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
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform transform){
        float fillAmount = sky.moon().getPhase();

        double moonAngularSize = sky.moon().angularSize();
        double diameter = projection.applyToAngle(moonAngularSize);
        CartesianCoordinates moonCoords = sky.moonPosition();

        Point2D transformedMoonCoord = transform.transform(moonCoords.x(), moonCoords.y());
        Point2D moonDiameterVector = transform.deltaTransform(0, diameter);
        double moonOffset = moonDiameterVector.magnitude()/2;

        ctx.setFill(Color.WHITE);
        ctx.fillOval(transformedMoonCoord.getX() - moonOffset,
                transformedMoonCoord.getY() - moonOffset,
                moonDiameterVector.magnitude(),
                moonDiameterVector.magnitude());
        ctx.setTextBaseline(VPos.BOTTOM);
        ctx.fillText(sky.moon().name(),
                transformedMoonCoord.getX() - moonOffset,
                transformedMoonCoord.getY() - moonOffset);

        applyMoonMask(diameter,
                    fillAmount,
                    moonOffset,
                    moonCoords,
                    moonDiameterVector,
                    transform);

        if(fillAmount > HALF_MOON_PHASE)
            correctMoonMasking(diameter,
                            fillAmount,
                            moonOffset,
                            transformedMoonCoord,
                            moonDiameterVector,
                            transform);
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

        double circleRadius = projection.circleRadiusForParallel(horizonCoord);
        Point2D transformedCircleRadius = transform.deltaTransform(circleRadius, 0);
        double moveFactor = Math.abs(transformedCircleRadius.getX())*2;
        double moveFactorHalved = Math.abs(transformedCircleRadius.getX());

        ctx.setLineWidth(2.0);
        ctx.setStroke(Color.RED);
        ctx.setFill(Color.RED);
        ctx.setTextBaseline(VPos.TOP);
        ctx.strokeOval(transformedCenterCoord.getX() - moveFactorHalved, transformedCenterCoord.getY() - moveFactorHalved,
                moveFactor, moveFactor);

        double textVerticalPosDeg = -0.5;
        int octantValue = 45;
        String north = "N";
        String east = "E";
        String south = "S";
        String west = "O";

        for(int i = 0; i < 8; ++i){
            HorizontalCoordinates cardinalHorCoord = HorizontalCoordinates.ofDeg(i * octantValue, textVerticalPosDeg);
            CartesianCoordinates projectedCoord = projection.apply(cardinalHorCoord);
            Point2D transformedCardinalPoint = transform.transform(projectedCoord.x(), projectedCoord.y());

            String cardinalPointName = cardinalHorCoord.azOctantName(north, east, south, west);
            ctx.fillText(cardinalPointName, transformedCardinalPoint.getX(), transformedCardinalPoint.getY());
        }
    }

    /**
     * Generates images of the stars and the asterisms.
     *
     * @param sky The observed sky at the moment of generation of an image of the sky.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform transform){
        Iterator<Asterism> asterismsIterator = sky.asterisms().iterator();
        double[] transformedPoints = new double[sky.starPositions().length];
        transform.transform2DPoints(sky.starPositions(), 0, transformedPoints, 0, transformedPoints.length/2);

        ctx.setLineWidth(1.0);
        ctx.setStroke(Color.BLUE);
        ctx.setLineJoin(StrokeLineJoin.ROUND);
        ctx.setTextBaseline(VPos.TOP);

        while(asterismsIterator.hasNext()){
            Asterism asterism = asterismsIterator.next();
            ctx.beginPath();
            int asterismIndex = 2*sky.asterismsIndices(asterism).get(0);
            double x = transformedPoints[asterismIndex];
            double y = transformedPoints[asterismIndex + 1];
            ctx.moveTo(x, y);
            boolean wasLastPointInCanvas = canvas.getBoundsInLocal().contains(x, y);

            for(int asterismStarIndex : sky.asterismsIndices(asterism).subList(1, sky.asterismsIndices(asterism).size())){
                int index = 2*asterismStarIndex;
                x = transformedPoints[index];
                y = transformedPoints[index + 1];
                if(canvas.getBoundsInLocal().contains(x, y) || wasLastPointInCanvas){
                    ctx.lineTo(x, y);
                }
                wasLastPointInCanvas = canvas.getBoundsInLocal().contains(x, y);
                ctx.moveTo(x, y);
            }
            ctx.stroke();
            ctx.closePath();
        }

        int starIndex = 0;
        for(Star star : sky.stars()) {
            int baseIndex = 2*starIndex;
            double x = transformedPoints[baseIndex];
            double y = transformedPoints[baseIndex + 1];
            Point2D diameterVector = transformedSizeBasedOnMagnitude(star.magnitude(), projection, transform);
            double halfMagnitude = diameterVector.magnitude()/2;
            Color color = BlackBodyColor.colorForTemperature(star.colorTemperature());

            ctx.setFill(color);
            ctx.fillOval(x - halfMagnitude,
                    y - halfMagnitude,
                    diameterVector.magnitude(),
                    diameterVector.magnitude());
            starIndex += 1;

            if(diameterVector.magnitude() > 1.5){
                ctx.setTextBaseline(VPos.TOP);
                ctx.fillText(star.name(),
                        x - halfMagnitude,
                        y - halfMagnitude);
            }
        }
    }

    /**
     * Generates an image of the sun.
     *
     * @param sky The observed sky at the moment of generation of an image of the sky.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform transform){
        CartesianCoordinates sunCoords = sky.sunPosition();
        Point2D transformedCoord = transform.transform(sunCoords.x(), sunCoords.y());

        double diameter = projection.applyToAngle(RAD_DIAMETER);
        Point2D diameterVector = transform.deltaTransform(0, diameter);

        double magnitudeHalved = diameterVector.magnitude()/2;
        double magnitudeMultiplied = diameterVector.magnitude() * 2.2;
        double magnitudeMultipliedAndHalved = (diameterVector.magnitude() * 2.2)/2;
        double magnitudePlusTwo = diameterVector.magnitude()+2;
        double magnitudePlusTwoHalved = (diameterVector.magnitude() + 2)/2;

        ctx.setFill(Color.YELLOW);
        ctx.fillOval(transformedCoord.getX() - magnitudePlusTwoHalved,
                transformedCoord.getY() - magnitudePlusTwoHalved,
                magnitudePlusTwo,
                magnitudePlusTwo);
        ctx.setTextBaseline(VPos.BOTTOM);
        ctx.fillText(sky.sun().name(),
                transformedCoord.getX() - magnitudeHalved,
                transformedCoord.getY() - magnitudeHalved);

        ctx.setFill(Color.YELLOW.deriveColor(1, 1, 1, 0.25));
        ctx.fillOval(transformedCoord.getX() - magnitudeMultipliedAndHalved,
                transformedCoord.getY() - magnitudeMultipliedAndHalved,
                magnitudeMultiplied,
                magnitudeMultiplied);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(transformedCoord.getX() - magnitudeHalved,
                transformedCoord.getY() - magnitudeHalved,
                diameterVector.magnitude(),
                diameterVector.magnitude());
    }

    /**
     * Generates images of the planets.
     *
     * @param sky The observed sky at the moment of generation of an image of the sky.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform transform) {
        double[] transformedPoints = new double[sky.planetPositions().length];
        transform.transform2DPoints(sky.planetPositions(), 0, transformedPoints, 0, transformedPoints.length/2);

        int planetIndex = 0;
        for(Planet planet : sky.planets()){
            int baseIndex = 2*planetIndex;
            double x = transformedPoints[baseIndex];
            double y = transformedPoints[baseIndex + 1];
            Point2D diameterVector = transformedSizeBasedOnMagnitude(planet.magnitude(), projection, transform);

            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillOval(x, y, diameterVector.magnitude(), diameterVector.magnitude());
            planetIndex += 1;

            ctx.setTextBaseline(VPos.BOTTOM);
            ctx.fillText(planet.name(), x, y);
        }
    }

    /**
     * Generates an image of the sky by calling all of the necessary methods in the correct order.
     *
     * @param sky The observed sky at the moment of generation of an image of the sky.
     * @param projection The projection used to project given coordinates into a two-dimensional plane.
     * @param transform The transformation used to convert the two-dimensional plane into a plane used by the images.
     */
    public void paint(ObservedSky sky, StereographicProjection projection, Transform transform){
        clear();
        drawStars(sky, projection, transform);
        drawPlanets(sky, projection, transform);
        drawSun(sky, projection, transform);
        drawMoon(sky, projection, transform);
        drawHorizon(projection, transform);
    }

    private Point2D transformedSizeBasedOnMagnitude(double celestialObjectMagnitude, StereographicProjection projection, Transform transform){
        double planetSize = MAGNITUDE_INTERVAL.clip(celestialObjectMagnitude);
        double sizeFactor = (99 - 17*planetSize) / 140;
        double diameter = sizeFactor * projection.applyToAngle(RAD_DIAMETER);
        return transform.deltaTransform(0, diameter);
    }

    private void applyMoonMask(double diameter,
                               double fillAmount,
                               double maskHorizontalOffset,
                               CartesianCoordinates moonCoords,
                               Point2D maskHeight,
                               Transform transform){

        double diameterSizeRatio = diameter * fillAmount;
        double rectangleBordersCurvature = (diameter/2) - diameterSizeRatio;
        Point2D maskCurveRadius = transform.deltaTransform(0, rectangleBordersCurvature);

        Point2D transformedMaskCoord;
        Point2D maskWidth;

        if(fillAmount <= HALF_MOON_PHASE) {
            transformedMaskCoord = transform.transform(moonCoords.x() + diameterSizeRatio, moonCoords.y());
            maskWidth = transform.deltaTransform(0, diameter - diameterSizeRatio);
        }
        else {
            transformedMaskCoord = transform.transform(moonCoords.x() + diameter * HALF_MOON_PHASE, moonCoords.y());
            maskWidth = new Point2D(maskHeight.getX(), maskHeight.getY());
        }
        ctx.setFill(Color.BLACK);
        ctx.fillRoundRect(transformedMaskCoord.getX() - maskHorizontalOffset,
                transformedMaskCoord.getY() - maskHorizontalOffset,
                maskWidth.magnitude(),
                maskHeight.magnitude(),
                maskCurveRadius.magnitude(),
                maskHeight.magnitude());
    }

    private void correctMoonMasking(double diameter,
                                    double fillAmount,
                                    double moonCorrectionVerticalOffset,
                                    Point2D transformedMoonCoord,
                                    Point2D moonDiameterVector,
                                    Transform transform){
        double circleThickness = (diameter / HALF_MOON_PHASE) * fillAmount - diameter;

        Point2D moonCorrectionDiameter = transform.deltaTransform(0, circleThickness);
        double moonCorrectionHorizontalOffset = moonCorrectionDiameter.magnitude()/2;

        ctx.setFill(Color.WHITE);
        ctx.fillOval(transformedMoonCoord.getX() - moonCorrectionHorizontalOffset,
                    transformedMoonCoord.getY() - moonCorrectionVerticalOffset,
                    moonCorrectionDiameter.magnitude(),
                    moonDiameterVector.magnitude());
    }
}
