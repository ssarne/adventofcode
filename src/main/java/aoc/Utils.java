package aoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

  public static List<String> getLines(String filename) {
    try {
      File file = new File("src/main/resources/" + filename);
      BufferedReader br = new BufferedReader(new FileReader(file));

      ArrayList<String> lines = new ArrayList<>();
      for (String line = br.readLine(); line != null; line = br.readLine()) {
        lines.add(line);
      }
      return lines;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String indent(int level) {
    return new String(new char[2 * level]).replace('\0', ' ');
  }

  public static int asInt(String s) {
    return Integer.parseInt(s);
  }

  public static int [] ints(int ... is) {
    return is;
  }

  public static void check(boolean actual, boolean expected) {
    if (actual != expected) {
      System.err.println("Failure: actual=" + actual + "  expected=" + expected);
    }
  }

  public static void check(int actual, int expected) {
    if (actual != expected) {
      System.err.println("Failure: actual=" + actual + "  expected=" + expected);
    }
  }

  public static void check(long actual, long expected) {
    if (actual != expected) {
      System.err.println("Failure: actual=" + actual + "  expected=" + expected);
    }
  }

  public static void check(String actual, String expected) {
    if (!actual.equals(expected)) {
      System.err.println("Failure: actual=" + actual + "  expected=" + expected);
    }
  }

  public static int after(String line, String part) {
    return line.indexOf(part) + part.length();
  }

}
