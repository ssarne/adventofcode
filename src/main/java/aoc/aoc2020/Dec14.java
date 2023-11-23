package aoc.aoc2020;

import java.util.*;

import static java.util.Arrays.stream;

import static aoc.utils.Utils.*;

public class Dec14 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1(getTestLines()), 165);
        // check(solve2("aoc2020/dec14_test.txt"), 1);
    }

    public static void task1() {
        var result = solve1(getLines());
        System.out.println("Result: " + result);
        check(result, readAnswerAsLong(1));
    }

    public static void task2() {
        var result = solve2(getLines());
        System.out.println("Result: " + result);
        check(result, readAnswerAsLong(2));
    }

    public static long solve1(List<String> input) {

        var mem = new HashMap<Long, Long>();
        String mask = "";

        for (String line : input) {
            if (line.startsWith("mask = ")) {
                mask = line.substring("mask = ".length());
                ensure (mask.length() == 36);
            }
            if (line.startsWith("mem")) {
                long addr = Long.parseLong(line.substring(line.indexOf('[') + 1, line.indexOf(']')));
                long value = Long.parseLong(line.substring(line.indexOf("= ") + "= ".length()));

                BitSet bits = new BitSet(36);
                for (int i = 0; i < 36; i++) {
                    if (mask.charAt(36 - i - 1) != 'X') {
                        bits.set(i, mask.charAt(36 - i - 1) == '1' ? true : false);
                    } else {
                        bits.set(i, (value >> i)  % 2 == 1 ? true : false);
                    }
                }
                mem.put(addr, bits.toLongArray()[0]);
            }
        }

        long sum = 0;
        for (long value : mem.values()) {
            sum += value;
        }
        return sum;
    }

    public static long solve2(List<String> input) {

        HashMap<Long, Long> mem = new HashMap<>();
        String mask = "";
        for (String line : input) {
            if (line.startsWith("mask = ")) {
                mask = line.substring("mask = ".length());
            }
            if (line.startsWith("mem")) {
                long addr = Long.parseLong(line.substring(line.indexOf('[') + 1, line.indexOf(']')));
                long value = Long.parseLong(line.substring(line.indexOf("= ") + "= ".length()));
                addToMem(mem, mask, addr, value, 0);
            }
        }

        long sum = 0;
        for (long value : mem.values()) {
            sum += value;
        }
        return sum;
    }

    private static void addToMem(HashMap<Long, Long> mem, String mask, long addr, long value, int i) {
        if (i == 36) {
            mem.put(addr, value);
            return;
        }

        long [] al = {addr};
        BitSet bits = BitSet.valueOf(al);

        if (mask.charAt(36 - i - 1) == '1') {
            bits.set(i, true);
            addToMem(mem, mask, bits.toLongArray()[0], value, i+1);
        }
        if (mask.charAt(36 - i - 1) == '0') {
            addToMem(mem, mask, addr, value, i+1);
        }
        if (mask.charAt(36 - i - 1) == 'X') {
            bits.set(i, true);
            addToMem(mem, mask, bits.toLongArray()[0], value, i+1);
            bits.set(i, false);
            addToMem(mem, mask, bits.toLongArray()[0], value, i+1);
        }
    }
}
