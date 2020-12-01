package aoc.aoc2018;

import static aoc.Utils.getLines;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class AdventOfCode12 {

  static List<Rule> rules = new ArrayList<>();
  static Plants plants = null;

  public static void main(String[] args) throws IOException {
    test();
    task1();
    task2();
  }

  public static void init(String filename) {
    List<String> lines = getLines(filename);
    String initial = lines.remove(0).substring("initial state: ".length());
    plants = new Plants(initial);

    for (String line : lines) {
      if (line.length() == 0) {
        continue;
      }

      String[] tokens = line.split(" ");
      rules.add(new Rule(tokens[0], tokens[2]));
    }
  }

  static void test() throws IOException {
    init("input12_test.txt");
    plants.print();
    doit(20);
  }

  static void task1() throws IOException {

    init("input12.txt");
    System.out.println("Input task 1: ");
    plants.print();
    System.out.println("");
    doit(20);
  }

  static void task2() throws IOException {

    init("input12.txt");
    System.out.print("Input task 2: ");
    plants.print();
    System.out.println("");
    // getBots(50000000000L);
    doit(100);

    // plants.print();
    long sum = plants.calc(50000000000L - 100);
    // print(plants, length);
    System.out.println("Result: " + sum);
  }

  static void doit(long loops) throws IOException {

    // Only for progress udpate
    long step = Math.max(loops / 100, 1000L);
    long checkin = 10;

    for (long age = 0; age < loops; age++) {

      if (age == checkin) { // progress update
        System.out.println(
            "age=" + age + "  length=" + plants.length + "  offest=" + plants.offset);
        checkin += Math.min(step, checkin);
      }

      plants.leftpad();
      plants.rightpad();

      plants.print();

      for (int j = 2; j < plants.length - 2; j++) {
        boolean a = false;
        for (Rule rule : rules) {
          if (match(j, rule, -2)
              && match(j, rule, -1)
              && match(j, rule, 0)
              && match(j, rule, 1)
              && match(j, rule, 2)) {
            a = rule.result;
          }
        }
        plants.next.set(j, a);
      }
      plants.swap();
    }

    // plants.print();
    long sum = plants.calc(0);
    // print(plants, length);
    System.out.println("Result: " + sum);
  }

  private static boolean match(int plant, Rule rule, int pos) {
    return (plants.plants.get(plant + pos) == (rule.pattern.charAt(2 + pos) == '#'));
  }

  private static boolean match2(int plant, Rule rule, int pos) {
    return (plant + pos >= 0 && plant + pos < plants.length)
        && (plants.plants.get(plant + pos) == (rule.pattern.charAt(2 + pos) == '#'));
  }

  private static boolean edgeMatch(int plant, Rule rule, int pos) {
    return (plant + pos < 0 || plant + pos >= plants.length) && rule.pattern.charAt(2 + pos) == '.';
  }

  static class Plants {
    BitSet plants = null;
    BitSet next = null;
    int offset = 0;
    int length = 0;

    public Plants(String initial) {
      length = initial.length() / 32;
      length = (length + 1) * 32;
      plants = new BitSet(length);
      offset = 0;
      for (int i = 0; i < initial.length(); i++) {
        plants.set(i, initial.charAt(i) == '#');
      }
    }

    void print() throws IOException {
      /*
            for (int i = 0; i < length; i++) {
              System.out.print(i == offset ? "0" : "-");
            }
            System.out.println();
      */
      for (int i = 0; i < length; i++) {
        System.out.print(plants.get(i) ? "#" : ".");
      }
      System.out.println();
    }

    public void leftpad() {
      if (plants.get(0) || plants.get(1) || plants.get(3) || plants.get(4)) { // leftpad
        offset += 32;
        int length2 = length + 32;
        BitSet tmp = new BitSet(length2);
        for (int i = 0; i < length; i++) {
          tmp.set(32 + i, plants.get(i));
        }
        plants = tmp;
        length = length2;
        next = new BitSet(length2);
      }
    }

    public void rightpad() {

      if (plants.get(length - 1)
          || plants.get(length - 2)
          || plants.get(length - 3)
          || plants.get(length - 4)) { // rightpad
        int length2 = length + 32;
        BitSet tmp = new BitSet(length2);
        for (int i = 0; i < length; i++) {
          tmp.set(i, plants.get(i));
        }
        plants = tmp;
        length = length2;
        next = new BitSet(length2);
      }
    }

    public void swap() {
      BitSet tmp = plants;
      plants = next;
      next = tmp;
    }

    public long calc(long offset2) {
      long sum = 0;
      for (int i = 0; i < length; i++) {
        if (plants.get(i)) {
          sum += i - offset + offset2;
        }
      }
      return sum;
    }
  }

  static class Rule {

    String pattern;
    boolean result;

    public Rule(String pattern, String result) {
      if (pattern.length() != 5) {
        throw new RuntimeException("wrong input pattern " + pattern);
      }
      if (result.length() != 1) {
        throw new RuntimeException("wrong input result" + result);
      }
      this.pattern = pattern;
      this.result = result.charAt(0) == '#';
    }
  }
}
