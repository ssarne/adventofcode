package aoc.aoc2020;

import aoc.utils.GridSparse;
import java.util.List;

import static aoc.utils.Utils.*;

public class Dec17 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1(getTestLines()), 112);
        check(solve2(getTestLines()), 848);
    }

    public static void task1() {
        var result = solve1(getLines());
        System.out.println("Result: " + result);
        check(result, readAnswerAsInt(1));
    }

    public static void task2() {
        var result = solve2(getLines());
        System.out.println("Result: " + result);
        check(result, readAnswerAsInt(2));
    }

    public static int solve1(List<String> input) {

        var grid = GridSparse.parse2D(3, input);

        for (int i = 0; i < 6; i++) {
            // grid.print(i);
            var next = new GridSparse(3);
            for (int z = grid.min(3) - 1; z <= grid.max(3) + 1; z++) {
                for (int y = grid.min(2) - 1; y <= grid.max(2) + 1; y++) {
                    for (int x = grid.min(1) - 1; x <= grid.max(1) + 1; x++) {
                        boolean b = grid.is('#', x, y, z);
                        boolean d = grid.is('.', x, y, z);
                        int n = grid.neighbours('#', x, y, z);
                        if (b && n == 2 || b && n == 3 || n == 3) {
                            next.put('#', x, y, z);
                        } else if (b || d) {
                            next.put('.', x, y, z);
                        }
                    }
                }
            }
            grid = next;
        }

        return grid.count('#');
    }

    public static int solve2(List<String> input) {

        var grid = GridSparse.parse2D(4, input);

        for (int i = 0; i < 6; i++) {
            // grid.print(i);
            GridSparse next = new GridSparse(4);
            for (int w = grid.min(4) - 1; w <= grid.max(4) + 1; w++) {
                for (int z = grid.min(3) - 1; z <= grid.max(3) + 1; z++) {
                    for (int y = grid.min(2) - 1; y <= grid.max(2) + 1; y++) {
                        for (int x = grid.min(1) - 1; x <= grid.max(1) + 1; x++) {
                            boolean b = grid.is('#', x, y, z, w);
                            boolean d = grid.is('.', x, y, z, w);
                            int n = grid.neighbours('#', x, y, z, w);
                            if (b && n == 2 || b && n == 3 || n == 3) {
                                next.put('#', x, y, z, w);
                            } else if (b || d) {
                                next.put('.', x, y, z, w);
                            }
                        }
                    }
                }
            }
            grid = next;
        }

        return grid.count('#');
    }
}
