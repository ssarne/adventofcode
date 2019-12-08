package aoc.aoc2019;

import static aoc.Utils.check;
import static aoc.Utils.getLines;
import static java.lang.System.out;

import java.util.HashMap;
import java.util.List;

public class Dec06 {

  public static void main(String[] args) throws Exception {
    test();
    task1();
    task2();
  }

  public static void test() throws Exception {
    HashMap<String, SpaceObject> space = buildSpace("aoc2019/dec06_test.txt");
    int count = count(space.get("COM"), 0);
    check(count, 42);
  }

  public static void task1() throws Exception {
    HashMap<String, SpaceObject> space = buildSpace("aoc2019/dec06.txt");
    int count = count(space.get("COM"), 0);
    check(count, 344238);
    System.out.println("Result: " + count);
  }

  public static void task2() throws Exception {
    HashMap<String, SpaceObject> space = buildSpace("aoc2019/dec06.txt");
    // print(space);

    int distance = distance("YOU", "SAN", space.get("COM"));
    check(distance, 436);
    System.out.println("Result: " + distance);
  }

  public static HashMap<String, SpaceObject> buildSpace(String input) throws Exception {

    List<String> lines = getLines(input);
    HashMap<String, SpaceObject> space = new HashMap<>();

    for (String line : lines) {
      String[] obs = line.split("\\)");
      SpaceObject center, orbiter;
      if (space.containsKey(obs[0])) {
        center = space.get(obs[0]);
      } else {
        center = new SpaceObject(null, obs[0]);
        space.put(center.name, center);
      }

      if (space.containsKey(obs[1])) {
        orbiter = space.get(obs[1]);
      } else {
        orbiter = new SpaceObject(obs[0], obs[1]);
        space.put(orbiter.name, orbiter);
      }

      if (!center.orbiters.containsKey(orbiter.name)) {
        center.orbiters.put(orbiter.name, orbiter);
      }
    }
    return space;
  }

  private static int find(String s1, SpaceObject com) {
    if (com.orbiters.containsKey(s1)) {
      return 0;
    }
    for (SpaceObject so : com.orbiters.values()) {
      int c = find(s1, so);
      if (c >= 0) {
        return c + 1;
      }
    }
    return -1;
  }

  private static int distance(String s1, String s2, SpaceObject com) {

    int c1 = find(s1, com);
    int c2 = find(s2, com);

    if (c1 < 0 || c2 < 0) {
      return -1;
    }

    if (c1 == 0 || c2 == 0) {
      return c1 + c2;
    }

    for (SpaceObject so : com.orbiters.values()) {
      int cc1 = find(s1, so);
      int cc2 = find(s2, so);
      if (cc1 >= 0 && cc2 >= 0) {
        return distance(s1, s2, so);
      }
    }
    return c1 + c2;
  }

  private static int count(SpaceObject c, int d) {
    int count = d;
    for (SpaceObject o : c.orbiters.values()) {
      count += count(o, d + 1);
    }
    return count;
  }

  private static class SpaceObject {

    String name;
    String center;
    int distance;
    HashMap<String, SpaceObject> orbiters = new HashMap<>();

    public SpaceObject(String center, String name) {
      this.name = name;
      this.center = center;
      this.distance = 0;
    }
  }

  public static void print(HashMap<String, SpaceObject> space) {
    for (SpaceObject sp : space.values()) {
      System.out.println(sp.center + "->" + sp.name);
    }
  }
}
