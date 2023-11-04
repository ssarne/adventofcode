package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.readAnswerAsInt;

import java.util.HashMap;

public class Dec11 {

  public static void main(String[] args) throws Exception {
    task1();
    task2();
  }

  public static void task1() throws Exception {
    HashMap<String, Long> panels = drawPanels(0L);
    System.out.println("Result: " + panels.size());
    check(panels.size(), readAnswerAsInt(1));
  }

  public static void task2() throws Exception {
    HashMap<String, Long> panels = drawPanels(1L);
    System.out.println("Result: " + panels.size());
    printPanels(panels);
  }

  private static HashMap<String, Long> drawPanels(long initial) {

    HashMap<String, Long> panels = new HashMap<>();
    panels.put("0,0", initial);
    int x = 0, y = 0;
    char dir = '^';
    long color, turn;

    IntCode.Program p = IntCode.Program.create(getLines().get(0));
    while (p.status != IntCode.Program.Status.HALT_OK) {
      color = color(panels, pos(x, y));
      p.input.add(color);

      IntCode.execute(p);
      color = p.output.poll();
      turn = p.output.poll();
      panels.put(pos(x, y), color);
      dir = getDir(dir, turn);
      x += dx(dir);
      y += dy(dir);
      // System.out.println("Step: " + dir + " " + x + "," + y + " " + newColor);
    }
    return panels;
  }

  private static long color(HashMap<String, Long> panels, String pos) {
    return panels.containsKey(pos) ? panels.get(pos) : 0L;
  }

  private static int dx(char dir) {
    if (dir == '<') {
      return -1;
    }
    if (dir == '>') {
      return 1;
    }
    return 0;
  }

  private static int dy(char dir) {
    if (dir == '^') {
      return 1;
    }
    if (dir == 'v') {
      return - 1;
    }
    return 0;
  }

  private static String pos(int x, int y) {
    return x + "," + y;
  }

  public static void printPanels(HashMap<String, Long> panels) {
    int minx = 0, maxx = 0, miny = 0, maxy = 0;
    for (String panel : panels.keySet()) {
      minx = Math.min(minx, Integer.parseInt(panel.split(",")[0]));
      maxx = Math.max(maxx, Integer.parseInt(panel.split(",")[0]));
      miny = Math.min(miny, Integer.parseInt(panel.split(",")[1]));
      maxy = Math.max(maxy, Integer.parseInt(panel.split(",")[1]));
    }
    for (int y = maxy; y >= miny; y--) {
      for (int x = maxx; x >= minx; x--) {
        System.out.print(color(panels, pos(x, y)) == 1 ? "*" : " ");
      }
      System.out.println();
    }
  }

  private static char getDir(char dir, long turn) {
    switch (dir) {
      case '^':
        return turn == 1L ? '<' : '>';
      case '>':
        return turn == 1L ? '^' : 'v';
      case '<':
        return turn == 1L ? 'v' : '^';
      case 'v':
        return turn == 1L ? '>' : '<';
      default:
        throw new RuntimeException("CMH");
    }
  }
}