package aoc.aoc2018;

import static aoc.Utils.asInt;
import static aoc.Utils.check;
import static aoc.Utils.getLines;

import aoc.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdventOfCode08 {

  public static void main(String[] args) throws Exception {
    test1();
    task1();
    test2();
    task2();
  }

  public static void test1() throws Exception {
    int[] input = input("input08_test.txt");
    Node root = new Node();
    read(input, 0, root);
    int result = root.sum();
    System.out.println("Result: " + result);
    Utils.check(result, 138);
  }

  public static void task1() throws Exception {
    int[] input = input("input08.txt");
    Node root = new Node();
    read(input, 0, root);
    int result = root.sum();
    System.out.println("Result: " + result);
  }

  public static void test2() throws Exception {
    int[] input = input("input08_test.txt");
    Node root = new Node();
    read(input, 0, root);
    int result = root.sum2();
    System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    int[] input = input("input08.txt");
    Node root = new Node();
    read(input, 0, root);
    int result = root.sum2();
    System.out.println("Result: " + result);
  }

  private static int[] input(String name) {
    List<String> lines = getLines(name);
    String[] inputs = lines.get(0).split(" ");
    return Arrays.stream(inputs).mapToInt(s -> asInt(s)).toArray();
  }

  static int read(int[] input, int pos, Node root) {
    int nodes = input[pos++];
    int entries = input[pos++];
    for (int i = 0; i < nodes; i++) {
      Node node = new Node();
      root.nodes.add(node);
      pos = read(input, pos, node);
    }
    for (int i = 0; i < entries; i++) {
      root.metas.add(input[pos++]);
    }
    return pos;
  }

  static class Node {
    List<Node> nodes;
    List<Integer> metas;

    public Node() {
      this.nodes = new ArrayList<Node>();
      this.metas = new ArrayList<Integer>();
    }

    int sum() {
      int sum = 0;
      for (Node node : nodes) {
        sum += node.sum();
      }
      for (Integer i : metas) {
        sum += i.intValue();
      }
      return sum;
    }

    int sum2() {
      int sum = 0;
      if (nodes.size() == 0) {
        for (Integer i : metas) {
          sum += i.intValue();
        }
      } else {
        for (Integer i : metas) {
          int pos = i.intValue() - 1;
          if (pos >= 0 && pos < nodes.size()) {
            sum += nodes.get(pos).sum2();
          }
        }
      }
      return sum;
    }
  }
}
