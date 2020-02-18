package ch.epfl.rigel.math;

import java.util.Locale;

public final class RightOpenInterval extends Interval{
    private RightOpenInterval(double low, double high){
        super(low, high);
    }

    public static RightOpenInterval of(double low, double high){
        if(low <= high){
            return new RightOpenInterval(low, high);
        }
        else throw new IllegalArgumentException();
    }

    public static RightOpenInterval symmetric(double size){
        if(size >= 0){
            return new RightOpenInterval(-(size/2), size/2);
        }
        else throw new IllegalArgumentException();
    }

    public double reduce(double v){
        return low()+floorMod(v-low(), high()-low());
    }

    @Override
    public String toString(){
        return String.format(Locale.ROOT, "[%s,%s[", low(), high());
    }

    @Override
    public boolean contains(double v) {
        return (v<high() && v>=low());
    }

    private double floorMod(double a, double b){
        return a - b*Math.floor(a/b);
    }
}
