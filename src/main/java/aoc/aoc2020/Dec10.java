package aoc.aoc2020;

import static aoc.utils.Utils.*;

import java.util.*;

public class Dec10 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() {
    check(solve1(getTestLines(1)), 35);
    check(solve1(getTestLines(2)), 220);
    check(solve2(getTestLines(1)), 8);
    check(solve2(getTestLines(2)), 19208);
  }

  public static void task1() {
    int result = solve1(getLines());
    System.out.println("Result: " + result);
    check(result, readAnswerAsInt(1));
  }

  public static void task2() {
    long result = solve2(getLines());
    System.out.println("Result: " + result);
    check(result, readAnswerAsLong(2));
  }

  public static int solve1(List<String> input) {
    int[] numbers = getPaddedInput(input);
    return adapt(numbers, 1, 0, 0, 0, 0, 0);
  }

  private static int[] getPaddedInput(List<String> input) {
    int[] n1 = getInts(input);
    int[] n2 = new int[n1.length + 2];
    Arrays.sort(n1);
    n2[0] = 0;
    n2[n2.length - 1] = n1[n1.length - 1] + 3;
    System.arraycopy(n1, 0, n2, 1, n1.length);
    return n2;
  }

  private static int adapt(int[] numbers, int n, int c, int... usage) {
    if (n == numbers.length) {
      return usage[1] * usage[3];
    }

    int diff = numbers[n] - c;
    ensure(diff <= 3 || diff >= 0);
    usage[diff]++;
    return adapt(numbers, n + 1, numbers[n], usage);
  }

  public static long solve2(List<String> input) {

    int[] numbers = getPaddedInput(input);
    long[] perms = new long[numbers.length];
    perms[0] = 1;

    for (int n = 1; n < numbers.length; n++) {
      if (n >= 3 && numbers[n] - numbers[n-3] <= 3) {
        perms[n] += perms[n-3];
      }
      if (n >= 2 && numbers[n] - numbers[n-2] <= 3) {
        perms[n] += perms[n-2];
      }
      if (n >= 1 && numbers[n] - numbers[n-1] <= 3) {
        perms[n] += perms[n-1];
      }
    }

    return perms[numbers.length - 1];
  }
}
