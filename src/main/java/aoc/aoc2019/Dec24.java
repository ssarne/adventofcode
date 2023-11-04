package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static aoc.utils.Utils.readAnswerAsInt;
import static aoc.utils.Utils.readAnswerAsLong;

import java.util.HashSet;
import java.util.List;

public class Dec24 {

  public static void main(String[] args) throws Exception {
    test1();
    task1();
    test2();
    task2();
  }

  public static void test1() throws Exception {
    Board board = doit1(getTestLines(1));
    // board.print();
    check(board.board, 2129920);
  }


  public static void task1() throws Exception {
    Board board = doit1(getLines());
    // board.print();
    System.out.println("Result 1: " + board.board);
    check(board.board, readAnswerAsInt(1));
  }

  public static Board doit1(List<String> input) throws Exception {

    Board board = Board.create(input);
    HashSet<Board> history = new HashSet<>();
    for (int i = 0; true; i++) {
      if (history.contains(board)) {
        // System.out.println("Match after: " + i);
        return board;
      }
      history.add(board);
      board = board.next();
    }
  }


  public static void test2() throws Exception {
    Board3D board = doit2(getLines(), 10);
    int result = board.count();
    check(result, 107);
  }

  public static void task2() throws Exception {
    Board3D board = doit2(getLines(), 200);
    int result = board.count();
    System.out.println("Result 2: " + result);
    check(result, readAnswerAsInt(2));
  }

  public static Board3D doit2(List<String> input, int reps) throws Exception {

    Board3D board = Board3D.create(input);
    for (int i = 0; i < reps; i++) {
      // board.print("time=" + i);
      board = board.next();
    }
    // board.print("time=" + reps);
    return board;
  }

  private static class Board {

    static int width = 5;
    static int height = 5;
    int board = 0;

    public Board() {
    }

    void set(int x, int y, char c) {
      set(x, y, c == '#');
    }

    void set(int x, int y, boolean c) {
      int pos = 5 * y + x;
      board |= (c ? 1 : 0) << pos;
    }

    boolean val(int x, int y) {
      int pos = 5 * y + x;
      return (board & (1 << pos)) > 0;
    }

    char get(int x, int y) {
      return val(x, y) ? '#' : '.';
    }

    public Board next() {
      Board next = new Board();
      for (int y = 0; y < Board.height; y++) {
        for (int x = 0; x < Board.width; x++) {
          boolean v = val(x, y);
          int n = 0;
          if (x > 0 && val(x - 1, y)) {
            n++;
          }
          if (x < 4 && val(x + 1, y)) {
            n++;
          }
          if (y < 4 && val(x, y + 1)) {
            n++;
          }
          if (y > 0 && val(x, y - 1)) {
            n++;
          }

          if (v && n == 1) {
            next.set(x, y, true);
          } else if (!v && (n == 1 || n == 2)) {
            next.set(x, y, true);
          }

        }
      }
      return next;
    }

    static Board create(List<String> input) {
      // Board board = new Board(w, lines.size());
      Board board = new Board();
      for (int y = 0; y < Board.height; y++) {
        String line = input.get(y);
        for (int x = 0; x < Board.width; x++) {
          char c = (x < line.length() ? line.charAt(x) : ' ');
          board.set(x, y, c);
        }
      }

      return board;
    }

    void print() {
      System.out.println("-----------------------");
      System.out.println(this.toString());
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          sb.append(get(x, y));
        }
        sb.append("\n");
      }
      return sb.toString();
    }

    @Override
    public int hashCode() {
      return board;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Board)) {
        return false;
      }
      Board that = (Board) obj;
      return this.board == that.board;
    }
  }

  private static class Board3D {

    static int width = 5;
    static int height = 5;
    static int level0 = 200;
    int[] boards = new int[401];

    public Board3D() {
    }

    void set(int x, int y, int l, char c) {
      set(x, y, l, c == '#');
    }

    void set(int x, int y, int l, boolean c) {
      int pos = 5 * y + x;
      boards[level0 + l] |= (c ? 1 : 0) << pos;
    }

    boolean val(int x, int y, int l) {
      int pos = 5 * y + x;
      return (boards[level0 + l] & (1 << pos)) > 0;
    }

    char get(int x, int y, int l) {
      if (x == 2 && y == 2) {
        return '?';
      }
      return val(x, y, l) ? '#' : '.';
    }

    public Board3D next() {
      Board3D next = new Board3D();
      for (int l = -199; l <= 199; l++) {
        if (boards[level0 + l - 1] == 0 && boards[level0 + l] == 0 && boards[level0 + l + 1] == 0) {
          continue;
        }
        for (int y = 0; y < Board.height; y++) {
          for (int x = 0; x < Board.width; x++) {

            if (x == 2 && y == 2) {
              continue;
            }

            int n = 0;

            // x+1, y
            if (x == 1 && y == 2) {
              for (int i = 0; i < height; i++) {
                if (val(0, i, l + 1)) {
                  n++;
                }
              }
            } else if (x == 4) {
              if (val(3, 2, l - 1)) {
                n++;
              }
            } else {
              if (val(x + 1, y, l)) {
                n++;
              }
            }

            // x-1, y
            if (x == 3 && y == 2) {
              for (int i = 0; i < height; i++) {
                if (val(4, i, l + 1)) {
                  n++;
                }
              }
            } else if (x == 0) {
              if (val(1, 2, l - 1)) {
                n++;
              }
            } else {
              if (val(x - 1, y, l)) {
                n++;
              }
            }

            // x, y+1
            if (x == 2 && y == 1) {
              for (int i = 0; i < width; i++) {
                if (val(i, 0, l + 1)) {
                  n++;
                }
              }
            } else if (y == 4) {
              if (val(2, 3, l - 1)) {
                n++;
              }
            } else {
              if (val(x, y + 1, l)) {
                n++;
              }
            }

            // x, y-1
            if (x == 2 && y == 3) {
              for (int i = 0; i < width; i++) {
                if (val(i, 4, l + 1)) {
                  n++;
                }
              }
            } else if (y == 0) {
              if (val(2, 1, l - 1)) {
                n++;
              }
            } else {
              if (val(x, y - 1, l)) {
                n++;
              }
            }

            // Update
            boolean v = val(x, y, l);
            if (v && n == 1) {
              next.set(x, y, l, true);
            } else if (!v && (n == 1 || n == 2)) {
              next.set(x, y, l, true);
            }
          }
        }
      }

      return next;
    }

    static Board3D create(List<String> input) {
      Board3D board = new Board3D();
      for (int y = 0; y < Board.height; y++) {
        String line = input.get(y);
        for (int x = 0; x < Board.width; x++) {
          char c = (x < line.length() ? line.charAt(x) : ' ');
          board.set(x, y, 0, c);
        }
      }

      return board;
    }

    public int count() {
      int n = 0;
      for (int l = -199; l < 200; l++) {
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            if (val(x, y, l)) {
              n++;
            }

          }
        }
      }
      return n;
    }

    public void print(String label) {
      System.out.println("---  " + label + "  ---");
      for (int l = -199; l < 200; l++) {
        if (boards[l + level0] > 0) {
          System.out.println("level=" + l);
          StringBuilder sb = new StringBuilder();
          for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
              sb.append(get(x, y, l));
            }
            sb.append("\n");
          }
          System.out.println(sb.toString());
        }
      }
    }
  }
}
