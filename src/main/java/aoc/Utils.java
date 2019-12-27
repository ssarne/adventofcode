package aoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

  public static List<String> getLines() {
    try {
      String name = getCallerClass();
      String year = name.split("\\.")[1].replace("aoc", "");
      String day = name.split("\\.")[2];
      if (!InputDownloader.hasInputFile(year, day)) {
        InputDownloader.getInputFile(year, day);
      }
      return getLines(InputDownloader.getPathname(year, day));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


  public static List<String> getLines(String filename) {
    if (filename == null) {
      return getLines();
    }
    try {
      String pathName = InputDownloader.getPathname(filename);
      File file = new File(pathName);
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

  private static String getCallerClass() {
    StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
    for (int i = 1; i < stElements.length; i++) {
      StackTraceElement ste = stElements[i];
      if (!ste.getClassName().equals(Utils.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
        return ste.getClassName();
      }
    }
    return null;
  }

  public static String indent(int level) {
    return new String(new char[2 * level]).replace('\0', ' ');
  }

  public static int asInt(String s) {
    return Integer.parseInt(s);
  }

  public static int[] ints(int... is) {
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

  public static String [] concat(String[] src, String s) {
    String [] res = new String[src.length + 1];
    System.arraycopy(src, 0, res, 0, src.length);
    res[src.length] = s;
    return res;
  }
}
