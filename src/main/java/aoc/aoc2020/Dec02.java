package aoc.aoc2020;

import static aoc.utils.Utils.asInt;
import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static aoc.utils.Utils.readAnswerAsInt;

import java.util.List;

public class Dec02 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    check(doit1(getTestLines()), 2);
    check(doit2(getTestLines()), 1);
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


  public static int doit1(List<String> lines) {

    int valid = 0;
    for (String line : lines) {
      //  3-9 r: pplzctdrc
      var tokens = line.replace("-", " ").replace(":", "").split(" ");
      int low = asInt(tokens[0]);
      int high = asInt(tokens[1]);
      char c = tokens[2].charAt(0);
      String pwd = tokens[3];

      int count = 0;
      for (int j = 0; j < pwd.length(); j++) {
        if (pwd.charAt(j) == c) {
          count++;
        }
      }
      if (count >= low && count <= high) {
        valid++;
      }
    }

    return valid;
  }


  public static int doit2(List<String> lines) throws Exception {

    int valid = 0;
    for (String line : lines) {
      //  3-9 r: pplzctdrc
      var tokens = line.replace("-", " ").replace(":", "").split(" ");
      int low = asInt(tokens[0]);
      int high = asInt(tokens[1]);
      char c = tokens[2].charAt(0);
      String pwd = tokens[3];

      if (pwd.charAt(low - 1) == c && pwd.charAt(high - 1) != c) {
        valid++;
      }

      if (pwd.charAt(low - 1) != c && pwd.charAt(high - 1) == c) {
        valid++;
      }
    }

    return valid;
  }
}
