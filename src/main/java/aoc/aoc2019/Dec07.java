package aoc.aoc2019;

import static aoc.Utils.check;
import static aoc.Utils.getLines;
import static aoc.Utils.ints;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Dec07 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {

    check(inUse(ints(0, 1, 2, 3, 3, 3), 0, 3), false);
    check(inUse(ints(0, 1, 2, 3, 3, 3), 3, 3), false);
    check(inUse(ints(0, 1, 2, 3, 3, 3), 4, 3), true);

    check(runThrusters("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0", ints(4,3,2,1,0)), 43210);
    check(runThrusters("3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0", ints(0,1,2,3,4)), 54321);
    check(runThrusters("3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0", ints(1,0,4,3,2)), 65210);
    check(findSettings("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0", 0, 5), 43210);
  }

  public static void task1() throws Exception {

    String line = getLines("aoc2019/dec07.txt").get(0);
    int max = findSettings(line, 0, 5);
    check(max, 87138);
    System.out.println("Result 1: " + max);
  }

  public static void task2() throws Exception {

    String line = getLines("aoc2019/dec07.txt").get(0);
    int max = findSettings(line, 5, 10);
    check(max, 17279674);
    System.out.println("Result 2: " + max);
  }

  private static int findSettings(String line, int low, int high) {
    int [] settings = ints(0, 0, 0, 0, 0);
    int max = findSettings(line, settings, 0, low, high);
    return max;
  }

  private static int findSettings(String line, int[] settings, int pos, int low, int high) {
    if (pos == settings.length) {
      return runThrusters(line, settings);
    }

    int max = 0;
    for (int i = low; i < high; i++) {
      if (inUse(settings, pos, i)) continue;
      settings[pos] =  i;
      int amplified = findSettings(line, settings, pos+1, low, high);
      max = Math.max(max, amplified);
    }
    return max;
  }

  private static int runThrusters(String line, int[] settings) {

    Program[] amps = new Program[settings.length];
    for (int i = 0; i < amps.length; i++) {
      int[] memory = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
      amps[i] = Program.create(memory);
      amps[i].input.add(settings[i]);
    }

    int amplified = 0;
    for (int n = 0; true; n++) {
      for (int i = 0; i < amps.length; i++) {
        // System.out.printf("%3d: %d %d", n, i, amplified);
        amps[i].input.add(amplified);
        IntCode.execute(amps[i]);
        amplified = amps[i].output.poll();
        // System.out.printf("-> %d      (%s)\n", amplified, amps[i].status);
      }
      if (amps[4].status == Program.Status.HALT_OK) {
        break;
      }
    }
    return amplified;
  }

  private static boolean inUse(int[] settings, int pos, int i) {
    for (int j = 0; j < pos; j++) {
      if (settings[j] == i) {
        return true;
      }
    }
    return false;
  }

  private static class Program {

    enum Status {
      HALT_OK,
      BLOCKED,
      ERROR,
      NA
    }

    Queue<Integer> input;
    Queue<Integer> output;
    Status status;
    int pc;
    int[] mem;

    Program(int[] memory) {
      this.pc = 0;
      this.mem = memory;
      this.status = Status.NA;
      this.input = new ConcurrentLinkedQueue();
      this.output = new ConcurrentLinkedQueue();
    }

    void print() {
      System.out.println("======================= pc=" + pc + "   input=" + input.toString());
      System.out.println("======================= memory=" + Arrays.toString(mem));
    }

    static Program create(int[] memory) {
      return new Program(memory);
    }

    static Program create(int[] memory, int[] input) {
      Program p = new Program(memory);
      for (int i : input) {
        p.input.add(i);
      }
      return p;
    }

    static Program create(String program, int[] input) {
      String line = getLines(program).get(0);
      int[] memory = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
      return create(memory, input);
    }
  }

  private static class IntCode {

    static Program execute(Program p) {

      // p.print();

      while (true) {

        final int pc = p.pc;
        int [] mem = p.mem;

        int op = p.mem[p.pc];
        int p1 = 0, p2 = 0, p3 = 0, v1 = 0, v2 = 0, v3 = 0;
        int m1 = 0, m2 = 0, m3 = 0;

        if (op >= 100) {
          int modes = op / 100;
          m1 = (modes / 1) % 10;
          m2 = (modes / 10) % 10;
          m3 = (modes / 100) % 10;
          op = op % 100;
        }

        // printOp(pc, mem);

        switch (op) {
          // Opcode 1 adds together numbers read from two positions and stores the result in a third position.
          case 1: // ADD
            p1 = mem[pc + 1];
            p2 = mem[pc + 2];
            p3 = mem[pc + 3];
            v1 = (m1 == 0 ? mem[p1] : p1);
            v2 = (m2 == 0 ? mem[p2] : p2);
            if (m3 == 1) throw new RuntimeException(("CMH"));
            mem[p3] = v1 + v2;
            p.pc += 4;
            break;

          // Opcode 2 multiplies the two inputs and stores the result in a third position.
          case 2: // MUL
            p1 = mem[pc + 1];
            p2 = mem[pc + 2];
            p3 = mem[pc + 3];
            v1 = (m1 == 0 ? mem[p1] : p1);
            v2 = (m2 == 0 ? mem[p2] : p2);
            if (m3 == 1) throw new RuntimeException(("CMH"));
            mem[p3] = v1 * v2;
            p.pc += 4;
            break;

          // Opcode 3 takes a single integer as input and saves it to the position given by its only parameter
          case 3: // INPUT
            if (p.input.isEmpty()) {
              p.status = Program.Status.BLOCKED;
              return p;
            }
            p1 = mem[pc + 1];
            v1 = p.input.poll();
            mem[p1] = v1;
            p.pc += 2;
            break;

          // Opcode 4 outputs the value of its only parameter.
          case 4: // OUTPUT
            p1 = mem[pc + 1];
            v1 = (m1 == 0 ? mem[p1] : p1);
            p.output.add(v1);
            p.pc += 2;
            break;

          // Opcode 5 is jump-if-true: if the first parameter is non-zero, it sets the instruction
          // pointer to the value from the second parameter. Otherwise, it does nothing.
          case 5:
            p1 = mem[pc + 1];
            p2 = mem[pc + 2];
            v1 = (m1 == 0 ? mem[p1] : p1);
            v2 = (m2 == 0 ? mem[p2] : p2);
            p.pc = (v1 != 0 ? v2 : pc + 3);
            break;

          // Opcode 6 is jump-if-false: if the first parameter is zero, it sets the instruction
          // pointer to the value from the second parameter. Otherwise, it does nothing.
          case 6:
            p1 = mem[pc + 1];
            p2 = mem[pc + 2];
            v1 = (m1 == 0 ? mem[p1] : p1);
            v2 = (m2 == 0 ? mem[p2] : p2);
            p.pc = (v1 == 0 ? v2 : pc + 3);
            break;

          // Opcode 7 is less than: if the first parameter is less than the second parameter, it
          // stores 1 in the position given by the third parameter. Otherwise, it stores 0.
          case 7:
            p1 = mem[pc + 1];
            p2 = mem[pc + 2];
            p3 = mem[pc + 3];
            v1 = (m1 == 0 ? mem[p1] : p1);
            v2 = (m2 == 0 ? mem[p2] : p2);
            if (m3 == 1) throw new RuntimeException(("CMH"));
            //v3 = (m2 == 0 ? mem[p3] : p3);
            mem[p3] = (v1 < v2 ? 1 : 0);
            p.pc += 4;
            break;

          // Opcode 8 is equals: if the first parameter is equal to the second parameter, it
          // stores 1 in the position given by the third parameter. Otherwise, it stores 0.
          case 8:
            p1 = mem[pc + 1];
            p2 = mem[pc + 2];
            p3 = mem[pc + 3];
            v1 = (m1 == 0 ? mem[p1] : p1);
            v2 = (m2 == 0 ? mem[p2] : p2);
            if (m3 == 1) throw new RuntimeException(("CMH"));
            // v3 = (m3 == 0 ? mem[p3] : p3);
            mem[p3] = (v1 == v2 ? 1 : 0);
            p.pc += 4;
            break;

          case 99:
            p.status = Program.Status.HALT_OK;
            return p;

          default:
            System.out.print("Error, pc=" + pc + " op=" + op);
            p.status = Program.Status.ERROR;
            return p;
        }
      }
    }

    static void printOp(int pc, int [] mem) {
      System.out.printf(
          "[%3d] %4s %4s %4s %4s   ||",
          pc,
          (pc < mem.length ? "" + mem[pc] : "-"),
          (pc + 1 < mem.length ? "" + mem[pc + 1] : "-"),
          (pc + 2 < mem.length ? "" + mem[pc + 2] : "-"),
          (pc + 3 < mem.length ? "" + mem[pc + 3] : "-"));

      for (int i = 0; i < mem.length && i < 16; i++) {
        System.out.printf(" %4s%s", mem[i], (i % 4 == 0 ? "  " : ""));
      }
      System.out.println();
    }
  }
}
