package aoc.aoc2019;

import static aoc.utils.Utils.asInt;
import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static aoc.utils.Utils.readAnswerAsInt;
import static aoc.utils.Utils.readAnswerAsLong;
import static java.lang.System.out;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Dec12 {

  public static void main(String[] args) throws Exception {
    test();
    test11();
    test12();
    task1();
    test21();
    task2();
  }

  public static void test() throws Exception {
    check("[23]", factors(23).toString());
    check("[2, 2, 3]", factors(12).toString());
  }

  public static void test11() throws Exception {
    // Task 1, example 1
    Moon[] moons = parse(getTestLines(1));
    steps(moons, 10);

    long e = energy(moons);
    check(e, 179);
  }

  public static void test12() throws Exception {

    Moon[] moons = parse(getTestLines(2));
    steps(moons, 100);

    long e = energy(moons);
    check(e, 1940);
  }

  public static void task1() throws Exception {

    var lines = getLines();
    Moon[] moons = parse(lines);
    steps(moons, 1000);

    long e = energy(moons);
    System.out.println("Result: " + e);
    check(e, readAnswerAsInt(1));
  }

  private static Moon[] parse(List<String> lines) {
    Moon[] moons = new Moon[lines.size()];
    for (int i = 0; i < lines.size(); i++) {
      var line = lines.get(i);
      var parts = line.strip()
          .replace("<", "")
          .replace(">", "")
          .replace("x", "")
          .replace("y", "")
          .replace("z", "")
          .replace("=", "")
          .replace(" ", "")
          .split(",");
      moons[i] = new Moon(asInt(parts[0]), asInt(parts[1]), asInt(parts[2]));
    }
    return moons;
  }

  private static void steps(Moon[] moons, int steps) {
    // print(moons, 0);
    for (int i = 1; i <= steps; i++) {
      step(moons);
      // print(moons, i);
    }
  }

  private static void step(Moon[] moons) {
    for (int m = 0; m < 4; m++) {
      for (int d = 0; d < 3; d++) {
        int dv = moons[m].dxdydz[d];
        for (int mi = 0; mi < 4; mi++) {
          if (m != mi) {
            dv += (moons[m].xyz[d] == moons[mi].xyz[d] ? 0 : (moons[m].xyz[d] < moons[mi].xyz[d] ? 1 : -1));
          }
        }
        moons[m].dxdydz[d] = dv;
      }
    }
    for (int m = 0; m < 4; m++) {
      for (int d = 0; d < 3; d++) {
        moons[m].xyz[d] += moons[m].dxdydz[d];
      }
    }
  }

  private static long energy(Moon[] moons) {
    long e = 0;
    for (int m = 0; m < 4; m++) {
      e += moons[m].tot();
    }
    return e;
  }

  public static void test21() throws Exception {
    // Task 2, example 1
    Moon[] moons = parse(getTestLines(1));
    long n = circle(moons);
    check(n, 2772);
  }

  public static void task2() throws Exception {

    var lines = getLines();
    Moon[] moons = parse(lines);

    long n = circle(moons);
    System.out.println("Result: " + n);
    check(n, readAnswerAsLong(2));
  }

  private static long circle(Moon[] moons) {
    // print(moons, 0);
    Moon[] start = new Moon[4];
    for (int i = 0; i < 4; i++) {
      start[i] = new Moon(moons[i].xyz[0], moons[i].xyz[1], moons[i].xyz[2]);
    }

    long [] circles = new long[3];
    for (long n = 1; true; n++) {
      step(moons);
      for(int d = 0; d < 3; d++) {
        boolean match = true;
        for (int m = 0; m < 4; m++) {
          if (moons[m].xyz[d] != start[m].xyz[d] || moons[m].dxdydz[d] != start[m].dxdydz[d]) {
            match = false;
          }
        }
        if (match) {
          if (circles[d] == 0) {
            circles[d] = n;
          }
        }
      }
      if (circles[0] != 0 && circles[1] != 0 && circles[2] != 0) {
        break;
      }
    }

    // out.printf("circles=<x=%d, y=%d, z=%d>\n", circles[0], circles[1], circles[2]);
    List<List<Long>> fxyz = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      fxyz.add(factors(circles[i]));
    }

    List<Long> factors = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      for (long l : fxyz.get(i)) {
        factors.add(l);
        for (int j = i + 1; j < 3; j++) {
          if (fxyz.get(j).contains(l)) {
            fxyz.get(j).remove(l);
          }
        }
      }
    }

    // out.println("Factors: " + factors);
    long r = 1;
    for (long l : factors) {
      r *= l;
    }
    return r;
  }

  private static List<Long> factors(long n) {
    List<Long> fs = new ArrayList<>();
    factors(fs, n, 2);
    return fs;
  }

  private static void factors(List<Long> fs, long n, long d) {
    while (true) {
      if (n == 1) {
        break;
      }
      if (n == d) {
        fs.add(d);
        break;
      }
      if (n % d == 0) {
        fs.add(d);
        n = n/d;
      } else {
        d += 1;
      }
    }
  }

  public static void print(Moon[] moons, int steps) {
    System.out.println("After " + steps +" steps:");
    for (int i = 0; i < moons.length; i++) {
      out.printf("pos=<x=%3d, y=%3d, z=%3d>, vel=<dx=%3d, dy=%3d, dz=%3d> e=<pot=%d, kin=%d, tot=%d>\n",
          moons[i].xyz[0], moons[i].xyz[1], moons[i].xyz[2],
          moons[i].dxdydz[0], moons[i].dxdydz[1], moons[i].dxdydz[2],
          moons[i].pot(), moons[i].kin(), moons[i].tot());
    }
  }

  private static class Moon {

    int[] xyz = new int[3];
    int[] dxdydz = new int[3];

    public Moon(int x, int y, int z) {
      this.xyz[0] = x;
      this.xyz[1] = y;
      this.xyz[2] = z;
      this.dxdydz[0] = 0;
      this.dxdydz[1] = 0;
      this.dxdydz[2] = 0;
    }

    long pot() {
      return Math.abs(xyz[0]) + Math.abs(xyz[1]) + Math.abs(xyz[2]);
    }

    long kin() {
      return Math.abs(dxdydz[0]) + Math.abs(dxdydz[1]) + Math.abs(dxdydz[2]);
    }

    long tot() {
      return pot() * kin();
    }
  }
}
