package aoc.aoc2019;

import static aoc.utils.Utils.getLines;

public class Dec21 {

  // Sensors: A B C D .. E F G H I  (true == ground, false == hole)
  // Temp registry: T
  // Jump registry J
  // AND X Y sets Y to true if both X and Y are true; otherwise, it sets Y to false.
  // OR  X Y sets Y to true if at least one of X or Y is true; otherwise, it sets Y to false.
  // NOT X Y sets Y to true if X is false; otherwise, it sets Y to false.

  // Jump if any of A-C has a hole and D is solid.
  static String[] program1 = {
      "NOT A T",
      "OR T J",
      "NOT B T",
      "OR T J",
      "NOT C T",
      "OR T J",
      "AND D J",
      "WALK"
  };

  // Check for D and H
  static String[] program2 = {
      "NOT A J",    // store
      "NOT B T",
      "AND H T",
      "OR T J",     // store
      "NOT C T",
      "AND H T",
      "OR T J",     // store
      "AND D J",    // req D
      "RUN"
  };


  public static void main(String[] args) throws Exception {
    task1();
    task2();
  }

  public static void task1() throws Exception {
    System.out.println("Result 1: " + doit(program1, false));
  }

  public static void task2() throws Exception {
    System.out.println("Result 2: " + doit(program2, false));
  }

  public static long doit(String[] springscript, boolean print) {

    String input = getLines().get(0);
    IntCode.Program program = IntCode.Program.create(input);
    for (String instr : springscript) {
      program.addInput(ascii(instr));
    }
    IntCode.execute(program);
    return asciiOutput(program, print);
  }

  private static long asciiOutput(IntCode.Program program, boolean print) {
    long overflow = 0;
    StringBuilder sb = new StringBuilder();
    while (!program.output.isEmpty()) {
      long l = program.output.poll().longValue();
      if (l <= 0xFF) {
        sb.append((char) l);
      } else {
        sb.append('\'').append(l).append('\'');
        overflow = l;
      }
    }
    if (print) {
      System.out.println(sb.toString());
    }
    return overflow;
  }

  private static String ascii(String instr) {
    return instr + "\n";
  }
}

