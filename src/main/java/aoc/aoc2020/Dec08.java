package aoc.aoc2020;

import static aoc.utils.Utils.*;

import java.util.*;

public class Dec08 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() {
    check(solve1(getTestLines()), 5);
    check(solve2(getTestLines()), 8);
  }

  public static void task1() {
    int result = solve1(getLines());
    System.out.println("Result: " + result);
    check(result, readAnswerAsLong(1));
  }

  public static void task2() {
    int result = solve2(getLines());
    System.out.println("Result: " + result);
    check(result, readAnswerAsLong(2));
  }

  public static int solve1(List<String> input) {

    VM vm = new VM(input);
    try {
      vm.execute();
    } catch(IllegalStateException e) {
      return vm.acc;
    }
    return -1;
  }

  public static int solve2(List<String> input) {

    for (int i = 0; i < input.size(); i++) {
      String backup = input.get(i);
      if (backup.contains("nop")) {
        input.set(i, backup.replace("nop", "jmp"));
      } else if (backup.contains("jmp")) {
        input.set(i, backup.replace("jmp", "nop"));
      }

      try {
        return new VM(input).execute();
      } catch (IllegalStateException e) {
        input.set(i, backup);
      }
    }
    return -1;
  }

  private static class VM {

    List<String> program;
    boolean[] visited;
    int acc = 0;
    int pc = 0;

    public VM(List<String> program) {
      this.program = program;
      this.visited = new boolean[program.size()];
    }

    private int execute() {

      while (pc < program.size()) {

        if (visited[pc]) {
          throw new IllegalStateException("Loop detected");
        }
        visited[pc] = true;

        String[] instr = program.get(pc).split(" ");
        String op = instr[0];
        int val = Integer.parseInt(instr[1].strip().replace("+", ""));

        switch (op) {
          case "nop":
            pc++;
            break;
          case "acc":
            acc += val;
            pc++;
            break;
          case "jmp":
            pc += val;
            break;
          default:
            throw new UnsupportedOperationException("Unknown op: " + op);
        }
      }
      return acc;
    }
  }

}
