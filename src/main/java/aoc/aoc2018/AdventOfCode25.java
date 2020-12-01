package aoc.aoc2018;

import static aoc.Utils.asInt;
import static aoc.Utils.check;
import static aoc.Utils.getLines;
import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AdventOfCode25 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    // task2();
  }

  public static void test() throws Exception {
    String result1 = doit("input25_test_1.txt");
    out.println("Result Test 1: " + result1);
    String result2 = doit("input25_test_2.txt");
    out.println("Result Test 2: " + result2);
    String result3 = doit("input25_test_3.txt");
    out.println("Result Test 3: " + result3);
    String result4 = doit("input25_test_4.txt");
    out.println("Result Test 4: " + result4);
  }

  public static void task1() throws Exception {
    String result = doit("input25.txt");
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    String result = doit("FIXME");
    System.out.println("Result: " + result);
  }

  public static String doit(String input) throws Exception {

    var points = new ArrayList<Point>();
    for (String line : getLines(input)) {
      points.add(Point.create(line.trim()));
    }

    var constallations = new ArrayList<Constallation>();
    for (Point point : points) {
      Constallation match = null;
      for (Iterator<Constallation> ci = constallations.iterator(); ci.hasNext(); ) {
        Constallation c = ci.next();
        if (c.align(point)) {
          if (match == null) {
            match = c;
            match.add(point);
          } else {
            match.merge(c);
            ci.remove();
          }
        }
      }
      if (match == null) {
        match = new Constallation();
        match.add(point);
        constallations.add(match);
      }
    }

    int result = constallations.size();
    //for (Constallation c : constallations) {
    //  out.println(c.toString());
    //}
    return "" + result;
  }

  static class Constallation {
    Set<Point> points = new HashSet<>();

    boolean align(Point point) {
      for (Point p : points) {
        if (p.distance(point) <= 3) {
          return true;
        }
      }
      return false;
    }

    boolean contains(Point point) {
      for (Point p : points) {
        if (p.distance(point) == 0) {
          return true;
        }
      }
      return false;
    }

    void add(Point point) {
      points.add(point);
    }

    void merge(Constallation c) {
      points.addAll(c.points);
    }

    public String toString() {
      return Arrays.toString(points.toArray());
    }
  }

  static class Point {
    int x, y, z, t;

    public Point(int x, int y, int z, int t) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.t = t;
    }

    static Point create(String line) {
      String[] coords = line.split(",");
      return new Point(asInt(coords[0]), asInt(coords[1]), asInt(coords[2]), asInt(coords[3]));
    }

    int distance(Point other) {
      return Math.abs(this.x - other.x)
          + Math.abs(this.y - other.y)
          + Math.abs(this.z - other.z)
          + Math.abs(this.t - other.t);
    }

    public String toString() {
      return "(" + x + "," + y + "," + z + "," + t + ")";
    }
  }
}
