package ch.epfl.rigel.math;

import java.util.Locale;

public final class ClosedInterval extends Interval{

    private ClosedInterval(double low, double high){
        super(low, high);
    }

    public static ClosedInterval of(double low, double high){
        if(low <= high){
            return new ClosedInterval(low, high);
        }
        else throw new IllegalArgumentException();
    }

    public static ClosedInterval symmetric(double size){
        if(size >= 0){
            return new ClosedInterval(-(size/2), size/2);
        }
        else throw new IllegalArgumentException();
    }

    //VOIR POUR LE TYPE
    double clip(double v){
        if(v<=low()) return low();
        else if (v>=high()) return high();
        else return v;
    }

    @Override
    public String toString(){
        return String.format(Locale.ROOT, "[%s,%s]", low(), high());
    }

    @Override
    public boolean contains(double v) {
        return (v<=high() && v>=low());
    }

}
