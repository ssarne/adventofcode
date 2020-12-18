package aoc.aoc2020;

import java.util.*;

import static aoc.utils.Utils.*;

public class Dec16 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1("aoc2020/dec16_test.txt"), 71);
        check(solve2("aoc2020/dec16_test.txt", "seat"), 14);
    }

    public static void task1() {
        var result = solve1("aoc2020/dec16.txt");
        check(result, 25916);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("aoc2020/dec16.txt", "departure");
        check(result, 2564529489989L);
        System.out.println("Result: " + result);
    }

    public static int solve1(String input) {

        var lines = getLines(input);
        var rules = parseRules(lines);
        var myTicket = parseMyTicket(lines);
        var tickets = parseTickets(lines);

        int sum = 0;
        for (Ticket ticket : tickets) {
            sum += validate(rules, ticket);
        }
        return sum;
    }

    public static long solve2(String input, String tag) {
        var lines = getLines(input);
        var rules = parseRules(lines);
        var myTicket = parseMyTicket(lines);
        var tickets = parseTickets(lines);
        var valids = getValidTickets(tickets, myTicket, rules);

        resolve(rules, valids);

        long p = 1;
        for (Rule rule : rules) {
            if (rule.name.startsWith(tag)) {
                p *= myTicket.values[rule.position];
            }
        }

        return p;
    }


    public static List<Rule> parseRules(List<String> lines) {

        List<Rule> rules = new ArrayList<>();

        int n = 0;
        while (n < lines.size()) {
            String line = lines.get(n++);
            if (line.strip().equals("")) {
                break;
            }
            rules.add(Rule.parse(line));
        }
        return rules;
    }

    public static Ticket parseMyTicket(List<String> lines) {

        for (int n = 0; n < lines.size(); n++) {
            if (lines.get(n).contains("your ticket")) {
                return new Ticket(lines.get(n + 1));
            }
        }
        throw new UnsupportedOperationException("Didn't find ticket.");
    }

    public static List<Ticket> parseTickets(List<String> lines) {

        List<Ticket> tickets = new ArrayList<>();

        int n = 0;
        while (n < lines.size()) {
            if (lines.get(n++).contains("nearby tickets")) {
                break;
            }
        }
        while (n < lines.size()) {
            tickets.add(new Ticket(lines.get(n++)));
        }
        return tickets;
    }

    private static int validate(List<Rule> rules, Ticket ticket) {
        int sum = 0;
        for (int i : ticket.values) {
            boolean match = false;
            for (Rule rule : rules) {
                if (rule.match(i)) {
                    match = true;
                }
            }
            if (!match) {
                sum += i;
            }
        }
        return sum;
    }

    private static List<Ticket> getValidTickets(List<Ticket> tickets, Ticket myTicket, List<Rule> rules) {
        List<Ticket> valids = new ArrayList<>();
        valids.add(myTicket);
        for (Ticket ticket : tickets) {
            boolean allValid = true;
            for (int v : ticket.values) {
                boolean valid = false;
                for (Rule rule : rules) {
                    if (rule.match(v)) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    allValid = false;
                    break;
                }
            }
            if (allValid) {
                valids.add(ticket);
            }
        }
        return valids;
    }

    private static void resolve(List<Rule> rules, List<Ticket> tickets) {

        int length = tickets.get(0).values.length;
        var matched = new boolean[length];
        var used = new HashMap<Integer, Rule>();

        boolean changed = true;
        for (int j = 0; changed; j++) {
            changed = false;

            for (Rule rule : rules) {
                if (rule.assigned())
                    continue;

                var candidates = new ArrayList<Integer>();
                for (int i = 0; i < length; i++) {
                    if (matched[i]) {
                        continue;
                    }

                    boolean candidate = true;
                    for (Ticket ticket : tickets) {
                        if (!rule.match(ticket.values[i])) {
                            candidate = false;
                            break;
                        }
                    }
                    if (candidate) {
                        candidates.add(i);
                    }
                }

                if (candidates.size() == 1) {
                    changed = true;
                    int i = candidates.get(0);
                    used.put(i, rule);
                    rule.position = i;
                    matched[i] = true;
                    continue;
                }
            }
        }
    }

    private static class Ticket {
        int[] values;

        public Ticket(String ticket) {
            this.values = asInts(ticket);
        }

        public String toString() {
            return "Ticket: " + Arrays.toString(values);
        }
    }

    private static class Rule {

        String name;
        int range1low;
        int range1high;
        int range2low;
        int range2high;
        int position = -1;

        public Rule(String name, int range1low, int range1high, int range2low, int range2high) {
            this.name = name;
            this.range1low = range1low;
            this.range1high = range1high;
            this.range2low = range2low;
            this.range2high = range2high;
        }

        public static Rule parse(String line) {
            var halfs = line.split(": ");
            var ranges = halfs[1].split(" or ");
            var low = ranges[0].split("-");
            var high = ranges[1].split("-");
            return new Rule(halfs[0], asInt(low[0]), asInt(low[1]), asInt(high[0]), asInt(high[1]));
        }

        public boolean assigned() {
            return position != -1;
        }

        public boolean match(int value) {
            return value >= range1low && value <= range1high
                    || value >= range2low && value <= range2high;
        }

        public String toString() {
            return "Rule " + name + "  " + range1low + "-" + range1high + "  " + range2low + "-" + range2high + "  position=" + position;
        }
    }
}
