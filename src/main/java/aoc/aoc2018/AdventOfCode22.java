package aoc.aoc2018;

import static aoc.Utils.check;
import static java.lang.System.out;

import java.util.LinkedList;
import java.util.Queue;

public class AdventOfCode22 {

  public static void main(String[] args) throws Exception {
    test();
    task();
  }

  public static void test() throws Exception {
    Cave cave = new Cave(510, 10, 10, 64);
    cave.print();
    System.out.println("Risk: " + cave.calcRiskLevel());
    System.out.println("Time: " + cave.walk());
  }

  public static void task() throws Exception {
    Cave cave = new Cave(7740, 12, 763, 4096);
    // cave.print();
    System.out.println("Risk: " + cave.calcRiskLevel());
    System.out.println("Time: " + cave.walk());  // (You guessed 1055. Your answer is too high.)
  }

  static class Cave {

    Pos[][] area;
    Pos target;
    int depth;
    int size;

    public Cave(int depth, int targetX, int targetY, int size) {
      this.area = new Pos[size][size];
      this.target = new Pos(targetX, targetY, 0, calcErosionLevel(0));
      this.depth = depth;
      this.size = size;

      for (int y = 0; y < size; y++) {
        for (int x = 0; x < size; x++) {
          int index = calcGeoIndex(x, y);
          area[x][y] = new Pos(x, y, index, calcErosionLevel(index));
        }
      }
    }

    int calcGeoIndex(int x, int y) {
      if (x == 0 && y == 0) {
        return 0;
      }
      if (x == target.x && y == target.y) {
        return 0;
      }
      if (y == 0) {
        return x * 16807;
      }
      if (x == 0) {
        return y * 48271;
      }
      return area[x - 1][y].erosionLevel * area[x][y - 1].erosionLevel;
    }

    int calcErosionLevel(int geoIndex) {
      return (geoIndex + depth) % 20183;
    }

    int calcRiskLevel() {
      return calcRiskLevel(0, 0, target.x + 1, target.y + 1);
    }

    int calcRiskLevel(int x, int y, int dx, int dy) {
      int r = 0;
      for (int iy = 0; iy < y + dy; iy++) {
        for (int ix = 0; ix < x + dx; ix++) {
          r += area[ix][iy].type.ordinal();
        }
      }
      return r;
    }

    void print() {
      for (int y = 0; y < size; y++) {
        for (int x = 0; x < size; x++) {
          if (x == 0 && y == 0) {
            out.print("M");
          } else if (x == target.x && y == target.y) {
            out.print("T");
          } else {
            out.print(area[x][y].type.symbol);
          }
        }
        out.println();
      }
    }

    public int walk() {
      Queue<Step> queue = new LinkedList<Step>();
      queue.add(new Step(area[0][0], Step.Tool.torch, 0, 0, Step.Direction.down));

      while (true) {
        Step step = queue.poll();

        // Found target with torch
        if (step.pos.x == target.x && step.pos.y == target.y && step.equipped == Step.Tool.torch && step.delay == 0) {
          return step.time;
        }

        // Switch of gear ongoing
        if (step.delay > 0) {
          queue.add(new Step(step.pos, step.equipped, step.time + 1, step.delay - 1, Step.Direction.gear));
          continue;
        }

        step.pos.visisted[step.equipped.ordinal()] = true;

        // Switch gear
        if (step.direction != Step.Direction.gear) {
          queue.add(new Step(step.pos, switchGear(step.equipped, step.pos.type), step.time + 1, 6, Step.Direction.gear));
        }

        // Up
        if (step.direction != Step.Direction.down && step.pos.y > 0 && walkable(step.equipped, area[step.pos.x][step.pos.y - 1])) {
          queue.add(new Step(area[step.pos.x][step.pos.y - 1], step.equipped, step.time + 1, 0, Step.Direction.up));
          area[step.pos.x][step.pos.y - 1].visisted[step.equipped.ordinal()] = true;
        }

        // Left
        if (step.direction != Step.Direction.right && step.pos.x > 0 && walkable(step.equipped, area[step.pos.x - 1][step.pos.y])) {
          queue.add(new Step(area[step.pos.x - 1][step.pos.y], step.equipped, step.time + 1, 0, Step.Direction.left));
          area[step.pos.x - 1][step.pos.y].visisted[step.equipped.ordinal()] = true;
        }

        // Right
        if (step.direction != Step.Direction.left && walkable(step.equipped, area[step.pos.x + 1][step.pos.y])) {
          queue.add(new Step(area[step.pos.x + 1][step.pos.y], step.equipped, step.time + 1, 0, Step.Direction.right));
          area[step.pos.x + 1][step.pos.y].visisted[step.equipped.ordinal()] = true;
        }

        // Down
        if (step.direction != Step.Direction.up && walkable(step.equipped, area[step.pos.x][step.pos.y + 1])) {
          queue.add(new Step(area[step.pos.x][step.pos.y + 1], step.equipped, step.time + 1, 0, Step.Direction.down));
          area[step.pos.x][step.pos.y + 1].visisted[step.equipped.ordinal()] = true;
        }
      }
    }

    private boolean walkable(Step.Tool equipped, Pos pos) {
      if (pos.visisted[equipped.ordinal()] == true) {
        return false;
      }
      if (pos.type == Pos.Ground.rocky) {
        return equipped != Step.Tool.neither;
      }
      if (pos.type == Pos.Ground.wet) {
        return equipped != Step.Tool.torch;
      }
      if (pos.type == Pos.Ground.narrow) {
        return equipped != Step.Tool.climbing_gear;
      }
      throw new UnsupportedOperationException("ohps");
    }

    private Step.Tool switchGear(Step.Tool equipped, Pos.Ground type) {
      if (type == Pos.Ground.rocky) { // rocky
        return equipped == Step.Tool.torch ? Step.Tool.climbing_gear : Step.Tool.torch;
      } else if (type == Pos.Ground.wet) { // wet
        return equipped == Step.Tool.climbing_gear ? Step.Tool.neither : Step.Tool.climbing_gear;
      } else if (type == Pos.Ground.narrow) { // narrow
        return equipped == Step.Tool.torch ? Step.Tool.neither : Step.Tool.torch;
      } else {
        throw new UnsupportedOperationException("unknown type " + type);
      }
    }
  }

  static class Pos {

    enum Ground {
      rocky('.'),
      wet('='),
      narrow('|');

      char symbol;
      Ground(char c) {
        this.symbol = c;
      }
    }

    int x, y;
    int geoIndex;
    int erosionLevel;
    Ground type;
    boolean [] visisted = new boolean[3];

    public Pos(int x, int y, int index, int level) {
      this.x = x;
      this.y = y;
      this.geoIndex = index;
      this.erosionLevel = level;
      this.type = Ground.values()[level % 3];
    }
  }

  static class Step {
    enum Tool {
      torch,
      climbing_gear,
      neither
    }

    enum Direction {
      up, down, right, left, gear
    }

    Pos pos;
    Tool equipped;
    int time = 0;
    int delay = 0;
    Direction direction;


    public Step(Pos pos, Tool equipped, int time, int delay, Direction direction) {
      this.pos = pos;
      this.equipped = equipped;
      this.time = time;
      this.delay = delay;
      this.direction = direction;
    }
  }
}
