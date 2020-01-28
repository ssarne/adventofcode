package aoc.aoc2019;

import static aoc.Utils.check;
import static aoc.Utils.getLines;
import static java.lang.System.in;
import static java.lang.System.out;

import java.util.Arrays;
import java.util.List;

public class Dec16 {

  public static void main(String[] args) throws Exception {
    test1();
    test2();
    task1();
    task2();
  }

  public static void test1() throws Exception {
    check(doit("12345678", 4), "01029498");
    check(doit("80871224585914546619083218645595", 100), "24176176");
    check(doit("19617804207202209144916044189917", 100), "73745418");
    check(doit("69317163492948606335995924319873", 100), "52432133");
  }

  public static void task1() throws Exception {
    String input = getLines("aoc2019/dec16.txt").get(0);
    String result = doit(input, 100);
    System.out.println("Result: " + result);
  }


  public static String doit(String input, int phases) throws Exception {

    int[] base = {0, 1, 0, -1};
    int[] phase0 = new int[input.length()];
    int[] phase1 = new int[input.length()];

    for (int i = 0; i < input.length(); i++) {
      phase0[i] = Character.getNumericValue(input.charAt(i));
    }

    for (int f = 0; f < phases; f++) {
      for (int p = 0; p < input.length(); p++) {
        //out.print("sum = ");
        int sum = 0;
        for (int i = 0; i < input.length(); i++) {
          int m = base[((i + 1) / (p + 1)) % 4];
          sum += m * phase0[i];
          //out.print((i != 0 ? " + " : "") + phase0[i] + " * " + m);
        }
        phase1[p] = Math.abs(sum) % 10;
        //out.println(" = " + phase1[p]);
      }
      int[] tmp = phase1;
      phase1 = phase0;
      phase0 = tmp;
      System.out.println("Phase " + f + ": " + Arrays.toString(phase0));
    }

    String result = "" + phase0[0] + phase0[1] + phase0[2] + phase0[3] + phase0[4] + phase0[5] + phase0[6] + phase0[7];
    return result;
  }

  public static void test2() throws Exception {
    check(doit2("03036732577212944063491565474664", 100, 10000), "84462026");
    check(doit2("02935109699940807407585447034323", 100, 10000), "78725270");
    check(doit2("03081770884921959731165446850517", 100, 10000), "53553731");
  }

  public static void task2() throws Exception {
    String input = getLines("aoc2019/dec16.txt").get(0);
    String result = doit2(input, 100, 10000);
    System.out.println("Result: " + result);
  }

  // This is simply
  public static String doit2(String input, int phases, int multiplier) {

    int len = input.length() * multiplier;
    int[] base = {0, 1, 0, -1};
    int[] phase0 = new int[len];
    int[] phase1 = new int[len];
    int offset = Integer.parseInt("" + input.charAt(0) + input.charAt(1) + input.charAt(2) + input.charAt(3) + input.charAt(4) + input.charAt(5) + input.charAt(6));

    System.out.println("START len=" + len + "  offset=" + offset + "  input=" + input);

    for (int m = 0; m < multiplier; m++) {
      int b = m * input.length();
      for (int i = 0; i < input.length(); i++) {
        phase0[b + i] = Character.getNumericValue(input.charAt(i));
      }
    }

    for (int f = 0; f < phases; f++) {
      System.out.println("Phase " + f);
      for (int p = offset; p < len; p++) {
        int sum = 0;
        for (int i = offset; i < len; i++) {
          int m = base[((i + 1) / (p + 1)) % 4];
          sum += m * phase0[i];
        }
        phase1[p] = Math.abs(sum) % 10;
      }
      int[] tmp = phase1;
      phase1 = phase0;
      phase0 = tmp;
    }

    String result = ""
                    + phase0[offset + 0]
                    + phase0[offset + 1]
                    + phase0[offset + 2]
                    + phase0[offset + 3]
                    + phase0[offset + 4]
                    + phase0[offset + 5]
                    + phase0[offset + 6]
                    + phase0[offset + 7];
    return result;
  }

}
