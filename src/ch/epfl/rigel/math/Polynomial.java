package ch.epfl.rigel.math;

public final class Polynomial {
    private double firstCoeff;
    private double[] otherCoeffs;
    //A DEFINIR
    private Polynomial(double coefficientN, double... coefficients){
        firstCoeff = coefficientN;
        int numberOfCoeffs = coefficients.length;
        otherCoeffs = new double[numberOfCoeffs+1];
        otherCoeffs[0]=coefficientN;
        for(int i=1; i<otherCoeffs.length; ++i){
            otherCoeffs[i] = coefficients[i-1];
        }
        iriuei
    }

    public static Polynomial of(double coefficientN, double... coefficients){
        if(coefficientN > 0 || coefficientN < 0){
            return new Polynomial(coefficientN, coefficients);
        }
        else throw new IllegalArgumentException();
    }

    //A DEFINIR
    public double at(double x){

    }

    //A DEFINIR
    @Override
    public String toString() {
        int length = otherCoeffs.length+1;
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
