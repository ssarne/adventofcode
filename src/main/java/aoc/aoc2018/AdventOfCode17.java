package aoc.aoc2018;

import static aoc.Utils.check;
import static aoc.Utils.getLines;
import static java.lang.System.out;

import aoc.Utils;
import java.util.ArrayList;
import java.util.List;

public class AdventOfCode17 {

  enum State {
    trickle,
    trickle_blocked,
    filled,
    blocked
  }

  public static void main(String[] args) {
    test();
    task();
  }

  public static void test() {
    int result = doit("input17_test.txt");
    Utils.check(result, 57);
  }

  public static void task() {
    doit("input17.txt");
  }

  public static int doit(String input) {

    List<String> lines = getLines(input);
    List<Line> barriers = getBarriers(lines);
    Earth earth = new Earth(barriers);

    earth.print();

    pour(earth, 500, 0, 0);
    earth.print();
    int sum = earth.count('|', '~');
    int still = earth.count('~');
    System.out.println("Total: " + sum);
    System.out.println("Remains: " + still);
    return sum;
  }

  private static List<Line> getBarriers(List<String> lines) {
    List<Line> barriers = new ArrayList<>();
    for (String line : lines) {
      int[] xy = new int[4];
      String[] tokens = line.split(", ");
      String val1 = tokens[0];
      int offset1 = val1.contains("x") ? 0 : 2;
      xy[0 + offset1] = Integer.parseInt(val1.substring("_=".length()));
      xy[1 + offset1] = xy[0 + offset1];
      String[] vals = tokens[1].split("\\.\\.");
      int offset2 = vals[0].contains("x") ? 0 : 2;
      xy[0 + offset2] = Integer.parseInt(vals[0].substring("_=".length()));
      xy[1 + offset2] = Integer.parseInt(vals[1]);
      barriers.add(new Line(xy[0], xy[2], xy[1], xy[3]));
    }
    return barriers;
  }

  public static State pour(Earth earth, int x, int y, int dx) {

    // earth.print();

    if (y >= earth.maxy) {
      return State.trickle;
    }

    if (earth.get(x, y) == '#') {
      return State.blocked;
    }

    if (earth.get(x, y) == '~') {
      return State.filled;
    }

    if (earth.get(x, y) == '+') {
      return pour(earth, x, y + 1, 0);
    }

    if (earth.get(x, y) == '|') {
      return State.trickle;
    }

    if (earth.get(x, y) != '.') {
      throw new UnsupportedOperationException("Not handled: " + earth.get(x, y));
    }

    earth.set(x, y, '|');
    State below = pour(earth, x, y + 1, 0);

    if (below == State.trickle) {
      return State.trickle;
    }

    if (dx != 0) {
      return pour(earth, x + dx, y, dx);
    }

    State left = pour(earth, x - 1, y, -1);
    State right = pour(earth, x + 1, y, 1);
    if ((left == State.trickle_blocked || left == State.blocked)
        && (right == State.trickle_blocked || right == State.blocked)) {
      earth.set(x, y, '~');
      fill(earth, x - 1, y, -1);
      fill(earth, x + 1, y, 1);
      return State.filled;
    }

    if (left == State.trickle || right == State.trickle) {
      return State.trickle;
    }

    throw new UnsupportedOperationException("Not handled: left=" + left + "  right=" + right);
  }

  private static void fill(Earth earth, int x, int y, int dx) {
    if (earth.get(x, y) == '#') {
      return;
    }
    if (earth.get(x, y) == '|') {
      earth.set(x, y, '~');
      fill(earth, x + dx, y, dx);
      return;
    }
  }

  static class Earth {

    int width, height;
    int minx, miny, maxx, maxy, basey;
    private char[][] slice;

    public Earth(List<Line> barriers) {

      this.minx = barriers.stream().mapToInt(b -> b.x1).min().getAsInt() - 1;
      this.basey = barriers.stream().mapToInt(b -> b.y1).min().getAsInt();
      this.miny = Math.min(basey, 0);
      this.maxx = barriers.stream().mapToInt(b -> b.x2).max().getAsInt() + 2;
      this.maxy = barriers.stream().mapToInt(b -> b.y2).max().getAsInt() + 1;

      this.width = maxx - minx;
      this.height = maxy - miny;

      this.slice = new char[width][height];
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          this.slice[x][y] = '.';
        }
      }

      for (Line line : barriers) {
        for (int y = line.y1; y <= line.y2; y++) {
          for (int x = line.x1; x <= line.x2; x++) {
            this.set(x, y, '#');
          }
        }
      }

      this.set(500, 0, '+');
    }

    public void set(int x, int y, char c) {
      slice[x - minx][y - miny] = c;
    }

    public char get(int x, int y) {
      if (x - minx < 0 || x - minx >= maxx) {
        System.out.println("ohps.");
      }
      if (y - miny < 0 || y >= maxy) {
        System.out.println("ohps.");
      }
      return slice[x - minx][y - miny];
    }

    public void print() {
      for (int x = 0; x < width; x++) {
        out.print("-");
      }
      out.println();
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          out.print(slice[x][y]);
        }
        out.println();
      }
    }

    public void print(int x, int y, int size) {
      for (int i = 0; i < 2 * size; i++) {
        out.print("-");
      }
      out.println();
      for (int i = Math.max(y - size, 0); i < Math.min(y + size, maxy); i++) {
        for (int j = Math.max(x - size, minx); j < Math.min(x + size, maxx); j++) {
          out.print(get(i, j));
        }
        out.println();
      }
    }

    public int count(char... chars) {
      int n = 0;
      for (int x = 0; x < width; x++) {
        for (int y = basey; y < maxy; y++) {
          for (char c : chars)
            if (slice[x][y] == c) {
              n++;
            }
        }
      }
      return n;
    }
  }

  static class Line {
    int x1, y1, x2, y2;

    public Line(int x1, int y1, int x2, int y2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
    }
  }
}
