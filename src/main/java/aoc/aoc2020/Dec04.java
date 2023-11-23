package aoc.aoc2020;

import static aoc.utils.Utils.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Dec04 {


  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() {
    check(doit(getTestLines(1), Dec04::isValid1), 2);
    check(doit(getTestLines(2), Dec04::isValid2), 4);
  }

  public static void task1() {
    int result = doit(getLines(), Dec04::isValid1);
    System.out.println("Result: " + result);
    check(result, readAnswerAsInt(1));
  }

  public static void task2() {
    int result = doit(getLines(), Dec04::isValid2);
    System.out.println("Result: " + result);
    check(result, readAnswerAsInt(2));
  }

  public static int doit(List<String> lines, Function<HashMap, Boolean> f) {

    int valid = 0;

    HashMap<String, String> pass = new HashMap<>();
    for (String line : lines) {

      if ((line.equals(""))) {  // Passport completed
        valid += f.apply(pass) ? 1 : 0;
        pass = new HashMap<>();
        continue;
      }

      for (String t : line.split(" ")) {
        String[] tt = t.split(":");
        ensure(!pass.containsKey(tt[0]));
        ensure(tt.length == 2);
        pass.put(tt[0], tt[1]);
      }
    }

    // Last passport
    if (pass.size() > 0) {
      if (f.apply(pass)) {
        valid++;
      }
    }

    return valid;
  }

  private static boolean isValid1(HashMap<String, String> stuff) {

    return stuff.containsKey("byr")
           && stuff.containsKey("iyr")
           && stuff.containsKey("eyr")
           && stuff.containsKey("hgt")
           && stuff.containsKey("hcl")
           && stuff.containsKey("ecl")
           && stuff.containsKey("pid");
    //&& stuff.containsKey("cid")
  }

  private static boolean isValid2(HashMap<String, String> stuff) {

    return isNumber(stuff.get("byr"), 1920, 2002)
           && isNumber(stuff.get("iyr"), 2010, 2020)
           && isNumber(stuff.get("eyr"), 2020, 2030)
           && (isNumberWithUnit(stuff.get("hgt"), 150, 193, "cm") || isNumberWithUnit(stuff.get("hgt"), 59, 76, "in"))
           && isColor(stuff, "hcl")
           && isEyeColor(stuff, "ecl")
           && isPid(stuff, "pid");     // ignore cid
  }

  private static void print(HashMap<String, String> stuff, boolean v) {
    System.out.print(v ? "Valid:   " : "INVALID: ");
    for (String k : stuff.keySet()) {
      System.out.print(k + ":" + stuff.get(k) + "  ");
    }
    System.out.println("");
  }

  private static boolean isColor(HashMap<String, String> stuff, String field) {
    if (!stuff.containsKey(field)) {
      return false;
    }

    String value = stuff.get(field);
    if (!value.startsWith("#")) {
      return false;
    }

    return value.substring(1).matches("[a-f0-9][a-f0-9][a-f0-9][a-f0-9][a-f0-9][a-f0-9]");
  }

  static Set<String> colors = new HashSet<>(Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth"));

  private static boolean isEyeColor(HashMap<String, String> stuff, String field) {
    if (!stuff.containsKey(field)) {
      return false;
    }

    return colors.contains(stuff.get(field));
  }

  private static boolean isPid(HashMap<String, String> stuff, String field) {
    if (!stuff.containsKey(field)) {
      return false;
    }

    return stuff.get(field).matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
  }
}
