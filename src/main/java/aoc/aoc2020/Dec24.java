package aoc.aoc2020;

import java.util.*;

import static aoc.utils.Utils.*;

/*********************************************************************************\
 NE                             E                                       SE

      0,4         1,3          2,2           3,1           4,0
            0,3          1,2          2,1           3,0
                  0,2          1,1           2,0
                         0,1          1,0
                               0,0

 NW                             W                                       SW
\*********************************************************************************/

public class Dec24 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1("aoc2020/dec24_test.txt"), 10);
        check(solve2("aoc2020/dec24_test.txt", 100), 2208);
    }

    public static void task1() {
        var result = solve1("aoc2020/dec24.txt");
        check(result, 330);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("aoc2020/dec24.txt", 100);
        check(result, 3711);
        System.out.println("Result: " + result);
    }

    public static int solve1(String input) {
        HashMap<String, Character> tiles = parseTiles(input);
        return count(tiles, 'B');
    }

    private static int count(HashMap<String, Character> tiles, char color) {
        int count = 0;
        for (char c : tiles.values()) {
            if (c == color) {
                count++;
            }
        }
        return count;
    }


    public static int solve2(String input, int n) {

        var tiles = parseTiles(input);
        for (int i = 1; i <= n; i++) {
            tiles = evolve(tiles);
            // System.out.println("Day " + i + ": " + count(tiles, 'B'));
        }
        return count(tiles, 'B');
    }

    private static HashMap<String, Character> evolve(HashMap<String, Character> tiles) {
        var next = new HashMap<String, Character>();

        // For all black tiles, check the adjacent tiles
        for (String pos : tiles.keySet()) {
            if (tiles.get(pos) == 'B') {
                int x = asInt(pos.split(",")[0]);
                int y = asInt(pos.split(",")[1]);
                life(tiles, next, x + 1, y + 1);
                life(tiles, next, x + 1, y);
                life(tiles, next, x, y + 1);
                life(tiles, next, x - 1, y - 1);
                life(tiles, next, x - 1, y);
                life(tiles, next, x, y - 1);
            }
        }
        return next;
    }

    private static void life(HashMap<String, Character> tiles, HashMap<String, Character> next, int x, int y) {
        int bs = countAdjacent(tiles, x, y, 'B');
        if (match(tiles, x, y, 'B')) {
            if (bs == 0 || bs > 2) {
                // skip tracking white tiles
                // next.put(x + "," + y, 'W');
            } else {
                next.put(x + "," + y, 'B');
            }
        } else {
            if (bs == 2) {
                next.put(x + "," + y, 'B');
            }
        }
    }

    private static int countAdjacent(HashMap<String, Character> tiles, int x, int y, char color) {
        return (match(tiles, x + 1, y + 1, color) ? 1 : 0)
                + (match(tiles, x + 1, y, color) ? 1 : 0)
                + (match(tiles, x, y + 1, color) ? 1 : 0)
                + (match(tiles, x - 1, y - 1, color) ? 1 : 0)
                + (match(tiles, x - 1, y, color) ? 1 : 0)
                + (match(tiles, x, y - 1, color) ? 1 : 0);
    }

    private static boolean match(HashMap<String, Character> tiles, int x, int y, char color) {
        return tiles.containsKey(x + "," + y) && tiles.get(x + "," + y).charValue() == color;
    }

    private static HashMap<String, Character> parseTiles(String input) {
        var lines = getLines(input);
        var tiles = new HashMap<String, Character>(); // tile -> color

        for (String line : lines) {
            int x = 0, y = 0;
            while (line != "") {
                if (line.startsWith("e")) {
                    line = line.substring(1);
                    x += 1;
                    y += 1;
                } else if (line.startsWith("se")) {
                    line = line.substring(2);
                    x += 1;
                    y += 0;
                } else if (line.startsWith("sw")) {
                    line = line.substring(2);
                    x += 0;
                    y += -1;
                } else if (line.startsWith("w")) {
                    line = line.substring(1);
                    x += -1;
                    y += -1;
                } else if (line.startsWith("nw")) {
                    line = line.substring(2);
                    x += -1;
                    y += 0;
                } else if (line.startsWith("ne")) {
                    line = line.substring(2);
                    x += 0;
                    y += 1;
                } else {
                    throw new UnsupportedOperationException("CMH");
                }
            }

            // flip tile
            Character color = tiles.get(x + "," + y);
            if (color == null || color == 'W') {
                tiles.put(x + "," + y, 'B');
            } else {
                ensure(color == 'B');
                tiles.put(x + "," + y, 'W');
            }
        }
        return tiles;
    }
}