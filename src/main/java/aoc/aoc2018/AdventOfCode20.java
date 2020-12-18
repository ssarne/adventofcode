package aoc.aoc2018;

import static aoc.utils.Utils.getLines;
import static java.lang.System.out;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class AdventOfCode20 {

  public static void main(String[] args) throws Exception {
    test();
    task();
  }

  static void test() throws Exception {

    List<String> lines = getLines("input20_test.txt");
    for (String line : lines) {
      if (line.charAt(0) == '#') {
        continue;
      }

      List<String> result = doit(line, 2);
      System.out.println("Result 1: " + result.get(0));
      System.out.println("Result 2: " + result.get(1));
    }
  }

  static void task() throws Exception {
    List<String> lines = getLines("input20.txt");
    List<String> result = doit(lines.get(0), 1000);
    System.out.println("Result 1: " + result.get(0));
    System.out.println("Result 2: " + result.get(1));
  }

  static List<String> doit(String input, int threshold) throws Exception {

    System.out.println("=====================================");
    System.out.println("Expr: " + input);
    Path paths = parsePathExpr(input);
    System.out.println("Stats: " + stats(input));
    System.out.print("Paths built.");
    System.out.println(" Total path length: " + paths.size());
    Castle castle = buildCastle(input, paths);
    castle.cement();
    castle.print();
    return findShortestPath(castle, threshold);
  }

  private static String stats(String input) {
    return input.chars().filter(c -> c == '(').count()
        + " left parenthesis. "
        + input.chars().filter(c -> c == ')').count()
        + " right parenthesis. "
        + input.chars().filter(c -> c == '|').count()
        + " bars. "
        + Path.idc
        + " nodes.";
  }

  private static List<String> findShortestPath(Castle castle, int threshold) {

    int lps = 0; // Number of shortest paths, ending in a room with which are higher than threshold

    Queue<Pos> queue = new LinkedList<>();
    queue.add(new Pos(0, 0, 0));
    Pos pos = null;
    while (!queue.isEmpty()) {
      pos = queue.poll();
      if (pos.i >= threshold) {
        lps++;
      }
      if (castle.isDoor(pos.x, pos.y - 1) && castle.get(pos.x, pos.y - 2) == '.') {
        queue.add(new Pos(pos.x, pos.y - 2, pos.i + 1));
        castle.set(pos.x, pos.y - 2, 'o');
      }
      if (castle.isDoor(pos.x - 1, pos.y) && castle.get(pos.x - 2, pos.y) == '.') {
        queue.add(new Pos(pos.x - 2, pos.y, pos.i + 1));
        castle.set(pos.x - 2, pos.y, 'o');
      }
      if (castle.isDoor(pos.x + 1, pos.y) && castle.get(pos.x + 2, pos.y) == '.') {
        queue.add(new Pos(pos.x + 2, pos.y, pos.i + 1));
        castle.set(pos.x + 2, pos.y, 'o');
      }
      if (castle.isDoor(pos.x, pos.y + 1) && castle.get(pos.x, pos.y + 2) == '.') {
        queue.add(new Pos(pos.x, pos.y + 2, pos.i + 1));
        castle.set(pos.x, pos.y + 2, 'o');
      }
      //      castle.print();
    }
    return List.of("" + pos.i, "" + lps);
  }

  static Castle buildCastle(String expr, Path paths) {
    Castle castle = new Castle(expr.length());
    buildCastle(castle, paths);
    return castle;
  }

  static void buildCastle(Castle castle, Path paths) {
    Pos start = new Pos(0, 0, 1);
    buildCastle(castle, paths, start);
  }

  static void buildCastle(Castle castle, Path path, Pos start) {

    if (handled(castle, path, start)) {
      return; // already done
    }

    Pos pos = buildCastle(castle, path.start, start);
    if (path.alternatives.size() > 0) {
      for (Path p : path.alternatives) {
        buildCastle(castle, p, pos);
      }
    }
  }

  private static boolean handled(Castle castle, Path path, Pos start) {
    HashMap<Integer, Path> done = castle.visited.get(start.sid);
    if (done == null) {
      done = new HashMap<>();
      castle.visited.put(start.sid, done);
    }

    if (done.containsKey(path.id)) {
      return true;
    }

    done.put(path.id, path);
    return false;
  }

  static Pos buildCastle(Castle castle, String steps, Pos start) {
    // System.out.println("Steps ("+start+") " + steps);
    Pos pos = start;
    for (int i = 0; i < steps.length(); i++) {
      switch (steps.charAt(i)) {
        case 'E':
          castle.door(pos.x + 1, pos.y, '|');
          castle.room(pos.x + 2, pos.y);
          pos = new Pos(pos.x + 2, pos.y, pos.i + 1);
          break;
        case 'W':
          castle.door(pos.x - 1, pos.y, '|');
          castle.room(pos.x - 2, pos.y);
          pos = new Pos(pos.x - 2, pos.y, pos.i + 1);
          break;
        case 'S':
          castle.door(pos.x, pos.y + 1, '-');
          castle.room(pos.x, pos.y + 2);
          pos = new Pos(pos.x, pos.y + 2, pos.i + 1);
          break;
        case 'N':
          castle.door(pos.x, pos.y - 1, '-');
          castle.room(pos.x, pos.y - 2);
          pos = new Pos(pos.x, pos.y - 2, pos.i + 1);
          break;
        case '$':
          break;
        default:
          throw new UnsupportedOperationException("" + steps.charAt(i));
      }
    }
    return pos;
  }

  static Path parsePathExpr(String expr) {
    String init = expr.substring(1, expr.length() - 1);
    return parsePathExpr(init, 0);
  }

  static Path parsePathExpr(String expr, int level) {

    // System.out.println(indent(level) + expr);

    var fp = expr.indexOf('(');
    if (fp == -1) {
      return new Path(expr);
    }

    Path path = new Path(expr.substring(0, fp));

    var pd = 0; // parenthesis depth
    var lp = -1; // last parenthesis
    for (int i = fp; ; i++) {
      char c = expr.charAt(i);
      if (expr.charAt(i) == '(' && pd == 0) {
        pd++;
      } else if (expr.charAt(i) == '|' && expr.charAt(i + 1) == ')' && pd == 1) {
        path.alternatives.add(parsePathExpr(expr.substring(fp + 1, i), level + 1));
        path.alternatives.add(new Path(""));
        lp = i + 1;
        break;
      } else if (expr.charAt(i) == '|' && pd == 1) {
        path.alternatives.add(parsePathExpr(expr.substring(fp + 1, i), level + 1));
        fp = i;
      } else if (expr.charAt(i) == ')' && pd == 1) {
        path.alternatives.add(parsePathExpr(expr.substring(fp + 1, i), level + 1));
        lp = i;
        break;
      } else if (expr.charAt(i) == '(') {
        pd++;
      } else if (expr.charAt(i) == ')') {
        pd--;
      } else {
        // eat...
      }
    }

    if (lp + 1 < expr.length()) {
      Path next = parsePathExpr(expr.substring(lp + 1, expr.length()), level + 1);
      for (Path p : path.alternatives) {
        p.append(next);
      }
    }

    return path;
  }

  static List<String> flatten(Path paths) {

    var result = List.of(paths.start);
    if (paths.alternatives.size() > 0) {
      List<String> tmp = new ArrayList<>();
      for (Path alt : paths.alternatives) {
        List<String> alts = flatten(alt);
        for (var s1 : result) {
          for (var s2 : alts) {
            tmp.add(s1 + s2);
          }
        }
      }
      result = tmp;
    }
    return result;
  }

  static class Path {

    static int idc = 0;

    int id;
    BigInteger size = null;
    String start;
    List<Path> alternatives;

    public Path(String start) {
      this.id = idc++;
      this.start = start;
      this.alternatives = new ArrayList<>();
    }

    public void append(Path next) {
      if (alternatives.size() == 0) {
        alternatives.add(next);
      } else {
        for (Path p : alternatives) {
          p.append(next);
        }
      }
    }

    public BigInteger size() {
      if (size != null) {
        return size;
      }
      if (alternatives.size() == 0) {
        size = BigInteger.valueOf((long) start.length());
        return size;
      }
      BigInteger sum = BigInteger.valueOf((long) start.length());
      for (Path p : alternatives) {
        sum = sum.add(p.size());
      }
      size = sum;
      return size;
    }
  }

  static class Pos {

    int x, y, i;
    String sid;

    public Pos(int x, int y, int i) {
      this.x = x;
      this.y = y;
      this.i = i;
      this.sid = x + "," + y;
    }

    public String toString() {
      return "(" + x + "," + y + ")[" + i + "]";
    }
  }

  static class Castle {

    HashMap<String, HashMap<Integer, Path>> visited = new HashMap<>();

    int minx, miny, maxx, maxy;
    int offset;

    private char[][] area;

    Castle(int length) {
      area = new char[2 * length][2 * length];
      offset = length;
      minx = miny = offset - 1;
      maxx = maxy = offset + 1;
      for (int y = 0; y < area.length; y++) {
        for (int x = 0; x < area.length; x++) {
          area[x][y] = ' ';
        }
      }
      room(0, 0);
      set(0, 0, 'X');
    }

    char get(int x, int y) {
      return area[x + offset][y + offset];
    }

    void set(int x, int y, char c) {
      area[x + offset][y + offset] = c;
    }

    void door(int x, int y, char c) {
      area[x + offset][y + offset] = c;
    }

    void room(int x, int y) {
      area[offset + x][offset + y] = '.';
      area[offset + x - 1][offset + y + 1] = '#';
      area[offset + x + 1][offset + y + 1] = '#';
      area[offset + x - 1][offset + y - 1] = '#';
      area[offset + x + 1][offset + y - 1] = '#';
      setIfUnset(x, y - 1, '?');
      setIfUnset(x - 1, y, '?');
      setIfUnset(x + 1, y, '?');
      setIfUnset(x, y + 1, '?');
      minx = Math.min(offset + x - 1, minx);
      miny = Math.min(offset + y - 1, miny);
      maxx = Math.max(offset + x + 1, maxx);
      maxy = Math.max(offset + y + 1, maxy);
    }

    private void setIfUnset(int x, int y, char c) {
      if (area[offset + x][offset + y] == ' ') {
        area[offset + x][offset + y] = c;
      }
    }

    void print(Pos pos, String path, Stack<Pos> stack) {
      System.out.println();
      print();
      System.out.println();
      System.out.println(path + "  (" + pos.x + ", " + pos.y + ")  " + stack.toString());
      for (int i = 0; i < path.length(); i++) {
        System.out.print(i == pos.i ? "^" : " ");
      }
      System.out.println();
    }

    void print() {
      for (int y = miny; y <= maxy; y++) {
        for (int x = minx; x <= maxx; x++) {
          out.print(area[x][y]);
        }
        out.println();
      }
    }

    public void cement() {
      for (int y = miny; y <= maxy; y++) {
        for (int x = minx; x <= maxx; x++) {
          if (area[x][y] == '?') {
            area[x][y] = '#';
          }
        }
      }
    }

    public boolean isDoor(int x, int y) {
      return get(x, y) == '|' || get(x, y) == '-';
    }

    public int doors(Pos pos) {
      int doors = 0;
      doors += (isDoor(pos.x, pos.y + 1) ? 1 : 0);
      doors += (isDoor(pos.x - 1, pos.y) ? 1 : 0);
      doors += (isDoor(pos.x + 1, pos.y) ? 1 : 0);
      doors += (isDoor(pos.x, pos.y - 1) ? 1 : 0);
      return doors;
    }
  }
}
