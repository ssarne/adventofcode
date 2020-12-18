package aoc.utils;

import static aoc.utils.Utils.*;
import static java.lang.System.out;

import java.util.*;

public class AdventOfCodeTemplate {

  public static void main(String[] args) throws Exception {
    test();
    // task1();
    // task2();
  }

  public static void test() throws Exception {
    String result = doit("FIXME");  // aoc2019/decXX.txt"
    check(result, "FIXME");
  }

  public static void task1() throws Exception {
    String result = doit("FIXME");
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    String result = doit("FIXME");
    System.out.println("Result: " + result);
  }

  public static String doit(String input) throws Exception {

    List<String> lines = getLines(input);

    for (String line : lines) {
      // print(recipes, lenght, x, y);
    }

    String result = "FIXME";
    return result;
  }

  private static int calc(int i) {
    int result = 0;
    return result;
  }

  private static int calc2(int i) {
    int result = 0;
    return result;
  }

  public static void print() {
    out.println("");
  }
}
