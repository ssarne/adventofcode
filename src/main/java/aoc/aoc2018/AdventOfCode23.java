package aoc.aoc2018;

import static aoc.utils.Utils.asInt;
import static aoc.utils.Utils.getLines;
import static java.lang.System.out;

import java.util.ArrayList;
import java.util.List;

public class AdventOfCode23 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    test2();
    task2();
  }

  public static void test() throws Exception {
    System.out.println("Result: " + getBots("input23_test.txt").size());
  }

  public static void task1() throws Exception {
    System.out.println("Result: " + getBots("input23.txt").size());
  }

  public static void test2() throws Exception {
    var bots = getBots("input23_test2.txt");
    // var point = calcInRangeOnSurfaces(bots);
    var point = calcInRangeForBoxes(bots);
    var manhattan = point.x + point.y + point.z;
    System.out.println("Result: " + manhattan);
  }

  public static void task2() throws Exception {
    var bots = getBots("input23.txt");
    // var point = calcInRangeOnSurfaces(bots);
    var point = calcInRangeForBoxes(bots);
    var manhattan = point.x + point.y + point.z;
    System.out.println("Result: " + manhattan);
  }

  private static Point calcInRangeForBoxes(List<Bot> bots) {
    Point p = new Point(0, 0, 0, 0);
    int divisor = 10;

    // int minx, maxx, miny, maxy, minz, maxz;
    int minx = bots.stream().mapToInt(b -> b.x).min().getAsInt();
    int miny = bots.stream().mapToInt(b -> b.y).min().getAsInt();
    int minz = bots.stream().mapToInt(b -> b.z).min().getAsInt();
    int maxx = bots.stream().mapToInt(b -> b.x).max().getAsInt();
    int maxy = bots.stream().mapToInt(b -> b.y).max().getAsInt();
    int maxz = bots.stream().mapToInt(b -> b.z).max().getAsInt();
    int dx = (maxx - minx) > divisor ? (maxx - minx) / divisor : 1;
    int dy = (maxy - miny) > divisor ? (maxy - miny) / divisor : 1;
    int dz = (maxz - minz) > divisor ? (maxz - minz) / divisor : 1;

    while (minx < maxx - 1 && miny < maxy - 1 && minz < maxz - 1) {
      out.println(
          String.format(
              "[%d,%d],[%d,%d],[%d,%d],[%d,%d,%d]",
              minx, maxx, miny, maxy, minz, maxz, dx, dy, dz));
      p = calcInRangeForBoxes(bots, minx, maxx, miny, maxy, minz, maxz, dx, dy, dz);
      out.println(String.format("Point: (%d,%d,%d): %d", p.x, p.y, p.z, p.n));

      minx = p.x;
      maxx = p.x + dx;
      miny = p.y;
      maxy = p.y + dy;
      minz = p.z;
      maxz = p.z + dy;
      dx = (maxx - minx) > divisor ? (maxx - minx) / divisor : 1;
      dy = (maxy - miny) > divisor ? (maxy - miny) / divisor : 1;
      dz = (maxz - minz) > divisor ? (maxz - minz) / divisor : 1;
    }
    return p;
  }

  private static Point calcInRangeForBoxes(
      List<Bot> bots,
      int minx,
      int maxx,
      int miny,
      int maxy,
      int minz,
      int maxz,
      int dx,
      int dy,
      int dz) {

    Point max = new Point(0, 0, 0, 0);
    for (int x = minx; x <= maxx; x += dx) {
      for (int y = miny; y <= maxy; y += dy) {
        for (int z = minz; z <= maxz; z += dz) {
          int n = calcInRange(bots, x, x + dx - 1, y, y + dy - 1, z, z + dz - 1);
          if (n > max.n) {
            max = new Point(x, y, z, n);
          }
        }
      }
    }
    return max;
  }

  private static int calcInRange(
      List<Bot> bots, int minx, int maxx, int miny, int maxy, int minz, int maxz) {
    int n = 0;
    for (Bot bot : bots) {
      int x = bot.x > maxx ? maxx : (bot.x < minx ? minx : bot.x);
      int y = bot.y > maxy ? maxy : (bot.y < miny ? miny : bot.y);
      int z = bot.z > maxz ? maxz : (bot.z < minz ? minz : bot.z);
      if (distance(bot, x, y, z) <= bot.r) {
        n++;
      }
    }
    return n;
  }

  private static int distance(Bot bot, int x, int y, int z) {
    return Math.abs(bot.x - x) + Math.abs(bot.y - y) + Math.abs(bot.z - z);
  }

  private static Point calcInRangeOnSurfaces(List<Bot> bots) {
    // HashMap<String, Integer> points = new HashMap<>();
    Point max = new Point(0, 0, 0, 0);
    for (Bot bot : bots) {
      System.out.println("Checking bot: " + bot.x + "," + bot.y + "," + bot.z + " r=" + bot.r);
      for (int x = 0; x <= bot.r; x++) {
        System.out.print(".");
        for (int y = 0; y <= bot.r - x; y++) {
          for (int z = 0; z <= bot.r - x - y; z++) {
            max = calcInRange(bots, max, bot.x + x, bot.y + y, bot.z + z);
            max = calcInRange(bots, max, bot.x + x, bot.y + y, bot.z - z);
            max = calcInRange(bots, max, bot.x + x, bot.y - y, bot.z + z);
            max = calcInRange(bots, max, bot.x + x, bot.y - y, bot.z - z);
            max = calcInRange(bots, max, bot.x - x, bot.y + y, bot.z + z);
            max = calcInRange(bots, max, bot.x - x, bot.y + y, bot.z - z);
            max = calcInRange(bots, max, bot.x - x, bot.y - y, bot.z + z);
            max = calcInRange(bots, max, bot.x - x, bot.y - y, bot.z - z);
          }
        }
      }
    }
    return max;
  }

  private static Point calcInRange(List<Bot> bots, Point max, int x, int y, int z) {
    int n = 0;
    for (Bot bot : bots) {
      int distance = distance(bot, x, y, z);
      if (distance <= bot.r) {
        n++;
      }
    }
    return n > max.n ? new Point(x, y, z, n) : max;
  }

  public static List<Bot> getBots(String input) throws Exception {

    List<Bot> allBots = new ArrayList<>();
    List<String> lines = getLines(input);
    for (String line : lines) {
      String[] cords = line.substring(line.indexOf('<') + 1, line.indexOf('>')).split(",");
      allBots.add(
          new Bot(
              asInt(cords[0]),
              asInt(cords[1]),
              asInt(cords[2]),
              asInt(line.substring(line.indexOf("r") + 2))));
    }

    Bot strongest = allBots.get(0);
    for (Bot bot : allBots) {
      if (bot.r > strongest.r) {
        strongest = bot;
      }
    }

    List<Bot> bots = new ArrayList<>();
    for (Bot bot : allBots) {
      int dx = Math.abs(strongest.x - bot.x);
      int dy = Math.abs(strongest.y - bot.y);
      int dz = Math.abs(strongest.z - bot.z);
      if (dx + dy + dz <= strongest.r) {
        bots.add(bot);
      }
    }

    return bots;
  }

  public static void print() {
    out.println("");
  }

  static class Bot {
    int x, y, z, r;

    public Bot(int x, int y, int z, int r) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.r = r;
    }
  }

  static class Point {
    int x, y, z, n;

    public Point(int x, int y, int z, int n) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.n = n;
    }
  }
}
