package aoc.aoc2018;

import static aoc.Utils.check;

import aoc.Utils;
import java.io.IOException;

public class AdventOfCode11 {

  static int[][] cells = new int[301][301];

  public static void init(int seed) {
    for (int x = 1; x <= 300; x++) {
      for (int y = 1; y <= 300; y++) {
        cells[x][y] = power(x, y, seed);
      }
    }
  }

  public static void test() throws IOException {
    init(57);
    Utils.check(power(122, 79), -5);

    init(39);
    Utils.check(power(217, 196), 0);

    init(71);
    Utils.check(power(101, 153), 4);
  }

  public static void task1() throws IOException {

    init(1133);

    int max = 0;
    int maxX = 0;
    int maxY = 0;
    int maxD = 0;

    for (int x = 1; x <= 288; x++) {
      for (int y = 1; y <= 288; y++) {
        int delta = 300 - ((int) Math.max(x, y));
        for (int d = 1; d < delta; d++) {

          int sum = powersquare(x, y, d);
          if (sum > max) {
            max = sum;
            maxX = x;
            maxY = y;
            maxD = d;
          }
        }
      }
    }

    System.out.println(maxX + "," + maxY + "," + maxD + ": " + max);
  }

  private static int powersquare(int x, int y, int d) {
    int sum = 0;
    for (int i = 0; i < d; i++) {
      for (int j = 0; j < d; j++) {
        sum += power(x + i, y + j);
      }
    }
    return sum;
  }

  private static int power(int x, int y) {
    return cells[x][y];
  }

  private static int power(int x, int y, int seed) {
    int id = x + 10;
    int p1 = id * y;
    int p2 = p1 + seed;
    int p3 = p2 * id;
    int d1 = p3 / 100;
    int d2 = d1 % 10;
    return d2 - 5;
  }

  public static void task2() throws IOException {}

  public static void main(String[] args) throws IOException {
    test();
    task1();
    task2();
  }
}
