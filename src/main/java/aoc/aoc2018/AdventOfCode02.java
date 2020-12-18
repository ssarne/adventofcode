package aoc.aoc2018;

import static aoc.utils.Utils.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AdventOfCode02 {

  public static void task1() throws IOException {

    List<String> lines = getLines("input02.txt");

    int twos = 0;
    int threes = 0;

    for (String line : lines) {
      HashMap<Character, Integer> chars = new HashMap<>();
      for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        Integer count = chars.get(c);
        if (count == null) {
          chars.put(c, 1);
        } else {
          chars.put(c, count.intValue() + 1);
        }
      }

      if (chars.values().stream().filter(v -> v.intValue() == 2).count() > 0) {
        twos++;
      }

      if (chars.values().stream().filter(v -> v.intValue() == 3).count() > 0) {
        threes++;
      }
    }
    System.out.println("Hash: " + twos * threes);
  }

  public static void task2() throws IOException {

    List<String> lines = getLines("input02.txt");
    HashMap<Character, Integer> chars = new HashMap<>();
    for (String line1 : lines) {
      for (String line2 : lines) {
        if (line1 != line2) {
          int missMatches = 0;
          for (int i = 0; i < line1.length(); i++) {
            if (line1.charAt(i) != line2.charAt(i)) {
              missMatches++;
            }
          }
          if (missMatches == 1) {
            String result = "";
            for (int i = 0; i < line1.length(); i++) {
              if (line1.charAt(i) == line2.charAt(i)) {
                result = result + line1.charAt(i);
              }
            }
            System.out.println("Match: " + result);
            return;
          }
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    task1();
    task2();
  }
}
