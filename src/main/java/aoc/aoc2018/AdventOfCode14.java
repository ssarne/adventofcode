package aoc.aoc2018;

import static aoc.utils.Utils.*;

import java.io.IOException;
import java.util.*;

public class AdventOfCode14 {

  public static void main(String[] args) throws IOException {
    test();
    task1();
    task2();
  }

  public static void test() throws IOException {
    check(doit(9), "5158916779");
    check(doit(5), "0124515891");
    check(doit(18), "9251071085");
    check(doit(2018), "5941429882");

    int[] input1 = {5, 1, 5, 8, 9};
    check(doit2(input1), 9);
    int[] input2 = {0, 1, 2, 4, 5};
    check(doit2(input2), 5);
    int[] input3 = {9, 2, 5, 1, 0};
    check(doit2(input3), 18);
    int[] input4 = {5, 9, 4, 1, 4};
    check(doit2(input4), 2018);
  }

  public static void task1() throws IOException {
    String result = doit(635041);
    System.out.println("Result: " + result);
    check(result, "" + 1150511382);
  }

  public static void task2() throws IOException {
    int[] input = {6, 3, 5, 0, 4, 1};
    int result = doit2(input);
    System.out.println("Result: " + result);
    check(result, 20173656);
  }

  public static int doit2(int[] target) throws IOException {

    //                       37144409
    int[] recipes = new int[100000000];
    int length = 0;

    int x = 0;
    int y = 1;
    recipes[length++] = 3;
    recipes[length++] = 7;

    for (; ; ) {
      int sum = recipes[x] + recipes[y];
      if (sum >= 10) {
        recipes[length++] = sum / 10;
        recipes[length++] = sum % 10;
      } else {
        recipes[length++] = sum;
      }

      x = (x + 1 + recipes[x]) % length;
      y = (y + 1 + recipes[y]) % length;

      for (int o = 0; o < 4 && length >= target.length + o; o++) {
        if (Arrays.equals(
            recipes, length - target.length - o, length - o, target, 0, target.length)) {
          return length - o - target.length;
        }
      }
    }
  }

  public static String doit(int target) throws IOException {

    int[] recipes = new int[target + 10 + 10];
    int length = 0;

    int x = 0;
    int y = 1;
    recipes[length++] = 3;
    recipes[length++] = 7;

    while (length < target + 10) {
      int sum = recipes[x] + recipes[y];
      if (sum >= 10) {
        recipes[length++] = sum / 10;
        recipes[length++] = sum % 10;
      } else {
        recipes[length++] = sum;
      }

      x = (x + 1 + recipes[x]) % length;
      y = (y + 1 + recipes[y]) % length;

      //      print(recipes, lenght, x, y);
    }

    String result = "";
    for (int i = 0; i < 10; i++) {
      result += recipes[target + i];
    }
    return result;
  }

  public static void print(int[] recipes, int length, int x, int y) {
    for (int i = 0; i < length; i++) {
      if (i == x) {
        System.out.print("(" + recipes[i] + ")");
      } else if (i == y) {
        System.out.print("[" + recipes[i] + "]");
      } else {
        System.out.print(" " + recipes[i] + " ");
      }
    }
    System.out.println();
  }
}
