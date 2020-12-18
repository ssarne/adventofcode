package aoc.aoc2020;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;

import java.util.List;

public class Dec02 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    check(doit1("aoc2020/dec02_test.txt"), 1);
    check(doit2("aoc2020/dec02_test.txt"), 1);
  }

  public static void task1() throws Exception {
    int result = doit1("aoc2020/dec02.txt");
    check(result, 465);
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    int result = doit2("aoc2020/dec02.txt");
    check(result, 294);
    System.out.println("Result: " + result);
  }


  public static int doit1(String input) {

    List<String> lines = getLines(input);

    int valid = 0;
    for (int i = 0; i < lines.size() - 1; i++) {
      //  3-9 r: pplzctdrc
      String s = lines.get(i);
      int p1 = s.indexOf('-');
      int p2 = s.indexOf(' ');
      int p3 = s.indexOf(':');
      int low = Integer.parseInt(s.substring(0, p1));
      int high = Integer.parseInt(s.substring(p1 + 1, p2));
      char c = s.charAt(p3 - 1);
      String pwd = s.substring(p3 + 2);

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


  public static int doit2(String input) throws Exception {

    List<String> lines = getLines(input);
    int valid = 0;
    for (int i = 0; i < lines.size() - 1; i++) {
      //  3-9 r: pplzctdrc
      String s = lines.get(i);
      int p1 = s.indexOf('-');
      int p2 = s.indexOf(' ');
      int p3 = s.indexOf(':');
      int low = Integer.parseInt(s.substring(0, p1));
      int high = Integer.parseInt(s.substring(p1 + 1, p2));
      char c = s.charAt(p3 - 1);
      String pwd = s.substring(p3 + 2);

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
