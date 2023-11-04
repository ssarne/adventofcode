package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.readAnswerAsInt;
import static aoc.utils.Utils.readAnswerAsLong;

import java.util.HashMap;

public class Dec19 {

  public static void main(String[] args) throws Exception {
    task1();
    task2();
  }

  public static void task1() throws Exception {
    HashMap<String, Character> board = new HashMap<>();
    String input = getLines().get(0);
    int count = 0;
    for (long x = 0; x < 50; x++) {
      for (long y = 0; y < 50; y++) {
        long out = inBeam(input, x, y);
        if (out == 1L) {
          count++;
        }
        board.put(x + "," + y, (out == 1L ? '#' : '.'));
      }
    }
    // Result 1: 213
    System.out.println("Result 1: " + count);
    check(count, readAnswerAsInt(1));
  }

  public static void task2() throws Exception {

    String input = getLines().get(0);
    long x = 0, y = 100;
    for (; true; y++) {
      for (; true; x++) {
        long out = inBeam(input, x, y);
        if (out == 1L) {
          break;
        }
      }
      long out = inBeam(input, x + 99, y - 99);
      if (out == 1L) {
        break;
      }
    }
    long count = 10000 * x + (y - 99);
    // Result 2: 7830987
    System.out.println("Result 2: " + count);
    check(count, readAnswerAsLong(2));
  }

  private static long inBeam(String input, long x, long y) {
    IntCode.Program program1 = IntCode.Program.create(input);
    program1.input.add(x);
    program1.input.add(y);
    IntCode.execute(program1);
    return program1.output.poll();
  }

  public static void print(HashMap<String, Character> board) {
    long minx = 0, maxx = 0, miny = 0, maxy = 0;
    for (String pos : board.keySet()) {
      int x = Integer.parseInt(pos.split(",")[0]);
      int y = Integer.parseInt(pos.split(",")[1]);
      minx = Math.min(minx, x);
      maxx = Math.max(maxx, x);
      miny = Math.min(miny, y);
      maxy = Math.max(maxy, y);
    }

    System.out.println("-----------------------");
    for (long y = miny; y <= maxy; y++) {
      for (long x = minx; x <= maxx; x++) {
        if (board.containsKey(x + "," + y)) {
          System.out.print(board.get(x + "," + y));
        } else {
          System.out.print(' ');
        }
      }
      System.out.println();
    }
  }
}

