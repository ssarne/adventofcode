package aoc.aoc2018;

import static aoc.Utils.check;
import static aoc.Utils.getLines;

import aoc.Utils;
import java.util.List;

public class AdventOfCode18 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    int result = doit("input18_test.txt", 10);
    Utils.check(result, 1147);
  }

  public static void task1() throws Exception {
    int result = doit("input18.txt", 10);
    System.out.println("Result: " + result);
    Utils.check(result, 505895);
  }

  public static void task2() throws Exception {
    int result = doit("input18.txt", 1000000000);
    System.out.println("Result: " + result);
  }

  public static int doit(String input, int turns) throws Exception {

    List<String> lines = getLines(input);
    int width = lines.get(0).length();
    int height = lines.size();
    char[][] area = new char[width][height];
    char[][] next = new char[width][height];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        area[x][y] = lines.get(y).charAt(x);
      }
    }
    print(area, 0);

    long step = Math.max(turns / 100, 1000L);
    long checkin = 10;
    int cycle = -1;
    int snapshotTurn = -1;
    char[][] snapshot = new char[width][height];

    for (int i = 0; i < turns; i++) {

      if (i == checkin) { // progress update
        print(area, i);
        checkin += Math.min(step, checkin);
        copy(area, snapshot);
        snapshotTurn = i;
      }

      if (snapshotTurn > 0 && i > snapshotTurn && equals(area, snapshot)) {
        cycle = i - snapshotTurn;
        while (i + cycle < turns) { // fast forward
          i = i + cycle;
        }
      }

      for (int y = 0; y < width; y++) {
        for (int x = 0; x < height; x++) {
          switch (area[x][y]) {
            case '.':
              {
                int trees = countTokens(area, x, y, width, height, '|');
                next[x][y] = trees >= 3 ? '|' : '.';
              }
              break;
            case '|':
              {
                int lumber = countTokens(area, x, y, width, height, '#');
                next[x][y] = lumber >= 3 ? '#' : '|';
              }
              break;
            case '#':
              {
                int lumber = countTokens(area, x, y, width, height, '#');
                int trees = countTokens(area, x, y, width, height, '|');
                next[x][y] = (trees >= 1 && lumber >= 1) ? '#' : '.';
              }
              break;
            default:
              throw new UnsupportedOperationException("ohps");
          }
        }
      }
      char[][] tmp = area;
      area = next;
      next = tmp;
      // print(area, i + 1);
    }

    print(area, turns);

    int trees = 0;
    int lumber = 0;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        switch (area[x][y]) {
          case '|':
            trees++;
            break;
          case '#':
            lumber++;
            break;
        }
      }
    }

    return trees * lumber;
  }

  private static void copy(char[][] area, char[][] snapshot) {
    for (int y = 0; y < area.length; y++) {
      for (int x = 0; x < area[0].length; x++) {
        snapshot[x][y] = area[x][y];
      }
    }
  }

  private static boolean equals(char[][] area, char[][] snapshot) {
    boolean match = true;
    for (int y = 0; y < area.length; y++) {
      for (int x = 0; x < area[0].length; x++) {
        if (area[x][y] != snapshot[x][y]) {
          match = false;
        }
      }
    }
    return match;
  }

  private static int countTokens(char[][] area, int x, int y, int width, int height, char c) {
    int tokens = 0;
    int ymin = Math.max(0, y - 1);
    int ymax = Math.min(y + 2, height);
    int xmin = Math.max(0, x - 1);
    int xmax = Math.min(x + 2, width);
    for (int y1 = ymin; y1 < ymax; y1++) {
      for (int x1 = xmin; x1 < xmax; x1++) {
        if (x1 == x && y1 == y) {
          continue;
        }
        if (area[x1][y1] == c) {
          tokens++;
        }
      }
    }
    return tokens;
  }

  public static void print(char[][] area, int turn) {
    System.out.println("Turn " + turn);
    for (int y = 0; y < area.length; y++) {
      for (int x = 0; x < area[0].length; x++) {
        System.out.print(area[x][y]);
      }
      System.out.println();
    }
  }
}
