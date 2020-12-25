package aoc.aoc2020;


import java.util.*;

import static aoc.utils.Utils.*;

public class Dec23 {

    enum Implementation {
        LIST, // simple linked list implementation, position is place in ring
        ARRAY, // array implementation, position is place
        IA_LINKED_LIST // index array for linked list, position contains neighbour
    };


    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        testAdjustCups();
        check(solve1("389125467", 10, Implementation.LIST), "92658374");
        check(solve1("389125467", 100, Implementation.LIST), "67384529");
        check(solve1("389125467", 10, Implementation.ARRAY), "92658374");
        check(solve1("389125467", 100, Implementation.ARRAY), "67384529");
        check(solve1("389125467", 10, Implementation.IA_LINKED_LIST), "92658374");
        check(solve1("389125467", 100, Implementation.IA_LINKED_LIST), "67384529");
        check(solve2("389125467", 9, 10, Implementation.ARRAY), 18);
        check(solve2("389125467", 9, 100, Implementation.ARRAY), 42);
        check(solve2("389125467", 1000000, 100, Implementation.ARRAY), 1L * 3 * 4);
        check(solve2("389125467", 9, 10, Implementation.IA_LINKED_LIST), 18);
        check(solve2("389125467", 9, 100, Implementation.IA_LINKED_LIST), 42);
        check(solve2("389125467", 1000000, 100, Implementation.IA_LINKED_LIST), 1L * 3 * 4);
        check(solve2("389125467", 1000000, 10000000, Implementation.IA_LINKED_LIST), 149245887792L);
    }

    public static void task1() {
        var result = solve1("538914762", 100, Implementation.IA_LINKED_LIST);
        check(result, "54327968");
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("538914762", 1000000, 10000000, Implementation.IA_LINKED_LIST);
        check(result, 157410423276L);
        System.out.println("Result: " + result);
    }

    public static String solve1(String input, int moves, Implementation impl) {

        switch(impl) {
            case LIST: {
                var cups = simulateWithLinkedList(input, moves);
                StringBuilder result = new StringBuilder();
                Iterator<Integer> i1 = cups.iterator();
                for (int i = i1.next(); i != 1; i = i1.next());
                while (i1.hasNext()) result.append(i1.next());
                Iterator<Integer> i2 = cups.iterator();
                for (int i = i2.next(); i != 1; i = i2.next()) result.append(i);
                return result.toString();
            }
            case ARRAY: {
                var cups = simulateWithArrays(input, input.length(), moves);
                StringBuilder result = new StringBuilder();
                int p = find(cups, 1);
                for (int i = p + 1; i < cups.length; i++) result.append(cups[i]);
                for (int i = 0; i < p; i++) result.append(cups[i]);
                return result.toString();
            }
            case IA_LINKED_LIST: {
                var cups = simulateWithIndexArray(input, input.length(), moves);
                StringBuilder result = new StringBuilder();
                for (int i = cups[1]; i != 1; i = cups[i]) result.append(i);
                return result.toString();
            }
        }
        return "";
    }

    public static long solve2(String input, int size, int moves, Implementation impl) {

        switch(impl) {
            case LIST: {
                throw new UnsupportedOperationException("n/a");
            }
            case ARRAY: {
                int [] cups = simulateWithArrays(input, size, moves);
                int pos = find(cups, 1);
                return 1L * cups[(pos + 1) % size] * cups[(pos + 2) % size];
            }
            case IA_LINKED_LIST: {
                int[] cups = simulateWithIndexArray(input, size, moves);
                return 1L * cups[1] * cups[cups[1]];
            }
        }
        return -1;
    }


    // This has a linked list in an index array, i.e. each cup (as index) stores next cup
    public static int[] simulateWithIndexArray(String input, int size, int moves) {

        // print("input: " + input);
        var cups = new int[size + 1];
        int p = ctoi(input, input.length() - 1);
        for (int i = 0; i < input.length(); i++) {
            int n = ctoi(input, i);
            cups[p] = n;
            p = n;
        }

        for (int n = input.length() + 1; n <= size; n++) {
            cups[p] = n;
            p = n;
        }

        int c = ctoi(input, 0);
        cups[p] = c;

        // print("cups array: " + prettyToString(cups));
        // print("cups:       " + iaCupsToString(cups, c, 15));

        for (int n = 1; n <= moves; n++) {

            ensure(c != 0);

            int i1 = cups[c];
            int i2 = cups[i1];
            int i3 = cups[i2];
            cups[c] = cups[i3];

            int d = c - 1;
            while (d == 0 || d == i1 || d == i2 || d == i3) {
                d = (d == 0 ? size : d - 1);
            }

            // System.out.println("Move " + n + ": cups=" + iaCupsToString(cups, c)
            // + " pick=" + Arrays.toString(ints(i1, i2, i3))
            // + "  c=" + c + " d=" + d
            // + "  array=" + prettyToString(cups));

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

            c = adjustCups(cups, pick, c, d, len1, len2);

            // for (int i = 1; i < cups.length; i++) ensure(find(cups, i) != -1);

            c = (c + 1) % size;
            cv = cups[c];
        }

        return cups;
    }


    private static void testAdjustCups() {
        // #d#c___##
        int[] cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);
        check(adjustCups(cups, ints(5, 6, 7), 3, 1, 3, 0), 3 + 3);
        check(cups, ints(1, 2, 5, 6, 7, 3, 4, 8, 9));

        // _##d##c__
        cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);
        check(adjustCups(cups, ints(8, 9, 1), 6, 3, 2, 1), 8);
        check(cups, ints(2, 3, 4, 8, 9, 1, 5, 6, 7));

        // ##c___#d#
        cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);
        check(adjustCups(cups, ints(4, 5, 6), 2, 8, 3, 0), 2);
        check(cups, ints(1, 2, 3, 7, 8, 9, 4, 5, 6));

        // ##c___##d
        cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);
        check(adjustCups(cups, ints(4, 5, 6), 2, 8, 3, 0), 2);
        check(cups, ints(1, 2, 3, 7, 8, 9, 4, 5, 6));

        // ##c___#d#
        cups = ints(1, 2, 3, 4, 5, 6, 7, 8, 9);
        check(adjustCups(cups, ints(4, 5, 6), 2, 7, 3, 0), 2);
        check(cups, ints(1, 2, 3, 7, 8, 4, 5, 6, 9));

        // [3, 8, 9, 1, 2, 5, 4, 6, 7]  pick=[8, 9, 1]  c=0 cv=3 d=4 dv=2   c___d#####
        cups = ints(3, 8, 9, 1, 2, 5, 4, 6, 7);
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
                if (len1 > 0) {
                    System.arraycopy(cups, d + 1, cups, d - len2 + 1 + 3, (cups.length) - (d + 1 + len1));
                    c += len1;
                }
                System.arraycopy(pick, 0, cups, d - len2 + 1, 3);
            } else { // ##c___#d#
                ensure(c < d);
                System.arraycopy(cups, c + 1 + 3, cups, c + 1, d - (c + 3));
                System.arraycopy(pick, 0, cups, d + 1 - 3, 3);
            }
            ensure(cv == cups[c]);
        } catch (RuntimeException e) {
            System.err.println("Adjust: cups=" + prettyToString(cups) + " pick=" + Arrays.toString(pick) + "  c=" + c + " cv=" + cups[c] + " d=" + d + " dv=" + (d >= 0 ? cups[d] : d));
            throw e;
        }

        return c;
    }

    private static String iaCupsToString(int[] cups, int current, int len) {
        StringBuilder sb = new StringBuilder("[ (" + current + ")");
        int c = cups[current];
        for (int i = 0; c != current && i < len; c = cups[c], i++) {
            sb.append("  ").append(c);
        }
        sb.append("]");
        return sb.toString();
    }

    public static List<Integer> simulateWithLinkedList(String input, int moves) {

        int max = input.length();
        var cups = new LinkedList<Integer>();
        for (int i = 0; i < input.length(); i++) {
            cups.add(Integer.parseInt(String.valueOf(input.charAt(i))));
        }

        // System.out.println("Cups=" + cups);

        int current = 0;
        int[] pick = new int[3];
        for (int n = 1; n <= moves; n++) {
            // System.out.println("Cups=" + cups + "  pos=" + current + " c=" + cups.get(current));

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

            // System.out.println("Cups=" + cups + "  pick=" + Arrays.toString(pick) + "  pos=" + destination + "  destination=" + cups.get(destination));

            for (int i = 0; i < pick.length; i++) {
                cups.add(destination + 1, pick[pick.length - 1 - i]);
                if (destination < current) current++;
            }

            current = (current + 1) % max;
        }

        // System.out.println("Cups=" + cups);
        return cups;
    }
}
