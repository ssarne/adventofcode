package aoc.aoc2018;

import aoc.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdventOfCode01 {

  public static void main(String[] args) {
    System.out.println(execute(1));
    System.out.println(execute(2));
  }

  private static int execute(int task) {

    int c = 0;
    Set<Integer> exists = new HashSet<>();
    exists.add(c);

    for (int lap = 1; true; lap++) {

      for (String line : Utils.getLines()) {
        if (line.charAt(0) == '+') {
          c += Integer.parseInt(line.substring(1));
        } else if (line.charAt(0) == '-') {
          c -= Integer.parseInt(line.substring(1));
        } else {
          System.out.println("Unhandled string: " + line);
        }

        if (exists.contains(c) && task == 2) {
          return c; // First frequency align
        }

        exists.add(c);
      }

      if (lap == 1 && task == 1) {
        return c; // Resulting frequency
      }
    }
  }
}
