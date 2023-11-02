package aoc.aoc2018;

import static aoc.utils.Utils.getLines;

import java.io.IOException;
import java.util.List;

public class AdventOfCode03 {

  public static void main(String[] args) throws IOException {
    System.out.println(task1());
    System.out.println(task2());
  }

  private static int[][] cloth = new int[1001][1001];
  private static int overlaps;

  public static int task1() throws IOException {

    List<String> lines = getLines();

    for (String line : lines) {
      Claim c = new Claim(line);
      for (int x = c.x; x < c.x + c.width; x++) {
        for (int y = c.y; y < c.y + c.height; y++) {
          if (cloth[x][y] == 0) {
            cloth[x][y] = c.id;
          } else if (cloth[x][y] == -1) {
            // do nothing
          } else {
            cloth[x][y] = -1;
            overlaps++;
          }
        }
      }
    }
    return overlaps;
  }

  public static int task2() throws IOException {

    List<String> lines = getLines();

    for (String line : lines) {
      Claim c = new Claim(line);
      boolean found = true;
      for (int x = c.x; x < c.x + c.width; x++) {
        for (int y = c.y; y < c.y + c.height; y++) {
          if (cloth[x][y] != c.id) {
            found = false;
          }
        }
      }
      if (found) {
        return c.id;
      }
    }
    throw new RuntimeException("CMH");
  }

  public static void print() {
    for (int x = 0; x < cloth.length; x++) {
      for (int y = 0; y < cloth[x].length; y++) {
        System.out.print(cloth[x][y] == -1 ? "X" : "" + cloth[x][y]);
      }
      System.out.println();
    }
    System.out.println();
  }

  private static class Claim {

    int id;
    int x;
    int y;
    int width;
    int height;

    public Claim(String input) {
      // #3 @ 5,5: 2x2
      // String pattern = "\\#(\\d+)";// @ (\\d+),(\\d+): (\\d+)x(\\d+)";
      // Pattern r = Pattern.compile(pattern);
      // Matcher m = r.matcher(input);
      // if (m.find()) {
      // id = Integer.parseInt(ids.substring(1));

      // #3 @ 5,5: 2x2
      String[] tokens = input.split(" ");
      String[] cordinates = tokens[2].split(",");
      String[] size = tokens[3].split("x");
      id = Integer.parseInt(tokens[0].substring(1));
      x = Integer.parseInt(cordinates[0]);
      y = Integer.parseInt(cordinates[1].substring(0, cordinates[1].length() - 1));
      width = Integer.parseInt(size[0]);
      height = Integer.parseInt(size[1]);
    }
  }

}
