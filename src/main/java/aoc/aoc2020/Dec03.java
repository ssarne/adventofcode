package aoc.aoc2020;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static aoc.utils.Utils.readAnswerAsInt;
import static aoc.utils.Utils.readAnswerAsLong;

import java.util.List;

public class Dec03 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() {
    check(doit1(getTestLines()), 7);
    check(doit2(getTestLines()), 336);
  }

  public static void task1() {
    long result = doit1(getLines());
    System.out.println("Result: " + result);
    check(result, readAnswerAsLong(1));
  }

  public static void task2() {
    long result = doit2(getLines());
    System.out.println("Result: " + result);
    check(result, readAnswerAsLong(2));
  }

  public static long doit1(List<String> input) {
    return doit(input, 1, 3);
  }

  public static long doit2(List<String> input) {
    return doit(input, 1, 1)
           * doit(input, 1, 3)
           * doit(input, 1, 5)
           * doit(input, 1, 7)
           * doit(input, 2, 1);
  }

  public static long doit(List<String> lines, int dy, int dx) {

    int width = lines.get(0).length();
    int trees = 0;

    for (int i = 0; true; i++) {
      int x = (i * dx) % width;
      int y = (i * dy);
      if (y >= lines.size()) {
        break;
      }
      char c = lines.get(y).charAt(x);
      if (c == '#') {
        trees++;
      }
    }

    return trees;
  }
}
