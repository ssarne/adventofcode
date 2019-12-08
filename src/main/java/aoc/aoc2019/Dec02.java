package aoc.aoc2019;

import static aoc.Utils.check;
import static aoc.Utils.getLines;

import java.util.Arrays;

public class Dec02 {

  public static void main(String[] args) throws Exception {
    task1();
    task2();
  }

  public static void task1() throws Exception {
    Program p = doit("aoc2019/dec02.txt", 12, 2);
    int result = p.mem[0];
    check(result, 5482655);
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < 100; j++) {
        Program p = doit("aoc2019/dec02.txt", i, j);
        if (p.mem[0] == 19690720) {
          String output = i + "" + j;
          check(output, "4967");
          System.out.println("Result: " + output);
          return;
        }
      }
    }
  }

  public static Program doit(String input, int noun, int verb) throws Exception {

    Program p = Program.read(input);
    p.mem[1] = noun;
    p.mem[2] = verb;

    p.execute();
    // print(memory, pc);

    return p;
  }

  private static class Program {

    enum Status {OK, ERROR, NA};

    final static int ADD = 1;
    final static int MUL = 2;
    final static int HALT = 99;

    int pc;
    int[] mem;
    Status status;

    Program(int pc, int [] memory) {
      this.pc = pc;
      this.mem = memory;
      this.status = Status.NA;
    }

    Status execute() {

      for (; mem[pc] != HALT; pc += 4) {

        int op = mem[pc];
        int p1 = mem[pc + 1];
        int p2 = mem[pc + 2];
        int p3 = mem[pc + 3];

        switch(op) {
          case ADD:
            mem[p3] = mem[p1] + mem[p2];
            break;

          case MUL:
            mem[p3] = mem[p1] * mem[p2];
            break;

          default:
            System.out.print("Error, pc=" + pc + " op=" + op);
            status = Status.ERROR;
            return status;
        }
      }

      status = Status.OK;
      return status;
    }

    void print() {
      System.out.println("======================= pc=" + pc);
      for (int i = 0; i + 3 < mem.length; i += 4) {
        System.out.printf("%d %d %d %d\n", mem[i], mem[i + 1], mem[i + 2], mem[i + 3]);
      }
    }

    static Program read(String input) {
      String line = getLines(input).get(0);
      int[] memory = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
      return new Program(0, memory);
    }
  }
}
