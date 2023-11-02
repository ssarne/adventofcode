package aoc.aoc2018;

import aoc.utils.CircularBuffer;

import static aoc.utils.Utils.check;

import java.io.IOException;

public class AdventOfCode09 {

  public static void main(String[] args) throws IOException {
    test();
    System.out.println(play(405, 70953));
    System.out.println(play(405, 100 * 70953));
  }

  public static void test() throws IOException {
    check(play(9, 27), 32);
    check(play(10, 1618), 8317);
    check(play(13, 7999), 146373);
    check(play(17, 1104), 2764);
    check(play(21, 6111), 54718);
    check(play(30, 5807), 37305);
  }

  private static long play(int players, int marbles) {

    CircularBuffer circle = new CircularBuffer(0);
    circle.add(1, 1);
    circle.add(2, 1);
    long[] scores = new long[players + 1];
    int player = 2;

    game(players, marbles, circle, scores, player);

    long max = 0;
    for (int i = 0; i < scores.length; i++) {
      if (scores[i] > max) {
        max = scores[i];
      }
    }
    return max;
  }

  private static void game(int players, int marbles, CircularBuffer circle, long[] scores, int player) {

    for (int i = 3; i <= marbles; i++) {

      // circle.print();

      int marble = i;
      player = (player >= players ? 1 : player + 1);

      if (marble % 23 == 0) {
        scores[player] += marble;
        scores[player] += circle.remove(-7);
      } else {
        circle.add(marble, 1);
      }
    }
  }
}
