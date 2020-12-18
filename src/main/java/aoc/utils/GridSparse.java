package aoc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static aoc.utils.Utils.check;

public class GridSparse {

    final static String dims = "xyzwvuts";
    final static int X = 0;
    final static int Y = 1;

    HashMap<String, Character> grid = new HashMap<>();
    int dimensions;
    int[] min;
    int[] max;

    public GridSparse(int dimensions) {
        this.dimensions = dimensions;
        min = new int[dimensions];
        max = new int[dimensions];
    }

    private String pos(int... xyz) {
        StringBuilder sb = new StringBuilder();
        sb.append(xyz[0]);
        for (int i = 1; i < dimensions; i++) {
            sb.append(',').append(xyz[i]);
        }
        return sb.toString();
    }

    public int min(int dimension) {
        return min[dimension - 1];
    }

    public int max(int dimension) {
        return max[dimension - 1];
    }

    public Character get(int... xyz) {
        return grid.get(pos(xyz));
    }

    public void put(char c, int... xyz) {
        for (int i = 0; i < dimensions; i++) {
            min[i] = Math.min(min[i], xyz[i]);
            max[i] = Math.max(max[i], xyz[i]);
        }
        grid.put(pos(xyz), c);
    }

    public boolean is(char c, int... xyz) {
        Character v = grid.get(pos(xyz));
        return v != null && v.charValue() == c;
    }

    public int neighbours(char v, int... xyz) {
        int[] neighbour = new int[dimensions];
        return neighbours(v, 0, xyz, neighbour);
    }

    private int neighbours(char v, int i, int[] center, int[] neighbour) {

        if (i == dimensions) {
            if (!Arrays.equals(center, neighbour)) {
                Character c = grid.get(pos(neighbour));
                if (c != null && c.charValue() == v) {
                    return 1;
                }
                return 0;
            }
            return 0;
        }

        int n = 0;
        neighbour[i] = center[i] - 1;
        n += neighbours(v, i + 1, center, neighbour);
        neighbour[i] = center[i];
        n += neighbours(v, i + 1, center, neighbour);
        neighbour[i] = center[i] + 1;
        n += neighbours(v, i + 1, center, neighbour);
        return n;
    }

    public int count(char c) {
        int n = 0;
        for (char v : grid.values()) {
            if (c == v) {
                n++;
            }
        }
        return n;
    }

    public static GridSparse parse2D(int dimensions, List<String> lines) {
        var grid = new GridSparse(dimensions);
        int [] point = new int[dimensions];
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                point[X] = x;
                point[Y] = y;
                grid.put(line.charAt(x), point);
            }
        }
        return grid;
    }

    public void print(int id) {
        System.out.println("========== " + id);
        int[] xyz = new int[dimensions];
        printr(0, xyz);
    }

    private void printr(int i, int[] xyz) {

        if (dimensions - i == 2) { // 2d matrix
            for (int y = min[Y]; y <= max[Y]; y++) {
                xyz[Y] = y;
                for (int x = min[X]; x <= max[X]; x++) {
                    xyz[X] = x;
                    Character v = grid.get(pos(xyz));
                    if (v != null) {
                        System.out.print(v);
                    } else {
                        System.out.print(' ');
                    }
                }
                System.out.println();
            }
            return;
        }

        for (int z = min[dimensions-i-1]; z <= max[dimensions-i-1]; z++) {
            System.out.println((i < dims.length() ? dims.charAt(dimensions-i-1) : "Dim" + i) + "=" + z);
            xyz[dimensions-i-1] = z;
            printr(i+1, xyz);
        }
    }

    public static void main(String [] args) {
        List<String> input = new ArrayList<>();
        input.add("#####");
        input.add("#....");
        input.add("#");
        GridSparse grid = parse2D(3, input);
        grid.put('*', 2, 2, 1);
        grid.put('*', 2, 1, 1);
        grid.put('*', 2, 0, 1);
        // grid.print(1);
        check(grid.count('*'), 3);
        check(grid.count('#'), 7);
        check(grid.get(1, 1, 0), '.');
    }
}
