package aoc.aoc2019;

import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.readAnswerAsInt;
import static aoc.utils.Utils.readAnswerAsLong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dec13 {

  public static void main(String[] args) throws Exception {
    task1();
    task2();
  }

  public static void task1() throws Exception {
    IntCode.Program p = IntCode.Program.create(getLines().get(0));
    IntCode.execute(p);

    HashMap<String, Long> board = readBoard(p);
    int blocks = countBlocks(board);
    // print(board);

    System.out.println("Result: " + blocks);
    check(blocks, readAnswerAsInt(1));
  }

  public static void task2() throws Exception {

    IntCode.Program p = IntCode.Program.create(getLines().get(0));
    // in front of halt instruction, there is a false-check
    // set value so first add store 0 at this addr
    p.mem.store(p.mem.read(2L), -1L);

    IntCode.execute(p);
    HashMap<String, Long> board = readBoard(p);
    List<Pos> ball = new ArrayList<>();
    ball.add(getBall(board));

    for (int n = 1; p.status != IntCode.Program.Status.HALT_OK; n++) {
      Pos pad = getPad(board);
      Pos next = getNextBallPos(ball, pad);
      long i = joystick(board, pad, next);
      // print(board, i, next);
      p.input.add(i);
      IntCode.execute(p);
      updateBoard(p, board);
      ball.add(getBall(board));
    }

    // print(board, 0, new Pos(0,0));
    var result = getScore(board);
    System.out.println("Result: " + result);
    check(result, readAnswerAsLong(2));
  }

  private static long joystick(HashMap<String, Long> board, Pos pad, Pos next) {
    if (pad.x > next.x) {
      return -1L;
    } else if (pad.x < next.x) {
      return 1L;
    } else {
      return 0L;
    }
  }

  private static Pos getNextBallPos(List<Pos> ball, Pos pad) {
    if (ball.size() > 1) {
      Pos current = ball.get(ball.size() - 1);
      Pos previous = ball.get(ball.size() - 2);
      int dx = current.x - previous.x;
      int dy = current.y - previous.y;
      if (current.x == pad.x && current.y + 1 == pad.y) {
        return new Pos(current.x, current.y);
      }
      return new Pos(current.x + dx, current.y + dy);
    } else {
      return ball.get(ball.size() - 1);
    }
  }

  private static HashMap<String, Long> readBoard(IntCode.Program p) {
    HashMap<String, Long> board = new HashMap<>();
    return updateBoard(p, board);
  }

  private static HashMap<String, Long> updateBoard(IntCode.Program p, HashMap<String, Long> board) {
    while (!p.output.isEmpty()) {
      long x = p.output.poll();
      long y = p.output.poll();
      long t = p.output.poll();
      board.put(pos(x, y), t);
    }
    return board;
  }

  private static int countBlocks(HashMap<String, Long> board) {
    int blocks = 0;
    for (Long t : board.values()) {
      blocks += (t == 2L ? 1 : 0);
    }
    return blocks;
  }


  public static void print(HashMap<String, Long> board, long joystick, Pos prediction) {
    int maxx = 0, maxy = 0;
    for (String panel : board.keySet()) {
      maxx = Math.max(maxx, Integer.parseInt(panel.split(",")[0]));
      maxy = Math.max(maxy, Integer.parseInt(panel.split(",")[1]));
    }

    Pos ball = getBall(board);
    Pos pad = getPad(board);
    System.out.println("- score=" + getScore(board) + "  ball=" + ball.x + "," + ball.y + "  next=" + prediction.x + "," + prediction.y + "  pad=" + pad.x + "," + pad.y + "  i=" + joystick + "  ---");
    for (int y = 0; y <= maxy; y++) {
      for (int x = 0; x <= maxx; x++) {
        System.out.print(token(board.containsKey(pos(x, y)) ? board.get(pos(x, y)) : -1L));
      }
      System.out.println();
    }
    System.out.println("-    -    -    -    -    -    -    -     -");

  }

  private static Long getScore(HashMap<String,Long> board) {
    return board.get("-1,0");
  }

  private static Pos getBall(HashMap<String, Long> board) {
    return getFromBoard(board, 4L);
  }

  private static Pos getPad(HashMap<String, Long> board) {
    return getFromBoard(board, 3L);
  }

  private static Pos getFromBoard(HashMap<String, Long> board, long token) {
    for (String pos : board.keySet()) {
      if (board.get(pos) == token) {
        return new Pos(Integer.parseInt(pos.split(",")[0]), Integer.parseInt(pos.split(",")[1]));
      }
    }
    throw new RuntimeException("CMH");
  }


  private static char token(long t) {
    switch ((int) t) {
      case 0:
        return ' '; // is an empty tile. No game object appears in this tile.
      case 1:
        return '#'; // is a wall tile. Walls are indestructible barriers.
      case 2:
        return '*'; //  is a block tile. Blocks can be broken by the ball.
      case 3:
        return '-'; // is a horizontal paddle tile. The paddle is indestructible.
      case 4:
        return 'o'; // is a ball tile. The ball moves diagonally and bounces off objects.
      case -1:
        return ' '; // internal signal
      default:
        throw new RuntimeException("CMH t=" + t);
    }
  }

  private static String pos(long x, long y) {
    return x + "," + y;
  }

  private static class Pos {

    int x, y;

    public Pos(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
}
