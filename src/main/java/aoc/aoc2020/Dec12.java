package aoc.aoc2020;

import aoc.Point;

import static aoc.Utils.*;

import java.util.*;

public class Dec12 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        testRotate();
        check(solve1("aoc2020/dec12_test.txt"), 25);
        check(solve2("aoc2020/dec12_test.txt"), 286);
    }

    public static void task1() {
        var result = solve1("aoc2020/dec12.txt");
        check(result, 1186);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("aoc2020/dec12.txt");
        check(result, 47806);
        System.out.println("Result: " + result);
    }

    public static int solve1(String input) {
        var lines = getLines(input);
        char direction = 'E';
        int x = 0, y = 0;

        for (String line : lines) {
            char instr = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));

            if (instr == 'F') {
                instr = direction;
            }

            if (instr == 'L' || instr == 'R') {
                direction = rotate(direction, value, instr);
                instr = direction;
                value = 0;
            }

            switch (instr) {
                case 'N':
                    y += value;
                    break;
                case 'S':
                    y -= value;
                    break;
                case 'E':
                    x += value;
                    break;
                case 'W':
                    x -= value;
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
        return Math.abs(x) + Math.abs(y);
    }

    public static int solve2(String input) {

        var lines = getLines(input);
        var p = new Point(0, 0);
        var wp = new Point(10, 1);

        for (String line : lines) {
            var instr = line.charAt(0);
            var value = Integer.parseInt(line.substring(1));

            if (instr == 'F') {
                p = p.add(wp.scale(value));
                continue;
            }

            if (instr == 'L' || instr == 'R') {
                wp = wp.rotate(instr, value);
                continue;
            }

            switch (instr) {
                case 'N':
                    wp = wp.dy(value);
                    break;
                case 'S':
                    wp = wp.dy(-value);
                    break;
                case 'E':
                    wp = wp.dx(value);
                    break;
                case 'W':
                    wp = wp.dx(-value);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        return p.manhattan();
    }

    private static char rotate(char direction, int angle, char rotation) {
        ensure(angle == 90 || angle == 180 || angle == 270);
        ensure("ENWS".contains(Character.toString(direction)));
        ensure("LR".contains(Character.toString(rotation)));
        int p = "ENWS".indexOf(direction);
        int d = (rotation == 'L' ? 1 : -1) * angle / 90;
        int dd = (4 + p + d) % 4;
        return "ENWS".charAt(dd);
    }

    private static void testRotate() {
        check(rotate('E', 90, 'L'), 'N');
        check(rotate('E', 90, 'R'), 'S');
        check(rotate('E', 180, 'L'), 'W');
        check(rotate('E', 270, 'L'), 'S');
        check(rotate('N', 90, 'L'), 'W');
        check(rotate('W', 90, 'L'), 'S');
        check(rotate('S', 90, 'L'), 'E');
    }
}
