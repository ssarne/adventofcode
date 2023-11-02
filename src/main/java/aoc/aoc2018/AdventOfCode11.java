package aoc.aoc2018;

import static aoc.utils.Utils.check;

import aoc.utils.Utils;

import java.io.IOException;

public class AdventOfCode11 {

    public static void main(String[] args) throws IOException {
        Utils.check(power(init(57), 122, 79), -5);
        Utils.check(power(init(39), 217, 196), 0);
        Utils.check(power(init(71), 101, 153), 4);
        System.out.println(task1());
        System.out.println(task2());
    }

    public static int[][] init(int seed) {
        int[][] cells = new int[301][301];
        for (int x = 1; x <= 300; x++) {
            for (int y = 1; y <= 300; y++) {
                cells[x][y] = power(x, y, seed);
            }
        }
        return cells;
    }

    public static String task1() throws IOException {

        var cells = init(1133);

        int max = 0;
        int maxX = 0;
        int maxY = 0;

        for (int x = 1; x <= 288; x++) {
            for (int y = 1; y <= 288; y++) {
                int sum = powersquare(cells, x, y, 3);
                if (sum > max) {
                    max = sum;
                    maxX = x;
                    maxY = y;
                }
            }
        }

        return maxX + "," + maxY + ": " + max;
    }

    public static String task2() throws IOException {

        var cells = init(1133);

        int max = 0;
        int maxX = 0;
        int maxY = 0;
        int maxD = 0;

        for (int x = 1; x <= 288; x++) {
            for (int y = 1; y <= 288; y++) {
                int delta = 300 - ((int) Math.max(x, y));
                for (int d = 1; d < delta; d++) {

                    int sum = powersquare(cells, x, y, d);
                    if (sum > max) {
                        max = sum;
                        maxX = x;
                        maxY = y;
                        maxD = d;
                    }
                }
            }
        }

        return maxX + "," + maxY + "," + maxD + ": " + max;
    }

    private static int powersquare(int[][] cells, int x, int y, int d) {
        int sum = 0;
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                sum += power(cells, x + i, y + j);
            }
        }
        return sum;
    }

    private static int power(int[][] cells, int x, int y) {
        return cells[x][y];
    }

    private static int power(int x, int y, int seed) {
        int id = x + 10;
        int p1 = id * y;
        int p2 = p1 + seed;
        int p3 = p2 * id;
        int d1 = p3 / 100;
        int d2 = d1 % 10;
        return d2 - 5;
    }
}
