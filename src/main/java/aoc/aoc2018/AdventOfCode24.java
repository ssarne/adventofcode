package aoc.aoc2018;

import static aoc.utils.Utils.after;
import static aoc.utils.Utils.asInt;
import static aoc.utils.Utils.check;
import static aoc.utils.Utils.getLines;
import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AdventOfCode24 {

  public static void main(String[] args) throws Exception {
    test();
    test2();
    task1();
    task2();
  }

  public static void test() throws Exception {
    List<Group> defense = new ArrayList<>();
    List<Group> infection = new ArrayList<>();
    setupGroups(defense, infection, 0, getLines("input24_test.txt"));
    String result = doit(defense, infection, true);
    out.println("Result Test 1: " + result);
  }

  public static void test2() throws Exception {
    List<Group> defense = new ArrayList<>();
    List<Group> infection = new ArrayList<>();
    setupGroups(defense, infection, 1570, getLines("input24_test.txt"));
    String result = doit(defense, infection, false);
    out.println("Result Test 2: " + result);
  }

  public static void task1() throws Exception {
    List<Group> defense = new ArrayList<>();
    List<Group> infection = new ArrayList<>();
    setupGroups(defense, infection, 0, getLines("input24.txt"));
    String result = doit(defense, infection, false);
    out.println("Result Task 1: " + result);
  }

  public static void task2() throws Exception {
    int boost;
    int units = 0;
    for (boost = 1; boost < 10000; boost++) {
      List<Group> defense = new ArrayList<>();
      List<Group> infection = new ArrayList<>();
      setupGroups(defense, infection, boost, getLines("input24.txt"));
      String result = doit(defense, infection, false);
      out.println("Interim Result Task 2: " + result);
      if (infection.size() == 0) {
        units = defense.stream().mapToInt(g -> g.units).sum();
        break;
      }
    }
    out.println("Result Task 2: boost=" + boost + "  units=" + units);
  }

  private static void setupGroups(
      List<Group> defense, List<Group> infection, int boost, List<String> lines) {
    List<Group> groups = null;
    Team team = null;
    for (String line : lines) {
      if (line.contains("Immune System:")) {
        groups = defense;
        team = Team.defens;
        continue;
      }
      if (line.contains("Infection:")) {
        groups = infection;
        team = Team.infection;
        continue;
      }

      if (line.trim().length() == 0) {
        continue;
      }

      int id = groups.size() + 1;
      int units = asInt(line.substring(0, line.indexOf(" units")));
      int unitHps = asInt(line.substring(after(line, "each with "), line.indexOf(" hit points")));
      int damage =
          (team == Team.defens ? boost : 0)
              + asInt(
                  line.substring(
                      after(line, "attack that does "),
                      line.indexOf(" ", after(line, "attack that does "))));
      int initiative = asInt(line.substring(line.lastIndexOf(" ") + 1));
      String immune =
          line.contains("immune to")
              ? line.substring(
                  after(line, "immune to "),
                  line.indexOf(";") > line.indexOf("immune to ")
                      ? line.indexOf(";")
                      : line.indexOf(")"))
              : "";
      String weak =
          line.contains("weak to")
              ? line.substring(
                  after(line, "weak to "),
                  line.indexOf(";") > line.indexOf("weak to ")
                      ? line.indexOf(";")
                      : line.indexOf(")"))
              : "";
      String damageType =
          line.substring(
              line.indexOf(" ", after(line, "attack that does ")), line.indexOf(" damage at "));

      groups.add(new Group(id, team, units, unitHps, damage, initiative, immune, weak, damageType));
    }
  }

  public static String doit(List<Group> defense, List<Group> infection, boolean print)
      throws Exception {

    while (defense.size() > 0 && infection.size() > 0) {

      if (print) printTeams(defense, infection);
      Collections.sort(defense);
      Collections.sort(infection);

      var attacks = new HashMap<Group, Attack>(); // defender -> attack
      doTargetting(infection, defense, attacks, print);
      doTargetting(defense, infection, attacks, print);
      if (print) out.println();

      var attackList = new ArrayList<>(attacks.values());
      attackList.sort(Comparator.comparing(Attack::initiative).reversed());

      for (Attack a : attackList) {
        if (a.attacker.units > 0 && a.target.units > 0) {
          int kills = a.damage() / a.target.unitHP;
          a.target.units -= kills;
          if (print) a.printBattle(kills);
          if (kills == 0 && attackList.size() == 1) {
            return "stale mate too";
          }
          if (a.target.units <= 0) {
            (a.target.team == Team.infection ? infection : defense).remove(a.target);
            if (print) out.println("Group " + a.target.id + " is dead.");
          }
        }
      }

      if (attackList.size() == 0) {
        return "stale mate";
      }
    }

    int sum = 0;
    sum += defense.stream().mapToInt(g -> g.units).sum();
    sum += infection.stream().mapToInt(g -> g.units).sum();
    return "" + sum;
  }

  private static void doTargetting(
      List<Group> attackers, List<Group> defenders, HashMap<Group, Attack> attacks, boolean print) {
    for (Group attacker : attackers) {
      Attack attack = null;
      for (Group defender : defenders) {
        if (attacks.containsKey(defender)) continue;
        Attack pot = new Attack(attacker, defender);
        if (pot.damage() <= 0) continue;
        if (print)  pot.printTargetting();
        if (attack == null
            || pot.damage() > attack.damage()
            || pot.damage() == attack.damage()
                && attacker.initiative > attack.attacker.initiative) {
          attack = pot;
        }
      }
      if (attack != null) {
        attacks.put(attack.target, attack);
      }
    }
  }

  private static void printTeamsRaw(List<Group> defense, List<Group> infection) {
    infection.stream().forEach(g -> out.println(g));
    defense.stream().forEach(g -> out.println(g));
  }

  private static void printTeams(List<Group> defense, List<Group> infection) {

    out.println("--------------------------------------------------");
    out.println(Team.infection.pretty + ":");
    infection.stream().forEach(g -> out.printf("Group %d contains %d units.\n", g.id, g.units));
    out.println(Team.defens.pretty + ":");
    defense.stream().forEach(g -> out.printf("Group %d contains %d units.\n", g.id, g.units));
    out.println("");
  }

  private static long count(List<Group> groups, Team team) {
    return groups.stream().filter(g -> g.team == team).count();
  }

  enum Team {
    defens("Immune system"),
    infection("Infection");
    String pretty;

    Team(String pretty) {
      this.pretty = pretty;
    }
  }

  static class Attack {
    Group attacker;
    Group target;

    public Attack(Group attacker, Group target) {
      this.attacker = attacker;
      this.target = target;
    }

    int initiative() {
      return attacker.initiative;
    }

    int damage() {

      for (String s : attacker.damageType) {
        if (target.immunity.contains(s)) {
          return 0;
        }
      }

      for (String s : attacker.damageType) {
        if (target.weakness.contains(s)) {
          return 2 * attacker.effectivePower();
        }
      }

      return attacker.effectivePower();
    }

    Attack printTargetting() {
      out.printf(
          "%s group %d would deal defending group %d %d damage\n",
          attacker.team.pretty, attacker.id, target.id, damage());
      return this;
    }

    Attack printBattle(int units) {
      out.printf(
          "%s group %d attacks defending group %d, killing %d units\n",
          attacker.team.pretty, attacker.id, target.id, units);
      return this;
    }
  }

  static class Group implements Comparable<Group> {
    int id;
    Team team;
    int units;
    int unitHP;
    int damage;
    int initiative;
    List<String> immunity;
    List<String> weakness;
    List<String> damageType;

    public Group(
        int id,
        Team team,
        int units,
        int unitHP,
        int damage,
        int initiative,
        String immunity,
        String weakness,
        String damageType) {
      this.id = id;
      this.team = team;
      this.units = units;
      this.unitHP = unitHP;
      this.damage = damage;
      this.initiative = initiative;
      this.immunity =
          immunity.length() == 0 ? new ArrayList<>() : Arrays.asList(immunity.trim().split(", "));
      this.weakness =
          weakness.length() == 0 ? new ArrayList<>() : Arrays.asList(weakness.trim().split(", "));
      this.damageType =
          damageType.length() == 0
              ? new ArrayList<>()
              : Arrays.asList(damageType.trim().split(", "));
    }

    int effectivePower() {
      return units * damage;
    }

    int initiative() {
      return initiative;
    }

    int id() {
      return id;
    }

    @Override
    public int compareTo(Group g) {
      int ret = g.effectivePower() - this.effectivePower();
      if (ret == 0) {
        ret = g.initiative() - this.initiative();
      }
      return ret;
    }

    @Override
    public String toString() {
      return "Group{"
          + "id="
          + id
          + ", team="
          + team
          + ", units="
          + units
          + ", unitHP="
          + unitHP
          + ", damage="
          + damage
          + ", initiative="
          + initiative
          + ", effectivePower="
          + effectivePower()
          + ", immunity="
          + Arrays.toString(immunity.toArray())
          + ", weakness="
          + Arrays.toString(weakness.toArray())
          + ", damageType="
          + Arrays.toString(damageType.toArray())
          + '}';
    }
  }
}
