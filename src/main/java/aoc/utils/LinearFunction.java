package aoc.utils;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;

/** f(x) = k*x + m */
public class LinearFunction {

  /** The identity function: f(x)=x */
  public static final LinearFunction ID = new LinearFunction(ONE, ZERO);

  private BigInteger k, m;

  /**
   * Create linear function with given constants for f(x) = k*x + m
   * @param k The multiplier
   * @param m The constant
   */
  public LinearFunction(BigInteger k, BigInteger m) {
    this.k = k;
    this.m = m;
  }

  /** Execute function with given value */
  public BigInteger apply(BigInteger x) {
    return x.multiply(k).add(m);
  }

  /** Aggregate two functions, this f(x) and given g(x) to create a new function h(x)=g(f(x)) */
  public LinearFunction aggregate(LinearFunction g) {
    // Let f(x)=k*x+m and g(x)=j*x+n, then h(x) = g(f(x)) = Ax+B = j*(k*x+m)+n = j*k*x + (j*m+n) =>
    // A=j*k, B=j*m+n
    return new LinearFunction(g.k.multiply(this.k), g.k.multiply(this.m).add(g.m));
  }

  /** Aggregate two functions, this f(x) and given g(x) to create a new function h(x)=g(f(x)) */
  public LinearFunction aggregate(LinearFunction g, BigInteger modulo) {
    // Let f(x)=k*x+m and g(x)=j*x+n, then h(x) = g(f(x)) = Ax+B = j*(k*x+m)+n = j*k*x + (j*m+n) =>
    // A=j*k, B=j*m+n
    return new LinearFunction(g.k.multiply(this.k).mod(modulo), g.k.multiply(this.m).add(g.m).mod(modulo));
  }

  /** Aggregate repetition of this function f(x) into a new function h(x)=f(f(...)) reps times */
  public LinearFunction repeat(BigInteger reps, BigInteger modulo) {
    if (reps.equals(ZERO)) {
      return ID;
    } else if (reps.mod(TWO).equals(ZERO)) {
      return this.aggregate(this, modulo).repeat(reps.divide(TWO), modulo);
    } else {
      return this.aggregate(this.repeat(reps.subtract(ONE), modulo), modulo);
    }
  }
}
