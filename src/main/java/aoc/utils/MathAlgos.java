package aoc.utils;

import static aoc.utils.Utils.check;

public class MathAlgos {

    public static void testChineseRemainder() {
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
