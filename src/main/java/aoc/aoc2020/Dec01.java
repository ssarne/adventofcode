package aoc.aoc2020;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;

import java.util.List;

public class Dec01 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    check(doit1("aoc2020/dec01_test.txt"), 514579);
    check(doit2("aoc2020/dec01_test.txt"), 241861950);
  }

  public static void task1() throws Exception {
    int result = doit1("aoc2020/dec01.txt");
    check(result, 858496);
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    int result = doit2("aoc2020/dec01.txt");
    check(result, 263819430);
    System.out.println("Result: " + result);
  }


  public static int doit1(String input) {

    List<String> lines = getLines(input);

    for (int i = 0; i < lines.size() - 1; i++) {
      int i1 = Integer.parseInt(lines.get(i));
      for (int j = i+1; j < lines.size(); j++) {
        int i2 = Integer.parseInt(lines.get(j));
        if (i1 + i2 == 2020) {
          return i1 * i2;
        }
      }
    }

    return -1;
  }


  public static int doit2(String input) throws Exception {

    List<String> lines = getLines(input);

    for (int i = 0; i < lines.size() - 1; i++) {
      int i1 = Integer.parseInt(lines.get(i));
      for (int j = i+1; j < lines.size(); j++) {
        int i2 = Integer.parseInt(lines.get(j));
        for (int k = j+1; k < lines.size(); k++) {
          int i3 = Integer.parseInt(lines.get(k));
          if (i1 + i2 + i3 == 2020) {
            return i1 * i2 * i3;
          }
        }
      }
    }

    return -1;
  }
}
