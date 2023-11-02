package aoc.aoc2018;

import static aoc.utils.Utils.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AdventOfCode02 {

  public static void main(String[] args) throws IOException {
    System.out.println(task1());
    System.out.println(task2());
  }

  public static int task1() throws IOException {

    List<String> lines = getLines();

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
    return twos * threes;
  }

  public static String task2() throws IOException {

    List<String> lines = getLines();
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
            return result;
          }
        }
      }
    }
    throw new RuntimeException("Failed.");
  }
}
