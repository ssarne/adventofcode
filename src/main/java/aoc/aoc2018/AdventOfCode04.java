package aoc.aoc2018;

import static aoc.utils.Utils.*;

import java.io.IOException;
import java.util.*;

public class AdventOfCode04 {

  public static void main(String[] args) throws IOException {
    doit(getTestLines());
    doit(getLines());
  }

  public static void doit(List<String> lines) throws IOException {

    lines.sort(String::compareTo);

    TreeMap<Integer, Guard> guards = new TreeMap<>();
    Guard guard = null;
    String date = "";
    int start = 0;
    int end = 0;
    int gid = 0;

    for (String line : lines) {
      line = line.replaceAll("\\[", "").replaceAll("\\]", "");
      // [1518-11-01 00:00] Guard #10 begins shift
      if (line.contains("Guard #")) {
        String[] tokens = line.split(" ");
        gid = Integer.parseInt(tokens[3].substring(1));
        guard = guards.get(gid);
        if (guard == null) {
          guard = new Guard(gid);
          guards.put(gid, guard);
        }
      }

      // [1518-11-01 00:05] falls asleep
      if (line.contains("falls asleep")) {
        String[] tokens = line.split(" ");
        start = Integer.parseInt(tokens[1].split(":")[1]);
      }

      // [1518-11-01 00:25] wakes up
      if (line.contains("wakes up")) {
        String[] tokens = line.split(" ");
        date = tokens[0];
        end = Integer.parseInt(tokens[1].split(":")[1]);
        guard.sleep(date, start, end);
      }
    }

    print(guards);

    Guard gmax = getMaxSleepyGuard(guards);
    int minute = gmax.getMostSleepyMinute();
    System.out.println(
        "Task 1 - Guard #" + gmax.id + ": minute=" + minute + " result=" + gmax.id * minute);

    Guard gm = getMaxSleepyGuardOnOneMinute(guards);
    System.out.println(
        "Task 2 - Guard #" + gm.id + ": minute=" + gm.minute + " result=" + gm.id * gm.minute);
  }

  private static Guard getMaxSleepyGuard(TreeMap<Integer, Guard> guards) {
    Guard guard = null;
    int max = 0;
    for (Guard g : guards.values()) {
      int s = g.getTotalAsleep();
      if (s > max) {
        max = s;
        guard = g;
      }
    }
    return guard;
  }

  private static Guard getMaxSleepyGuardOnOneMinute(TreeMap<Integer, Guard> guards) {
    Guard guard = null;
    int max = 0;
    int minute = 0;
    for (int i = 0; i < 60; i++) {
      for (Guard g : guards.values()) {
        int s = g.getAsleepForMin(i);
        if (s > max) {
          max = s;
          guard = g;
          minute = i;
        }
      }
    }
    guard.minute = minute;
    return guard;
  }

  public static void print(TreeMap<Integer, Guard> guards) {
    for (Guard guard : guards.values()) {
      System.out.println("Guard #: " + guard.id);
      for (String shift : guard.shifts.keySet()) {
        System.out.print(shift + " ");
        boolean[] asleep = guard.shifts.get(shift);
        for (int i = 0; i < asleep.length; i++) {
          System.out.print(asleep[i] ? "X" : ".");
        }
        System.out.println();
      }
    }
  }

  private static class Guard {

    int id;
    int minute;
    TreeMap<String, boolean[]> shifts = new TreeMap<>();

    public Guard(int id) {
      this.id = id;
      this.minute = -1;
    }

    public void sleep(String date, int start, int end) {

      boolean[] asleep = shifts.get(date);
      if (asleep == null) {
        asleep = new boolean[60];
        shifts.put(date, asleep);
      }

      for (int i = start; i < end; i++) {
        asleep[i] = true;
      }
    }

    public int getTotalAsleep() {
      int s = 0;
      for (boolean[] asleep : shifts.values()) {
        for (int i = 0; i < asleep.length; i++) {
          if (asleep[i]) {
            s++;
          }
        }
      }
      return s;
    }

    public int getAsleepForMin(int min) {
      int s = 0;
      for (boolean[] asleep : shifts.values()) {
        if (asleep[min]) {
          s++;
        }
      }
      return s;
    }

    public int getMostSleepyMinute() {
      int[] totSleep = new int[60];
      for (boolean[] asleep : shifts.values()) {
        for (int i = 0; i < asleep.length; i++) {
          if (asleep[i]) {
            totSleep[i]++;
          }
        }
      }

      int max = 0;
      for (int i = 0; i < totSleep.length; i++) {
        if (totSleep[i] > totSleep[max]) {
          max = i;
        }
      }
      return max;
    }
  }
}
