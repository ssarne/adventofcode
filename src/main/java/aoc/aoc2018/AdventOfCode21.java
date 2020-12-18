package aoc.aoc2018;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AdventOfCode21 {

  static final boolean trace = false;

  public static void main(String[] args) throws Exception {
    // task1();
    task2();
  }

  //      255                  11111111   0x    FF
  //    65536         10000000000000000   0x 10000
  //    65899         10000000101101011   0x 1016B
  //  4332021   10000100001100111110101   0x4219F5
  // 16777215  111111111111111111111111   0xFFFFFF
  //  9566170  100100011111011111011010   0x91F7DA

  // Brute force, from input 1 and above.
  // Run for 10k instructions and see if it halts.
  public static void task1() throws Exception {
    List<String> lines = getLines("input21.txt");
    List<Instruction> program = new ArrayList<>();
    int pc = readProgram(lines, program);
    for (int i = 0; i < 40000000; i++) {
      if (i % 1000000 == 0) System.out.print(".");
      int[] regs = {i, 0, 0, 0, 0, 0};
      if (runProgram(program, pc, regs, 10000, -1) == false) {
        System.out.println("Result: " + i);
      }
    }
  }

  // Instruction 28 checks r0 == r4
  // Any value in r4 at instruction 28 will align and halt
  // Print all unique values there
  // (run for as long as needed to find new ones)
  public static void task2() throws Exception {
    List<String> lines = getLines("input21.txt");
    List<Instruction> program = new ArrayList<>();
    int pc = readProgram(lines, program);
    printProgram(program, pc);
    // int[] regs = {9566170, 0, 0, 0, 0, 0};   // This is the first completion
    int[] regs = {4711, 0, 0, 0, 0, 0};
    runProgram(program, pc, regs, 5000000000L, 28);
  }

  private static void printProgram(List<Instruction> program, int pc) {
    System.out.println("===========================================================");
    System.out.println("Program, size=" + program.size() + ", pc=" + pc + ": ");
    for (int i = 0; i < program.size(); i++) {
      Instruction instruction = program.get(i);
      System.out.println(
          String.format(
              "%-64s",
              print(i, pc, instruction.op, instruction.a, instruction.b, instruction.c, null)));
    }
    System.out.println("===========================================================");
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

  private static HashSet<Integer> vals = new HashSet<>();

  public static boolean runProgram(List<Instruction> program, int pc, int[] regs, long haltAt, int debug)
      throws Exception {

    for (long i = 0; i < haltAt; i++) {

      if (regs[pc] >= program.size() || regs[pc] < 0) { // program halts.
        System.out.println("pc oob: regs[pc]=" + regs[pc] + "  i=" + i);
        return false;
      }

      if (regs[pc] == debug) {
        int r4 = regs[4];
        if (!vals.contains(r4)) {
          vals.add(r4);
          System.out.println("pc=" + debug + "  i=" + i + ":   r4=" + regs[4]);
        }
      }

      Instruction instruction = program.get(regs[pc]);
      if (trace) {
        System.out.print(
            String.format(
                "%-64s    %-48s -> ",
                print(i, pc, instruction.op, instruction.a, instruction.b, instruction.c, regs),
                Arrays.toString(regs)));
      }

      regs = eval(instruction.op, instruction.a, instruction.b, instruction.c, regs);

      if (trace) {
        System.out.println(Arrays.toString(regs));
      }

      regs[pc]++;
    }
    return true;
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
        s2 =
            String.format(
                "r%d = %s + %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? "r" + b : "" + regs[b],
                regs == null ? "-" : "" + (regs[a] + regs[b]));
        break;

        //  addi (add immediate) stores into register C the result of adding register A and value B.
      case "addi":
        s1 = String.format("%s [%d] %d r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %s + %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? b : "" + b,
                regs == null ? "-" : "" + (regs[a] + b));
        break;

        // Multiplication:
        // mulr (multiply register) stores into register C the result of multiplying register A and
        // register B.
      case "mulr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %s * %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? "r" + b : "" + regs[b],
                regs == null ? "-" : "" + (regs[a] * regs[b]));

        break;

        // muli (multiply immediate) stores into register C the result of multiplying register A and
        // value B.
      case "muli":
        s1 = String.format("%s [%d] %d r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %s * %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? b : "" + b,
                regs == null ? "-" : "" + (regs[a] * b));

        break;

        // Bitwise AND:
        // banr (bitwise AND register) stores into register C the result of the bitwise AND of
        // register A and register B.
      case "banr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %s & %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? "r" + b : "" + regs[b],
                regs == null ? "-" : "" + (regs[a] & regs[b]));
        break;

        // bani (bitwise AND immediate) stores into register C the result of the bitwise AND of
        // register A and value B.
      case "bani":
        s1 = String.format("%s [%d] %d r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %s & %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? b : "" + b,
                regs == null ? "-" : "" + (regs[a] & b));

        break;

        // Bitwise OR:
        //  borr (bitwise OR register) stores into register C the result of the bitwise OR of
        // register A and register B.
      case "borr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %s | %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? "r" + b : "" + regs[b],
                regs == null ? "-" : "" + (regs[a] | regs[b]));
        break;

        // bori (bitwise OR immediate) stores into register C the result of the bitwise OR of
        // register A and value B.
      case "bori":
        s1 = String.format("%s [%d] %d r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %s | %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? b : "" + b,
                regs == null ? "-" : "" + (regs[a] | b));

        break;

        // Assignment:
        // setr (set register) copies the contents of register A into register C. (Input B is
        // ignored.)
      case "setr":
        s1 = String.format("%s [%d] r%d", instr, a, c);
        s2 = String.format("r%d = %s", c, regs == null ? "r" + a : "" + (regs[a]));
        break;

        // seti (set immediate) stores value A into register C. (Input B is ignored.)
      case "seti":
        s1 = String.format("%s %d r%d", instr, a, c);
        s2 = String.format("r%d = %d", c, a);
        break;

        // Greater-than testing:
        // gtir (greater-than immediate/register) sets register C to 1 if value A is greater than
        // register B. Otherwise, register C is set to 0.
      case "gtir":
        s1 = String.format("%s %d [%d] r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %d > %s = %s",
                c,
                a,
                regs == null ? "r" + b : "" + regs[b],
                regs == null ? "-" : "" + (a > regs[b] ? 1 : 0));
        break;

        // gtri (greater-than register/immediate) sets register C to 1 if register A is greater than
        // value B. Otherwise, register C is set to 0.
      case "gtri":
        s1 = String.format("%s [%d] %d r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %s > %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                b,
                regs == null ? "-" : "" + (regs[a] > b ? 1 : 0));
        break;

        // gtrr (greater-than register/register) sets register C to 1 if register A is greater than
        // register B. Otherwise, register C is set to 0.
      case "gtrr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = %s > %s = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? "r" + b : "" + regs[b],
                regs == null ? "-" : "" + (regs[a] > regs[b] ? 1 : 0));
        break;

        //    Equality testing:
        // eqir (equal immediate/register) sets register C to 1 if value A is equal to register B.
        // Otherwise, register C is set to 0.
      case "eqir":
        s1 = String.format("%s [%d] %d r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = (%d == %s) = %s",
                c,
                a,
                regs == null ? "r" + b : "" + regs[b],
                regs == null ? "-" : "" + (a == regs[b] ? 1 : 0));
        break;

        // eqri (equal register/immediate) sets register C to 1 if register A is equal to value B.
        // Otherwise, register C is set to 0.
      case "eqri":
        s1 = String.format("%s [%d] %d r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = (%s == %d) = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                b,
                regs == null ? "-" : "" + (regs[a] == b ? 1 : 0));
        break;

        // eqrr (equal register/register) sets register C to 1 if register A is equal to register B.
        // Otherwise, register C is set to 0.
      case "eqrr":
        s1 = String.format("%s [%d] [%d] r%d", instr, a, b, c);
        s2 =
            String.format(
                "r%d = (%s == %s) = %s",
                c,
                regs == null ? "r" + a : "" + regs[a],
                regs == null ? "r" + b : "" + regs[b],
                regs == null ? "-" : "" + (regs[a] == regs[b] ? 1 : 0));
        break;

      default:
        throw new UnsupportedOperationException("unknown op: " + instr);
    }
    return String.format(
        "[%8s][%4s]   %-32s    %-48s",
        regs == null ? "" : "" + i, (regs == null ? "" + i : "" + regs[pc]), s1, s2);
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
  }
}
