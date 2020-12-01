package aoc.aoc2018;

import java.io.IOException;

public class AdventOfCode09 {

  private static long play(int players, int marbles) {

    // init
    Circle circle = new Circle(marbles);
    circle.add(0);
    circle.add(2);
    circle.add(1);
    long[] scores = new long[players + 1];
    int player = 2;
    int current = 1;
    // print(circle, current, player);

    game(players, marbles, circle, scores, player, current);

    long max = 0;
    for (int i = 0; i < scores.length; i++) {
      if (scores[i] > max) {
        max = scores[i];
      }
    }
    return max;
  }

  private static void game(
      int players, int marbles, Circle circle, long[] scores, int player, int current) {

    int progress = Math.max(marbles / 100, 10000);

    for (int i = 3; i <= marbles; i++) {
      int marble = i;
      player = (player >= players ? 1 : player + 1);

      if (marble % 23 == 0) {
        scores[player] += marble;
        current = (current - 7 >= 0 ? current - 7 : current - 7 + circle.size());
        scores[player] += circle.remove(current);
      } else {
        current = (current + 2 < circle.size() + 1 ? current + 2 : current + 2 - circle.size());
        if (current < circle.size()) {
          circle.add(current, marble);
        } else {
          circle.add(marble);
        }
      }
      if (i == progress) {
        System.out.print(".");
        progress += marbles / 100;
      }
      // print(circle, current, player);
    }
  }

  public static void test() throws IOException {
    check(play(9, 27), 32);

    check(play(10, 1618), 8317);
    check(play(13, 7999), 146373);
    check(play(17, 1104), 2764);
    System.out.println("test 1");
    for (int i = 0; i < 10000; i++) { // warmup
      check(play(9, 27), 32);
    }
    System.out.println("test 2");
    check(play(21, 6111), 54718);
    check(play(30, 5807), 37305);
    System.out.println("test 3");
  }

  public static void task1() throws IOException {
    System.out.println("\nWinning elf has score: " + play(405, 70953));
  }

  public static void task2() throws IOException {
    System.out.println("Winning elf has score: " + play(405, 100 * 70953));
  }

  public static void main(String[] args) throws IOException {
    test();
    task1();
    task2();
  }

  private static void check(long actual, long expected) {
    if (actual != expected) {
      System.err.println("Failure: actual=" + actual + "  expected=" + expected);
    }
  }

  static void print(Circle circle, int current, int marble) {
    System.out.print("[" + marble + "]");
    for (int i = 0; i < circle.size(); i++) {
      if (i == current) {
        System.out.printf(" (" + circle.get(i) + ")");
      } else {
        System.out.printf("  " + circle.get(i) + " ");
      }
    }
    System.out.println();
  }

  static class Circle {

    int[] array;
    int size = 0;

    public Circle(int maxSize) {
      array = new int[maxSize];
    }

    public void add(int i) {
      add(size, i);
    }

    public void add(int pos, int i) {
      if (pos == size) {
        array[pos] = i;
      } else {
        System.arraycopy(array, pos, array, pos + 1, size - pos);
        array[pos] = i;
      }
      size++;
    }

    public int remove(int pos) {
      int ret = array[pos];
      System.arraycopy(array, pos + 1, array, pos, size - pos);
      size--;
      return ret;
    }

    public int size() {
      return size;
    }

    public int get(int i) {
      return array[i];
    }
  }
}
