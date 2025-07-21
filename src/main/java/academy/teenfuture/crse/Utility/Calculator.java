package academy.teenfuture.crse.utility;


/**
 * Do a Calculator Demo
 *
 * Todo: adding functions for minus, multiple and division
 */
public class Calculator {

    /**
     * Do Addition
     * @param a 1st float to be added
     * @param b 2nd float to be added
     * @return the Sum value
     */
    public float add(float a, float b) {
      return a + b;
    }

    public float minus(float a, float b) {
      return a - b;
    }

    public float multiple(float a, float b) {
      return a * b;
    }

    public float division(float a, float b) {
      if (b == 0) {
        throw new IllegalArgumentException("Division by zero is not allowed.");
      }
      return a / b;
    }

    public double dAdd(double a, double b) {
      return a + b;
    }

    public double dMinus(double a, double b) {
      return a - b;
    }

    public double dExponent(double base, double exponent) {
      return Math.pow(base, exponent);
    }



  }