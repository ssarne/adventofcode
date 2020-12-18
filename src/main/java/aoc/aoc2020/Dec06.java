package aoc.aoc2020;

import static aoc.utils.Utils.*;

import java.util.*;
import java.util.function.Function;

public class Dec06 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() {
    check(doit("aoc2020/dec06_test.txt", Dec06::calc1), 11);
    check(doit("aoc2020/dec06_test.txt", Dec06::calc2), 6);
  }

  public static void task1() {
    var result = doit("aoc2020/dec06.txt", Dec06::calc1);
    check(result, 6768);
    System.out.println("Result: " + result);
  }

  public static void task2() {
    int result = doit("aoc2020/dec06.txt", Dec06::calc2);
    check(result, 3489);
    System.out.println("Result: " + result);
  }

  public static int doit(String input, Function<List, Integer> calc) {

    List<Group> groups = new ArrayList<>();
    Group group = new Group();
    for (String line : getLines(input)) {
      if (line.trim().length() == 0) {
        groups.add(group);
        group = new Group();
        continue;
      }
      group.add(line);
    }

    if (group.answers.length() > 0) {
      groups.add(group);
    }

    return calc.apply(groups);
  }


  private static int calc1(List<Group> groups) {
    int sum = 0;
    for (Group group : groups) {
      Set<Character> cs = new HashSet();
      for (char c : group.answers.toCharArray()) {
        cs.add(c);
      }
      sum += cs.size();
    }
    return sum;
  }


  private static int calc2(List<Group> groups) {
    int sum = 0;
    for (Group group : groups) {
      HashMap<Character, Integer> cs = new HashMap();
      for (char c : group.answers.toCharArray()) {
        Integer n = cs.get(c);
        if (n == null) {
          cs.put(c, 1);
        } else {
          cs.put(c, n + 1);
        }
      }

      for (int i : cs.values()) {
        if (i == group.members) {
          sum++;
        }
      }
    }
    return sum;
  }

  private static class Group {

    String answers = "";
    int members = 0;

    public void add(String line) {
      answers = answers + line;
      members++;
    }
  }
}
