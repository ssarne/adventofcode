package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.ints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dec05 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    Program p;

    // equal to 8
    p = Program.create(ints(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), ints(1)).execute();
    check(p.output.get(0), 0);

    p = Program.create(ints(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), ints(8)).execute();
    check(p.output.get(0), 1);

    // is less than 8
    p = Program.create(ints(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), ints(1)).execute();
    check(p.output.get(0), 1);

    p = Program.create(ints(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), ints(8)).execute();
    check(p.output.get(0), 0);

    // equal to 8
    p = Program.create(ints(3, 3, 1108, -1, 8, 3, 4, 3, 99), ints(1)).execute();
    check(p.output.get(0), 0);

    p = Program.create(ints(3, 3, 1108, -1, 8, 3, 4, 3, 99), ints(8)).execute();
    check(p.output.get(0), 1);

    // is less than 8
    p = Program.create(ints(3, 3, 1107, -1, 8, 3, 4, 3, 99), ints(1)).execute();
    check(p.output.get(0), 1);

    p = Program.create(ints(3, 3, 1107, -1, 8, 3, 4, 3, 99), ints(8)).execute();
    check(p.output.get(0), 0);

    // 0 check
    p = Program.create(ints(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), ints(8)).execute();
    check(p.output.get(0), 1);

    p = Program.create(ints(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), ints(0)).execute();
    check(p.output.get(0), 0);

    // 0 check
    p = Program.create(ints(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), ints(8)).execute();
    check(p.output.get(0), 1);

    p = Program.create(ints(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), ints(0)).execute();
    check(p.output.get(0), 0);

    // 8 check
    p = Program.read("aoc2019/dec05_test.txt", ints(0)).execute();
    check(p.output.get(0), 999);

    p = Program.read("aoc2019/dec05_test.txt", ints(1)).execute();
    check(p.output.get(0), 999);

    p = Program.read("aoc2019/dec05_test.txt", ints(8)).execute();
    check(p.output.get(0), 1000);

    p = Program.read("aoc2019/dec05_test.txt", ints(9)).execute();
    check(p.output.get(0), 1001);
  }

  public static void task1() throws Exception {
    Program p = Program.read("aoc2019/dec05.txt", ints(1)).execute();
    // System.out.println(Arrays.toString(p.output.toArray()));
    int result = p.output.get(p.output.size() - 1);
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    Program p = Program.read("aoc2019/dec05.txt", ints(5)).execute();
    System.out.println("Result: " + Arrays.toString(p.output.toArray()));
  }

  private static class Program {

    enum Status {
      OK,
      ERROR,
      NA
    };

    static final int HALT = 99;

    int pc;
    int[] mem;
    int[] input;
    int inputPos;
    List<Integer> output = new ArrayList<>();
    Status status;

    Program(int pc, int[] memory, int[] input) {
      this.pc = pc;
      this.mem = memory;
      this.status = Status.NA;
      this.input = input;
      this.inputPos = 0;
    }

    Program execute() {

      printProgram();

      while (true) {

        int op = mem[pc];
        int p1 = 0, p2 = 0, p3 = 0, v1 = 0, v2 = 0, v3 = 0;
        int m1 = 0, m2 = 0, m3 = 0;

        if (op >= 100) {
          int modes = op / 100;
          m1 = (modes / 1) % 10;
          m2 = (modes / 10) % 10;
          m3 = (modes / 100) % 10;
          op = op % 100;
        }

        printOp();

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
            pc += 4;
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
            pc += 4;
            break;

          // Opcode 3 takes a single integer as input and saves it to the position given by its only parameter
          case 3: // INPUT
            p1 = mem[pc + 1];
            v1 = input[inputPos++];
            mem[p1] = v1;
            pc += 2;
            break;

          // Opcode 4 outputs the value of its only parameter.
          case 4: // OUTPUT
            p1 = mem[pc + 1];
            v1 = (m1 == 0 ? mem[p1] : p1);
            output.add(v1);
            pc += 2;
            break;

            // Opcode 5 is jump-if-true: if the first parameter is non-zero, it sets the instruction
            // pointer to the value from the second parameter. Otherwise, it does nothing.
          case 5:
            p1 = mem[pc + 1];
            p2 = mem[pc + 2];
            v1 = (m1 == 0 ? mem[p1] : p1);
            v2 = (m2 == 0 ? mem[p2] : p2);
            pc = (v1 != 0 ? v2 : pc + 3);
            break;

            // Opcode 6 is jump-if-false: if the first parameter is zero, it sets the instruction
            // pointer to the value from the second parameter. Otherwise, it does nothing.
          case 6:
            p1 = mem[pc + 1];
            p2 = mem[pc + 2];
            v1 = (m1 == 0 ? mem[p1] : p1);
            v2 = (m2 == 0 ? mem[p2] : p2);
            pc = (v1 == 0 ? v2 : pc + 3);
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
            pc += 4;
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
            pc += 4;
            break;

          case 99:
            status = Status.OK;
            return this;

          default:
            System.out.print("Error, pc=" + pc + " op=" + op);
            status = Status.ERROR;
            return this;
        }
      }
    }

    void printOp() {
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

    void printProgram() {
      System.out.println("======================= pc=" + pc + "   input=" + Arrays.toString(input));
      System.out.println("======================= memory=" + Arrays.toString(mem));
    }

    static Program read(String program, int[] input) {
      String line = getLines(program).get(0);
      int[] memory = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
      return new Program(0, memory, input);
    }

    static Program create(int[] memory, int[] input) {
      return new Program(0, memory, input);
    }
  }
}
