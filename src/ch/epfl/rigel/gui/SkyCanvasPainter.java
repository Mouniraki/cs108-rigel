package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.*;
import ch.epfl.rigel.math.Angle;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

import java.util.List;

public class SkyCanvasPainter {
    final private Canvas canvas;
    private GraphicsContext ctx;

    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
    }

    public void clear(){
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform transform){
        EquatorialCoordinates moonPosition = sky.moon().equatorialPos();
        EquatorialToHorizontalConversion conversion = new EquatorialToHorizontalConversion(sky.observationInstant(), sky.observationPos());
        HorizontalCoordinates coordinates = conversion.apply(moonPosition);

        double moonSize = sky.moon().angularSize();
        double diameter = 2 * Math.tan(moonSize / 4);

        CartesianCoordinates projCoord = projection.apply(coordinates);
        Point2D point2D = transform.transform(projCoord.x(), projCoord.y());

        ctx.setFill(Color.WHITE);
        ctx.fillOval(point2D.getX(), point2D.getY(), diameter, diameter);
    }

    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform transform){
        List<Star> stars = sky.stars();
        EquatorialToHorizontalConversion conversion = new EquatorialToHorizontalConversion(sky.observationInstant(), sky.observationPos());
        BlackBodyColor.readFile();

        for(Star star : stars){
            HorizontalCoordinates coordinates = conversion.apply(star.equatorialPos());
            CartesianCoordinates projCoord = projection.apply(coordinates);

            Point2D point2D = transform.transform(projCoord.x(), projCoord.y());
            double starSize = star.magnitude();

            if(starSize < -2){
                starSize = -2;
            }
            if(starSize > 5){
                starSize = 5;
            }

            double sizeFactor = (99 - (17 * starSize)) / 140;
            double diameter = sizeFactor * 2 * Math.tan( (Angle.ofDeg(0.5)) / 4 );

            Color color = BlackBodyColor.colorForTemperature(star.colorTemperature());

            ctx.setFill(color);
            ctx.fillOval(point2D.getX(), point2D.getY(), diameter*transform.getTx(), diameter*transform.getTy()); //not sure if correct diameter, but very similar to Professor's sky.png file
        }
    }

    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform transform){
        Sun sun = sky.sun();
        EclipticCoordinates sunPos = sun.eclipticPos();
        EclipticToEquatorialConversion eclToEqu = new EclipticToEquatorialConversion(sky.observationInstant());
        EquatorialToHorizontalConversion equToHor = new EquatorialToHorizontalConversion(sky.observationInstant(), sky.observationPos());

        HorizontalCoordinates coordinates = equToHor.apply(eclToEqu.apply(sunPos));

        CartesianCoordinates projCoord = projection.apply(coordinates);
        Point2D point2D = transform.transform(projCoord.x(), projCoord.y());

//        double sunSize = sun.angularSize();
        double diameter = 2 * Math.tan( (Angle.ofDeg(0.5)) / 4 );
        ctx.setFill(Color.YELLOW);

        if(diameter * 2.2 > diameter + 2){
            ctx.setGlobalAlpha(0.25);
            ctx.fillOval(point2D.getX(), point2D.getY(), diameter*transform.getTx()*2.2, diameter*transform.getTy()*2.2 );
            ctx.setGlobalAlpha(1.0);

            ctx.fillOval(point2D.getX(), point2D.getY(), diameter*transform.getTx()+2, diameter*transform.getTy()+2 );
        }
        else{
            ctx.fillOval(point2D.getX(), point2D.getY(), diameter*transform.getTx()+2, diameter*transform.getTy()+2 );

            ctx.setGlobalAlpha(0.25);
            ctx.fillOval(point2D.getX(), point2D.getY(), diameter*transform.getTx()*2.2, diameter*transform.getTy()*2.2 );
            ctx.setGlobalAlpha(1.0);
        }

        ctx.setFill(Color.WHITE);
        ctx.fillOval(point2D.getX(), point2D.getY(), diameter*transform.getTx(), diameter*transform.getTy() );
    }

    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform transform) {
        List<Planet> planets = sky.planets();
        EquatorialToHorizontalConversion conversion = new EquatorialToHorizontalConversion(sky.observationInstant(), sky.observationPos());
        BlackBodyColor.readFile();

        for(Planet planet : planets){
            HorizontalCoordinates coordinates = conversion.apply(planet.equatorialPos());
            CartesianCoordinates projCoord = projection.apply(coordinates);

            Point2D point2D = transform.transform(projCoord.x(), projCoord.y());
            double starSize = planet.magnitude();

            if(starSize < -2){
                starSize = -2;
            }
            if(starSize > 5){
                starSize = 5;
            }

            double sizeFactor = (99 - (17 * starSize)) / 140;
            double diameter = sizeFactor * 2 * Math.tan( (Angle.ofDeg(0.5)) / 4 );

            ctx.setFill(Color.LIGHTGRAY);
            ctx.fillOval(point2D.getX(), point2D.getY(), diameter*transform.getTx(), diameter*transform.getTy()); //not sure if correct diameter, but very similar to Professor's sky.png file
        }

    }

    public void drawSkyCanvas(ObservedSky sky, StereographicProjection projection, Transform transform){
        clear();
        drawStars(sky, projection, transform);
        drawSun(sky, projection, transform);
        drawMoon(sky, projection, transform);//and so on, order, 1étoiles, 2planètes, 3Soleil, 4Lune.

    }

}
