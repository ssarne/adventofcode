package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Dec18 {

  static boolean debug = false;

  public static void main(String[] args) throws Exception {
    test1();
    task1();
    test2();
    task2();
  }

  static void test1() throws Exception {
    keyTest();
    check(doit("aoc2019/dec18_test1.txt"), 8);
    check(doit("aoc2019/dec18_test2.txt"), 86);
    check(doit("aoc2019/dec18_test3.txt"), 132);
    check(doit("aoc2019/dec18_test4.txt"), 136);
    check(doit("aoc2019/dec18_test5.txt"), 81);
  }

  static void task1() throws Exception {
    int result = doit("aoc2019/dec18.txt");
    System.out.println("Result: " + result);
  }

  static void test2() throws Exception {
    check(doit2("aoc2019/dec18_test6.txt"), 24);
    check(doit2("aoc2019/dec18_test7.txt"), 32);
    check(doit2("aoc2019/dec18_test8.txt"), 74);
  }

  static void task2() throws Exception {
    int result = doit2("aoc2019/dec18.txt");
    System.out.println("Result: " + result);
  }

  static int doit(String input) throws Exception {

    Board board = Board.create(input);
    board.print();

    Graph graph = board.createGraph();
    graph.print();

    Path path = graph.djikstra(addKey(board.keys, '@'));
    System.out.println("Path: " + path);

    return path.length;// last.d;
  }

  static int doit2(String input) throws Exception {

    Board board = Board.create(input);
    board.split();
    board.print();

    Graph graph = board.createGraph();
    graph.print();

    QPath path = graph.qdjikstra(addKeys(board.keys, '@', '£', '$', '&'));
    System.out.println("Path: " + path);

    return path.length;// last.d;
  }

  private static int getShift(char key) {
    int shift = key - 'A';
    switch (key) {
      case '@':
        shift = 26;
        break;
      case '£':
        shift = 27;
        break;
      case '$':
        shift = 28;
        break;
      case '&':
        shift = 29;
        break;
      default:
    }
    return shift;
  }

  private static boolean hasKey(int keys, char key) {
    key = Character.isLowerCase(key) ? Character.toUpperCase(key) : key;
    int shift = getShift(key);
    int pos = 1 << shift;
    int check = pos & keys;
    return check > 0;
  }

  private static int addKey(int keys, char key) {
    key = Character.isLowerCase(key) ? Character.toUpperCase(key) : key;
    int shift = getShift(key);
    int pos = 1 << shift;
    int res = pos | keys;
    return res;
  }

  private static int addKeys(int keys, char ... key) {
    for (char c : key) {
      keys = addKey(keys, c);
    }
    return keys;
  }

  private static boolean hasKeys(int required, int existing) {
    return (required & existing) == required;
  }

  private static String keyString(int keys) {
    StringBuilder sb = new StringBuilder();
    if (hasKey(keys, '@')) {
      sb.append('@');
    }
    if (hasKey(keys, '£')) {
      sb.append('£');
    }
    if (hasKey(keys, '$')) {
      sb.append('$');
    }
    if (hasKey(keys, '&')) {
      sb.append('&');
    }
    for (char c = 'A'; c <= 'Z'; c++) {
      if (hasKey(keys, c)) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  private static int asKeys(String keys) {
    int ks = 0;
    for (char c : keys.toCharArray()) {
      ks = addKey(ks, c);
    }
    return ks;
  }


  static void keyTest() {
    int keys = asKeys("A");
    check(hasKey(keys, 'A'), true);
    check(hasKey(keys, 'B'), false);
    check(hasKey(keys, 'C'), false);
    keys = addKey(keys, 'C');
    check(hasKey(keys, 'A'), true);
    check(hasKey(keys, 'B'), false);
    check(hasKey(keys, 'C'), true);
    check(keyString(keys), "AC");
    check(hasKeys(asKeys("A"), keys), true);
    check(hasKeys(asKeys("B"), keys), false);
    check(hasKeys(asKeys("C"), keys), true);
    check(hasKeys(asKeys("AB"), keys), false);
    check(hasKeys(asKeys("AC"), keys), true);
    check(hasKeys(asKeys("ABC"), keys), false);
  }


  private static class Pos {

    int x, y, d;
    int keys;

    Pos(int x, int y, int d, int keys) {
      this.x = x;
      this.y = y;
      this.d = d;
      this.keys = keys;
    }

    void add(char key) {
      keys = addKey(keys, key);
    }

    public String toString() {
      return "(" + x + "," + y + ") " + " d=" + d + "  keys=" + keyString(keys);
    }
  }

  private static class Graph {

    Map<Character, Node> nodes = new HashMap<>();

    Node getOrCreateNode(char c) {
      Node n = nodes.get(c);
      if (n == null) {
        n = new Node(c);
        nodes.put(c, n);
      }
      return n;
    }

    Path djikstra(int goal) {

      Map<String, Path> paths = new HashMap<>();
      Path start = new Path(nodes.get('@'), asKeys("@"), 0, null);
      paths.put(start.id(), start);

      while (true) {

        Path np = null; // next path

        for (Iterator<String> ip = paths.keySet().iterator(); ip.hasNext(); ) {
          Path p = paths.get(ip.next());
          boolean waste = true;

          for (Edge e : p.current.edges) {
            if (!hasKeys(e.keys, p.keys)) {
              // cannot unlock doors
              waste = false;
            } else if (e.to.visits.contains(addKey(p.keys, e.to.name))) {
              // path there already taken
            } else if (np == null || p.length + e.length < np.length) {
              // best so far
              np = new Path(e.to, addKey(p.keys, e.to.name), p.length + e.length, p);
              waste = false;
            } else {
              // shorter path chosen now
              waste = false;
            }
          }

          if (waste) {
            ip.remove();
          }
        }

        assert np != null;
        assert !paths.containsKey(np.id());

        // Found path
        if (np.keys == goal) {
          return np;
        }

        np.current.visits.add(np.keys);
        paths.put(np.id(), np);
      }
    }

    QPath qdjikstra(int goal) {

      Map<String, QPath> paths = new HashMap<>();
      QPath start = new QPath(asKeys("@£$&"), 0, null, nodes.get('@'), nodes.get('£'), nodes.get('$'), nodes.get('&'));
      paths.put(start.id(), start);

      while (true) {

        QPath np = null; // next path

        for (Iterator<String> ip = paths.keySet().iterator(); ip.hasNext(); ) {
          QPath p = paths.get(ip.next());
          boolean waste = true;

          for (Node curr : p.current) {

            for (Edge e : curr.edges) {
              if (!hasKeys(e.keys, p.keys)) {
                // cannot unlock doors
                waste = false;
              } else if (e.to.visits.contains(addKey(p.keys, e.to.name))) {
                // path there already taken
              } else if (np == null || p.length + e.length < np.length) {
                // best so far
                np = new QPath(addKey(p.keys, e.to.name), p.length + e.length, p, subst(p.current, e.from, e.to));
                waste = false;
              } else {
                // shorter path chosen now
                waste = false;
              }
            }
          }

          if (waste) {
            ip.remove();
          }
        }

        assert np != null;
        assert !paths.containsKey(np.id());

        // Found path
        if (np.keys == goal) {
          return np;
        }

        paths.put(np.id(), np);
        for (Node curr : np.current) {
          curr.visits.add(np.keys);
        }
      }
    }

    private Node [] subst(Node[] current, Node from, Node to) {
      Node [] next = new Node[current.length];
      for (int i = 0; i < current.length; i++) {
        next[i] = current[i].name == from.name ? to : current[i];
      }
      return next;
    }


    void print() {
      for (Node n : nodes.values()) {
        System.out.println(n);
      }
    }
  }

  private static class Node {

    char name;
    List<Edge> edges = new ArrayList<>();
    Set<Integer> visits = new HashSet<>();

    Node(char name) {
      this.name = name;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(name);
      for (Edge e : edges) {
        sb.append("  ").append(e);
      }
      return sb.toString();
    }
  }

  private static class Edge {

    Node from;
    Node to;
    int keys;
    int length;

    Edge(Node from, Node to, int keys, int length) {
      this.from = from;
      this.to = to;
      this.keys = keys;
      this.length = length;
    }

    public String toString() {
      return from.name + "-(" + length + ")-" + keyString(keys) + "->" + to.name;
    }
  }

  private static class Path {

    Node current;
    int keys;
    int length;
    Path prev;

    Path(Node current, int keys, int length, Path prev) {
      this.current = current;
      this.keys = keys;
      this.length = length;
      this.prev = prev;
    }

    String id() {
      return keyString(keys) + "_" + length + "_" + current.name;
    }

    private void trace(StringBuilder sb, Path prev) {
      if (prev == null) {
        return;
      }
      trace(sb, prev.prev);
      sb.append(prev.current.name);
      sb.append("->");
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      trace(sb, prev);
      sb.append(current.name);
      return id() + "  " + sb.toString();
    }
  }

  private static class QPath {

    Node [] current;
    int keys;
    int length;
    QPath prev;

    QPath(int keys, int length, QPath prev, Node ... current) {
      this.current = current;
      this.keys = keys;
      this.length = length;
      this.prev = prev;
    }

    String id() {
      return keyString(keys) + "_" + length + "_" + current[0].name + current[1].name + current[2].name + current[3].name;
    }

    private void trace(StringBuilder sb, QPath prev) {
      if (prev == null) {
        return;
      }
      trace(sb, prev.prev);
      sb.append(prev.current[0].name);
      sb.append(prev.current[1].name);
      sb.append(prev.current[2].name);
      sb.append(prev.current[3].name);
      sb.append("->");
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      trace(sb, prev);
      sb.append(current[0].name);
      sb.append(current[1].name);
      sb.append(current[2].name);
      sb.append(current[3].name);
      return id() + "  " + sb.toString();
    }
  }

  private static class Board {

    int width, height;
    char[][] board;
    int keys = 0;

    Board(int x, int y) {
      this.width = x;
      this.height = y;
      this.board = new char[height][width];
    }

    void set(int x, int y, char c) {
      board[y][x] = c;
    }

    char get(int x, int y) {
      return board[y][x];
    }

    char get(Pos pos) {
      return board[pos.y][pos.x];
    }

    Pos getStart() {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (get(x, y) == '@') {
            return new Pos(x, y, 0, 0);
          }
        }
      }
      return null;
    }

    static Board create(String input) {
      List<String> lines = getLines(input);
      Board board = new Board(lines.get(0).length(), lines.size());
      for (int y = 0; y < lines.size(); y++) {
        String line = lines.get(y);
        for (int x = 0; x < line.length(); x++) {
          char c = line.charAt(x);
          board.set(x, y, c);
          if (Character.isLetter(c) && Character.isLowerCase(c)) {
            board.keys = addKey(board.keys, c);
          }
        }
      }
      return board;
    }

    Board scratch() {
      Board scratch = new Board(width, height);
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          scratch.set(x, y, get(x, y));
        }
      }
      return scratch;
    }

    Graph createGraph() {
      Graph graph = new Graph();
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          char c = get(x, y);
          if (c == '@' || c == '£' || c == '$' || c == '&' || Character.isLowerCase(c)) {
            Node start = graph.getOrCreateNode(c);
            Board scratch = this.scratch();
            Queue<Pos> bfs = new LinkedList<>();
            bfs.add(new Pos(x, y, 0, 0));
            scratch.set(x, y, '#');
            while (!bfs.isEmpty()) {
              Pos p = bfs.poll();
              Pos[] candidates = {
                  new Pos(p.x + 1, p.y, p.d + 1, p.keys),
                  new Pos(p.x - 1, p.y, p.d + 1, p.keys),
                  new Pos(p.x, p.y + 1, p.d + 1, p.keys),
                  new Pos(p.x, p.y - 1, p.d + 1, p.keys)
              };
              for (Pos pp : candidates) {
                if (!scratch.on(pp)) {
                  continue;
                }
                char cc = scratch.get(pp);
                if (cc == '#') {
                  continue;
                }
                if (cc == '.' || cc == '@' || cc == '£' || cc == '$' || cc == '&') {
                  bfs.add(pp);
                  scratch.set(pp.x, pp.y, '#');
                  continue;
                }
                if (Character.isLowerCase(cc)) {
                  Node n = graph.getOrCreateNode(cc);
                  Edge e = new Edge(start, n, pp.keys, pp.d);
                  start.edges.add(e);
                  // bfs.add(pp);
                  scratch.set(pp.x, pp.y, '#');
                  continue;
                }
                if (Character.isUpperCase(cc)) {
                  pp.add(cc);
                  bfs.add(pp);
                  scratch.set(pp.x, pp.y, '#');
                  continue;
                }
              }
            }
          }
        }
      }
      return graph;
    }


    void print() {

      System.out.println("-----------------------");
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          System.out.print(get(x, y));
        }
        System.out.println();
      }
    }

    boolean on(Pos pos) {
      return on(pos.x, pos.y);
    }

    boolean on(int x, int y) {
      if (x < 0 || x >= width) {
        return false;
      }
      if (y < 0 || y >= height) {
        return false;
      }
      return true;
    }

    public void split() {
      Pos pos = getStart();
      set(pos.x, pos.y, '#');
      set(pos.x+1, pos.y, '#');
      set(pos.x-1, pos.y, '#');
      set(pos.x, pos.y+1, '#');
      set(pos.x, pos.y-1, '#');
      set(pos.x+1, pos.y+1, '@');
      set(pos.x-1, pos.y-1, '£');
      set(pos.x-1, pos.y+1, '$');
      set(pos.x+1, pos.y-1, '&');
    }
  }
}