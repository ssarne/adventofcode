package aoc.aoc2018;

import static aoc.Utils.check;
import static aoc.Utils.getLines;

import aoc.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AdventOfCode16 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {

    int result = count3matches("input16_test.txt");
    System.out.println("Result test: " + result);
    Utils.check(result, 1);
  }

  public static void task1() throws Exception {
    int result = count3matches("input16.txt");
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    List<String> lines = getLines("input16.txt");
    List<Execution> executions = new ArrayList<>();
    List<int[]> program = new ArrayList<>();
    readInput(lines, executions, program);

    System.out.println("Executions: " + executions.size());
    System.out.println("Operations: " + operations.length);

    deduceOps(executions);
    int result = runProgram(program);
    System.out.println("Result: " + result);
  }

  public static int count3matches(String input) throws Exception {

    List<String> lines = getLines(input);
    List<Execution> executions = new ArrayList<>();
    List<int[]> program = new ArrayList<>();
    readInput(lines, executions, program);

    System.out.println("Executions: " + executions.size());
    System.out.println("Operations: " + operations.length);

    int threes = 0;
    for (Execution exec : executions) {
      int matches = 0;
      for (Operation op : operations) {
        int[] output = eval(op.name, exec.instr[1], exec.instr[2], exec.instr[3], exec.input);
        if (Arrays.equals(output, exec.output)) {
          matches++;
        }
      }

      if (matches >= 3) {
        threes++;
      }
    }
    return threes;
  }

  public static void deduceOps(List<Execution> executions) throws Exception {

    boolean found = true;
    while (found) {
      found = false;

      for (Execution exec : executions) {
        if (opCodes.containsKey(exec.instr[0])) {
          continue;
        }
        int matches = 0;
        for (Operation op : operations) {
          if (op.code >= 0) {
            continue;
          }
          int[] output = eval(op.name, exec.instr[1], exec.instr[2], exec.instr[3], exec.input);
          if (Arrays.equals(output, exec.output)) {
            matches++;
          }
        }

        if (matches == 1) {
          for (Operation op : operations) {
            if (op.code >= 0) {
              continue;
            }
            int[] output = eval(op.name, exec.instr[1], exec.instr[2], exec.instr[3], exec.input);
            if (Arrays.equals(output, exec.output)) {
              System.out.println("Op code assigned: " + op.name + " " + exec.instr[0]);
              op.code = exec.instr[0];
              opCodes.put(op.code, op);
              found = true;
            }
          }
        }
      }
    }

    found = true;
    for (Operation op : operations) {
      if (op.code < 0) {
        found = false;
      }
    }
    if (found) {
      System.out.println("All op codes found");
    }
  }

  private static void readInput(
      List<String> lines, List<Execution> executions, List<int[]> program) {
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);

      if (line.trim().length() == 0) {
        continue;
      }

      if (line.startsWith("Before:")) {
        int[] regIn =
            Arrays.stream(
                    line.substring("Before: ".length())
                        .trim()
                        .replaceAll("(\\[|\\])", "")
                        .split(", "))
                .mapToInt(s -> Integer.parseInt(s))
                .toArray();

        int[] instr =
            Arrays.stream(lines.get(++i).split(" ")).mapToInt(s -> Integer.parseInt(s)).toArray();

        int[] regOut =
            Arrays.stream(
                    lines
                        .get(++i)
                        .substring("After: ".length())
                        .trim()
                        .replaceAll("(\\[|\\])", "")
                        .split(", "))
                .mapToInt(s -> Integer.parseInt(s))
                .toArray();

        executions.add(new Execution(instr, regIn, regOut));
        continue;
      }

      String[] instrs = line.trim().split(" ");
      int[] instr = new int[4];
      for (int j = 0; j < instrs.length; j++) {
        instr[j] = Integer.parseInt(instrs[j]);
      }
      program.add(instr);
    }
    return;
  }

  public static int runProgram(List<int[]> program) throws Exception {

    int[] regs = {0, 0, 0, 0};
    for (int[] instruction : program) {
      Operation op = opCodes.get(instruction[0]);
      if (op == null) {
        System.out.println(
            "Error: "
                + instruction[0]
                + " "
                + instruction[1]
                + " "
                + instruction[2]
                + " "
                + instruction[3]);
      }
      // System.out.println(op.name + " " + instruction[1] + " " + instruction[2] + " " +
      // instruction[3]);
      int[] input = regs;
      regs = eval(op.name, instruction[1], instruction[2], instruction[3], input);
      // System.out.println(Arrays.toString(input) + " -> " + Arrays.toString(regs));
    }
    return regs[0];
  }

  static int[] eval(String instr, int a, int b, int c, int[] regs) {

    int[] output = new int[4];
    System.arraycopy(regs, 0, output, 0, 4);

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

  static class Execution {

    int[] input;
    int[] output;
    int[] instr;

    public Execution(int[] instr, int[] input, int[] output) {
      this.input = input;
      this.output = output;
      this.instr = instr;
    }
  }

  static class Operation {

    String name;
    int code;

    public Operation(String name, int code) {
      this.name = name;
      this.code = code;
    }
  }

  static Operation[] operations = {
    new Operation("addr", -1),
    new Operation("addi", -1),
    new Operation("mulr", -1),
    new Operation("muli", -1),
    new Operation("banr", -1),
    new Operation("bani", -1),
    new Operation("borr", -1),
    new Operation("bori", -1),
    new Operation("setr", -1),
    new Operation("seti", -1),
    new Operation("gtir", -1),
    new Operation("gtri", -1),
    new Operation("gtrr", -1),
    new Operation("eqir", -1),
    new Operation("eqri", -1),
    new Operation("eqrr", -1),
  };

  static HashMap<Integer, Operation> opCodes = new HashMap<Integer, Operation>();
}
