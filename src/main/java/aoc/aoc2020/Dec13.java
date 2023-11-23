package aoc.aoc2020;

import aoc.utils.MathAlgos;

import static aoc.utils.Utils.getTestLines;
import static java.util.Arrays.stream;

import static aoc.utils.Utils.*;

import java.util.*;

public class Dec13 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1(getTestLines()), 295);
        check(solve2a(getTestLines()), 1068781);
        check(solve2b(getTestLines()), 1068781);
    }

    public static void task1() {
        var result = solve1(getLines());
        System.out.println("Result: " + result);
        check(result, readAnswerAsInt(1));
    }

    public static void task2() {
        var result = solve2b(getLines());
        System.out.println("Result: " + result);
        check(result, readAnswerAsLong(2));
    }

    public static long solve1(List<String> input) {

        long time = Long.parseLong(input.get(0));
        String busses = input.get(1);
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

    public static long solve2a(List<String> input) {

        String busses = input.get(1);
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

    public static long solve2b(List<String> input) {

        String[] bb = input.get(1).split(",");

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
        return MathAlgos.chineseRemainder(bbsl, ttsl);
    }
}
