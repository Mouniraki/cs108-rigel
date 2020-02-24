package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

/**
 * A polynomial.
 *
 * @author Mounir Raki  (310287)
 */
public final class Polynomial {
    private double[] coefficientsTable;

    private Polynomial(double coefficientN, double... coefficients){
        coefficientsTable = new double[coefficients.length+1];
        coefficientsTable[0] = coefficientN;
        System.arraycopy(coefficients, 0, coefficientsTable, 1, coefficients.length);
    }

    /**
     * Constructs a polynomial with a first obligatory non-zero coefficient
     * and other coefficients.
     *
     * @param coefficientN : the first coefficient
     * @param coefficients : the other coefficients, stored in an array
     * @throws IllegalArgumentException : if the first coefficient is equal to 0.
     * @return a new polynomial
     */
    public static Polynomial of(double coefficientN, double... coefficients){
        checkArgument(coefficientN != 0);
        return new Polynomial(coefficientN, coefficients);
    }

    /**
     * Evaluates a polynomial
     * @param x
     * @return
     */
    public double at(double x){
        double result = coefficientsTable[0];
        for(int i=1; i<coefficientsTable.length; ++i){
            result = result * x + coefficientsTable[i];
        }
        return result;
    }

    /**
     * Redefines the toString method from Objects to construct the textual representation of a polynomial.
     * @return the textual representation of the polynomial.
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < coefficientsTable.length; ++i){
            if(coefficientsTable[i] != 0) {
                if (coefficientsTable[i] > 0 && i > 0 && i <= coefficientsTable.length - 1) string.append("+");
                if (Math.abs(coefficientsTable[i]) != 1.0) string.append(coefficientsTable[i]);
                else if (coefficientsTable[i] == -1.0) string.append("-");
                if (i < coefficientsTable.length - 2) string.append("x^").append(coefficientsTable.length - i - 1);
                else if (i < coefficientsTable.length - 1) string.append("x");
            }
        }
        return string.toString();
    }

    /**
     * Redefines
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {throw new UnsupportedOperationException();}

    @Override
    public int hashCode() {throw new UnsupportedOperationException();}
}
