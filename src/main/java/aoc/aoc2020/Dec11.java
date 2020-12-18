package aoc.aoc2020;

import java.util.*;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;

public class Dec11 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve("aoc2020/dec11_test1.txt", 4, false), 37);
        check(solve("aoc2020/dec11_test1.txt", 5, true), 26);
    }

    public static void task1() {
        long result = solve("aoc2020/dec11.txt", 4, false);
        check(result, 2334);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        long result = solve("aoc2020/dec11.txt", 5, true);
        check(result, 2100);
        System.out.println("Result: " + result);
    }

    public static int solve(String input, int limit, boolean look) {
        Floor floor = new Floor(getLines(input));
        for (int i = 0; true; i++) {
            // floor.print(i);
            if (!floor.evolve(limit, look)) {
                return floor.count();
            }
        }
    }

    private static class Floor {

        int width, height;
        char[][] current;
        char[][] next;

        public Floor(List<String> lines) {
            width = lines.get(0).length();
            height = lines.size();
            current = new char[width][height];
            next = new char[width][height];
            for (int y = 0; y < height; y++) {
                String line = lines.get(y);
                for (int x = 0; x < width; x++) {
                    current[x][y] = line.charAt(x);
                }
            }
        }

        public void print(int i) {
            System.out.println("=====================  " + i);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    System.out.print(current[x][y]);
                }
                System.out.println();
            }
        }

        public boolean evolve(int limit, boolean look) {
            boolean changed = false;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (current[x][y] == 'L' && neigbours(x, y, look) == 0) {
                        next[x][y] = '#';
                        changed = true;
                    } else if (current[x][y] == '#' && neigbours(x, y, look) >= limit) {
                        next[x][y] = 'L';
                        changed = true;
                    } else {
                        next[x][y] = current[x][y];
                    }
                }
            }
            char[][] tmp = current;
            current = next;
            next = tmp;
            return changed;
        }

        private int neigbours(int x, int y, boolean look) {
            int sum = 0;
            for (int dx = -1; dx < 2; dx++) {
                for (int dy = -1; dy < 2; dy++) {
                    if (!(dx == 0 && dy == 0) && occupied(x + dx, y + dy, dx, dy, look)) {
                        sum++;
                    }
                }
            }
            return sum;
        }

        private boolean occupied(int x, int y, int dx, int dy, boolean look) {
            while (x >= 0 && x < width && y >= 0 && y < height) {
                if (current[x][y] == '#') {
                    return true;
                }
                if (current[x][y] == 'L') {
                    return false;
                }
                if (!look) {
                    break;
                }
                x += dx;
                y += dy;
            }
            return false;
        }

        public int count() {
            int sum = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (current[x][y] == '#') {
                        sum++;
                    }
                }
            }
            return sum;
        }
    }
}
