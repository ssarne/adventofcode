package aoc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

  public static List<String> getLines() {
    try {
      String name = getCallerClass();
      String year = name.split("\\.")[1].replace("aoc", "");
      String day = name.split("\\.")[2].replace("Kt", "");
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

  public static String getCallerClass() {
    StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
    for (int i = 1; i < stElements.length; i++) {
      StackTraceElement ste = stElements[i];
      if (!ste.getClassName().equals(Utils.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
        return ste.getClassName();
      }
    }
    return null;
  }

  public static long[] getNumbers(String input) {
    List<String> lines = getLines(input);
    long [] numbers = new long[lines.size()];
    for (int i = 0; i < lines.size(); i++) {
      numbers[i] = Long.parseLong(lines.get(i));
    }
    return numbers;
  }


  public static int[] getInts(String input) {
    List<String> lines = getLines(input);
    int [] numbers = new int[lines.size()];
    for (int i = 0; i < lines.size(); i++) {
      numbers[i] = Integer.parseInt(lines.get(i));
    }
    return numbers;
  }


  public static String indent(int level) {
    return new String(new char[2 * level]).replace('\0', ' ');
  }

  public static int asInt(String s) {
    return Integer.parseInt(s);
  }

  public static int[] asInts(String s) {
    String [] sv = s.split(",");
    int [] ints = new int[sv.length];
    for (int i = 0; i < sv.length; i++) {
      ints[i] = Integer.parseInt(sv[i]);
    }
    return ints;
  }


  public static int[] ints(int... is) {
    return is;
  }

  public static long[] longs(long... ls) {
    return ls;
  }

  public static int ctoi(String input, int index) {
    return Integer.parseInt(String.valueOf(input.charAt(index)));
  }

  public static int find(int[] arr, int value) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static void check(boolean actual, boolean expected) {
    if (actual != expected) {
      System.err.println("Failure: actual=" + actual + "  expected=" + expected);
    }
  }

  public static void check(long [] actual, long [] expected) {
    if (actual.length != expected.length) {
      System.err.println("Failure: length actual=" + actual.length + "  length expected=" + expected.length);
    }
    for (int i = 0; i < actual.length; i++) {
      if (actual[i] != expected[i]) {
        System.err.println("Failure: actual["+i+"]=" + actual[i] + "  expected[\"+i+\"]=" + expected[i]);
      }
    }
  }

  public static void check(int [] actual, int [] expected) {
    if (actual.length < 20 && expected.length < 20) {
      check(Arrays.toString(actual), Arrays.toString(expected));
      return;
    }
    if (actual.length != expected.length) {
      System.err.println("Failure: length actual=" + actual.length + "  length expected=" + expected.length);
    }
    for (int i = 0; i < actual.length; i++) {
      if (actual[i] != expected[i]) {
        System.err.println("Failure: actual["+i+"]=" + actual[i] + "  expected["+i+"]=" + expected[i]);
      }
    }
  }

  public static void check(char actual, char expected) {
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

  public static void ensure(boolean b) {
    if (!b) {
      throw new AssertionError("Failed sanity check");
    }
  }

  public static boolean isNumber(String s) {
    if (s == null) {
      return false;
    }

    try {
      Integer.parseInt(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isNumber(String s, int low, int high) {
    if (s == null) {
      return false;
    }

    try {
      int i = Integer.parseInt(s);
      return i >= low && i <= high;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean isNumberWithUnit(String s, int low, int high, String unit) {
    if (s == null) {
      return false;
    }

    if (!s.endsWith(unit)) {
      return false;
    }

    return isNumber(s.replace(unit, ""), low, high);
  }

  public static void print(int[] arr) {
    System.out.println(prettyToString(arr));
  }

  public static void print(String str) {
    System.out.println(str);
  }

  public static String prettyToString(int[] arr) {
    StringBuilder sb = new StringBuilder("[");
    sb.append(arr.length > 0 ? Integer.toString(arr[0]) : "");
    for (int i = 1; i < Math.min(arr.length, 15); i++) {
      sb.append(" ").append(arr[i]);
    }
    if (arr.length > 20) {
      sb.append(" ...");
      for (int i = arr.length - 5; i < arr.length; i++) {
        sb.append(" ").append(arr[i]);
      }
    }
    sb.append("]");
    return sb.toString();
  }
}
