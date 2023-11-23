package aoc.aoc2020;


import static aoc.utils.Utils.*;

import java.util.*;
import java.util.function.Function;

public class Dec05 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }


  public static void test() {
    check(doit(getTestLines(), Dec05::calc1), 820);
    check(doit(getTestLines(), Dec05::calc2), 1);
  }

  public static void task1() {
    var result = doit(getLines(), Dec05::calc1);
    System.out.println("Result: " + result);
    check(result, readAnswerAsInt(1));
  }

  public static void task2() {
    int result = doit(getLines(), Dec05::calc2);
    System.out.println("Result: " + result);
    check(result, readAnswerAsInt(2));
  }

  public static int doit(List<String> lines, Function<HashMap, Integer> calc) {

    HashMap<Integer, String> seats = new HashMap<>();
    for (String line : lines) {
      int row = binary(line, 'F', 'B', 0, 127, 0, 7);
      int seat = binary(line, 'L', 'R', 0, 7, 7, 10);
      int id = row * 8 + seat;
      seats.put(id, line);
    }

    return calc.apply(seats);
  }

  private static int binary(String line, char l, char h, int low, int high, int start, int end) {
    int row;
    for (int i = start; i < end; i++) {
      if (line.charAt(i) == h) {
        low = 1 + (low + high) / 2;
      } else {
        ensure(line.charAt(i) == l);
        high = (low + high) / 2;
      }
    }
    row = low;
    return row;
  }

  private static int calc1(HashMap<Integer, String> seats) {
    int max = 0;
    for (int id : seats.keySet()) {
      max = Math.max(max, id);
    }
    return max;
  }

  private static int calc2(HashMap<String, String> seats) {
    for (int i = 0; i < 127 * 8 + 8; i++) {
      if (!seats.containsKey(i)) {
        if (seats.containsKey(i - 1) && seats.containsKey(i + 1)) {
          return i;
        }
      }
    }
    return -1;
  }
}
