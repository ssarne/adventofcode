package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static aoc.utils.Utils.ints;
import static aoc.utils.Utils.readAnswerAsInt;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class Dec03 {

  public static void main(String[] args) throws Exception {
    test();
    tasks();
  }

  public static void test() throws Exception {
    check(doit(getTestLines(1))[0], 6);
    check(doit(getTestLines(2))[0], 159);
    check(doit(getTestLines(3))[0], 135);
    check(doit(getTestLines(4))[0], 6);
    check(doit(getTestLines(1))[1], 30);
    check(doit(getTestLines(2))[1], 610);
    check(doit(getTestLines(3))[1], 410);
    check(doit(getTestLines(4))[1], 30);
  }

  public static void tasks() throws Exception {
    var res = doit(getLines());
    System.out.println(res[0]);
    check(res[0], readAnswerAsInt(1));
    System.out.println(res[1]);
    check(res[1], readAnswerAsInt(2));
  }

  public static int [] doit(List<String> lines) throws Exception {

    HashMap<String, Point> path1 = buildPath(lines.get(0));
    HashMap<String, Point> path2 = buildPath(lines.get(1));
    // print(path1, path2);

    // Calc manhattan distance to crossings, use closest to origo
    int mh = Integer.MAX_VALUE;
    for (String p : path1.keySet()) {
      if (path2.containsKey(p)) {
        int ddd = Math.abs(path1.get(p).x) + Math.abs(path1.get(p).y);
        mh = Math.min(mh, ddd);
      }
    }

    // Calc cable distance to first intersection
    int min = Integer.MAX_VALUE;
    for (String p : path1.keySet()) {
      if (path2.containsKey(p)) {
        int ddd = path1.get(p).d + path2.get(p).d;
        min = Math.min(min, ddd);
      }
    }
    return ints(mh, min);
  }

  private static void print(HashMap<String, Point> path1, HashMap<String, Point> path2) {
    for (int i = 10; i > -10; i--) { // y
      for (int j = -10; j < 100; j++) { // x
        if (i == 0 && j == 0) {
          System.out.print(" oo ");
        } else if (path1.containsKey(j + "," + i) && path2.containsKey(j + "," + i)) {
          System.out.printf(" %2d ", path1.get(j + "," + i).d + path2.get(j + "," + i).d);
        } else if (path1.containsKey(j + "," + i)) {
          System.out.printf(" %2d ", path1.get(j + "," + i).d);
        } else if (path2.containsKey(j + "," + i)) {
          System.out.printf(" %2d ", path2.get(j + "," + i).d);
        } else {
          System.out.print(" .. ");
        }
      }
      System.out.println();
    }
  }

  private static HashMap<String, Point> buildPath(String line) {

    HashMap<String, Point> path = new HashMap<>();
    int x = 0;
    int y = 0;
    int d = 0;

    String[] parts = line.split(",");
    for (String part : parts) {
      char direction = part.charAt(0);
      int delta = Integer.parseInt(part.substring(1));
      for (int i = 0; i < delta; i++) {
        if (direction == 'R') x++;
        else if (direction == 'D') y--;
        else if (direction == 'L') x--;
        else if (direction == 'U') y++;
        else System.out.println("Unknown dir: " + direction);
        if (x == 0 && y == 0) System.out.println("ERROR - back to 0");
        String point = x + "," + y;
        d++;

        if (!path.containsKey(point)) {
          path.put(point, new Point(x, y, d));
        }
      }
    }

    return path;
  }

  // Fix up a path according to shortest distance there.
  private static void fix(HashMap<String, Integer> path, Queue<Point> qq, int x, int y, int d) {
    if (path.containsKey(x + "," + y)) {
      if (path.get(x + "," + y) == d) {
        qq.add(new Point(x, y, d));
      }
      if (path.get(x + "," + y) > d) {
        path.put(x + "," + y, d);
        qq.add(new Point(x, y, d));
      }
    }
  }

  static class Point {
    int x, y, d;
    Point(int x, int y, int d) {
      this.x = x;
      this.y = y;
      this.d = d;
    }
  }
}

