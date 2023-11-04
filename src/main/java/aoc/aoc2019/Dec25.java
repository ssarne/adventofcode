package aoc.aoc2019;

import static aoc.utils.Utils.getLines;

import java.util.BitSet;
import java.util.Scanner;

public class Dec25 {

  // Map: https://docs.google.com/spreadsheets/d/1BnPPJKHe67HKNMeUElOpVm9yx5NF8PRtGJdkfeYQFPY/edit?folder=0AE2nUwx96RYPUk9PVA#gid=0

  static String[] actions = {
      "east",
      "take sand",
      "east",
      // "take molten lava", // The molten lava is way too hot! You melt!
      "west",
      "west",
      "west",
      "south",
      // "take giant electromagnet", // The giant electromagnet is stuck to you.  You can't move!!
      "south",
      "take candy cane",
      "north",
      "east",
      // "take escape pod", // You're launched into space! Bye!
      "south",
      "north",
      "east",
      "south",
      // "take photons", // It is suddenly completely dark! You are eaten by a Grue!
      "north",
      "east",
      "take space law space brochure",
      "south",
      "take fuel cell",
      "south",
      "west",
      "north",
      "north",
      "west",
      "west",
      "west",
      "west",
      "north",
      "north",
      "take wreath",
      "east",
      "take fixed point",
      "west",
      "north",
      // "take infinite loop", ...
      "north",
      "take spool of cat6",
      "south",
      "south",
      "south",
      "south",
      "north",
      "south",
      "east",
      "east",
      "east",
      "south",
      "south",
      // inv
  };

  static String[] items = {
      "sand",
      "candy cane",
      "space law space brochure",
      "fuel cell",
      "wreath",
      "fixed point",
      "spool of cat6"
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
    String output = execute(droid, "west");
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

