package aoc.aoc2019;

import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.longs;

public class Dec23 {

  static NIC nics[] = new NIC[50];
  static Long result1 = null;
  static Long result2 = null;
  static long[] nat = null;
  static long[] prev = null;
  static int idleCount = 0;

  public static void main(String[] args) throws Exception {

    String input = getLines().get(0);
    for (int i = 0; i < nics.length; i++) {
      nics[i] = new NIC(input, i);
    }

    while (result1 == null || result2 == null) {
      for (int i = 0; i < nics.length; i++) {
        nics[i].run();
      }

      if (idle() && idleCount == 3) {
        if (prev != null && prev[0] == nat[0] && prev[1] == nat[1]) {
          result2 = Long.valueOf(nat[1]);
        }
        send(255L, 0, nat[0], nat[1]);
        prev = longs(nat[0], nat[1]);
      }
    }

    System.out.println("Result 1: " + result1);
    System.out.println("Result 2: " + result2);
  }

  private static boolean idle() {
    for (int i = 0; i < nics.length; i++) {
      if (!nics[i].program.input.isEmpty()) {
        idleCount = 0;
        return false;
      }
    }
    idleCount++;
    return true;
  }

  public static void send(long from, long to, long x, long y) {
    // System.out.println("from=" + from + " to=" + to + " x=" + x + " y=" + y);
    if (to == 255) {
      nat = longs(x, y);
      if (result1 == null) {
        result1 = y;
      }
      return;
    }
    if (to < 50) {
      nics[(int) to].program.input.add(x);
      nics[(int) to].program.input.add(y);
    }
  }

  private static class NIC {

    IntCode.Program program;
    String input;
    int id;

    public NIC(String input, int id) {
      this.input = input;
      this.id = id;
      program = IntCode.Program.create(input);
      program.input.add((long) id);
    }

    public void run() {
      if (program.input.isEmpty()) {
        program.input.add(-1L);
      }
      IntCode.execute(program);
      if (!program.output.isEmpty()) {
        long id = program.output.poll();
        long x = program.output.poll();
        long y = program.output.poll();
        send(this.id, id, x, y);
      }
    }
  }
}

