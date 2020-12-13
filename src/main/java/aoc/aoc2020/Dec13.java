package aoc.aoc2020;

import static java.util.Arrays.stream;

import static aoc.Utils.*;

import java.util.*;

public class Dec13 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1("aoc2020/dec13_test.txt"), 295);
        check(solve2a("aoc2020/dec13_test.txt"), 1068781);
        check(solve2b("aoc2020/dec13_test.txt"), 1068781);
    }

    public static void task1() {
        var result = solve1("aoc2020/dec13.txt");
        check(result, 3385);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2b("aoc2020/dec13.txt");
        check(result, 600689120448303L);
        System.out.println("Result: " + result);
    }

    public static long solve1(String input) {
        var lines = getLines(input);
        long time = Long.parseLong(lines.get(0));
        String busses = lines.get(1);
        String[] bb = busses.split(",");
        long best = Long.MAX_VALUE;
        long bestId = Long.MAX_VALUE;
        for (String buss : bb) {
            if (buss.equals("x"))
                continue;
            long n = Long.parseLong(buss);
            long d = n;
            while (d < time) {
                d += n;
            }
            if (d < best) {
                best = d;
                bestId = n;
            }
        }
        return (best - time) * bestId;
    }

    public static long solve2a(String input) {
        var lines = getLines(input);
        String busses = lines.get(1);
        String[] bb = busses.split(",");
        long[] bbs = new long[bb.length];
        long[] tts = new long[bb.length];
        for (int i = 0; i < bb.length; i++) {
            bbs[i] = bb[i].equals("x") ? 1 : Long.parseLong(bb[i]);
            tts[i] = bbs[i];
        }
        while (true) {
            tts[0] += bbs[0];
            for (int i = 1; i < tts.length; i++) {
                if (bbs[i] == 1) {
                    tts[i] = tts[i - 1] + 1;
                    continue;
                }
                while (tts[i] < tts[i - 1] + 1) {
                    tts[i] += bbs[i];
                }
                if (tts[i] == tts[i - 1] + 1) {
                    if (i == tts.length - 1) {
                        return tts[0];
                    }
                    continue;
                }
                break;
            }
        }
    }

    public static long solve2b(String input) {

        String[] bb = getLines(input).get(1).split(",");

        List<Long> bbs = new ArrayList<>();
        List<Long> tts = new ArrayList<>();
        for (int i = 0; i < bb.length; i++) {
            if (bb[i].equals("x")) {
                continue;
            }
            bbs.add(Long.parseLong(bb[i]));
            tts.add(-1 * (long) i);
        }

        long[] bbsl = new long[bbs.size()];
        long[] ttsl = new long[bbs.size()];
        for (int i = 0; i < bbsl.length; i++) {
            bbsl[i] = bbs.get(i);
            ttsl[i] = tts.get(i);
        }

        // System.out.println(Arrays.toString(bbsl));
        // System.out.println(Arrays.toString(ttsl));
        return chineseRemainder(bbsl, ttsl);
    }

    public static void testChineseRemainder() {
        long[] l1 = {3, 7, 10};
        long[] l2 = {2, 3, 3};
        check(chineseRemainder(l1, l2), 143);
    }

    // https://medium.com/free-code-camp/how-to-implement-the-chinese-remainder-theorem-in-java-db88a3f1ffe0
    private static long chineseRemainder(long[] num, long[] rem) {

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
