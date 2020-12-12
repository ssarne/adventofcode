package aoc.aoc2020;

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
        int dx = 0, dy = 0;
        int wx = 10, wy = 1;

        for (String line : lines) {
            var instr = line.charAt(0);
            var dist = Integer.parseInt(line.substring(1));

            if (instr == 'F') {
                dx += dist * wx;
                dy += dist * wy;
                continue;
            }

            if (instr == 'L' || instr == 'R') {
                ensure(dist == 90 || dist == 180 || dist == 270);
                int a = (instr == 'L' ? dist : 360 - dist);
                int t = wx;
                switch (a) {
                    case 90:
                        wx = -wy;
                        wy = t;
                        break;
                    case 180:
                        wx = -wx;
                        wy = -wy;
                        break;
                    case 270:
                        wx = wy;
                        wy = -t;
                        break;
                }
                continue;
            }

            switch (instr) {
                case 'N':
                    wy += dist;
                    break;
                case 'S':
                    wy -= dist;
                    break;
                case 'E':
                    wx += dist;
                    break;
                case 'W':
                    wx -= dist;
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        return Math.abs(dx) + Math.abs(dy);
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
