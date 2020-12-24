package aoc.aoc2020;


import aoc.utils.Utils;

import java.util.*;

import static aoc.utils.Utils.*;

public class Dec23 {


    public static void main(String[] args) throws Exception {
        test();
        //task1();
        //task2();
    }

    public static void test() {
        testAdjustCups();
        //check(solve1("389125467", 10), "92658374");
        //check(solve1("389125467", 100), "67384529");
        check(solve2("389125467", 9, 10), 18);
        //check(solve2("389125467", 9, 100), 42);

        // check(solve2("389125467", 1000000, 10000000), 149245887792L);
    }

    public static void task1() {
        var result = solve1("538914762", 100);
        check(result, "54327968");
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("538914762", 1000000, 10000000);
        check(result, 157410423276L);
        System.out.println("Result: " + result);
    }

    public static String solve1(String input, int moves) {
        // simulateWithLinkedList(input, moves);
        int[] cups = simulateWithArrays(input, input.length(), moves);

        StringBuilder result = new StringBuilder();
        int p = find(cups, 1);
        for (int i = p + 1; i < cups.length; i++) {
            result.append(cups[i]);
        }
        for (int i = 0; i < p; i++) {
            result.append(cups[i]);
        }
        return result.toString();
    }

    public static long solve2(String input, int size, int moves) {

        // With cups ring in array
        // int[] cups = simulateWithArrays(input, size, moves);
        // int pos = find(cups, 1);
        // return 1L * cups[pos + 1] * cups[pos + 2];

        // With index array
        int[] cups = simulateWithIndexArray(input, size, moves);
        int pos = find(cups, 1);
        return 1L * cups[pos + 1] * cups[pos + 2];
    }


    // This has a linked list in an index array, i.e. each cup (as index) stores next cup
    public static int[] simulateWithIndexArray(String input, int size, int moves) {
        var cups = new int[size + 1]; // new LinkedList<Integer>();
        cups[size] = ctoi(input, 0);
        for (int i = 0; i < input.length(); i++) {
            cups[i] = ctoi(input, i);
        }
        for (int i = input.length(); i < size; i++) {
            cups[i-1] = i;
        }

        print(cups);

        int c = size;
        for (int n = 1; n <= moves; n++) {

            int i1 = cups[c];
            int i2 = cups[i1];
            int i3 = cups[i2];
            cups[c] = i3;

            int d = c - 1;
            while (d == 0 || d == i1 || d == i2 || d == i3) {
                d = (d == 0 ? size : d - 1);
            }

            System.out.println(n + ": cups=" + cupsToString(cups, c) + " pick=" + Arrays.toString(ints(i1, i2, i3)) + "  c=" + c + " d=" + d);

            int t = cups[d];
            cups[d] = i1;
            cups[i1] = i2;
            cups[i2] = i3;
            cups[i3] = t;

            c = cups[c];
        }

        return cups;
    }

    // This implementation keeps the ring in an array, each position in the array is the position in the ring
    // Quite inefficient, but can complete - say time ¨10 minutes
    public static int[] simulateWithArrays(String input, int size, int moves) {
        var cups = new int[size]; // new LinkedList<Integer>();
        for (int i = 0; i < input.length(); i++) {
            cups[i] = Integer.parseInt(String.valueOf(input.charAt(i)));
        }
        for (int i = input.length(); i < size; i++) {
            cups[i] = i + 1;
        }
        //printCups(cups);

        int c = 0;
        int cv = cups[c];
        int[] pick = new int[3];
        int print = 12500;
        for (int n = 1; n <= moves; n++) {

            if (print == n) {
                System.out.println("n=" + n);
                print += Math.min(print, 100000);
            }

            int len1 = Math.min(3, size - c - 1);
            int len2 = Math.max(0, c + 3 + 1 - size);

            //    #####c___##### or __##########c_
            if (len1 > 0) System.arraycopy(cups, c + 1, pick, 0, len1);
            if (len2 > 0) System.arraycopy(cups, 0, pick, len1, len2);

            int dv = cv - 1;
            while (dv == 0 || dv == pick[0] || dv == pick[1] || dv == pick[2]) {
                dv = (dv == 0 ? size : dv - 1);
            }

            int d = find(cups, dv);

            // System.out.println(n + ": cups=" + cupsToString(cups) + " pick=" + Arrays.toString(pick) + "  c=" + c + " cv=" + cv + " d=" + d + " dv=" + dv);

            //    ##d##c___##### or __###d######c_ or #####c___##d##
            c = adjustCups(cups, pick, c, d, len1, len2);

            // for (int i = 1; i < cups.length; i++) ensure(find(cups, i) != -1);

            c = (c + 1) % size;
            cv = cups[c];
        }

        return cups;
    }


    private static void testAdjustCups() {
        int[] cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);  // #d#c___##
        check(adjustCups(cups, ints(5, 6, 7), 3, 1, 3, 0), 3 + 3);
        check(cups, ints(1, 2, 5, 6, 7, 3, 4, 8, 9));

        cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);  // _##d##c__
        check(adjustCups(cups, ints(8, 9, 1), 6, 3, 2, 1), 8);
        check(cups, ints(2, 3, 4, 8, 9, 1, 5, 6, 7));

        cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);  // ##c___#d#
        check(adjustCups(cups, ints(4, 5, 6), 2, 8, 3, 0), 2);
        check(cups, ints(1, 2, 3, 7, 8, 9, 4, 5, 6));

        cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);  // ##c___##d
        check(adjustCups(cups, ints(4, 5, 6), 2, 8, 3, 0), 2);
        check(cups, ints(1, 2, 3, 7, 8, 9, 4, 5, 6));

        cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);  // ##c___#d#
        check(adjustCups(cups, ints(4, 5, 6), 2, 7, 3, 0), 2);
        check(cups, ints(1, 2, 3, 7, 8, 4, 5, 6, 9));

        // [3, 8, 9, 1, 2, 5, 4, 6, 7]  pick=[8, 9, 1]  c=0 cv=3 d=4 dv=2
        cups = ints(3, 8, 9, 1, 2, 5, 4, 6, 7); // c___d#####
        check(adjustCups(cups, ints(8, 9, 1), 0, 4, 3, 0), 0);
        check(cups, ints(3, 2, 8, 9, 1, 5, 4, 6, 7));

    }

    // Cases:  ##d##c___#####   __###d######c_   #####c___##d##
    private static int adjustCups(int[] cups, int[] pick, int c, int d, int len1, int len2) {

        try {
            ensure(len1 + len2 == 3);
            int cv = cups[c];
            int dv = cups[d];

            if (d < c && len2 == 0) { // ##d##c___#####
                System.arraycopy(cups, d + 1, cups, d + 1 + 3, c - d);
                System.arraycopy(pick, 0, cups, d + 1, 3);
                c += 3;
            } else if (d < c && len2 > 0) { // _##d##c__   =>   ##d___##c
                System.arraycopy(cups, len2, cups, 0, d);
                int from = d + 1;
                int to = d - len2 + 1 + 3;
                int len = (cups.length) - (d + 1 + len1);
                if (len1 > 0) System.arraycopy(cups, from, cups, to, len);
                c += len1;
                System.arraycopy(pick, 0, cups, d - len2 + 1, 3);
            } else { // ##c___#d#
                int from = c + 1 + 3;
                int to = c + 1;
                int len = d - c - 3;
                System.arraycopy(cups, from, cups, to, len);
                d -= 3;
                System.arraycopy(pick, 0, cups, d + 1, 3);
            }
            ensure(cv == cups[c]);
        } catch (RuntimeException e) {
            System.err.println("Adjust: cups=" + cupsToString(cups) + " pick=" + Arrays.toString(pick) + "  c=" + c + " cv=" + cups[c] + " d=" + d + " dv=" + (d >= 0 ? cups[d] : d));
            throw e;
        }

        return c;
    }

    public static void print(int[] arr) {
        StringBuilder sb = new StringBuilder("cups=[");
        for (int i = 0; i < Math.min(arr.length, 15); i++) {
            sb.append(arr[i]).append(" ");
        }
        if (arr.length > 20) {
            sb.append("...");
            for (int i = arr.length - 5; i < arr.length ; i++) {
                sb.append(" ").append(arr[i]);
            }
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    private static String cupsToString(int[] cups) {
        StringBuilder sb = new StringBuilder("cups=[");
        for (int i = 0; i < Math.min(cups.length, 15); i++) {
            sb.append(cups[i]).append(" ");
        }
        sb.append("...]");
        return sb.toString();
    }

    private static String cupsToString(int[] cups, int current) {
        StringBuilder sb = new StringBuilder("cups=[");
        int c = current;
        for (int i = 0; i == 0 || c != current; i++) {
            sb.append(c).append(" ");
            c = cups[c];
        }
        sb.append("]");
        return sb.toString();
    }


    private static int find(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return -1;
    }


    public static List<Integer> simulateWithLinkedList(String input, int moves) {

        int max = input.length();
        var cups = new LinkedList<Integer>();
        for (int i = 0; i < input.length(); i++) {
            cups.add(Integer.parseInt(String.valueOf(input.charAt(i))));
        }

        System.out.println("Cups=" + cups);

        int current = 0;
        int[] pick = new int[3];
        for (int n = 1; n <= moves; n++) {
            System.out.println("Cups=" + cups + "  pos=" + current + " c=" + cups.get(current));

            int p = 0;
            for (; p < 3 && current + 1 < cups.size(); p++) {
                pick[p] = cups.remove(current + 1);
            }
            for (; p < 3; p++) {
                pick[p] = cups.remove(0);
                current -= 1;
            }

            int destination = -1;
            for (int i = 1; i <= max && destination == -1; i++) {
                int label = cups.get(current) - i;
                if (label <= 0) label += max;

                for (int it = 0; it < cups.size(); it++) {
                    if (cups.get(it) == label) {
                        destination = it;
                        break;
                    }
                }
            }

            System.out.println("Cups=" + cups + "  pick=" + Arrays.toString(pick) + "  pos=" + destination + "  destination=" + cups.get(destination));

            for (int i = 0; i < pick.length; i++) {
                cups.add(destination + 1, pick[pick.length - 1 - i]);
                if (destination < current) current++;
            }

            current = (current + 1) % max;
        }

        System.out.println("Cups=" + cups);
        return cups;
    }
}
