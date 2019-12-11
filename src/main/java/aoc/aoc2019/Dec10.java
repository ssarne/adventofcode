package aoc.aoc2019;

import static aoc.Utils.check;
import static aoc.Utils.getLines;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Dec10 {

  public static void main(String[] args) throws Exception {
    //test();
    doit(null);
  }

  public static void test() throws Exception {
    check(45, (int) angle(1, -1));
    check(26, (int) angle(1, -2));
    check(135, (int) angle(1, 1));
    check(153, (int) angle(1, 2));
    check(116, (int) angle(2, 1));
    check(315, (int) angle(-1, -1));
    check(333, (int) angle(-1, -2));
    check(225, (int) angle(-1, 1));
    check(206, (int) angle(-1, 2));
    //check(doit("aoc2019/dec10_test1.txt"), 8);
    //check(doit("aoc2019/dec10_test2.txt"), 33);
    //check(doit("aoc2019/dec10_test3.txt"), 35);
    //check(doit("aoc2019/dec10_test4.txt"), 41);
    //check(doit("aoc2019/dec10_test5.txt"), 210);
    // doit("aoc2019/dec10_test6.txt");
    doit("aoc2019/dec10_test5.txt");
  }

  public static int doit(String input) throws Exception {

    List<String> lines = getLines(input);

    int m = 0, a = 0, b = 0;
    int ytot = lines.size();
    int xtot = lines.get(0).length();
    char[][] space = createSpace(lines, ytot, xtot);

    // printSpace(space, " start");

    for (int y = 0; y < ytot; y++) {
      for (int x = 0; x < xtot; x++) {

        if (space[y][x] == '#') {
          int n = 0;
          space[y][x] = '*';
          markHidden(ytot, xtot, space, y, x);
          n = countHits(ytot, xtot, space, n);

          if (n > m) {
            m = n;
            a = x;
            b = y;
          }

          reset(ytot, xtot, space);
        }
      }
    }

    System.out.println("Result 1: " + m);

    space[b][a] = '*';
    // printSpace(space, " start (" + a + "," + b + ")");
    List<Pos> astroids = getPositions(a, b, ytot, xtot, space);

    Pos last = destroyClockwise(astroids, 200);
    System.out.println("Result 2: " + (last.x * 100 + last.y));
    return (last.y * 100 + last.x);
  }

  @Nullable
  private static Pos destroyClockwise(List<Pos> astroids, int number) {
    astroids.sort(Dec10::compareAngle);
    ;
    Pos last = null;
    int n = 0;
    for (Iterator<Pos> i = astroids.iterator(); i.hasNext() && n < number;) {
      Pos p = i.next();
      if (last != null && last.angle == p.angle) {
        // System.out.println("Skipping: " + p);
        continue;
      } else {
        i.remove();
        last = p;
        n++;
        // System.out.println("Removing " + n + ": " + last);
      }
    }
    return last;
  }

  @NotNull
  private static List<Pos> getPositions(int a, int b, int ytot, int xtot, char[][] space) {
    List<Pos> astroids = new LinkedList<>();
    for (int yi = 0; yi < ytot; yi++) {
      for (int xi = 0; xi < xtot; xi++) {
        if (space[yi][xi] == '#') {
          Pos pos = new Pos(xi, yi, xi - a, yi - b);
          astroids.add(pos);
        }
      }
    }
    return astroids;
  }

  private static void reset(int ytot, int xtot, char[][] space) {
    for (int yi = 0; yi < ytot; yi++) {
      for (int xi = 0; xi < xtot; xi++) {
        if (space[yi][xi] == '#') {
          // n++;
        } else if (space[yi][xi] == '_') {
          space[yi][xi] = '#';
        } else if (space[yi][xi] == '*') {
          space[yi][xi] = '#';
        }
      }
    }
  }

  private static int countHits(int ytot, int xtot, char[][] space, int n) {
    for (int yi = 0; yi < ytot; yi++) {
      for (int xi = 0; xi < xtot; xi++) {
        if (space[yi][xi] == '#') {
          n++;
        }
      }
    }
    return n;
  }

  private static void markHidden(int ytot, int xtot, char[][] space, int y, int x) {
    for (int yi = 0; yi < ytot; yi++) {
      for (int xi = 0; xi < xtot; xi++) {
        int dy = yi - y;
        int dx = xi - x;
        boolean hit = false;
        if (dy != 0 || dx != 0) {
          for (int i = 1; inRange(y + dy * i, x + dx * i, ytot, xtot); i++) {
            if (space[y + dy * i][x + dx * i] == '#') {
              if (hit == false) {
                hit = true;
              } else {
                space[y + dy * i][x + dx * i] = '_';
              }
            }
          }
        }
      }
    }
  }

  private static char[][] createSpace(List<String> lines, int ytot, int xtot) {
    char[][] space = new char[ytot][xtot];
    for (int y = 0; y < ytot; y++) {
      String line = lines.get(y);
      for (int x = 0; x < xtot; x++) {
        space[y][x] = line.charAt(x);
      }
    }
    return space;
  }

  private static boolean inRange(int y, int x, int ytot, int xtot) {
    return 0 <= y && y < ytot && 0 <= x && x < xtot;
  }

  public static void printSpace(char[][] space, String description) {
    System.out.println("------ " + description);
    for (int yi = 0; yi < space.length; yi++) {
      for (int xi = 0; xi < space[0].length; xi++) {
        System.out.print(space[yi][xi]);
      }
      System.out.println();
    }
  }

  public static int compareAngle(Pos p1, Pos p2) {
    if (p1.angle != p2.angle) {
      return (int) (p1.angle - p2.angle);
    }
    return p1.distance - p2.distance;
  }

  private static double angle(int dx, int dy) {
    double angle = 0;
    if(dx >= 0 && dy <= 0) { // 0-90
      angle = 90 - Math.toDegrees(Math.atan2(-1 * dy, dx));
    } else if (dx >= 0 && dy > 0) { // 90-180
      angle = 90 + Math.toDegrees(Math.atan2(dy, dx));
    } else if(dx < 0 && dy >= 0) { // 180-270
      angle = 270 - Math.toDegrees(Math.atan2(dy, -1 * dx));
    } else if(dx < 0 && dy < 0) { // 270-360
      angle = 270 + Math.toDegrees(Math.atan2(-1 * dy, -1 * dx));
    }
    return angle;
  }

  private static class Pos {
    int y, x, dy, dx, distance;
    double angled;
    long angle;
    Pos(int x, int y, int dx, int dy) {
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.dy = dy;
      this.distance = Math.abs(dy) + Math.abs(dx);
      this.angled = angle(dx, dy);
      this.angle = (long) (100000 * this.angled);
    }

    @Override
    public String toString() {
      return "Pos{" +
             "x=" + x +
             ", y=" + y +
             ", dx=" + dx +
             ", dy=" + dy +
             ", distance=" + distance +
             ", angle=" + angle +
             '}';
    }
  }
}
