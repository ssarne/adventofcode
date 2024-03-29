package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.readAnswerAsInt;

import java.util.List;
import java.util.function.Function;

public class Dec01 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    check(calc(12), 2);
    check(calc(14), 2);
    check(calc(1969), 654);
    check(calc(100756), 33583);

    check(calc2(1969), 966);
    check(calc2(100756), 50346);
  }

  public static void task1() throws Exception {
    int result = doit(Dec01::calc, getLines());
    check(result, readAnswerAsInt(1));
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    int result = doit(Dec01::calc2, getLines());
    check(result, readAnswerAsInt(2));
    System.out.println("Result: " + result);
  }

  public static int doit(Function<Integer, Integer> fn, List<String> lines) throws Exception {

    int sum = 0;
    for (String line : lines) {
      int i = Integer.parseInt(line);
      sum += fn.apply(i);
    }
    return sum;
  }

  private static int calc(int i) {
    int result = i / 3;
    result = result - 2;
    if (result < 0) {
      result = 0;
    }
    return result;
  }

  private static int calc2(int i) {
    if (i <= 0) {
      return 0;
    }
    int result = calc(i);
    return result + calc2(result);
  }
}
