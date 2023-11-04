package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static aoc.utils.Utils.readAnswerAsInt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Dec20 {

  public static void main(String[] args) throws Exception {
    test1();
    test2();
    task1();
    task2();
  }

  public static void test1() {
    check(doit(getTestLines(1), 0).d - 1, 23);
    check(doit(getTestLines(2), 0).d - 1, 58);
  }

  public static void test2() {
    check(doit(getTestLines(1), 1).d - 1, 26);
    check(doit(getTestLines(3), 1).d - 1, 396);
  }

  public static void task1() {
    Pos result = doit(getLines(), 0);
    System.out.println("Result 1: " + (result.d - 1)); // -1, since step into portal doesn't count
    check(result.d - 1, readAnswerAsInt(1));
  }

  public static void task2() {
    Pos result = doit(getLines(), 1);
    System.out.println("Result 2: " + (result.d - 1)); // -1, since step into portal doesn't count
    check(result.d - 1, readAnswerAsInt(2));
  }

  private static Pos doit(List<String> lines, int dl) {

    Board board = Board.create(lines, dl);
    // board.print();
    Queue<Pos> bfs = new LinkedList<>();
    Portal start = board.portals.byNames.get("AA");
    Portal end = board.portals.byNames.get("ZZ");
    bfs.add(start.enter(board));
    while (!bfs.isEmpty()) {
      Pos p = bfs.poll();
      Pos[] candidates = {
          new Pos(p.x + 1, p.y, p.d + 1, p.l, p.last, p.board),
          new Pos(p.x - 1, p.y, p.d + 1, p.l, p.last, p.board),
          new Pos(p.x, p.y + 1, p.d + 1, p.l, p.last, p.board),
          new Pos(p.x, p.y - 1, p.d + 1, p.l, p.last, p.board)
      };
      for (Pos pp : candidates) {
        if (end.canExit(pp)) {
          // pp.printPath("\n");
          return pp;
        }
        if (!board.on(pp)) {
          continue;
        }
        char cc = board.get(pp);
        if (cc == '#') {
          continue;
        }
        if (board.visited.contains(pp.x + "," + pp.y + "," + pp.l)) {
          continue;
        }
        if (cc == '.' || cc == '*') {
          bfs.add(pp);
          board.set(pp.x, pp.y, '*');
          board.visited.add(pp.x + "," + pp.y + "," + pp.l);
          continue;
        }
        if (Character.isLetter(cc)) {
          Portal portal = board.portals.get(pp.x, pp.y);
          if (portal != null && portal.canPass(pp)) {
            bfs.add(portal.pass(pp));
            board.visited.add(pp.x + "," + pp.y + "," + pp.l);
            board.visited.add(pp.x + "," + pp.y + "," + pp.l);
          }
        }
      }
    }

    return null;
  }

  private static class Board {

    int width, height;
    char[][] board;
    Portals portals = new Portals();
    Set<String> visited = new HashSet<>();

    public Board(int x, int y) {
      this.width = x;
      this.height = y;
      this.board = new char[height][width];
    }


    void set(int x, int y, char c) {
      board[y][x] = c;
    }

    char get(int x, int y) {
      return board[y][x];
    }

    char get(Pos pos) {
      return board[pos.y][pos.x];
    }

    static Board create(List<String> lines, int dl) {
      int w = getWidth(lines);
      Board board = new Board(w, lines.size());
      for (int y = 0; y < lines.size(); y++) {
        String line = lines.get(y);
        for (int x = 0; x < w; x++) {
          char c = (x < line.length() ? line.charAt(x) : ' ');
          board.set(x, y, c);
        }
      }

      // Gather portals
      for (int y = 0; y < board.height; y++) {
        for (int x = 0; x < board.width; x++) {
          if (Character.isLetter(board.get(x, y))) {
            int ex = x, ey = y;
            String name = "";
            if (x + 2 < board.width && board.isLetter(x + 1, y) && board.get(x + 2, y) == '.') {
              ex = x + 1;
              name = "" + board.get(x, y) + board.get(ex, y);
            } else if (x - 2 >= 0 && board.isLetter(x - 1, y) && board.get(x - 2, y) == '.') {
              ex = x - 1;
              name = "" + board.get(ex, y) + board.get(x, y);
            } else if (y + 2 < board.height && board.isLetter(x, y + 1) && board.get(x, y + 2) == '.') {
              ey = y + 1;
              name = "" + board.get(x, y) + board.get(x, ey);
            } else if (y - 2 >= 0 && board.isLetter(x, y - 1) && board.get(x, y - 2) == '.') {
              ey = y - 1;
              name = "" + board.get(x, ey) + board.get(x, y);
            } else {
              // throw new RuntimeException("CMH");
            }
            if (x != ex || y != ey) {
              Portal portal = board.portals.create(name, ex, ey, dl);
              portal.addEnd(ex, ey, x, y, board.width, board.height); // ...AB
            }
          }
        }
      }

      return board;
    }

    private static int getWidth(List<String> lines) {
      int w = 0;
      for (String line : lines) {
        w = Math.max(w, line.length());
      }
      return w;
    }


    void print() {

      System.out.println("-----------------------");
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          System.out.print(get(x, y));
        }
        System.out.println();
      }
      System.out.println();
      System.out.println(portals.byNames.keySet().stream()
          .map(n -> portals.byNames.get(n).toString())
          .collect(Collectors.joining(", ")));
    }

    public boolean on(Pos p) {
      return on(p.x, p.y);
    }

    public boolean on(int x, int y) {
      return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isLetter(Pos p) {
      return isLetter(p.x, p.y);
    }

    public boolean isLetter(int x, int y) {
      return Character.isLetter(get(x, y));
    }
  }

  private static class Pos {

    int x, y, d, l;
    Pos last;
    Board board;

    Pos(int x, int y) {
      this.x = x;
      this.y = y;
      this.d = 0;
      this.l = 0;
      this.last = null;
      this.board = null;
    }

    Pos(int x, int y, int d, int l, Pos lastPortal, Board board) {
      this.x = x;
      this.y = y;
      this.d = d;
      this.l = l;
      this.last = lastPortal;
      this.board = board;
    }

    public String toString() {
      return "(" + x + "," + y + ") " + " d=" + d + " l=" + l
             + (last == null ? "" : " from=" + board.portals.get(last.x, last.y).name
                                    + "(" + last.x + "," + last.y + "," + last.l + "d=" + d + ")");
    }

    private void printPath(String endl) {
      if (last != null) {
        last.printPath("   ->   ");
      }
      Portal prev = (last == null ? board.portals.get("AA") : board.portals.get(last.x, last.y));
      Portal curr = board.portals.get(x, y);
      System.out.print(prev.name + "->" + curr.name + " " + (d - (last == null ? 0 : last.d)) + " (" + x + "," + y + "," + l + ")" + endl);
    }

  }

  private static class Portals {

    HashMap<String, Portal> byNames = new HashMap<>();
    HashMap<String, Portal> byPos = new HashMap<>();

    Portal create(String name, int x, int y, int dl) {
      Portal portal = byNames.get(name);
      if (portal == null) {
        portal = new Portal(name, dl);
        byNames.put(name, portal);
      }
      String pos = x + "," + y;
      assert !byPos.containsKey(pos);
      byPos.put(pos, portal);
      return portal;
    }

    public Portal get(String name) {
      return byNames.get(name);
    }

    public Portal get(int x, int y) {
      return byPos.get(x + "," + y);
    }
  }

  private static class Portal {

    Pos[] inner = null;  // ...AB   in[A, B]
    Pos[] outer = null;  // AB...   in[B, A]
    String name;
    int dl;              // counter for level change, set to 1 for task 2

    public Portal(String name, int dl) {
      this.name = name;
      this.dl = dl;
    }

    public Portal addEnd(int x, int y, int xb, int yb, int w, int h) {
      if (xb == 0 || xb == w - 1 || yb == 0 || yb == h - 1) {
        assert outer == null;
        outer = new Pos[2];
        outer[0] = new Pos(x, y);
        outer[1] = new Pos(xb, yb);
      } else {
        assert inner == null;
        inner = new Pos[2];
        inner[0] = new Pos(x, y);
        inner[1] = new Pos(xb, yb);
      }
      return this;
    }

    public Pos pass(Pos p) {
      Pos pp;
      if (inner[0].x == p.x && inner[0].y == p.y) {
        p.board.visited.add(outer[0].x + "," + outer[0].y + "," + (p.l + dl));
        pp = new Pos(outer[0].x + (outer[0].x - outer[1].x), outer[0].y + (outer[0].y - outer[1].y), p.d, p.l + dl, p, p.board);
      } else if (outer[0].x == p.x && outer[0].y == p.y) {
        p.board.visited.add(inner[0].x + "," + inner[0].y + "," + (p.l - dl));
        pp = new Pos(inner[0].x + (inner[0].x - inner[1].x), inner[0].y + (inner[0].y - inner[1].y), p.d, p.l - dl, p, p.board);
      } else {
        throw new RuntimeException("CMH");
      }
      // System.out.println(p.board.portals.get(p.last.x, p.last.y).name + "->" + name + " " + (p.d - p.last.d) + " (" + pp.x + "," + pp.y + "," + pp.l + ")");
      return pp;
    }

    public Pos enter(Board board) {
      if (inner == null && outer != null) {
        return new Pos(outer[0].x + (outer[0].x - outer[1].x), outer[0].y + (outer[0].y - outer[1].y), 0, 0, new Pos(outer[0].x, outer[0].y, 0, 0, null, board), board);
      } else {
        throw new RuntimeException("CMH");
      }
    }

    public boolean canExit(Pos pp) {
      return inner == null && (outer != null && outer[0].x == pp.x && outer[0].y == pp.y && pp.l == 0);
    }

    public boolean canPass(Pos pp) {
      return (inner != null && outer != null)
             && (inner[0].x == pp.x && inner[0].y == pp.y
                 || outer[0].x == pp.x && outer[0].y == pp.y && (dl == 0 || pp.l > 0));
    }

    public String toString() {
      return name + " ("
             + (inner != null ? inner[0].x + "," + inner[0].y : "") + "<->"
             + (outer != null ? outer[0].x + "," + outer[0].y : "") + ")";
    }
  }
}
