package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static aoc.utils.Utils.readAnswerAsLong;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import aoc.utils.LinearFunction;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Dec22 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() {
    check(positionOf(getTestLines(1), 10, 1), 7);
    check(positionOf(getTestLines(2), 10, 1), 4);
    check(positionOf(getTestLines(3), 10, 1), 5);
    check(positionOf(getTestLines(4), 10, 1), 4);
  }

  public static void task1() throws Exception {
    int result = positionOf(getLines(), 10007, 2019);
    System.out.println("Result: " + result);
    check(result, readAnswerAsLong(1));
  }

  public static void task2() {
    long result = cardAt(getLines(), 119315717514047L, 2020, 101741582076661L);
    System.out.println("Result: " + result);
    check(result, readAnswerAsLong(2));
  }

  /**
   * Simulate the shuffling process, line by line, of given size deck
   * Find the position of card 2019
   */
  private static int positionOf(List<String> lines, int size, int card) {

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

  private static long cardAt(List<String> lines, long size, long position, long reps) {

    var biSize = BigInteger.valueOf(size); // Size of deck
    var biReps = BigInteger.valueOf(reps); // Times to shuffle
    var biPos = BigInteger.valueOf(position); // Times to shuffle
    var operations = new LinkedList<LinearFunction>();

    // Create linear representation of each shuffle op
    // Apply operations in reverse order, to backtrack to original position
    // Each operation is the reverse version
    for (String line : lines) {
      if (line.startsWith("deal with increment ")) {
        // Reverse p2 = (p1 * value) % size
        // p1 = p2 * modInverse(value, size)
        int inc = Integer.parseInt(line.substring("deal with increment ".length()));
        BigInteger z = BigInteger.valueOf(inc).modInverse(biSize);
        operations.add(new LinearFunction(ONE.multiply(z).mod(biSize), ZERO));
      } else if (line.startsWith("cut ")) {
        // Reverse p2 = (p1 + size - value) % size
        // p1 = (p2 + value - size) % size = p2 + value % size
        int cut = Integer.parseInt(line.substring("cut ".length()));
        operations.add(new LinearFunction(ONE, BigInteger.valueOf(cut).mod(biSize)));
      } else if (line.equals("deal into new stack")) {
        // Reverse p2 = size - p1 - 1
        // p1 = (size - p2 - 1) % size = -p2 - 1
        operations.add(new LinearFunction(ONE.negate(), ONE.negate().subtract(biSize)));
      } else {
        throw new RuntimeException("CMH " + line);
      }
    }

    // Aggregate shuffle operations into one operation
    var operation = LinearFunction.ID;
    for (int i = operations.size() - 1; i >= 0; i--) {
      operation = operation.aggregate(operations.get(i));
    }

    // Aggregate reps into one operation
    operation = operation.repeat(biReps, biSize);

    // Lookup what position the given position started at
    return operation.apply(biPos).mod(biSize).longValue();
  }
}
