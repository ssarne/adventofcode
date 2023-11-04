package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static aoc.utils.Utils.readAnswerAsLong;
import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Dec14 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    check(doit(getTestLines(1)), 31);
    check(doit(getTestLines(2)), 165);
    check(doit(getTestLines(3)), 13312);
    check(doit(getTestLines(4)), 180697);
    check(doit(getTestLines(5)), 2210736);

    // max long: 9223372036854775808
    long ores = 1000000000000L;
    check(fuel(ores, getTestLines(3)), 82892753L);
    check(fuel(ores, getTestLines(4)), 5586022L);
    check(fuel(ores, getTestLines(5)), 460664L);
  }

  public static void task1() throws Exception {
    long result = doit(getLines());
    check(result, readAnswerAsLong(1));
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    long fuel = fuel(1000000000000L, getLines());
    check(fuel, readAnswerAsLong(2));
    System.out.println("Result: " + fuel);
  }

  public static long doit(List<String> lines) throws Exception {

    HashMap<String, Reaction> reactions = parseReactions(lines);
    HashMap<String, Chemical> stash = new HashMap<>();

    long ores = nanofactor(reactions, stash, "FUEL", 1);
    return ores;
  }


  public static long fuel(long ores, List<String> lines) throws Exception {

    HashMap<String, Reaction> reactions = parseReactions(lines);
    HashMap<String, Chemical> stash = new HashMap<>();

    long costPerFuel = nanofactor(reactions, stash, "FUEL", 1);
    long step = 100000L;
    long threshold = ores / 2;

    long fuel = 0;
    while (true) {
      long cost = nanofactor(reactions, stash, "FUEL", step);
      if (cost < ores) {
        fuel += step;
        ores -= cost;
      }
      if (ores < threshold) {
        step = Math.max(step / 10L, 1L);
        threshold = ores / 2;
      }
      if (cost > ores && step == 1L) {
        break;
      }
      if (cost > ores) {
        throw new RuntimeException("CMH");
      }
    }

    return fuel;
  }

  private static void printStash(HashMap<String, Chemical> stash) {
    for (Chemical c : stash.values()) {
      out.println(c.name + ": " + c.quantity);
    }
  }

  private static HashMap<String, Reaction> parseReactions(List<String> lines) {
    HashMap<String, Reaction> reactions = new HashMap<>();
    for (String line : lines) {
      String[] sides = line.split("=>");
      Chemical result = Chemical.parse(sides[1].strip().split(" "));
      Reaction formula = new Reaction(result);
      String[] inputs = sides[0].strip().split(",");
      for (int i = 0; i < inputs.length; i++) {
        formula.inputs.add(Chemical.parse(inputs[i].strip().split(" ")));
      }
      if (reactions.containsKey(formula.result.name)) {
        System.out.println(formula);
        System.out.println(reactions.get(formula.result.name));
        // throw new RuntimeException("CMH");
      }
      reactions.put(formula.result.name, formula);
    }
    return reactions;
  }

  private static long nanofactor(HashMap<String, Reaction> reactions, HashMap<String, Chemical> stash, String name, long quantity) {

    if (name.equals("ORE")) {
      return quantity;
    }

    long ores = 0;
    Chemical c = stash.get(name);
    if (c != null) {
      if (c.quantity >= quantity) {
        c.quantity -= quantity;
        return ores;
      } else {
        quantity -= c.quantity;
        c.quantity = 0;
      }
    }

    Reaction reaction = reactions.get(name);
    if (reaction == null) {
      throw new RuntimeException("CMH");
    }
    long factor = quantity / reaction.result.quantity;
    long rest = quantity % reaction.result.quantity;
    if (rest > 0) {
      factor += 1;
    }

    for (Chemical i : reaction.inputs) {
      ores += nanofactor(reactions, stash, i.name, i.quantity * factor);
    }

    long remains = reaction.result.quantity * factor - quantity;
    if (stash.containsKey(name)) {
      stash.get(name).quantity += remains;
    } else {
      stash.put(name, new Chemical(name, remains));
    }

    return ores;
  }

  private static class Reaction {

    Chemical result;
    List<Chemical> inputs = new ArrayList<>();

    public Reaction(Chemical result) {
      this.result = result;
    }

    public String toString() {
      return result + " <= " + Arrays.toString(inputs.toArray(new Chemical[0]));
    }
  }

  private static class Chemical {

    String name;
    long quantity;

    public Chemical(String name, long quantity) {
      this.name = name;
      this.quantity = quantity;
    }

    public static Chemical parse(String... input) {
      return new Chemical(input[1], Integer.parseInt(input[0]));
    }

    public String toString() {
      return name + " " + quantity;
    }
  }
}
