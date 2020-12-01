package aoc.aoc2018;

import static aoc.Utils.check;
import static aoc.Utils.getLines;
import static java.lang.System.out;

import aoc.Utils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AdventOfCode15 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    // task2();
  }

  public static void test() throws Exception {
//    int result1 = doit("input15_test_1.txt");
//    System.out.println("Result test: " + result1);
//
//    int result2 = doit("input15_test_2.txt");
//    System.out.println("Result test2: " + result2);
//    check(result2, 27730);

    int result3 = doit("input15_test_3.txt");
    System.out.println("Result test3: " + result3);
    Utils.check(result3, 36334);
  }

  public static void task1() throws Exception {
    //int result = doit("input15.txt");
    // 260202 too high
    //System.out.println("Result: " + result);
  }

  public static void task2() throws Exception {
    int result = doit("FIXME");
    System.out.println("Result: " + result);
  }

  public static int doit(String input) throws Exception {

    List<String> lines = getLines(input);
    Board board = new Board(lines);
    board.print(0);

    int round = 1;
    for (; board.inProgress(); round++) {
      board.turn(round);
      board.print(round);
    }

    return board.score(round - 1);
  }

  static class Board {

    int height, width;
    Entity[][] board;
    List<Elf> elves = new ArrayList<>();
    List<Gnome> gnomes = new ArrayList<>();

    public Board(List<String> input) {
      height = input.size();
      width = input.get(0).length();
      board = new Entity[width][height];
      for (int y = 0; y < input.size(); y++) {
        String line = input.get(y);
        for (int x = 0; x < line.length(); x++) {
          switch (line.charAt(x)) {
            case '#':
              board[x][y] = new Wall(x, y);
              break;
            case '.':
              board[x][y] = null;
              break;
            case 'G':
              Gnome g = new Gnome(x, y);
              board[x][y] = g;
              gnomes.add(g);
              break;
            case 'E':
              Elf e = new Elf(x, y);
              board[x][y] = e;
              elves.add(e);
              break;
            default:
              throw new UnsupportedOperationException("ohps");
          }
        }
      }
    }

    void print(int round) {
      out.println("After round " + round);
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          out.print(board[x][y] != null ? board[x][y].name() : ".");
        }
        for (int x = 0; x < width; x++) {
          if (board[x][y] != null && board[x][y].isCharacter()) {
            out.print(" " + board[x][y].name() + "(" + board[x][y].hp() + ")");
          }
        }

        out.println();
      }
    }

    public boolean inProgress() {
      return elves.size() > 0 && gnomes.size() > 0;
    }

    public int score(int round) {
      int sum = 0;
      if (elves.size() > 0) {
        for (Elf e : elves) {
          sum += e.hp;
        }
      }
      if (gnomes.size() > 0) {
        for (Gnome g : gnomes) {
          sum += g.hp;
        }
      }
      System.out.println("Score " + sum + "*" + round + "=" + sum * round);
      return sum * round;
    }

    public void turn(int round) {
      for (int y = 0; y < height && inProgress(); y++) {
        for (int x = 0; x < width && inProgress(); x++) {
          if (board[x][y] != null && board[x][y].isCharacter()) {
            Character c = (Character) board[x][y];
            if (c.turn < round) {
              c.turn++;
              Pos p = c.move(this, x, y);
              c.strike(this, p.x, p.y);
            }
          }
        }
      }
    }
  }

  abstract static class Entity {

    int x, y;

    Entity(int x, int y) {
      this.x = x;
      this.y = y;
    }

    abstract String name();

    abstract boolean isCharacter();

    abstract int hp();
  }

  abstract static class Character extends Entity {

    int hp = 200;
    int turn = 0;

    Character(int x, int y) {
      super(x, y);
    }

    boolean isCharacter() {
      return true;
    }

    int hp() {
      return hp;
    }

    abstract boolean hits(Entity e);

    Pos move(Board board, int x, int y) {

      // Check if already there
      if (hits(board.board[x][y - 1])
          || hits(board.board[x - 1][y])
          || hits(board.board[x + 1][y])
          || hits(board.board[x][y + 1])) {
        return new Pos(x, y);
      }

      // find closest enemy
      Queue<Step> bfs = new LinkedList<>();
      addInitStep(x, y - 1, bfs, board);
      addInitStep(x - 1, y, bfs, board);
      addInitStep(x + 1, y, bfs, board);
      addInitStep(x, y + 1, bfs, board);

      boolean[][] visited = new boolean[board.width][board.height];
      visited[x][y] = true;

      Step step = null;
      while (!bfs.isEmpty()) {

        step = bfs.poll();

        if (hits(board.board[step.current.x][step.current.y - 1])
            || hits(board.board[step.current.x - 1][step.current.y])
            || hits(board.board[step.current.x + 1][step.current.y])
            || hits(board.board[step.current.x][step.current.y + 1])) {
          break;
        }

        addNextStep(step.current.x, step.current.y - 1, step.firstStep, bfs, board, visited);
        addNextStep(step.current.x - 1, step.current.y, step.firstStep, bfs, board, visited);
        addNextStep(step.current.x + 1, step.current.y, step.firstStep, bfs, board, visited);
        addNextStep(step.current.x, step.current.y + 1, step.firstStep, bfs, board, visited);
      }

      if (step != null) {
        // walk towards it
        this.x = step.firstStep.x;
        this.y = step.firstStep.y;
        board.board[step.firstStep.x][step.firstStep.y] = this;
        board.board[x][y] = null;
        return step.firstStep;
      }
      return new Pos(x, y);
    }

    private void addInitStep(int x, int y, Queue<Step> bfs, Board board) {
      if (board.board[x][y] == null) {
        bfs.add(new Step(new Pos(x, y), new Pos(x, y)));
      }
    }

    private void addNextStep(
        int x, int y, Pos firstStep, Queue<Step> bfs, Board board, boolean[][] visited) {
      if (board.board[x][y] == null && visited[x][y] == false) {
        bfs.add(new Step(firstStep, new Pos(x, y)));
        visited[x][y] = true;
      }

      // TODO - need to account for when friend die to be in the right spot asap.

      /*
            if (board.board[x][y] != null  // try go through characters (since they may move)
                && visited[x][y] == false
                && board.board[x][y].isCharacter()
                && (x != firstStep.x || y != firstStep.y)) {
              bfs.add(new Step(firstStep, new Pos(x, y)));
              visited[x][y] = true;
            }
      */
    }

    void strike(Board board, int x, int y) {
      Character target = null;
      if (y - 1 >= 0) {
        if (this.hits(board.board[x][y - 1])) {
          target = ((Character) board.board[x][y - 1]);
        }
      }
      if (x - 1 >= 0) {
        if (this.hits(board.board[x - 1][y])) {
          Character candidate = (Character) board.board[x - 1][y];
          if (target == null || candidate.hp < target.hp) {
            target = candidate;
          }
        }
      }
      if (x + 1 < board.width) {
        if (this.hits(board.board[x + 1][y])) {
          Character candidate = ((Character) board.board[x + 1][y]);
          if (target == null || candidate.hp < target.hp) {
            target = candidate;
          }
        }
      }
      if (y + 1 < board.width) {
        if (this.hits(board.board[x][y + 1])) {
          Character candidate = ((Character) board.board[x][y + 1]);
          if (target == null || candidate.hp < target.hp) {
            target = candidate;
          }
        }
      }

      if (target != null) {
        target.hit(board, 3);
      }
    }

    void hit(Board board, int damage) {
      this.hp -= 3;
      if (hp <= 0) {
        if (this instanceof Elf) {
          board.elves.remove(this);
        }
        if (this instanceof Gnome) {
          board.gnomes.remove(this);
        }
        board.board[x][y] = null;
      }
    }
  }

  static class Elf extends Character {

    Elf(int x, int y) {
      super(x, y);
    }

    String name() {
      return "E";
    }

    boolean hits(Entity e) {
      return e != null && e instanceof Gnome;
    }
  }

  static class Gnome extends Character {

    Gnome(int x, int y) {
      super(x, y);
    }

    String name() {
      return "G";
    }

    boolean hits(Entity e) {
      return e != null && e instanceof Elf;
    }
  }

  static class Wall extends Entity {

    Wall(int x, int y) {
      super(x, y);
    }

    String name() {
      return "#";
    }

    int hp() {
      throw new UnsupportedOperationException("no hp on wall");
    }

    boolean isCharacter() {
      return false;
    }
  }

  static class Pos {

    int x, y;

    public Pos(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  static class Step {

    Pos current;
    Pos firstStep;

    public Step(Pos first, Pos current) {
      this.firstStep = first;
      this.current = current;
    }
  }
}
