package aoc.aoc2019;

import static aoc.Utils.check;
import static aoc.Utils.getLines;
import static java.lang.System.out;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class Dec22 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    check(doit1("aoc2019/dec22_test1.txt", 10, 1), 7);
    check(doit1("aoc2019/dec22_test2.txt", 10, 1), 4);
    check(doit1("aoc2019/dec22_test3.txt", 10, 1), 5);
    check(doit1("aoc2019/dec22_test4.txt", 10, 1), 4);

    check(doit2("aoc2019/dec22_test1.txt", 10, 1, 1), 7);
    check(doit2("aoc2019/dec22_test2.txt", 10, 1, 1), 4);
    check(doit2("aoc2019/dec22_test3.txt", 10, 1, 1), 5);
    check(doit2("aoc2019/dec22_test4.txt", 10, 1, 1), 4);
  }

  public static void task1() throws Exception {
    int result = doit1("aoc2019/dec22.txt", 10007, 2019);
    System.out.println("Result: " + result);
  }

  private static int doit1(String input, int size, int card) throws Exception {

    List<String> lines = getLines(input);
    int[] cards = new int[size];
    int[] next = new int[size];
    for (int i = 0; i < size; i++) {
      cards[i] = i;
    }

    for (String line : lines) {

      if (line.startsWith("deal with increment ")) {
        int inc = Integer.parseInt(line.substring("deal with increment ".length()));
        for (int i = 0; i < cards.length; i++) {
          next[i * inc % size] = cards[i];
        }
      } else if (line.startsWith("cut ")) {
        int cut = Integer.parseInt(line.substring("cut ".length()));
        cut = (cut < 0 ? size + cut : cut);
        for (int i = 0; i < cards.length; i++) {
          next[(size + i - cut) % size] = cards[i];
        }
      } else if (line.equals("deal into new stack")) {
        for (int i = 0; i < cards.length; i++) {
          next[size - i - 1] = cards[i];
        }
      } else if (line.startsWith("Result: ")) {
        int[] result = Arrays.stream(line.substring("Result: ".length()).split(" "))
            .mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < cards.length; i++) {
          if (!(cards[i] == result[i])) {
            System.err.println(i + ": " + cards[i] + " != " + result[i]);
          }
        }
        continue;
      } else {
        assert false; // CMH
      }

      int[] tmp = next;
      next = cards;
      cards = tmp;
    }

    for (int i = 0; i < cards.length; i++) {
      if (cards[i] == card) {
        return i;
      }
    }
    return -1;
  }

  public static long doit2(String input, long size, long card, long reps) {

    List<String> lines = getLines(input);
    int CUT = 1;
    int INC = 2;
    int STACK = 3;
    long [] shuffles = new long[2 * lines.size()];

    int pos = 0;
    for (String line : lines) {
      if (line.startsWith("deal with increment ")) {
        long inc = Long.parseLong(line.substring("deal with increment ".length()));
        shuffles[pos++] = INC;
        shuffles[pos++] = inc;
      } else if (line.startsWith("cut ")) {
        long cut = Long.parseLong(line.substring("cut ".length()));
        cut = (cut < 0 ? size + cut : cut);
        shuffles[pos++] = CUT;
        shuffles[pos++] = cut;
      } else if (line.equals("deal into new stack")) {
        shuffles[pos++] = STACK;
        shuffles[pos++] = 0;
      } else if (line.startsWith("Result: ")) {
        continue;
      } else {
        assert false; // CMH
      }
    }

    for (long r = 0; r < reps; r++) {
      System.out.println("r=" + r + ", card=" + card);
      for (int pc = 0; pc < shuffles.length; pc += 2) {
        if (shuffles[pc] == INC) {
          card = card * shuffles[pc+1] % size;
        } else if (shuffles[pc] == CUT) {
          card = (card + size - shuffles[pc+1]) % size;
        } else if (shuffles[pc] == STACK) {
          card = size - card - 1;
        }
      }
    }

    return card;


//    BigInteger biCard = BigInteger.valueOf(card);
//    BigInteger biSize = BigInteger.valueOf(size);
//    for (long r = 0; r < reps; r++) {
//      System.out.println("r=" + r + ", card=" + biCard);
//      for (int pc = 0; pc < shuffles.length; pc += 2) {
//        if (shuffles[pc] == INC) {
//          biCard = biCard.multiply(BigInteger.valueOf(shuffles[pc+1])).mod(biSize);
//        } else if (shuffles[pc] == CUT) {
//          long cut = shuffles[pc+1];
//          cut = (cut < 0 ? size + cut : cut);
//          biCard = (biCard.add(biSize).subtract(BigInteger.valueOf(cut))).mod(biSize);
//        } else if (shuffles[pc] == STACK) {
//          biCard = biSize.subtract(biCard).subtract(BigInteger.ONE);
//        }
//      }
//    }
//     return biCard.longValue();
  }

  public static void task2() throws Exception {
    System.out.println(doit2("aoc2019/dec22.txt", 119315717514047L, 2020, 100));
  }

}
