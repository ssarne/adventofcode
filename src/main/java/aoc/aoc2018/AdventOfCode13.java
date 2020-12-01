package aoc.aoc2018;

import static aoc.Utils.getLines;

import java.io.IOException;
import java.util.List;

public class AdventOfCode13 {

  public static Board init(String filename) {
    return new Board(getLines(filename));
  }

  public static void main(String[] args) throws IOException {
    test();
    // task();
  }

  static void test() throws IOException {
    Board board = init("input13_test.txt");
    board.print();
    doit(board);
  }

  static void task() throws IOException {

    Board board = init("input13.txt");
    board.print();
    doit(board);
  }

  static void doit(Board board) throws IOException {

    String lastCart = "";
    boolean first = true;

    for (int age = 1; true; age++) {
      for (int y = 0; y < board.y; y++) {
        for (int x = 0; x < board.x; x++) {
          if (board.carts[x][y] != null) {

            Cart cart = board.carts[x][y];
            if (cart.age >= age) {
              continue;
            }

            int x2 = nextX(cart.direction, x);
            int y2 = nextY(cart.direction, y);
            if (board.carts[x2][y2] != null) {
              if (first) {
                System.out.println("First collision: " + x2 + "," + y2);
                first = false;
              }
              board.carts[x][y] = null;
              board.carts[x2][y2] = null;
              continue;
            }
            board.carts[x][y] = null;
            board.carts[x2][y2] = cart;
            cart.age++;
            cart.direction = nextDirection(cart.direction, board.rails[x2][y2], cart);
          }
        }
      }
      // board.print();
      if (board.check()) {
        return;
      }
    }
  }

  private static Cart.Direction nextDirection(Cart.Direction direction, char c, Cart cart) {

    if (direction == Cart.Direction.up && c == '|') {
      return Cart.Direction.up;
    }
    if (direction == Cart.Direction.up && c == '-') {
      throw new RuntimeException("boom");
    }
    if (direction == Cart.Direction.up && c == '/') {
      return Cart.Direction.right;
    }
    if (direction == Cart.Direction.up && c == '\\') {
      return Cart.Direction.left;
    }

    if (direction == Cart.Direction.down && c == '|') {
      return Cart.Direction.down;
    }
    if (direction == Cart.Direction.down && c == '-') {
      throw new RuntimeException("boom");
    }
    if (direction == Cart.Direction.down && c == '/') {
      return Cart.Direction.left;
    }
    if (direction == Cart.Direction.down && c == '\\') {
      return Cart.Direction.right;
    }

    if (direction == Cart.Direction.left && c == '|') {
      throw new RuntimeException("boom");
    }
    if (direction == Cart.Direction.left && c == '-') {
      return Cart.Direction.left;
    }
    if (direction == Cart.Direction.left && c == '/') {
      return Cart.Direction.down;
    }
    if (direction == Cart.Direction.left && c == '\\') {
      return Cart.Direction.up;
    }

    if (direction == Cart.Direction.right && c == '|') {
      throw new RuntimeException("boom");
    }
    if (direction == Cart.Direction.right && c == '-') {
      return Cart.Direction.right;
    }
    if (direction == Cart.Direction.right && c == '/') {
      return Cart.Direction.up;
    }
    if (direction == Cart.Direction.right && c == '\\') {
      return Cart.Direction.down;
    }

    if (c != '+') {
      throw new RuntimeException("boom direction=" + direction + " c=" + c);
    }

    Cart.Turn turn = cart.crossing();
    if (turn == Cart.Turn.straight) {
      return cart.direction;
    }

    if (direction == Cart.Direction.right && turn == Cart.Turn.left) {
      return Cart.Direction.up;
    }
    if (direction == Cart.Direction.right && turn == Cart.Turn.right) {
      return Cart.Direction.down;
    }

    if (direction == Cart.Direction.left && turn == Cart.Turn.left) {
      return Cart.Direction.down;
    }
    if (direction == Cart.Direction.left && turn == Cart.Turn.right) {
      return Cart.Direction.up;
    }

    if (direction == Cart.Direction.up && turn == Cart.Turn.left) {
      return Cart.Direction.left;
    }
    if (direction == Cart.Direction.up && turn == Cart.Turn.right) {
      return Cart.Direction.right;
    }

    if (direction == Cart.Direction.down && turn == Cart.Turn.left) {
      return Cart.Direction.right;
    }
    if (direction == Cart.Direction.down && turn == Cart.Turn.right) {
      return Cart.Direction.left;
    }

    throw new RuntimeException("boom");
  }

  private static int nextX(Cart.Direction direction, int x) {
    switch (direction) {
      case up:
        return x;
      case down:
        return x;
      case right:
        return x + 1;
      case left:
        return x - 1;
    }
    throw new RuntimeException("ohps");
  }

  private static int nextY(Cart.Direction direction, int y) {
    switch (direction) {
      case up:
        return y - 1;
      case down:
        return y + 1;
      case right:
        return y;
      case left:
        return y;
    }
    throw new RuntimeException("ohps");
  }

  static class Board {

    int x, y;
    char[][] rails;
    Cart[][] carts;

    public Board(List<String> board) {

      int xmax = 0;
      for (String line : board) {
        xmax = Math.max(line.length(), xmax);
      }

      this.x = xmax;
      this.y = board.size();
      this.rails = new char[x][y];
      this.carts = new Cart[x][y];

      for (int y = 0; y < board.size(); y++) {
        String line = board.get(y);
        for (int x = 0; x < xmax; x++) {
          this.rails[x][y] = (x < line.length() ? line.charAt(x) : ' ');
          switch (rails[x][y]) {
            case 'v':
              {
                rails[x][y] = '|';
                carts[x][y] = new Cart(x, y, Cart.Direction.down);
                break;
              }
            case '>':
              {
                rails[x][y] = '-';
                carts[x][y] = new Cart(x, y, Cart.Direction.right);
                break;
              }
            case '^':
              {
                rails[x][y] = '|';
                carts[x][y] = new Cart(x, y, Cart.Direction.up);
                break;
              }
            case '<':
              {
                rails[x][y] = '-';
                carts[x][y] = new Cart(x, y, Cart.Direction.left);
                break;
              }
          }
        }
      }
    }

    void print() {
      for (int y = 0; y < this.y; y++) {
        for (int x = 0; x < this.x; x++) {
          if (carts[x][y] != null) {
            System.out.print(carts[x][y].directionAsCar());
          } else {
            System.out.print(rails[x][y]);
          }
        }
        System.out.println();
      }
    }

    boolean check() {
      int cs = 0;
      String lastCart = "";
      for (int y = 0; y < this.y; y++) {
        for (int x = 0; x < this.x; x++) {
          if (carts[x][y] != null) {
            cs++;
            lastCart = x + "," + y;
          }
        }
      }
      // System.out.println("Carts: " + cs + "   last=" + lastCart);
      if (cs <= 1) {
        System.out.println("Last carts: " + lastCart);
        return true;
      }
      return false;
    }
  }

  static class Cart {

    enum Direction {
      up,
      right,
      down,
      left
    }

    enum Turn {
      left,
      straight,
      right
    }

    int x, y;
    Direction direction;
    Turn nextTurn = Turn.left;
    int age = 0;

    public Cart(int x, int y, Direction direction) {
      this.x = x;
      this.y = y;
      this.direction = direction;
    }

    public Turn crossing() {
      Turn turn = nextTurn;
      switch (turn) {
        case left:
          nextTurn = Cart.Turn.straight;
          break;
        case straight:
          nextTurn = Cart.Turn.right;
          break;
        case right:
          nextTurn = Cart.Turn.left;
          break;
      }
      return turn;
    }

    char directionAsCar() {
      switch (direction) {
        case up:
          return '^';
        case down:
          return 'v';
        case left:
          return '<';
        case right:
          return '>';
      }
      return '0';
    }
  }
}
