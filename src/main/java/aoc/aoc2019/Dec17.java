package aoc.aoc2019;

import static aoc.Utils.getLines;

import aoc.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public class Dec17 {

  public static void main(String[] args) throws Exception {
    task1();
    task2();
  }

  public static void task1() throws Exception {

    IntCode.Program program1 = IntCode.Program.create(getLines().get(0));
    IntCode.execute(program1);
    HashMap<Pos, Character> board = buildBoard(program1);

    int sum = calcIntersections(board);
    System.out.println("Result 1: " + sum);
  }

  public static void task2() throws Exception {

    String program = getLines().get(0);
    IntCode.Program program1 = IntCode.Program.create(program);
    IntCode.execute(program1);
    HashMap<Pos, Character> board = buildBoard(program1);

    // print(board);

    Pos start = find(board, '^');
    List<String> path = findPath(board, new ArrayList<String>(), start, '^');
    // System.out.println("Path: " + path);

    ABC abc = findABC(path);
    // System.out.println(abc);

    program1 = IntCode.Program.create(program);
    program1.mem.store(0, 2);
    program1.addInput(ascii(abc.main));
    program1.addInput(ascii(abc.a));
    program1.addInput(ascii(abc.b));
    program1.addInput(ascii(abc.c));
    program1.addInput(ascii("n"));

    IntCode.restart(program1);
    Result result = Result.parse(program1.output);
    // System.out.println("Output: \n" + result.output);
    System.out.println("Result 2: " + result.value);
  }

  private static String ascii(String ascii) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < ascii.length(); i++) {
      if (Character.isLetter(ascii.charAt(i))) {
        sb.append(ascii.charAt(i));
      } else if (Character.isDigit(ascii.charAt(i))) {
        sb.append(ascii.charAt(i));
        while (i + 1 < ascii.length() && Character.isDigit(ascii.charAt(i + 1))) {
          sb.append(ascii.charAt(++i));
        }
      }
      sb.append(i + 1 < ascii.length() ? ',' : '\n');
    }
    return sb.toString();
  }

  private static ABC findABC(List<String> path) {
    for (int l = 2; l < 20; l += 2) {
      String a = "";
      for (int i = 0; i < l; i++) {
        a += path.get(i);
      }
      ABC abc = findABC(path, "A", l, a);
      if (abc != null) {
        return abc;
      }
    }
    return null;
  }

  private static ABC findABC(List<String> path, String usage, int pos, String... abc) {

    // Done
    if (pos == path.size()) {
      return new ABC(usage, abc);
    }

    // Match of existing abc
    for (int i = 0; i < abc.length; i++) {
      String candidate = "";
      int j = 0;
      while (candidate.length() < abc[i].length()) {
        candidate += path.get(pos + j++);
      }
      if (candidate.equals(abc[i])) {
        ABC r = findABC(path, usage + "ABC".charAt(i), pos + j, abc);
        if (r != null) {
          return r;
        }
      }
    }

    int n = abc.length;
    if (n == 3) {
      return null;
    }

    // Create new a, b or c
    for (int l = 2; l < 20; l += 2) {
      String s = "";
      for (int i = 0; i < l; i++) {
        s += path.get(pos + i);
      }
      ABC r = findABC(path, usage + "ABC".charAt(n), pos + l, Utils.concat(abc, s));
      if (r != null) {
        return r;
      }
    }

    return null;
  }

  private static List<String> findPath(HashMap<Pos, Character> board, List<String> path, Pos start, char direction) {

    direction = turn(board, path, start, direction);
    if (direction == '_') {
      return path;
    }

    Pos pos = start;
    while (match(board, next(pos, direction), '#')) {
      pos = next(pos, direction);
    }

    path.add("" + (Math.abs(start.x - pos.x) + Math.abs(start.y - pos.y)));
    return findPath(board, path, pos, direction);
  }

  private static char turn(HashMap<Pos, Character> board, List<String> path, Pos pos, char direction) {
    if (direction == '^' && match(board, next(pos, '<'), '#')) {
      path.add("L");
      return '<';
    } else if (direction == '^' && match(board, next(pos, '>'), '#')) {
      path.add("R");
      return '>';
    } else if (direction == '<' && match(board, next(pos, 'v'), '#')) {
      path.add("L");
      return 'v';
    } else if (direction == '<' && match(board, next(pos, '^'), '#')) {
      path.add("R");
      return '^';
    } else if (direction == '>' && match(board, next(pos, '^'), '#')) {
      path.add("L");
      return '^';
    } else if (direction == '>' && match(board, next(pos, 'v'), '#')) {
      path.add("R");
      return 'v';
    } else if (direction == 'v' && match(board, next(pos, '>'), '#')) {
      path.add("L");
      return '>';
    } else if (direction == 'v' && match(board, next(pos, '<'), '#')) {
      path.add("R");
      return '<';
    } else {
      return '_';
    }
  }

  private static boolean match(HashMap<Pos, Character> board, Pos pos, char c) {
    Character cc = board.get(pos);
    return cc != null && cc.charValue() == c;
  }

  private static Pos next(Pos pos, char direction) {
    switch (direction) {
      case '>':
        return new Pos(pos.x + 1, pos.y);
      case '<':
        return new Pos(pos.x - 1, pos.y);
      case '^':
        return new Pos(pos.x, pos.y - 1);
      case 'v':
        return new Pos(pos.x, pos.y + 1);
      default:
        throw new RuntimeException("CMH");
    }
  }

  private static Pos find(HashMap<Pos, Character> board, char c) {
    for (Pos pos : board.keySet()) {
      if (board.get(pos).equals(c)) {
        return pos;
      }
    }
    return null;
  }

  private static int calcIntersections(HashMap<Pos, Character> board) {
    int sum = 0;
    for (Pos pos : board.keySet()) {
      if (board.get(pos) == '#') {
        if (board.containsKey(pos(pos.x + 1, pos.y)) && board.get(pos(pos.x + 1, pos.y)) == '#'
            && board.containsKey(pos(pos.x - 1, pos.y)) && board.get(pos(pos.x - 1, pos.y)) == '#'
            && board.containsKey(pos(pos.x, pos.y + 1)) && board.get(pos(pos.x, pos.y + 1)) == '#'
            && board.containsKey(pos(pos.x, pos.y - 1)) && board.get(pos(pos.x, pos.y - 1)) == '#') {
          // board.put(pos(x,y), 'O');
          sum += pos.x * pos.y;
        }
      }
    }
    // print(board);
    return sum;
  }

  private static HashMap<Pos, Character> buildBoard(IntCode.Program program) {
    HashMap<Pos, Character> board = new HashMap<>();
    int x = 0;
    int y = 0;
    while (!program.output.isEmpty()) {
      char c = (char) program.output.poll().longValue();
      switch (c) {
        case '#':
          board.put(pos(x++, y), c);
          break;
        case '.':
          board.put(pos(x++, y), c);
          break;
        case '^':
          board.put(pos(x++, y), c);
          break;
        case '>':
          board.put(pos(x++, y), c);
          break;
        case '<':
          board.put(pos(x++, y), c);
          break;
        case 'v':
          board.put(pos(x++, y), c);
          break;
        case 'X':
          board.put(pos(x++, y), c);
          break;
        case 10:
          y++;
          x = 0;
          break;
        default:
          throw new RuntimeException("CMH: " + c);
      }
    }
    return board;
  }


  private static Pos pos(long x, long y) {
    return new Pos(x, y);
  }

  public static void print(HashMap<Pos, Character> board) {
    long minx = 0, maxx = 0, miny = 0, maxy = 0;
    for (Pos pos : board.keySet()) {
      minx = Math.min(minx, pos.x);
      maxx = Math.max(maxx, pos.x);
      miny = Math.min(miny, pos.y);
      maxy = Math.max(maxy, pos.y);
    }

    System.out.println("-----------------------");
    for (long y = miny; y <= maxy; y++) {
      for (long x = minx; x <= maxx; x++) {
        if (board.containsKey(pos(x, y))) {
          System.out.print(board.get(pos(x, y)));
        } else {
          System.out.print(' ');
        }
      }
      System.out.println();
    }
  }

  private static class ABC {

    String main;
    String a;
    String b;
    String c;

    public ABC(String main, String a, String b, String c) {
      this.main = main;
      this.a = a;
      this.b = b;
      this.c = c;
    }

    public ABC(String usage, String... abc) {
      this.main = usage;
      this.a = abc.length > 0 ? abc[0] : "";
      this.b = abc.length > 1 ? abc[1] : "";
      this.c = abc.length > 2 ? abc[2] : "";
    }

    public ABC(String usage, List<String> abc) {
      this.main = usage;
      this.a = abc.size() > 0 ? abc.get(0) : "";
      this.b = abc.size() > 1 ? abc.get(1) : "";
      this.c = abc.size() > 2 ? abc.get(2) : "";
    }

    @Override
    public String toString() {
      return "ABC{" +
             "main='" + main + '\'' +
             ", a='" + a + '\'' +
             ", b='" + b + '\'' +
             ", c='" + c + '\'' +
             '}';
    }
  }

  private static class Result {

    long value;
    String output;

    public Result(long value, String output) {
      this.value = value;
      this.output = output;
    }

    private static Result parse(Queue<Long> output) {
      StringBuilder sb = new StringBuilder();
      long result = 0;
      while (!output.isEmpty()) {
        long l = output.poll();
        if (l <= 0xFF) {
          sb.append((char) l);
        } else {
          result = l;
        }
      }
      return new Result(result, sb.toString());
    }
  }

  private static class Pos {

    long x, y;

    public Pos(long x, long y) {
      this.x = x;
      this.y = y;
    }

    public Pos(String s) {
      String[] sp = s.split(",");
      this.x = Long.parseLong(sp[0]);
      this.y = Long.parseLong(sp[1]);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Pos pos = (Pos) o;
      return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
      return (int) (1000 * x + y);
    }

    @Override
    public String toString() {
      return x + "," + y;
    }
  }
}
