package aoc.aoc2018;

import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static java.lang.System.out;

import java.util.List;

public class AdventOfCode05 {

  public static void main(String[] args) throws Exception {
    doit(getTestLines());
    doit(getLines());
  }

  public static void doit(List<String> lines) throws Exception {

    for (String line : lines) {
      out.println(line);
      String result = deduce(line);
      out.println("Task 1: " + result.length() + "   " + result);
      int min = line.length();
      for (char c = 'A'; c <= 'Z'; c++) {
        result = deduce(line, c);
        if (result.length() < min) {
          min = result.length();
        }
      }
      out.println("Task 2: " + min);
    }
  }

  private static String deduce(String input) {
    return deduce(input, (char) ('z' + 1));
  }

  private static String deduce(String input, char exclude) {
    String line = input;
    int diff = 'a' - 'A';
    int e1 = exclude;
    int e2 = e1 + diff;
    int n;
    do {
      n = line.length();
      for (int i = 0; i < line.length() - 1; i++) {
        if (line.charAt(i) == e1 || line.charAt(i) == e2) {
          line = line.substring(0, i) + line.substring(i + 1);
          break;
        }
        if (Math.abs(line.charAt(i) - line.charAt(i + 1)) == diff) {
          line = line.substring(0, i) + line.substring(i + 2);
          break;
        }
      }
    } while (line.length() != n);
    return line;
  }

  public static void print() {
    out.println("");
  }
}
