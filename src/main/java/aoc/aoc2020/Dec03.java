package aoc.aoc2020;

import static aoc.Utils.check;
import static aoc.Utils.getLines;

import java.util.List;

public class Dec03 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() {
    check(doit1("aoc2020/dec03_test.txt"), 7);
    check(doit2("aoc2020/dec03_test.txt"), 336);
  }

  public static void task1() {
    int result = doit1("aoc2020/dec03.txt");
    check(result, 148);
    System.out.println("Result: " + result);
  }

  public static void task2() {
    int result = doit2("aoc2020/dec03.txt");
    check(result, 727923200);
    System.out.println("Result: " + result);
  }

  public static int doit1(String input) {
    return doit(input, 1, 3);
  }

  public static int doit2(String input) {
    return doit(input, 1, 1)
           * doit(input, 1, 3)
           * doit(input, 1, 5)
           * doit(input, 1, 7)
           * doit(input, 2, 1);
  }

  public static int doit(String input, int dy, int dx) {

    List<String> lines = getLines(input);
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
