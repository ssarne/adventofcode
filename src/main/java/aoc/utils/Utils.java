package aoc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

  private static int [] getYearAndDay() {
    String name = getCallerClass();
    int year = Integer.parseInt(name
            .split("\\.")[1]
            .replace("aoc", ""));
    int day = Integer.parseInt(name
            .split("\\.")[2]
            .replace("Kt", "")
            .replace("AdventOfCode", "")
            .replace("Dec", ""));
    return ints(year, day);
  }

  public static List<String> getLines() {
    var yd = getYearAndDay();
    return getLines(yd[0], yd[1]);
  }

  public static List<String> getLines(int year, int day) {
    try {
      var yds = (day < 10 ? "0" + day : day);
      if (!InputDownloader.hasInputFile("" + day, "dec" + yds)) {
        InputDownloader.getInputFile("" + year, "dec" + yds);
      }
      return getLinesFromPath(InputDownloader.getInputPath("" + year, "dec" + yds));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static int readAnswerAsInt(int part) {
    return Integer.parseInt(readAnswer(part));
  }

  public static long readAnswerAsLong(int part) {
    return Long.parseLong(readAnswer(part));
  }

  public static String readAnswer(int part) {
    var yd = getYearAndDay();
    return readAnswer(part, yd[0], yd[1]);
  }

  public static String readAnswer(int part, int year, int day) {
    var yds = (day < 10 ? "0" + day : day);
    var path = InputDownloader.getInputPath("" + year, "dec" + yds)
        .replace(".txt", ".out");
    var lines = getLinesFromPath(path);
    return lines.get(part - 1);
  }

  public static List<String> getTestLines() {
    return getTestLines(1);
  }

  public static List<String> getTestLines(int i) {
    var yd = getYearAndDay();
    var yds = (yd[1] < 10 ? "0" + yd[1] : yd[1]);
    var paths = new String[] {
            "src/main/resources/aoc" + yd[0] + "/dec" + yds + "_test.txt",
            "src/main/resources/aoc" + yd[0] + "/dec" + yds + "_test" + i + ".txt",
            "src/main/resources/aoc" + yd[0] + "/dec" + yds + "_test0" + i + ".txt",
            "src/main/resources/aoc" + yd[0] + "/input" + yds + "_test.txt",
            "src/main/resources/aoc" + yd[0] + "/input" + yds + "_test" + i + ".txt",
            "src/main/resources/aoc" + yd[0] + "/input" + yds + "_test_" + i + ".txt",
            "src/main/resources/aoc" + yd[0] + "/input_" + yds + "_test.txt",
            "src/main/resources/aoc" + yd[0] + "/input_" + yds + "_test" + i + ".txt",
            "src/main/resources/aoc" + yd[0] + "/input_" + yds + "_test_" + i + ".txt",
    };
    var path = "";
    for (String p : paths)
      if (new File(p).exists())
        path = p;
    return getLinesFromPath(path);
  }

  public static List<String> getLines(String filename) {
    if (filename == null) {
      throw new RuntimeException("Given filename is null. Use getLines()");
    }

    var yd = getYearAndDay();
    var base = "src/main/resources/";
    if (filename.startsWith(base)) {
      base = "";
    }
    String path = base + "aoc" + yd[0] + "/" + filename;
    return getLinesFromPath(path);
  }

  private static List<String> getLinesFromPath(String path) {
    try {

      File file = new File(path);
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
    return getNumbers(getLines(input));
  }

  public static long[] getNumbers(List<String> input) {
    long [] numbers = new long[input.size()];
    for (int i = 0; i < input.size(); i++) {
      numbers[i] = Long.parseLong(input.get(i));
    }
    return numbers;
  }

  public static int[] getInts(String input) {
    return getInts(getLines(input));
  }

  public static int[] getInts(List<String> lines) {
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

  public static String [] strings(String... ss) {
    return ss;
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
