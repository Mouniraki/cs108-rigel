package ch.epfl.rigel.math;

public final class Angle {
    private Angle(){}

    public final static double TAU = 2*Math.PI;

    private static final double RAD_PER_HR = 24 / TAU;
    private static final double HR_PER_RAD = TAU / 24;
    private static final double RAD_PER_SEC = TAU / 86400;

    //A REVOIR
    public static double normalizePositive(double rad){
        double newRad;

        if(rad < 0) newRad = rad + TAU;

    }

    public static double ofArcsec(double sec){
        return sec * RAD_PER_SEC;
    }

    //A COMPLETER
    public static double ofDMS(int deg, int min, double sec){}

    public static double ofDeg(double deg){
        return Math.toRadians(deg);
    }

    public static double toDeg(double rad){
        return Math.toDegrees(rad);
    }

    public static double ofHr(double hr){
        return hr * RAD_PER_HR;
    }
    public static double toHr(double rad){
        return rad * HR_PER_RAD;
    }
}
