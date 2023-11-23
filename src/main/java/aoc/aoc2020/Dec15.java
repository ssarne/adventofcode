package aoc.aoc2020;

import aoc.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.readAnswerAsInt;

public class Dec15 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1(Utils.ints(0, 3, 6), 2020), 436);
        check(solve1(Utils.ints(1, 3, 2), 2020), 1);
        check(solve1(Utils.ints(2, 1, 3), 2020), 10);
        check(solve1(Utils.ints(1, 2, 3), 2020), 27);
        check(solve1(Utils.ints(2, 3, 1), 2020), 78);
        check(solve2(Utils.ints(0, 3, 6), 2020), 436);
        check(solve2(Utils.ints(1, 3, 2), 2020), 1);
        check(solve2(Utils.ints(2, 1, 3), 2020), 10);
        check(solve2(Utils.ints(1, 2, 3), 2020), 27);
        check(solve2(Utils.ints(2, 3, 1), 2020), 78);
    }

    public static void task1() {
        int[] input = Arrays.stream(getLines().get(0).split(",")).mapToInt(Integer::parseInt).toArray();
        var result = solve1(input, 2020);
        check(result, readAnswerAsInt(1));
        result = solve2(input, 2020);
        check(result, readAnswerAsInt(1));
        System.out.println("Result: " + result);
    }

    public static void task2() {
        int[] input = Arrays.stream(getLines().get(0).split(",")).mapToInt(Integer::parseInt).toArray();
        var result = solve2(input, 30000000);
        System.out.println("Result: " + result);
        check(result, readAnswerAsInt(2));

    }

    public static long solve1(int[] input, int end) {

        int[] history = new int[end];
        for (int n = 0; n < input.length; n++) {
            history[n] = input[n];
        }
        for (int n = input.length; n < end; n++) {
            int p1 = findPrev(history, n, history[n - 1]);
            history[n] = (p1 == -1 ? 0 : n - 1 - p1);
        }
        return history[end - 1];
    }

    private static int findPrev(int[] history, int n, int c) {
        for (int i = n - 2; i >= 0; i--) {
            if (history[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public static long solve2(int[] input, int end) {

        HashMap<Integer, Integer> history = new HashMap<>();

        int n = 1;
        for (; n < input.length - 1; n++) {
            history.put(input[n - 1], n);
        }

        Pair l = new Pair(input[n-1], n++);
        Pair c = new Pair(input[n-1], n++);

        for (; n <= end; n++) {
            if (l.v == c.v) {
                int p1 = l.p;          // previous occurrence
                history.put(l.v, l.p);
                l.set(c.v, c.p);
                c.set(n - 1 - p1, n);
            } else if (history.containsKey(c.v)) {
                int p1 = history.get(c.v);
                history.put(l.v, l.p);
                l.set(c.v, c.p);
                c.set(n - 1 - p1, n);
            } else {
                history.put(l.v, l.p);
                l.set(c.v, c.p);
                c.set(0, n);
            }
        }
        return c.v;
    }

    private static class Pair {
        int p;
        int v;

        public Pair(int value, int position) {
            p = position;
            v = value;
        }

        public void set(int value, int position) {
            p = position;
            v = value;
        }
    }
}
