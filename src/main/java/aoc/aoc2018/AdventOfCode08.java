package aoc.aoc2018;

import aoc.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static aoc.utils.Utils.*;

public class AdventOfCode08 {

  public static void main(String[] args) throws Exception {
    Utils.check(execute1(getTestLines()), 138);
    System.out.println(execute1(getLines()));
    Utils.check(execute2(getTestLines()), 66);
    System.out.println(execute2(getLines()));
  }

  public static int execute1(List<String> lines) throws Exception {
    int[] input = input(lines);
    Node root = new Node();
    read(input, 0, root);
    return root.sum();
  }

  public static int execute2(List<String> lines) throws Exception {
    int[] input = input(lines);
    Node root = new Node();
    read(input, 0, root);
    return root.sum2();
  }

  private static int[] input(List<String> lines) {
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
