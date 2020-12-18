package aoc.aoc2020;

import static aoc.utils.Utils.*;

public class Dec18 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1("aoc2020/dec18_test.txt"), 71 + 51 + 26 + 437 + 12240 + 13632);
        check(solve2("aoc2020/dec18_test.txt"), 231 + 51 + 46 + 1445 + 669060 + 23340);
    }

    public static void task1() {
        var result = solve1("aoc2020/dec18.txt");
        check(result, 3159145843816L);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("aoc2020/dec18.txt");
        check(result, 55699621957369L);
        System.out.println("Result: " + result);
    }

    public static long solve1(String input) {
        var lines = getLines(input);
        long sum = 0;
        for (String line : lines) {
            sum += eval(Precedence.LR, line);
        }
        return sum;
    }

    public static long solve2(String input) {
        return getLines(input).stream()
                .mapToLong(s -> eval(Precedence.PLUS, s))
                .sum();
    }

    enum Precedence {LR, PLUS};
    private static long eval(Precedence p, String line) {

        if (line.charAt(0) == '#')
            return 0;

        // handle parenthesis expression
        if (line.charAt(0) == '(') {
            int e = findMatchingParenthesis(line, 0);
            long t = eval(p, line.substring(1, e));
            line = (t + " " + line.substring(Math.min(e + 2, line.length()))).trim();
        }

        if (line.indexOf(' ') == -1) { // only one token
            return Long.parseLong(line);
        }

        int i = line.indexOf(' ');
        long t1 = Long.parseLong(line.substring(0, i));
        char op =  line.charAt(i + 1);

        if (p == Precedence.PLUS && op == '*') {
            String rest = line.substring(i + 1 + 1 + 1);
            line = "" + eval(p, rest);
        } else {
            line = line.substring(i + 1 + 1 + 1);
        }

        if (line.charAt(0) == '(') {
            int e = findMatchingParenthesis(line, 0);
            long t2 = eval(p, line.substring(1, e));
            line = t2 + " " + line.substring(Math.min(e + 2, line.length()));
        }

        int s2 = line.indexOf(' ');
        long t2;
        if (s2 == -1) {
            t2 = Long.parseLong(line);
            line = "";
        } else {
            t2 = Long.parseLong(line.substring(0, s2));
            line = line.substring(s2 + 1);
        }

        long t;
        switch (op) {
            case '+':
                t = t1 + t2;
                break;
            case '*':
                t = t1 * t2;
                break;
            default:
                throw new UnsupportedOperationException("" + op);
        }

        if (line.length() != 0) {
            t = eval(p, t + " " + line);
        }
        return t;
    }

    private static int findMatchingParenthesis(String line, int s) {
        int p = 1;
        for (int i = s + 1; i < line.length(); i++) {
            if (line.charAt(i) == '(') {
                p++;
            }
            if (line.charAt(i) == ')') {
                p--;
            }
            if (p == 0) {
                return i;
            }
        }
        return -1;
    }
}
