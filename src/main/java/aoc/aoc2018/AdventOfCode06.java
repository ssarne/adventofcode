package aoc.aoc2018;

import static aoc.Utils.getLines;
import static java.lang.System.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AdventOfCode06 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    test2();
    task2();
  }

  public static void test() throws Exception {
    var points = getPoints("input06_test.txt");
    var area = new Area1(points);
    bfs(points, area, true);
    int result = area.eval(points, true);
    System.out.println("Result: " + result);
  }

  public static void task1() throws Exception {
    var points = getPoints("input06.txt");
    var area = new Area1(points);
    bfs(points, area, false);
    int result = area.eval(points, false);
    System.out.println("Result: " + result);
  }

  public static void test2() throws Exception {
    var points = getPoints("input06_test.txt");
    var area = new Area2(points);
    bfs(points, area, true);
    int result = area.eval(points, 32, true);
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    var points = getPoints("input06.txt");
    var area = new Area2(points);
    bfs(points, area, false);
    int result = area.eval(points, 10000, false);
    System.out.println("Result: " + result);
  }

  public static void bfs(List<Point> points, Area area, boolean print) throws Exception {

    var queue = new LinkedList<Point>();
    for (Point p : points) {
      queue.add(p);
    }

    // bfs
    boolean first = true;
    while (queue.size() > 0) {
      var p = queue.pop();
      if (p.dist > 0 && first && print) {
        area.print();
        first = false;
      }
      if (area.add(p)) {
        area.enqueue(queue, p, p.x - 1, p.y);
        area.enqueue(queue, p, p.x + 1, p.y);
        area.enqueue(queue, p, p.x, p.y - 1);
        area.enqueue(queue, p, p.x, p.y + 1);
      }
    }

    if (print) area.print();
  }

  private static ArrayList<Point> getPoints(String input) {
    var lines = getLines(input);
    var points = new ArrayList<Point>();

    int id = 1;
    for (String line : lines) {
      points.add(new Point(line, id++));
    }
    return points;
  }

  interface Area {
    boolean add(Point p);

    void enqueue(LinkedList<Point> queue, Point p, int x, int y);

    void print();

    void print(HashMap<Integer, Incrementor> map);
  }

  static class Area1 implements Area {
    int maxx, maxy;
    Point[][] area;

    public Area1(List<Point> points) {
      maxx = 2 + points.stream().mapToInt(p -> p.x).max().getAsInt();
      maxy = 2 + points.stream().mapToInt(p -> p.y).max().getAsInt();
      area = new Point[maxx][maxy];
    }

    public void print() {
      print(null);
    }

    public void print(HashMap<Integer, Incrementor> map) {
      for (int x = 0; x < maxx; x++) {
        out.print("---");
      }
      out.println();
      for (int y = 0; y < maxy; y++) {
        for (int x = 0; x < maxx; x++) {
          if (area[x][y] == null) {
            out.print("   ");
          } else if (area[x][y] == Point.NONE) {
            out.print(" . ");
          } else if (map == null) {
            out.print(String.format("%3d", area[x][y].id));
          } else {
            if (map.containsKey(area[x][y].id)) {
              out.print(String.format("%3d", area[x][y].id));
            } else {
              out.print("   ");
            }
          }
        }
        out.println();
      }
    }

    public boolean add(Point p) {
      if (area[p.x][p.y] == null) {
        area[p.x][p.y] = p;
        return true;
      }

      if (area[p.x][p.y] != null && area[p.x][p.y].id != p.id && area[p.x][p.y].dist == p.dist) {
        area[p.x][p.y] = Point.NONE;
        return false;
      }

      // same - eat.
      return false;
    }

    public void enqueue(LinkedList<Point> queue, Point p, int x, int y) {
      if (x < 0 || x >= maxx) {
        return;
      }
      if (y < 0 || y >= maxy) {
        return;
      }
      if (area[x][y] == Point.NONE) {
        return;
      }
      if (area[x][y] != null && area[x][y].dist <= p.dist) {
        return;
      }
      queue.add(new Point(p.id, x, y, p.dist + 1));
    }

    public int eval(List<Point> points, boolean print) {
      HashMap<Integer, Incrementor> areas = new HashMap<>();
      for (Point p : points) {
        areas.put(p.id, new Incrementor());
      }
      for (int x = 0; x < maxx; x++) {
        areas.remove(area[x][0].id);
        areas.remove(area[x][maxy - 1].id);
      }
      for (int y = 0; y < maxy; y++) {
        areas.remove(area[0][y].id);
        areas.remove(area[maxx - 1][y].id);
      }
      for (int x = 1; x < maxx - 1; x++) {
        for (int y = 1; y < maxy - 1; y++) {
          Incrementor inc = areas.get(area[x][y].id);
          if (inc != null) inc.i++;
        }
      }

      if (print) print(areas);

      return areas.values().stream().mapToInt(i -> i.i).max().getAsInt();
    }
  }

  static class Area2 implements Area {

    int maxx, maxy, maxp;
    Point[][][] area;

    public Area2(List<Point> points) {
      maxx = 2 + points.stream().mapToInt(p -> p.x).max().getAsInt();
      maxy = 2 + points.stream().mapToInt(p -> p.y).max().getAsInt();
      maxp = points.size() + 2;
      area = new Point[maxx][maxy][maxp];
    }

    public boolean add(Point p) {
      if (area[p.x][p.y][p.id] == null) {
        area[p.x][p.y][p.id] = p;
        return true;
      }

      // area[p.x][p.y][p.id] != null
      return false;
    }

    public void enqueue(LinkedList<Point> queue, Point p, int x, int y) {
      if (x < 0 || x >= maxx) {
        return;
      }
      if (y < 0 || y >= maxy) {
        return;
      }
      if (area[x][y][p.id] != null) {
        return;
      }
      queue.add(new Point(p.id, x, y, p.dist + 1));
    }

    @Override
    public void print() {
      return;
    }

    public int eval(List<Point> points, int limit, boolean print) {
      int n = 0;
      for (int x = 0; x < maxx; x++) {
        for (int y = 0; y < maxy; y++) {
          int sum = 0;
          for (int p = 0; p < maxp; p++) {
            if (area[x][y][p] != null) {
              sum += area[x][y][p].dist;
            }
          }
          if (sum < limit) {
            n++;
          }
        }
      }

      if (print) print(limit);

      return n;
    }

    public void print(int limit) {
      for (int x = 0; x < maxx; x++) {
        out.print("-");
      }
      out.println();

      for (int y = 0; y < maxy; y++) {
        for (int x = 0; x < maxx; x++) {
          int sum = 0;
          for (int p = 0; p < maxp; p++) {
            if (area[x][y][p] != null) {
              sum += area[x][y][p].dist;
            }
          }
          if (sum < limit) {
            out.print("#");
          } else {
            out.print(" ");
          }
        }
        out.println();
      }
      return;
    }

    public void print(HashMap<Integer, Incrementor> map) {
      return;
    }
  }

  static class Point {

    static Point NONE = new Point(0, 0, 0, 0);

    int id;
    int x;
    int y;
    int dist;

    public Point(String point, int id) {
      this.id = id;
      this.dist = 0;
      String[] cords = point.split(", ");
      x = Integer.parseInt(cords[0]);
      y = Integer.parseInt(cords[1]);
    }

    public Point(int id, int x, int y, int dist) {
      this.id = id;
      this.x = x;
      this.y = y;
      this.dist = dist;
    }
  }

  static class Incrementor {
    int i = 0;
  }
}
