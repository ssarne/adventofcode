package aoc.aoc2018;

import static aoc.utils.Utils.getLines;
import static aoc.utils.Utils.getTestLines;
import static java.lang.System.out;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AdventOfCode07 {

  public static void main(String[] args) throws Exception {
    out.println("Test:   " + doit(getTestLines(), 2));
    out.println("Result: " + doit(getLines(), 5));
  }

  public static String doit(List<String> lines, int workers) throws Exception {

    HashMap<String, Step> steps = new HashMap<>();
    for (String line : lines) {
      String[] tokens = line.split(" ");
      createStep(steps, tokens[1]);
      createStep(steps, tokens[7]);
    }
    for (String line : lines) {
      String[] tokens = line.split(" ");
      Step step1 = steps.get(tokens[1]);
      Step step2 = steps.get(tokens[7]);
      step1.deps.put(step2.id, step2);
      step2.reqs.put(step1.id, step1);
    }

    StringBuilder result = new StringBuilder();
    Step[] tasks = new Step[workers];
    Step[][] execution = new Step[(60 + 25) * steps.size()][workers]; // time, workers
    int time = 1;
    for (; time < execution.length; time++) {

      for (int i = 0; i < workers; i++) {
        if (tasks[i] != null && execution[time][i] == null) {
          Step completed = tasks[i]; // done with task
          for (Step dep : completed.deps.values()) {
            dep.reqs.remove(completed.id);
          }
          tasks[i] = null;
          // then free to grab task
        }
      }

      if (steps.size() == 0 && !isWorking(tasks)) {
        break;
      }

      for (int i = 0; i < workers; i++) {
        if (tasks[i] != null && execution[time][i] != null) {
          continue; // has task
        }

        // grab task
        Optional<Step> first =
            steps
                .values()
                .stream()
                .filter(s -> s.reqs.size() == 0)
                .min((s1, s2) -> s1.id.compareTo(s2.id));
        if (first.isPresent()) {
          Step step = first.get();
          result.append(step.id);
          steps.remove(step.id);
          tasks[i] = step;
          int duration = 60 + step.id.charAt(0) - 'A' + 1;
          for (int t = 0; t < duration; t++) {
            execution[time + t][i] = step;
          }
        }
      }
    }

    return result + " / " + (time - 1);
  }

  private static boolean isWorking(Step[] tasks) {
    for (int i = 0; i < tasks.length; i++) {
      if (tasks[i] != null) {
        return true;
      }
    }
    return false;
  }

  private static void createStep(HashMap<String, Step> steps, String token) {
    Step step = steps.get(token);
    if (step == null) {
      step = new Step(token);
      steps.put(token, step);
    }
  }

  public static void print() {
    out.println("");
  }

  static class Step {
    String id;
    HashMap<String, Step> reqs = new HashMap<>();
    HashMap<String, Step> deps = new HashMap<>();

    public Step(String id) {
      this.id = id;
    }
  }
}
