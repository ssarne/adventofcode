package aoc.aoc2019;

import static aoc.Utils.getLines;
import static aoc.Utils.longs;

import java.util.BitSet;
import java.util.Scanner;

public class Dec25 {

  // Map: https://docs.google.com/spreadsheets/d/1BnPPJKHe67HKNMeUElOpVm9yx5NF8PRtGJdkfeYQFPY/edit?folder=0AE2nUwx96RYPUk9PVA#gid=0

  static String[] actions = {
      "east",
      "take antenna",
      "west",
      "north",
      "take weather machine",
      "north",
      "take klein bottle",
      "east",
      "take spool of cat6",
      "east",
      "north",
      "west",
      "north",
      "take cake",
      "south",
      "east",
      "east",
      "north",
      "north",
      "take tambourine",
      "south",
      "south",
      "south",
      "take shell",
      "north",
      "west",
      "south",
      "south",
      "take mug",
      "north",
      "west",
      "south",
      "south",
      "inv"
  };

  static String[] items = {
      "shell",
      "klein bottle",
      "tambourine",
      "weather machine",
      "antenna",
      "spool of cat6",
      "mug",
      "cake"
  };

  public static void main(String[] args) throws Exception {

    String input = getLines().get(0);
    Scanner stdin = new Scanner(System.in);
    IntCode.Program droid = IntCode.Program.create(input);
    IntCode.execute(droid);
    asciiOutput(droid, true);

    for (String command : actions) {
      execute(droid, command);
    }
    for (String item : items) {
      execute(droid, "drop " + item);
    }

    testItems(droid, new BitSet(), 0);

    while (droid.status != IntCode.Program.Status.HALT_OK) {
      String command = stdin.nextLine();
      execute(droid, command);
    }
  }

  private static boolean testItems(IntCode.Program droid, BitSet bitSet, int pos) {
    if (pos == items.length) {
      return performItemsTest(droid, bitSet);
    }

    bitSet.set(pos, false);
    if (testItems(droid, bitSet, pos + 1)) {
      return true;
    }
    bitSet.set(pos, true);
    if (testItems(droid, bitSet, pos + 1)) {
      return true;
    }
    return false;
  }

  private static boolean performItemsTest(IntCode.Program droid, BitSet bitSet) {
    for (int i = 0; i < items.length; i++) {
      if (bitSet.get(i)) {
        execute(droid, "take " + items[i]);
      }
    }
    String output = execute(droid, "east");
    if (output.contains("heavier")) {
      System.out.println("Too light: " + bitSet);
    } else if (output.contains("lighter")) {
      System.out.println("Too heavy: " + bitSet);
    } else {
      System.out.println("Match: " + bitSet);
      return true;
    }
    for (int i = 0; i < items.length; i++) {
      if (bitSet.get(i)) {
        execute(droid, "drop " + items[i]);
      }
    }
    return false;
  }

  private static String execute(IntCode.Program droid, String command) {
    System.out.println("Command: " + command);
    droid.addInput(command + "\n");
    IntCode.execute(droid);
    return asciiOutput(droid, true);
  }

  private static String asciiOutput(IntCode.Program program, boolean print) {
    StringBuilder sb = new StringBuilder();
    while (!program.output.isEmpty()) {
      long l = program.output.poll().longValue();
      if (l <= 0xFF) {
        sb.append((char) l);
      } else {
        sb.append('\'').append(l).append('\'');
      }
    }
    String output = sb.toString();
    if (print) {
      System.out.println(output);
    }
    return output;
  }
}

