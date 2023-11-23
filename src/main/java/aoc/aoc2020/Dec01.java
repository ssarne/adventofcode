package aoc.aoc2020;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static aoc.utils.Utils.readAnswerAsInt;

import java.util.List;

public class Dec01 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    check(doit1(getTestLines()), 514579);
    check(doit2(getTestLines()), 241861950);
  }

  public static void task1() throws Exception {
    int result = doit1(getLines());
    System.out.println("Result: " + result);
    check(result, readAnswerAsInt(1));
  }

  public static void task2() throws Exception {
    int result = doit2(getLines());
    System.out.println("Result: " + result);
    check(result, readAnswerAsInt(2));
  }


  public static int doit1(List<String> input) {

    for (int i = 0; i < input.size() - 1; i++) {
      int i1 = Integer.parseInt(input.get(i));
      for (int j = i+1; j < input.size(); j++) {
        int i2 = Integer.parseInt(input.get(j));
        if (i1 + i2 == 2020) {
          return i1 * i2;
        }
      }
    }

    return -1;
  }


  public static int doit2(List<String> input) throws Exception {

    for (int i = 0; i < input.size() - 1; i++) {
      int i1 = Integer.parseInt(input.get(i));
      for (int j = i+1; j < input.size(); j++) {
        int i2 = Integer.parseInt(input.get(j));
        for (int k = j+1; k < input.size(); k++) {
          int i3 = Integer.parseInt(input.get(k));
          if (i1 + i2 + i3 == 2020) {
            return i1 * i2 * i3;
          }
        }
      }
    }

    return -1;
  }
}
