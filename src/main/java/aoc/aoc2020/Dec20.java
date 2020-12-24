package aoc.aoc2020;

import java.util.*;

import static aoc.utils.Utils.*;

public class Dec20 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1("aoc2020/dec20_test.txt"), 20899048083289L);
        check(solve2("aoc2020/dec20_test.txt"), 273);
    }

    public static void task1() {
        var result = solve1("aoc2020/dec20.txt");
        check(result, 22878471088273L);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("aoc2020/dec20.txt");
        check(result, 1680);
        System.out.println("Result: " + result);
    }

    public static long solve1(String input) {
        var lines = getLines(input);
        List<Image> tiles = parseTiles(lines);
        var puzzle = solvePuzzle(tiles);
        return puzzle.tiles[0][0].id
                * puzzle.tiles[0][puzzle.size - 1].id
                * puzzle.tiles[puzzle.size - 1][0].id
                * puzzle.tiles[puzzle.size - 1][puzzle.size - 1].id;
    }

    public static long solve2(String input) {
        var lines = getLines(input);
        var tiles = parseTiles(lines);
        var puzzle = solvePuzzle(tiles);
        var image = puzzle.crop();

        //puzzle.printIds();
        //puzzle.print();
        //image.print();

        String[] nessie = {
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   "};

        int monsters = 0;
        Orientation oo = null;
        for (Orientation o : Orientation.values()) {
            for (int y = 0; y < image.size() - nessie.length; y++) {
                for (int x = 0; x < image.size() - nessie[0].length(); x++) {
                    boolean match = true;
                    for (int j = 0; j < nessie.length && match; j++) {
                        for (int i = nessie[j].indexOf('#'); i >= 0 && match; i = nessie[j].indexOf('#', i + 1)) {
                            if (!(image.get(x + i, y + j, o) == '#')) {
                                match = false;
                            }
                        }
                    }
                    if (match) {
                        monsters++;
                        ensure(oo == null || oo == o); // check orientation, verify that all monsters are on the same page
                        oo = o;
                    }
                }
            }
        }

        int n = 0; // count # in image
        for (int y = 0; y < image.size(); y++) {
            for (int x = 0; x < image.size(); x++) {
                if (image.get(x, y, oo) == '#') {
                    n++;
                }
            }
        }

        long m = 0; // count # in nessie
        for (String s : nessie) {
            for (char c : s.toCharArray()) {
                if (c == '#') {
                    m++;
                }
            }
        }

        return n - monsters * m;
    }

    private enum Orientation {

        R0(0, false),
        R1(1, false),
        R2(2, false),
        R3(3, false),
        F0(0, true),
        F1(1, true),
        F2(2, true),
        F3(3, true);

        public final int r;
        public final boolean f;

        Orientation(int r, boolean f) {
            this.r = r;
            this.f = f;
        }
    }

    private static Puzzle solvePuzzle(List<Image> tiles) {
        int size = (int) Math.sqrt(tiles.size());
        var puzzle = new Puzzle(size);
        solvePuzzleRecurse(puzzle, tiles, 0);
        return puzzle;
    }

    private static boolean solvePuzzleRecurse(Puzzle puzzle, List<Image> tiles, int n) {
        if (n == tiles.size()) {
            return true;
        }

        int x = n % puzzle.size;
        int y = n / puzzle.size;

        for (Image tile : tiles) {
            if (!puzzle.contains(tile)) {
                for (Orientation d : Orientation.values()) {
                    if (puzzle.match(x, y, tile, d)) {
                        puzzle.set(x, y, tile, d);
                        if (solvePuzzleRecurse(puzzle, tiles, n + 1)) {
                            return true;
                        }
                    }
                }
            }
            puzzle.set(x, y, null, null);
        }

        return false;
    }

    private static List<Image> parseTiles(List<String> lines) {
        List<Image> tiles = new ArrayList<>();
        Image tile = null;
        for (String line : lines) {
            if (line.startsWith("Tile")) {
                long id = Long.parseLong(line.substring("Tile ".length(), line.indexOf(":")));
                tile = new Image(id);
                continue;
            }
            if (line.equals("")) {
                tiles.add(tile);
                continue;
            }
            tile.canvas.add(line);
        }
        tiles.add(tile);

        return tiles;
    }

    private static class Puzzle {

        public int size;
        public Image[][] tiles;
        public Orientation[][] directions;

        public Puzzle(int size) {
            this.size = size;
            tiles = new Image[size][size];
            directions = new Orientation[size][size];
        }

        public boolean contains(Image tile) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (tiles[x][y] != null && tiles[x][y].id == tile.id) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean match(int x, int y, Image tile, Orientation d) {
            if (x > 0 && tiles[x - 1][y] != null
                    && !(tiles[x - 1][y].east(directions[x - 1][y]).equals(tile.west(d)))) {
                return false;
            }
            if (y > 0 && tiles[x][y - 1] != null
                    && !(tiles[x][y - 1].south(directions[x][y - 1]).equals(tile.north(d)))) {
                return false;
            }
            if (x < size - 1 && tiles[x + 1][y] != null
                    && !(tiles[x + 1][y].west(directions[x + 1][y]).equals(tile.east(d)))) {
                return false;
            }
            if (y < size - 1 && tiles[x][y + 1] != null
                    && !(tiles[x][y + 1].north(directions[x][y + 1]).equals(tile.south(d)))) {
                return false;
            }
            return true;
        }

        public void set(int x, int y, Image tile, Orientation d) {
            this.tiles[x][y] = tile;
            this.directions[x][y] = d;
        }

        public char get(int x, int y) {
            int tsize = tiles[0][0].canvas.get(0).length();
            int tx = x / tsize;
            int ty = y / tsize;
            var tile = tiles[tx][ty];
            return tile.get(x % tsize, y % tsize, directions[tx][ty]);
        }

        public void printIds() {
            System.out.println("================= tile ids");
            for (int ty = 0; ty < size; ty++) {
                for (int tx = 0; tx < size; tx++) {
                    System.out.print(this.tiles[tx][ty].id + "  ");
                }
                for (int tx = 0; tx < size; tx++) {
                    System.out.print("  " + this.directions[tx][ty]);
                }
                System.out.println();
            }
        }

        public void print() {
            int tsize = tiles[0][0].canvas.get(0).length();
            System.out.println("================= " + size + "*" + tsize + " x " + size + "*" + tsize);
            System.out.println();
            for (int ty = 0; ty < size; ty++) {
                for (int cy = 0; cy < tsize; cy++) {
                    for (int tx = 0; tx < size; tx++) {
                        for (int cx = 0; cx < tsize; cx++) {
                            System.out.print(this.tiles[tx][ty].get(cx, cy, directions[tx][ty]));
                        }
                        System.out.print(" ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }

        public Image crop() {
            int tsize = tiles[0][0].canvas.get(0).length();
            Image image = new Image(0);
            for (int ty = 0; ty < size; ty++) {
                for (int cy = 1; cy < tsize - 1; cy++) {
                    StringBuilder sb = new StringBuilder();
                    for (int tx = 0; tx < size; tx++) {
                        for (int cx = 1; cx < tsize - 1; cx++) {
                            sb.append(this.tiles[tx][ty].get(cx, cy, directions[tx][ty]));
                        }
                    }
                    image.canvas.add(sb.toString());
                }
            }
            return image;
        }
    }

    private static class Image {

        private final long id;
        List<String> canvas = new ArrayList<>();

        public Image(long id) {
            this.id = id;
        }

        public int size() {
            return canvas.size();
        }

        public String east(Orientation direction) {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < canvas.size(); y++) {
                sb.append(get(canvas.size() - 1, y, direction));
            }
            return sb.toString();
        }

        public String north(Orientation direction) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < canvas.size(); x++) {
                sb.append(get(x, 0, direction));
            }
            return sb.toString();
        }

        public String west(Orientation direction) {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < canvas.size(); y++) {
                sb.append(get(0, y, direction));
            }
            return sb.toString();
        }

        public String south(Orientation direction) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < canvas.size(); x++) {
                sb.append(get(x, canvas.size() - 1, direction));
            }
            return sb.toString();
        }

        public char get(int x, int y, Orientation o) {
            int csize = this.canvas.get(0).length();
            int ox = ox(x, y, o, csize);
            int oy = oy(x, y, o, csize);
            return this.canvas.get(oy).charAt(ox);
        }

        private int ox(int x, int y, Orientation o, int size) {
            switch (o) {
                case R0:
                    return x;
                case R1:
                    return y;
                case R2:
                    return size - x - 1;
                case R3:
                    return size - y - 1;
                case F0:
                    return size - x - 1;
                case F1:
                    return y;
                case F2:
                    return x;
                case F3:
                    return size - y - 1;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        private int oy(int x, int y, Orientation o, int size) {
            switch (o) {
                case R0:
                    return y;
                case R1:
                    return size - x - 1;
                case R2:
                    return size - y - 1;
                case R3:
                    return x;
                case F0:
                    return y;
                case F1:
                    return x;
                case F2:
                    return size - y - 1;
                case F3:
                    return size - x - 1;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public void print() {
            System.out.println("================= Image " + id);
            for (String line : this.canvas) {
                for (char c : line.toCharArray()) {
                    System.out.print(c);
                }
                System.out.println();
            }
        }
    }
}
