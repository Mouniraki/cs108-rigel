package ch.epfl.rigel.coordinates;

import java.util.function.Function;

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    private HorizontalCoordinates center;
    private double sinLambda1;
    private double cosLambda1;

    public StereographicProjection(HorizontalCoordinates center){
        this.center = center;
//        double d = 1 / (1 + Math.sin()                        );
        this.sinLambda1 = Math.sin(center.az());
        this.cosLambda1 = Math.cos(center.az());
//        center.az();
//        center.alt();
    }
//TODO
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates horizontalCoordinates) {
        return null;
    }

    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
//        double centerX = 0;
        double centerY = ( this.cosLambda1 / ( Math.sin(hor.lat()) + this.sinLambda1 ) );
        return CartesianCoordinates.of(0, centerY);
    }

    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        return ( Math.cos(parallel.lat()) / ( ( Math.sin(parallel.lat()) + this.sinLambda1 ) )  );
    }

    public double applyToAngle(double rad){
        return (2 * Math.tan(rad / 4));
    }



}
