package aoc.utils;

import java.util.ArrayList;
import java.util.List;

import static aoc.utils.Utils.check;

public class MathAlgos {

    public static void main(String [] args) {
        testGCD();
        testFactors();
        testChineseRemainder();
    }

    // The greatest common divisor (GCD) of two integers (numbers),
    // the largest number that divides them both without a remainder
    // Euclidian algorithm
    public static long gcd(long a, long b) {
        if (a == 0) {
            return b;
        }
        return gcd(b % a, a);
    }

    private static void testGCD() {
        check(gcd(10, 15), 5);
        check(gcd(35, 10), 5);
        check(gcd(31, 2), 1);
        check(gcd(31, 3), 1);
        check(gcd(21, 3), 3);
    }

    public static List<Long> factors(long n) {
        List<Long> fs = new ArrayList<>();
        long end = (long) Math.sqrt(n);
        long d = 2;
        while (n != 1 && d <= end) {
            if (n == d) {
                fs.add(d);
                break;
            }
            if (n % d == 0) {
                fs.add(d);
                n = n/d;
            } else {
                d += 1;
            }
        }
        if (n > end) {
            fs.add(n);
        }
        return fs;
    }

    private static void testFactors() {
        check(factors(32).stream().mapToLong(Long::longValue).toArray(), Utils.longs(2, 2, 2, 2, 2));
        check(factors(31).stream().mapToLong(Long::longValue).toArray(), Utils.longs(31));
        check(factors(150).stream().mapToLong(Long::longValue).toArray(), Utils.longs(2, 3, 5, 5));
        check(factors(21).stream().mapToLong(Long::longValue).toArray(), Utils.longs(3, 7));
    }

    private static void testChineseRemainder() {
        long[] l1 = {3, 7, 10};
        long[] l2 = {2, 3, 3};
        check(chineseRemainder(l1, l2), 143);
    }

    // https://medium.com/free-code-camp/how-to-implement-the-chinese-remainder-theorem-in-java-db88a3f1ffe0
    public static long chineseRemainder(long[] num, long[] rem) {

        long product = 1;
        for (int i = 0; i < num.length; i++) {
            product *= num[i];
        }

        long partialProduct[] = new long[num.length];
        long inverse[] = new long[num.length];
        long sum = 0;

        for (int i = 0; i < num.length; i++) {
            partialProduct[i] = product / num[i]; //floor division
            inverse[i] = computeInverse(partialProduct[i], num[i]);
            sum += partialProduct[i] * inverse[i] * rem[i];
        }

        long res = sum % product;
        if (res < 0)
            res += product;

        return res;
    }

    public static long computeInverse(long a, long b) {

        long m = b, t, q;
        long x = 0, y = 1;

        if (b == 1)
            return 0;

        // Apply extended Euclid Algorithm
        while (a > 1) {
            // q is quotient
            q = a / b;
            t = b;

            // now proceed same as Euclid's algorithm
            b = a % b;
            a = t;
            t = x;
            x = y - q * x;
            y = t;
        }

        // Make x1 positive
        if (y < 0)
            y += m;

        return y;
    }
}
