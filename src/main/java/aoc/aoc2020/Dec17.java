package aoc.aoc2020;

import java.util.*;

import static aoc.Utils.*;

public class Dec17 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1("aoc2020/dec17_test.txt"), 112);
        check(solve2("aoc2020/dec17_test.txt"), 848);
    }

    public static void task1() {
        var result = solve1("aoc2020/dec17.txt");
        check(result, 1);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("aoc2020/dec17.txt");
        check(result, 1);
        System.out.println("Result: " + result);
    }

    public static int solve1(String input) {
        var lines = getLines(input);
        var grid = new Grid();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                grid.put(x, y, 0, 0, line.charAt(x));
            }
        }

        for (int i = 0; i < 6; i++) {
            // grid.print(i);
            Grid next = new Grid();
            for (int w = grid.minw - 1; w <= grid.maxw + 1; w++) {
                for (int z = grid.minz - 1; z <= grid.maxz + 1; z++) {
                    for (int y = grid.miny - 1; y <= grid.maxy + 1; y++) {
                        for (int x = grid.minx - 1; x <= grid.maxx + 1; x++) {
                            boolean b = grid.is(x, y, z, w, '#');
                            boolean d = grid.is(x, y, z, w, '.');
                            int n = grid.neighbours(x, y, z, w, '#');
                            if (b && n == 2 || b && n == 3 || n == 3) {
                                next.put(x, y, z, w, '#');
                            } else if (b || d) {
                                next.put(x, y, z, w, '.');
                            }
                        }
                    }
                }
            }
            grid = next;
        }

        return grid.count('#');
    }

    public static int solve2(String input) {
        return -1;
    }

    private static class Grid {
        HashMap<String, Character> grid = new HashMap<>();
        int minx = 0, miny = 0, minz = 0, minw = 0, maxx = 0, maxy = 0, maxz = 0, maxw = 0;

        public Grid() {
        }

        private String point(int x, int y, int z, int w) {
            return x + "," + y + "," + z + "," + w;
        }

        public void put(int x, int y, int z, int w, char c) {
            minx = Math.min(x, minx);
            miny = Math.min(y, miny);
            minz = Math.min(z, minz);
            minw = Math.min(w, minw);
            maxx = Math.max(x, maxx);
            maxy = Math.max(y, maxy);
            maxz = Math.max(z, maxz);
            maxw = Math.max(w, maxw);
            grid.put(point(x, y, z, w), c);
        }

        public boolean is(int x, int y, int z, int w, char c) {
            Character v = grid.get(point(x, y, z, w));
            return v != null && v.charValue() == c;
        }

        public Character get(int x, int y, int z, int w) {
            return grid.get(point(x, y, z, w));
        }

        public int neighbours(int px, int py, int pz, int pw, char v) {
            int n = 0;
            int center = 0;
            int neighbour = 0;

            for (int w = pw - 1; w <= pw + 1; w++) {
                for (int z = pz - 1; z <= pz + 1; z++) {
                    for (int y = py - 1; y <= py + 1; y++) {
                        for (int x = px - 1; x <= px + 1; x++) {
                            if (!(px == x && py == y && pz == z && pw == w)) {
                                neighbour++;
                                Character c = grid.get(point(x, y, z, w));
                                if (c != null && c.charValue() == v) {
                                    n++;
                                }
                            } else {
                                center++;
                            }
                        }
                    }
                }
            }

            if (center != 1 || neighbour != 80) {
                ensure(center == 1);
                ensure(neighbour + center == 81);
            }
            return n;
        }

        public int count(char c) {
            int n = 0;
            for (char v : grid.values()) {
                if (c == v) {
                    n++;
                }
            }
            return n;
        }

        public void print(int i) {
            System.out.println("==========" + i);
            for (int w = minw; w <= maxw; w++) {
                System.out.println("w=" + w);
                for (int z = minz; z <= maxz; z++) {
                    System.out.println("z=" + z);
                    for (int y = miny; y <= maxy; y++) {
                        for (int x = minx; x <= maxx; x++) {
                            Character v = grid.get(point(x, y, z, w));
                            if (v != null) {
                                System.out.print(v);
                            }
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }
    }
}
