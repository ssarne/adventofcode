package aoc.aoc2020;

import static aoc.Utils.*;

import java.util.*;

public class Dec07 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() {
    check(solve1("aoc2020/dec07_test.txt"), 4);
    check(solve2("aoc2020/dec07_test.txt"), 32);
    check(solve2("aoc2020/dec07_test2.txt"), 126);
  }

  public static void task1() {
    int result = solve1("aoc2020/dec07.txt");
    check(result, 257);
    System.out.println("Result: " + result);
  }

  public static void task2() {
    int result = solve2("aoc2020/dec07.txt");
    check(result, 1038);
    System.out.println("Result: " + result);
  }

  public static int solve1(String input) {
    HashMap<String, Bag> bags = readBags(input);
    Set<String> goldies = new HashSet<>();
    for (Bag bag : bags.values()) {
      if (canContain(bag, bags, goldies, "shiny gold")) {
        goldies.add(bag.color);
      }
    }
    return goldies.size();
  }

  public static int solve2(String input) {
    HashMap<String, Bag> bags = readBags(input);
    Bag bag = bags.get("shiny gold");
    return count(bags, bag) - 1;
  }

  private static HashMap<String, Bag> readBags(String input) {
    HashMap<String, Bag> bags = new HashMap<>();
    for (String line : getLines(input)) {
      Bag bag = new Bag(line);
      bags.put(bag.color, bag);
    }
    return bags;
  }

  private static boolean canContain(Bag bag, Map<String,Bag> bags, Set<String> matches, String target) {
    if  (matches.contains(bag.color)) {
      return true;
    }
    for (Dest d : bag.contains) {
      if (d.color.equals(target)) {
        matches.add(bag.color);
        return true;
      }
      if (matches.contains(d.color)) {
        return true;
      }
    }

    for (Dest d : bag.contains) {
      if (canContain(bags.get(d.color), bags, matches, target)) {
        return true;
      }
    }
    return false;
  }

  private static int count(HashMap<String,Bag> bags, Bag bag) {
    int sum = 1;
    for (Dest d : bag.contains) {
      sum += d.n * count(bags, bags.get(d.color));
    }
    return sum;
  }

  private static class Bag {
    String color;
    List<Dest> contains = new ArrayList<>();
    public Bag(String line) {
      color = line.substring(0, line.indexOf(" bags contain"));
      if (!line.contains("no other bags")) {
        line = line.substring(line.indexOf("contain") + "contain".length() + 1);
        line = line.replace(".", "");
        line = line.replace(" bags", "");
        line = line.replace(" bag", "");
        String [] targets = line.split(", ");
        for (String target : targets) {
          int p = target.indexOf(" ");
          int n = Integer.parseInt(target.substring(0, p));
          String color = target.substring(p + 1);
          contains.add(new Dest(color, n));
        }
      }
    }
  }

  private static class Dest {
    String color;
    int n;

    public Dest(String color, int n) {
      this.color = color;
      this.n = n;
    }
  }
}