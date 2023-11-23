package aoc.aoc2020;

import aoc.utils.Pair;
import aoc.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static aoc.utils.Utils.*;

public class Dec19 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve(getTestLines(1)), 1);
        check(solve(getTestLines(2)), 2);
        check(solve(getTestLines(3)), 3);
        check(solve(getTestLines(4)), 12);
    }

    public static void task1() {
        var result = solve(getLines());
        System.out.println("Result: " + result);
        check(result, readAnswerAsInt(1));
    }

    public static void task2() {
        var parsed = parse(getLines());
        parsed.left.put("8", Rule.parse("8: 42 | 42 8"));
        parsed.left.put("11", Rule.parse("11: 42 31 | 42 11 31"));
        var result = matches(parsed.left, parsed.right);

        System.out.println("Result: " + result);
        check(result, readAnswerAsInt(2));
    }

    private static long solve(List<String> input) {
        var parsed = parse(input);
        var count = matches(parsed.left, parsed.right);
        return count;
    }

    private static Pair<HashMap<String, Rule>, List<String>> parse(List<String> input) {

        HashMap<String, Rule> rules = new HashMap<>();
        List<String> messages = new ArrayList<>();

        for (String line : input) {
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            if (line.contains(":")) {
                Rule rule = Rule.parse(line);
                rules.put(rule.id, rule);
                continue;
            }

            messages.add(line);
        }
        return new Pair(rules, messages);
    }

    private static long matches(HashMap<String, Rule> rules, List<String> messages) {

        Rule zero = rules.get("0");
        var matches = messages.stream()
            .filter(msg -> zero.match(msg, rules))
            .collect(Collectors.toList());
        return matches.size();
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
                            .forEach(i -> next.add(n + i));
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
