package aoc.aoc2020;

import static aoc.Utils.*;

import java.util.*;

public class Dec09 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() {
    check(solve1("aoc2020/dec09_test.txt", 5), 127);
    check(solve2("aoc2020/dec09_test.txt", 5), 62);
  }

  public static void task1() {
    long result = solve1("aoc2020/dec09.txt", 25);
    check(result, 507622668);
    System.out.println("Result: " + result);
  }

  public static void task2() {
    long result = solve2("aoc2020/dec09.txt", 25);
    check(result, 76688505);
    System.out.println("Result: " + result);
  }

  public static long solve1(String input, int n) {

    long[] numbers = getNumbers(input);
    return findFirstNonSum(n, numbers);
  }

  private static long findFirstNonSum(int n, long[] numbers) {
    for (int i = n; i < numbers.length; i++) {
      boolean match = false;
      for (int j = i - n; !match && j < i-1; j++) {
        for (int k = j + 1; !match && k < i; k++) {
          if (numbers[i] == numbers[j] + numbers[k]) {
            match = true;
          }
        }
      }

      if (!match) {
        return numbers[i];
      }
    }
    return -1;
  }

  public static long solve2(String input, int n) {
    long[] numbers = getNumbers(input);
    long target = findFirstNonSum(n, numbers);

    for (int i = 0; i < numbers.length; i++) {
      long sum = 0;
      for (int j = i; j < numbers.length && sum < target; j++) {
        sum += numbers[j];
        if (sum == target) {
          long min = numbers[i];
          long max = numbers[i];
          for (int k = i; k <= j; k++) {
            min = Math.min(min, numbers[k]);
            max = Math.max(max, numbers[k]);
          }
          return min + max;
        }
      }
    }
    return -1;
  }
}
