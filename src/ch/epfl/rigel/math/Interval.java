package ch.epfl.rigel.math;

public abstract class Interval {
    private final double low;
    private final double high;
    protected Interval(double l, double h){
        low = l;
        high = h;
    }

    public abstract boolean contains(double v);

    @Override
    public final boolean equals(Object obj){
        throw new UnsupportedOperationException();
    }
    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }

    public double low(){
        return low;
    }
    public double high(){
        return high;
    }
    public double size(){
        return high-low;
    }
}
