package aoc.aoc2020;

import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static aoc.utils.Utils.*;

public class Dec19 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve("aoc2020/dec19_test1.txt"), 1);
        check(solve("aoc2020/dec19_test2.txt"), 2);
        check(solve("aoc2020/dec19_test3.txt"), 3);
        check(solve("aoc2020/dec19_test4.txt"), 12);
    }

    public static void task1() {
        var result = solve("aoc2020/dec19.txt");
        check(result, 285);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve("aoc2020/dec19_2.txt");
        check(result, 412);
        System.out.println("Result: " + result);
    }

    public static long solve(String input) {
        var lines = getLines(input);
        HashMap<String, Rule> rules = new HashMap<>();
        List<String> messages = new ArrayList<>();

        for (String line : lines) {
            if (line.length() == 0 || line.startsWith("#")) {
                continue;
            }

            if (line.contains(":")) {
                Rule rule = Rule.parse(line);
                rules.put(rule.id, rule);
                continue;
            }

            messages.add(line);
        }

        Rule zero = rules.get("0");
        int matches = 0;
        for (String msg : messages) {
            matches += zero.match(msg, rules) ? 1 : 0;
        }
        return matches;
    }

    private static class Rule {
        String id;
        String text = null;
        List<List<String>> alternatives = new ArrayList<>();

        public Rule(String id) {
            this.id = id;
        }

        public static Rule parse(String line) {
            String[] id_rule = line.split(": ");
            String[] alternatives = id_rule[1].split(" \\| ");

            Rule rule = new Rule(id_rule[0]);
            for (String alt : alternatives) {
                if (alt.contains("\"")) {
                    ensure(alt.charAt(0) == '\"');
                    ensure(alt.charAt(alt.length() - 1) == '\"');
                    rule.text = alt.substring(1, alt.length() - 1);
                    continue;
                }

                rule.alternatives.add(Arrays.asList(alt.split(" ")));
            }
            return rule;
        }

        public boolean match(String line, HashMap<String, Rule> rules) {
            for (int r : submatch(line, rules, 0)) {
                if (r == line.length()) {
                    return true;
                }
            }
            return false;
        }

        public List<Integer> submatch(String line, HashMap<String, Rule> rules, int indent) {

            if (text != null) {
                var res = new ArrayList<Integer>();
                if (line.startsWith(text)) {
                    res.add(text.length());
                }
                return res;
            }

            var res = new ArrayList<Integer>();
            for (var alt : alternatives) {
                var current = new ArrayList<Integer>();
                current.add(0);
                for (String ruleId : alt) {
                    var next = new ArrayList<Integer>();
                    var rule = rules.get(ruleId);
                    for (int n : current) {
                        rule.submatch(line.substring(n), rules, indent + 1)
                                .stream()
                                .map(i -> next.add(n + i));
                    }
                    current = next;
                }
                res.addAll(current);
            }
            return res;
        }

        @Override
        public String toString() {
            return "Rule " + id + ": "
                    + (text != null ? text : "") + ""
                    + (alternatives != null ? alternatives : "");
        }
    }
}
