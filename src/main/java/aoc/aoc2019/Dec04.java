package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.readAnswerAsInt;

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
    int[] end = {8, 0, 3};
    int result = doit(start, end, true);
    check(result, 44);
  }

  public static void task1() throws Exception {
    // Your puzzle input is 356261-846303.
    int[] start = {3,5,6,2,6,1};
    int[] end = {8,4,6,3,0,3};
    int result = doit(start, end, false);
    System.out.println("Result: " + result);
    check(result, readAnswerAsInt(1));
  }

  public static void task2() throws Exception {
    // Your puzzle input is 356261-846303.
    int[] start = {3,5,6,2,6,1};
    int[] end = {8,4,6,3,0,3};
    int result = doit(start, end, true);
    System.out.println("Result: " + result);
    check(result, readAnswerAsInt(2));
  }

  public static int doit(int[] start, int[] end, boolean strict) throws Exception {

    int[] num = new int[start.length];
    int count = 0;

    for (int i = start[0]; i <= end[0]; i++) {
      num[0] = i;
      count += count(num, start, end, 1, strict);
    }
    return count;
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
