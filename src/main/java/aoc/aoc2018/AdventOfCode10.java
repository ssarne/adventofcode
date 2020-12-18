package aoc.aoc2018;

import static aoc.utils.Utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdventOfCode10 {

  public static void main(String[] args) throws IOException {
    test();
    task1();
  }

  public static void test() throws IOException {
    List<String> lines = getLines("input10_test.txt");
    Sky sky = parse(lines);
    int time = getTime(sky);
    System.out.println("t=" + time);
    if (time != -1) {
      sky.print(time);
    }
  }

  public static void task1() throws IOException {
    List<String> lines = getLines("input10.txt");
    Sky sky = parse(lines);
    int time = getTime(sky);
    System.out.println("t=" + time);
    if (time != -1) {
      sky.print(time);
    }
  }

  private static int getTime(Sky sky) {
    int[] dx = new int[5];
    for (int t = 0; t < 100000; t++) {
      int[] dim = sky.getDimensions(t);
      dx[t % 5] = dim[1] - dim[0];
      if (growing(dx, t)) {
        return t - 4;
      }
    }
    return -1;
  }

  private static boolean growing(int[] dx, int t) {
    int l = dx.length;
    return dx[(l + t - 4) % l] < dx[(l + t - 3) % l]
        && dx[(l + t - 3) % l] < dx[(l + t - 2) % l]
        && dx[(l + t - 2) % l] < dx[(l + t - 1) % l]
        && dx[(l + t - 1) % l] < dx[(l + t - 0) % l];
  }

  private static Sky parse(List<String> lines) {
    Sky sky = new Sky();
    // position=< 9,  1> velocity=< 0,  2>
    for (String line : lines) {
      String[] p1 = line.substring(line.indexOf('<') + 1, line.indexOf('>')).split(",");
      String[] p2 = line.substring(line.lastIndexOf('<') + 1, line.lastIndexOf('>')).split(",");
      sky.add(
          new Point(
              asInt(p1[0].trim()), asInt(p1[1].trim()), asInt(p2[0].trim()), asInt(p2[1].trim())));
    }
    return sky;
  }

  static class Sky {
    ArrayList<Point> points = new ArrayList<>();

    void add(Point p) {
      points.add(p);
    }

    int[] getDimensions(int time) {
      int[] dimensions = new int[4];
      dimensions[0] = points.stream().mapToInt(p -> p.x(time)).min().getAsInt();
      dimensions[1] = points.stream().mapToInt(p -> p.x(time)).max().getAsInt();
      dimensions[2] = points.stream().mapToInt(p -> p.y(time)).min().getAsInt();
      dimensions[3] = points.stream().mapToInt(p -> p.y(time)).max().getAsInt();
      return dimensions;
    }

    int print(int time) {
      int[] d = getDimensions(time);
      int dx = 2 + d[1] - d[0];
      int dy = 2 + d[3] - d[2];
      char[][] sky = new char[dx][dy];
      for (Point p : points) {
        sky[1 - d[0] + p.x(time)][1 - d[2] + p.y(time)] = 'X';
      }
      for (int x = 0; x < dx; x++) {
        System.out.print("-");
      }
      System.out.println(" t=" + time + "  dx=" + dx + "  dy=" + dy);
      for (int y = 0; y < dy; y++) {
        for (int x = 0; x < dx; x++) {
          System.out.print(sky[x][y] == 'X' ? "X" : " ");
        }
        System.out.println();
      }
      return dx;
    }
  }

  static class Point {

    int x;
    int y;
    int dx;
    int dy;

    public Point(int x, int y, int dx, int dy) {
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.dy = dy;
    }

    int x(int time) {
      return x + time * dx;
    }

    int y(int time) {
      return y + time * dy;
    }
  }
}
