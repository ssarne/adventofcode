package aoc.aoc2019;

import static aoc.Utils.check;

public class Dec04 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {

    int[] test = {2, 4, 4, 7, 9, 9};
    check(true, match(test, test, test, true));

    int[] start = {2, 6, 4};
    int[] end = {8, 0, 3}; // {8, 0, 3, 9, 3, 5};
    String result = doit(start, end, true);
    System.out.println("Test result: " + result);
  }

  public static void task1() throws Exception {
    int[] start = {2, 6, 4, 7, 9, 3};
    int[] end = {8, 0, 3, 9, 3, 5};
    String result = doit(start, end, false);
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    int[] start = {2, 6, 4, 7, 9, 3};
    int[] end = {8, 0, 3, 9, 3, 5};
    String result = doit(start, end, true);
    System.out.println("Result: " + result);
  }

  public static String doit(int[] start, int[] end, boolean strict) throws Exception {

    int[] num = new int[start.length];
    int count = 0;

    for (int i = start[0]; i <= end[0]; i++) {
      num[0] = i;
      count += count(num, start, end, 1, strict);
    }
    String result = "" + count;
    return result;
  }

  public static int count(int[] num, int[] start, int[] end, int pos, boolean strict) {

    if (pos == num.length) {
      return (match(num, start, end, strict) ? 1 : 0);
    }

    int count = 0;
    for (int i = num[pos - 1]; i <= 9; i++) {
      num[pos] = i;
      count += count(num, start, end, pos + 1, strict);
    }
    return count;
  }

  static boolean match(int[] num, int[] start, int[] end, boolean strict) {

    boolean match = false;

    for (int i = 1; i < num.length; i++) {
      if (num[i - 1] == num[i]) {
        if (!strict
            || !(i > 1 && num[i - 2] == num[i] || i + 1 < num.length && num[i + 1] == num[i])) {
          match = true;
        }
      }
    }

    for (int i = 1; i < num.length; i++) {
      if (num[i - 1] > num[i]) {
        match = false;
      }
    }

    // check smaller/equal than end
    for (int i = 0; i < num.length; i++) {
      if (num[i] < end[i]) {
        break;
      }
      if (num[i] > end[i]) {
        match = false;
        break;
      }
      // same -> continue
    }

    // check larger/equal than start
    for (int i = 0; i < num.length; i++) {
      if (num[i] > start[i]) {
        break;
      }
      if (num[i] < start[i]) {
        match = false;
        break;
      }
      // same -> continue
    }

    //    if (match) {
    //      System.out.println("match: " + Arrays.toString(num));
    //    }
    return match;
  }
}
