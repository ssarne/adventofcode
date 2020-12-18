package aoc.aoc2018;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;

import aoc.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdventOfCode19 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    int[] regs = {0, 0, 0, 0, 0, 0};
    List<String> lines = getLines("input19_test.txt");
    List<Instruction> program = new ArrayList<>();
    int pc = readProgram(lines, program);
    runProgram(program, pc, regs, -1);
    String result = "" + regs[0];
    Utils.check(result, "7");
  }

  public static void task1() throws Exception {
    int[] regs = {0, 0, 0, 0, 0, 0};
    List<String> lines = getLines("input19.txt");
    List<Instruction> program = new ArrayList<>();
    int pc = readProgram(lines, program);
    runProgram(program, pc, regs, -1);
    String result = "" + regs[0];
    Utils.check(result, "878");
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    int[] regs = {1, 0, 0, 0, 0, 0};
    List<String> lines = getLines("input19.txt");
    List<Instruction> program = new ArrayList<>();
    int pc = readProgram(lines, program);
    runProgram(program, pc, regs, 32);
    long result = factorSum(regs[2] + regs[4]);
    System.out.println("Result: " + result);
  }

  public static long factorSum(long n) {
    int sum = 0;
    for (long i = 1; i <= n; i++) {
      if (n % i == 0) {
        sum += i;
      }
    }
    return sum;
  }

  public static int readProgram(List<String> lines, List<Instruction> program) throws Exception {

    int pc = -1;
    for (String line : lines) {
      if (line.startsWith("#ip")) {
        pc = Integer.parseInt(line.substring("#ip ".length()));
        continue;
      }
      String[] instrs = line.trim().split(" ");
      program.add(
          new Instruction(
              instrs[0],
              Integer.parseInt(instrs[1]),
              Integer.parseInt(instrs[2]),
              Integer.parseInt(instrs[3])));
    }
    return pc;
  }

  public static boolean runProgram(List<Instruction> program, int pc, int[] regs, int haltAt)
      throws Exception {

    long checkin = 10;
    int[] snapshot = null;

    for (long i = 0; ; i++) {

      // if (Arrays.equals(regs, snapshot)) {
      //  System.out.println("Circle: ");
      // }

      if (i == checkin) {
        // snapshot = new int[regs.length];
        // System.arraycopy(regs, 0, snapshot, 0, regs.length);

        System.out.println("instructions retired=" + i + "    " + Arrays.toString(regs));
        checkin += Math.min(671088640, checkin);
      }

      if (regs[pc] >= program.size()) {
        return false;
      }

      Instruction instruction = program.get(regs[pc]);
      if (instruction.c == 0)
        System.out.print(
            String.format(
                "%-64s    %-32s -> ",
                print(i, pc, instruction.op, instruction.a, instruction.b, instruction.c, regs),
                Arrays.toString(regs)));

      regs = eval(instruction.op, instruction.a, instruction.b, instruction.c, regs);
      if (instruction.c == 0) System.out.println(Arrays.toString(regs));

      if (regs[pc] == haltAt) {
        System.out.println("Halting at " + haltAt + ":  " + Arrays.toString(regs));
        return true;
      }
      regs[pc]++;
    }
    // return regs;
  }

  static int[] eval(String instr, int a, int b, int c, int[] regs) {

    int[] output = regs; // new int[regs.length];
    // System.arraycopy(regs, 0, output, 0, regs.length);

    switch (instr) {
        // Addition:
        // addr (add register) stores into register C the result of adding register A and register
        // B.
      case "addr":
        output[c] = regs[a] + regs[b];
        break;
        //  addi (add immediate) stores into register C the result of adding register A and value B.
      case "addi":
        output[c] = regs[a] + b;
        break;
        // Multiplication:
        // mulr (multiply register) stores into register C the result of multiplying register A and
        // register B.
      case "mulr":
        output[c] = regs[a] * regs[b];
        break;
        // muli (multiply immediate) stores into register C the result of multiplying register A and
        // value B.
      case "muli":
        output[c] = regs[a] * b;
        break;
        // Bitwise AND:
        // banr (bitwise AND register) stores into register C the result of the bitwise AND of
        // register A and register B.
      case "banr":
        output[c] = regs[a] & regs[b];
        break;
        // bani (bitwise AND immediate) stores into register C the result of the bitwise AND of
        // register A and value B.
      case "bani":
        output[c] = regs[a] & b;
        break;
        // Bitwise OR:
        //  borr (bitwise OR register) stores into register C the result of the bitwise OR of
        // register A and register B.
      case "borr":
        output[c] = regs[a] | regs[b];
        break;
        // bori (bitwise OR immediate) stores into register C the result of the bitwise OR of
        // register A and value B.
      case "bori":
        output[c] = regs[a] | b;
        break;
        // Assignment:
        // setr (set register) copies the contents of register A into register C. (Input B is
        // ignored.)
      case "setr":
        output[c] = regs[a];
        break;
        // seti (set immediate) stores value A into register C. (Input B is ignored.)
      case "seti":
        output[c] = a;
        break;
        // Greater-than testing:
        // gtir (greater-than immediate/register) sets register C to 1 if value A is greater than
        // register B. Otherwise, register C is set to 0.
      case "gtir":
        output[c] = (a > regs[b] ? 1 : 0);
        break;
        // gtri (greater-than register/immediate) sets register C to 1 if register A is greater than
        // value B. Otherwise, register C is set to 0.
      case "gtri":
        output[c] = (regs[a] > b ? 1 : 0);
        break;
        // gtrr (greater-than register/register) sets register C to 1 if register A is greater than
        // register B. Otherwise, register C is set to 0.
      case "gtrr":
        output[c] = (regs[a] > regs[b] ? 1 : 0);
        break;
        //    Equality testing:
        // eqir (equal immediate/register) sets register C to 1 if value A is equal to register B.
        // Otherwise, register C is set to 0.
      case "eqir":
        output[c] = (a == regs[b] ? 1 : 0);
        break;
        // eqri (equal register/immediate) sets register C to 1 if register A is equal to value B.
        // Otherwise, register C is set to 0.
      case "eqri":
        output[c] = (regs[a] == b ? 1 : 0);
        break;
        // eqrr (equal register/register) sets register C to 1 if register A is equal to register B.
        // Otherwise, register C is set to 0.
      case "eqrr":
        output[c] = (regs[a] == regs[b] ? 1 : 0);
        break;
      default:
        throw new UnsupportedOperationException("unknown op: " + instr);
    }
    return output;
  }

  static String print(long i, int pc, String instr, int a, int b, int c, int[] regs) {

    String s1, s2;

    switch (instr) {
        // Addition:
        // addr (add register) stores into register C the result of adding register A and register
        // B.
      case "addr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 = String.format("r%d = %d + %d = %d", c, regs[a], regs[b], regs[a] + regs[b]);
        break;

        //  addi (add immediate) stores into register C the result of adding register A and value B.
      case "addi":
        s1 = String.format("%s [%d] _%d_ r%d", instr, a, b, c);
        s2 = String.format("r%d = %d + %d = %d", c, regs[a], b, regs[a] + b);
        break;

        // Multiplication:
        // mulr (multiply register) stores into register C the result of multiplying register A and
        // register B.
      case "mulr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 = String.format("r%d = %d * %d = %d", c, regs[a], regs[b], regs[a] * regs[b]);
        break;

        // muli (multiply immediate) stores into register C the result of multiplying register A and
        // value B.
      case "muli":
        s1 = String.format("%s [%d] _%d_ r%d", instr, a, b, c);
        s2 = String.format("r%d = %d * %d = %d", c, regs[a], b, regs[a] * b);
        break;

        // Bitwise AND:
        // banr (bitwise AND register) stores into register C the result of the bitwise AND of
        // register A and register B.
      case "banr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 = String.format("r%d = %d & %d = %d", c, regs[a], regs[b], regs[a] & regs[b]);
        break;

        // bani (bitwise AND immediate) stores into register C the result of the bitwise AND of
        // register A and value B.
      case "bani":
        s1 = String.format("%s [%d] _%d_ r%d", instr, a, b, c);
        s2 = String.format("r%d = %d & %d = %d", c, regs[a], b, regs[a] & b);
        break;

        // Bitwise OR:
        //  borr (bitwise OR register) stores into register C the result of the bitwise OR of
        // register A and register B.
      case "borr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 = String.format("r%d = %d | %d = %d", c, regs[a], regs[b], regs[a] | regs[b]);
        break;

        // bori (bitwise OR immediate) stores into register C the result of the bitwise OR of
        // register A and value B.
      case "bori":
        s1 = String.format("%s [%d] _%d_ r%d", instr, a, b, c);
        s2 = String.format("r%d = %d | %d = %d", c, regs[a], b, regs[a] | b);
        break;

        // Assignment:
        // setr (set register) copies the contents of register A into register C. (Input B is
        // ignored.)
      case "setr":
        s1 = String.format("%s [%d] ___ r%d", instr, a, c);
        s2 = String.format("r%d = %d", c, regs[a]);
        break;

        // seti (set immediate) stores value A into register C. (Input B is ignored.)
      case "seti":
        s1 = String.format("%s _%d_ ___ r%d", instr, a, c);
        s2 = String.format("r%d = %d", c, a);
        break;

        // Greater-than testing:
        // gtir (greater-than immediate/register) sets register C to 1 if value A is greater than
        // register B. Otherwise, register C is set to 0.
      case "gtir":
        s1 = String.format("%s _%d_ [%d] r%d", instr, a, b, c);
        s2 = String.format("r%d = %d > %d = %d", c, a, regs[b], a > regs[b] ? 1 : 0);
        break;

        // gtri (greater-than register/immediate) sets register C to 1 if register A is greater than
        // value B. Otherwise, register C is set to 0.
      case "gtri":
        s1 = String.format("%s [%d] _%d_ r%d", instr, a, b, c);
        s2 = String.format("r%d = %d > %d = %d", c, regs[a], b, regs[a] > b ? 1 : 0);
        break;

        // gtrr (greater-than register/register) sets register C to 1 if register A is greater than
        // register B. Otherwise, register C is set to 0.
      case "gtrr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 = String.format("r%d = %d > %d = %d", c, regs[a], regs[b], regs[a] > regs[b] ? 1 : 0);
        break;

        //    Equality testing:
        // eqir (equal immediate/register) sets register C to 1 if value A is equal to register B.
        // Otherwise, register C is set to 0.
      case "eqir":
        s1 = String.format("%s [%d] _%d_ r%d", instr, a, b, c);
        s2 = String.format("r%d = %d = %d = %d", c, regs[a], b, regs[a] == b ? 1 : 0);
        break;

        // eqri (equal register/immediate) sets register C to 1 if register A is equal to value B.
        // Otherwise, register C is set to 0.
      case "eqri":
        s1 = String.format("%s [%d] _%d_ r%d", instr, a, b, c);
        s2 = String.format("r%d = %d = %d = %d", c, regs[a], b, regs[a] == b ? 1 : 0);
        break;

        // eqrr (equal register/register) sets register C to 1 if register A is equal to register B.
        // Otherwise, register C is set to 0.
      case "eqrr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 = String.format("r%d = %d = %d = %d", c, regs[a], regs[b], regs[a] == regs[b] ? 1 : 0);
        break;

      default:
        throw new UnsupportedOperationException("unknown op: " + instr);
    }
    return String.format("[%8d][%2d]   %s    %s", i, regs[pc], s1, s2);
  }

  static class Execution {

    int[] input;
    int[] output;
    Instruction instr;

    public Execution(Instruction instr, int[] input, int[] output) {
      this.input = input;
      this.output = output;
      this.instr = instr;
    }
  }

  static class Instruction {

    String op;
    int a;
    int b;
    int c;

    public Instruction(String op, int a, int b, int c) {
      this.op = op;
      this.a = a;
      this.b = b;
      this.c = c;
    }

    public String toString() {
      return op + " " + a + " " + b + " " + c;
    }
  }
}
