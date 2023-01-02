package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Dec15 {

  public static void main(String[] args) throws Exception {
    doit();
  }

  public static void doit() throws Exception {

    IntCode.Program program = IntCode.Program.create(getLines().get(0));
    HashMap<String, Character> board = new HashMap<>();
    board.put("0,0", '.');

    // north (1), south (2), west (3), and east (4).
    explore(program, board, 0, 1, 1);
    explore(program, board, 0, -1, 2);
    explore(program, board, -1, 0, 3);
    explore(program, board, 1, 0, 4);

    // print(board, 0, 0);

    Pos pos = findOxygen(board);
    System.out.println("Result: " + pos.d);

    int max = fillOxygen(board, pos);
    System.out.println("Result: " + max);

  }

  private static int fillOxygen(HashMap<String, Character> board, Pos pos) {

    Queue<Pos> queue = new LinkedList<>();
    queue.add(new Pos(pos.x, pos.y, 0));

    int max = 0;

    while (!queue.isEmpty()) {
      Pos p = queue.poll();
      char c = board.get(p.x + "," + p.y);
      if (c == '#') {
        continue;
      }
      if (c == 'o' && p.d != 0) {
        continue;
      }

      if (!(c == '_' || c == '.' || (p.d == 0))) {
        throw new RuntimeException("CMH");
      }

      max = Math.max(max, p.d);
      board.put(p.x + "," + p.y, 'o');
      // print(board, p.x, p.y);

      queue.add(new Pos(p.x - 1, p.y, p.d + 1));
      queue.add(new Pos(p.x + 1, p.y, p.d + 1));
      queue.add(new Pos(p.x, p.y + 1, p.d + 1));
      queue.add(new Pos(p.x, p.y - 1, p.d + 1));
    }
    return max;
  }

  private static Pos findOxygen(HashMap<String, Character> board) {

    Queue<Pos> queue = new LinkedList<>();
    queue.add(new Pos(0, 0, 0));
    Pos pos = null;

    while (!queue.isEmpty()) {
      Pos p = queue.poll();
      char c = board.get(p.x + "," + p.y);
      if (c == '#') continue;
      if (c == '_') continue;
      if (c == 'o') {
        pos = p;
        break;
      }

      board.put(p.x + "," + p.y, '_');
      // print(board, p.x, p.y);

      queue.add(new Pos(p.x - 1, p.y, p.d + 1));
      queue.add(new Pos(p.x + 1, p.y, p.d + 1));
      queue.add(new Pos(p.x, p.y + 1, p.d + 1));
      queue.add(new Pos(p.x, p.y - 1, p.d + 1));
    }
    return pos;
  }


  static void explore(IntCode.Program p, HashMap<String, Character> board, int x, int y, long dir) {
    if (board.containsKey(pos(x, y))) {
      return; // exploration done already
    }

    // print(board, x, y);

    p.input.add(dir);
    IntCode.execute(p);
    long r = p.output.poll();

    if (r == 0) {
      board.put(pos(x, y), '#');
      return;
    }

    if (r == 2L) {
      board.put(pos(x, y), 'o');
    } else if (r == 1L) {
      board.put(pos(x, y), '.');
    } else {
      throw new RuntimeException("CMH");
    }

    explore(p, board, x, y+1, 1);
    explore(p, board, x, y-1, 2);
    explore(p, board, x-1, y, 3);
    explore(p, board, x+1, y, 4);

    p.input.add(back(dir));
    IntCode.execute(p);
    r = p.output.poll();
    if (r != 1L) {
      throw new RuntimeException("WAT");
    }
  }

  // north (1), south (2), west (3), and east (4).
  private static long back(long dir) {
    if (dir == 1L) {
      return 2L;
    }
    if (dir == 2L) {
      return 1L;
    }
    if (dir == 3L) {
      return 4L;
    }
    if (dir == 4L) {
      return 3L;
    }
    return 0L;
  }

  private static String pos(long x, long y) {
    return x + "," + y;
  }

  public static void print(HashMap<String, Character> board, int rx, int ry) {
    int minx = 0, maxx = 0, miny = 0, maxy = 0;
    for (String panel : board.keySet()) {
      minx = Math.min(minx, Integer.parseInt(panel.split(",")[0]));
      maxx = Math.max(maxx, Integer.parseInt(panel.split(",")[0]));
      miny = Math.min(miny, Integer.parseInt(panel.split(",")[1]));
      maxy = Math.max(maxy, Integer.parseInt(panel.split(",")[1]));
    }

    System.out.println("--(" + rx + "," + ry + ")---------------------");
    for (int y = miny; y <= maxy; y++) {
      for (int x = minx; x <= maxx; x++) {
        if (x == rx && y == ry) {
          System.out.print('R');
        } else if (board.containsKey(pos(x, y))) {
          System.out.print(board.get(pos(x, y)));
        } else {
          System.out.print(' ');
        }
      }
      System.out.println();
    }
  }


  private static class Pos {
    int x, y, d;
    public Pos(int x, int y, int d) {
      this.x = x;
      this.y = y;
      this.d = d;
    }
  }
}
