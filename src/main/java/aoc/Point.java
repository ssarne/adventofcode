package aoc;

public class Point {

    final int x, y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point p) {
        return new Point(x + p.x, y + p.y);
    }

    public Point scale(int n) {
        return new Point(x * n, y * n);
    }

    public Point dx(int dx) {
        return new Point(x + dx, y);
    }

    public Point dy(int dy) {
        return new Point(x, y + dy);
    }

    public Point rotate(char direction, int angle) {
        int a = (direction == 'L' ? angle : 360 - angle);
        switch (a) {
            case 0:
                return new Point(x, y);
            case 90:
                return new Point(-y, x);
            case 180:
                return new Point(-x, -y);
            case 270:
                return new Point(y, -x);
            default:
                throw new UnsupportedOperationException("Unsupported angle " + angle);
        }
    }
}
