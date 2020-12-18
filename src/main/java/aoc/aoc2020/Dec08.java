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
    check(solve1("aoc2020/dec08_test.txt"), 5);
    check(solve2("aoc2020/dec08_test.txt"), 8);
  }

  public static void task1() {
    int result = solve1("aoc2020/dec08.txt");
    check(result, 1563);
    System.out.println("Result: " + result);
  }

  public static void task2() {
    int result = solve2("aoc2020/dec08.txt");
    check(result, 767);
    System.out.println("Result: " + result);
  }

  public static int solve1(String input) {

    List<String> lines = getLines(input);
    VM vm = new VM(lines);
    try {
      vm.execute();
    } catch(IllegalStateException e) {
      return vm.acc;
    }
    return -1;
  }

  public static int solve2(String input) {

    List<String> lines = getLines(input);
    for (int i = 0; i < lines.size(); i++) {
      String backup = lines.get(i);
      if (backup.contains("nop")) {
        lines.set(i, backup.replace("nop", "jmp"));
      } else if (backup.contains("jmp")) {
        lines.set(i, backup.replace("jmp", "nop"));
      }

      try {
        return new VM(lines).execute();
      } catch (IllegalStateException e) {
        lines.set(i, backup);
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
