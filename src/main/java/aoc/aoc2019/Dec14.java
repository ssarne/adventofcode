package aoc.aoc2019;

import static aoc.Utils.check;
import static aoc.Utils.getLines;
import static java.lang.System.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dec14 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    check(doit("aoc2019/dec14_test1.txt"), 31);
    check(doit("aoc2019/dec14_test2.txt"), 165);
    check(doit("aoc2019/dec14_test3.txt"), 13312);
    check(doit("aoc2019/dec14_test4.txt"), 180697);
    check(doit("aoc2019/dec14_test5.txt"), 2210736);

    // max long: 9223372036854775808
    long ores = 1000000000000L;
    check(fuel(ores, "aoc2019/dec14_test3.txt"), 82892753L);
    check(fuel(ores, "aoc2019/dec14_test4.txt"), 5586022L);
    check(fuel(ores, "aoc2019/dec14_test5.txt"), 460664L);
  }

  public static void task1() throws Exception {
    long result = doit("aoc2019/dec14.txt");
    check(result, 168046);
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    long fuel = fuel(1000000000000L, "aoc2019/dec14.txt");
    // check(fuel, 6972987L);
    System.out.println("Result: " + fuel);
  }

  public static long doit(String input) throws Exception {

    List<String> lines = getLines(input);
    HashMap<String, Reaction> reactions = parseReactions(lines);
    HashMap<String, Chemical> stash = new HashMap<>();

    long ores = nanofactor(reactions, stash,  "FUEL", 1);
    return ores;
  }


  public static long fuel(long ores, String input) throws Exception {

    List<String> lines = getLines(input);
    HashMap<String, Reaction> reactions = parseReactions(lines);
    HashMap<String, Chemical> stash = new HashMap<>();

    long costPerFuel = nanofactor(reactions, stash,  "FUEL", 1);
    long step = 100000L;
    long threshold = ores / 2;
    out.println("step=" + step +  "  ores=" + ores + "  threshold=" + threshold + "  costPerFuel=" + costPerFuel);

    long fuel = 0;
    while (true) {
      long cost = nanofactor(reactions, stash,  "FUEL", step);
      if (cost < ores) {
        fuel += step;
        ores -= cost;
      }
      if (ores < threshold) {
        out.println("step=" + step +  "  ores=" + ores + "  threshold=" + threshold + "  fuel=" + fuel);
        step = Math.max(step / 10L, 1L);
        threshold = ores / 2;
      }
      if (cost > ores && step == 1L) {
        out.println("step=" + step +  "  ores=" + ores + "  threshold=" + threshold + "  fuel=" + fuel);
        printStash(stash);
        break;
      }
      if (cost > ores) {
        throw new RuntimeException("CMF");
      }
    }

    return fuel;
  }

  private static void printStash(HashMap<String,Chemical> stash) {
    for (Chemical c : stash.values()) {
      out.println(c.name + ": " + c.quantity);
    }
  }

  private static HashMap<String, Reaction> parseReactions(List<String> lines) {
    HashMap<String, Reaction> reactions = new HashMap<>();
    for (String line : lines) {
      String [] sides = line.split("=>");
      Chemical result = Chemical.parse(sides[1].strip().split(" "));
      Reaction formula = new Reaction(result);
      String [] inputs = sides[0].strip().split(",");
      for (int i = 0; i < inputs.length; i++) {
        formula.inputs.add(Chemical.parse(inputs[i].strip().split(" ")));
      }
      reactions.put(formula.result.name, formula);
    }
    return reactions;
  }

  private static long nanofactor(HashMap<String,Reaction> reactions, HashMap<String,Chemical> stash, String name, long quantity) {

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
    if (reaction == null)
      throw new RuntimeException("CMH");
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
  }

  private static class Chemical {
    String name;
    long quantity;
    public Chemical(String name, long quantity) {
      this.name = name;
      this.quantity = quantity;
    }

    public static Chemical parse(String ... input) {
      return new Chemical(input[1], Integer.parseInt(input[0]));
    }
  }
}
