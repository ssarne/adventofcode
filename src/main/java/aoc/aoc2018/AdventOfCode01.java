package aoc.aoc2018;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AdventOfCode01 {

  public static void main(String[] args) throws IOException {

    int c = 0;
    Set<Integer> exists = new HashSet<>();
    exists.add(c);

    for (int lap = 1; true; lap++) {

      File file = new File("src/main/java/com/spotify/personal/ssa/advent/input01.txt");
      BufferedReader br = new BufferedReader(new FileReader(file));

      for (String line = br.readLine(); line != null; line = br.readLine()) {
        if (line.charAt(0) == '+') {
          c += Integer.parseInt(line.substring(1));
        } else if (line.charAt(0) == '-') {
          c -= Integer.parseInt(line.substring(1));
        } else {
          System.out.println("Unhandled string: " + line);
        }

        if (exists.contains(c)) {
          System.out.println("Task 1: First frequency align: " + c);
          return;
        }

        exists.add(c);
      }

      // exists.stream().sorted().forEach(s -> System.out.println(s));
      if (lap == 1) {
        System.out.println("Task 1: Final frequency: " + c);
      }
    }
  }
}
