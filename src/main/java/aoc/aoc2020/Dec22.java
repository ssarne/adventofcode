package aoc.aoc2020;

import java.util.*;

import static aoc.utils.Utils.*;

public class Dec22 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1("aoc2020/dec22_test.txt"), 306);
        check(solve2("aoc2020/dec22_test2.txt"), 105);
        check(solve2("aoc2020/dec22_test.txt"), 291);

    }

    public static void task1() {
        var result = solve1("aoc2020/dec22.txt");
        check(result, 32199);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("aoc2020/dec22.txt");
        check(result, 33780);
        System.out.println("Result: " + result);
    }

    public static long solve1(String input) {

        var lines = getLines(input);
        Queue<Integer> p1 = new LinkedList<>();
        Queue<Integer> p2 = new LinkedList<>();
        parseDecks(lines, p1, p2);

        for (int round = 0; !(p1.isEmpty() || p2.isEmpty()); round++) {
            dbg("-- Round " + round + " --");
            dbg("Player 1: " + p1);
            dbg("Player 2: " + p2);
            int i1 = p1.poll();
            int i2 = p2.poll();
            dbg("Player 1 plays: " + i1);
            dbg("Player 2 plays: " + i2);
            ensure(i1 != i2);

            if (i1 > i2) {
                dbg("Player 1 wins the round!");
                p1.add(i1);
                p1.add(i2);
            } else {
                dbg("Player 2 wins the round!");
                p2.add(i2);
                p2.add(i1);
            }
        }

        dbg("== Post-game results ==");
        dbg("Player 1: " + p1);
        dbg("Player 2: " + p2);

        return (p1.size() > 0 ? score(p1) : score(p2));
    }

    public static long solve2(String input) {
        var lines = getLines(input);
        Queue<Integer> p1 = new LinkedList<>();
        Queue<Integer> p2 = new LinkedList<>();

        parseDecks(lines, p1, p2);
        Win winner = playRecursiveCombat(copy(p1, p1.size()), copy(p2, p2.size()), 0, new Counter());

        dbg("== Post-game results ==");
        dbg("Player 1: " + winner.p1);
        dbg("Player 2: " + winner.p2);
        dbg("Winner: Player " + winner.player);

        return winner.score;
    }

    private static int score(Queue<Integer> p) {
        int score = 0;
        Iterator<Integer> it = p.iterator();
        for (int i = p.size(); it.hasNext(); i--) {
            score += i * it.next();
        }
        return score;
    }

    private static Queue<Integer> copy(Queue<Integer> list, int len) {
        Queue<Integer> next = new LinkedList<>();
        Iterator<Integer> it = list.iterator();
        for (int i = 0; i < len && it.hasNext(); i++) {
            next.add(it.next());
        }
        return next;
    }

    private static Win playRecursiveCombat(Queue<Integer> p1, Queue<Integer> p2, int level, Counter games) {

        int game = games.inc();
        dbg(indent(level) + "=== Game " + game + " ===");
        var history = new HashSet<String>();
        for (int round = 1; p1.size() > 0 && p2.size() > 0; round++) {

            dbg(indent(level) + "-- Round " + round + " (Game " + game + ") --");
            dbg(indent(level) + "Player 1: " + p1);
            dbg(indent(level) + "Player 2: " + p2);
            String h = p1.toString() + " :: " + p2.toString(); // i1 + "," + i2;
            if (history.contains(h)) {
                dbg(indent(level) + "Autowin Player 1");
                return new Win(1, 1, score(p1), game, p1, p2);
            }
            history.add(h);

            int i1 = p1.poll();
            int i2 = p2.poll();
            ensure(i1 != i2);

            dbg(indent(level) + "Player 1 plays: " + i1);
            dbg(indent(level) + "Player 2 plays: " + i2);

            int winner;
            if (i1 <= p1.size() && i2 <= p2.size()) {
                dbg(indent(level) + "Playing a sub-game to determine the winner...");
                Win win = playRecursiveCombat(copy(p1, i1), copy(p2, i2), level + 1, games);
                winner = win.player;
                dbg(indent(level) + "...anyway, back to game " + game);
            } else {
                winner = (i1 > i2 ? 1 : 2);
            }

            if (winner == 1) {
                p1.add(i1);
                p1.add(i2);
            } else {
                p2.add(i2);
                p2.add(i1);
            }
            dbg(indent(level) + "Player " + winner + " wins round " + round + " of game " + game + "!");
        }

        Win win;
        if (p1.size() == 0) {
            win = new Win(2, 2, score(p2), game, p1, p2);
        } else {
            win = new Win(2, 1, score(p1), game, p1, p2);
        }
        dbg(indent(level) + "The winner of game " + win.game + " is player " + win.player + "!");
        return win;
    }

    private static void parseDecks(List<String> lines, Queue<Integer> p1, Queue<Integer> p2) {
        var p = p1;
        for (String line : lines) {
            if (line.length() == 0) continue;
            if (line.contains("Player 1")) {
                p = p1;
                continue;
            }
            if (line.contains("Player 2")) {
                p = p2;
                continue;
            }
            p.add(Integer.parseInt(line));
        }
    }

    private static void dbg(String line) {
        // System.out.println(line);
    }

    private static class Counter {
        int c = 0;

        int inc() {
            return ++c;
        }
    }

    private static class Win {
        int type = 0;
        int player = 0;
        int score = 0;
        int game;
        Queue<Integer> p1;
        Queue<Integer> p2;

        public Win(int type, int player, int score, int game, Queue<Integer> p1, Queue<Integer> p2) {
            this.type = type;
            this.player = player;
            this.score = score;
            this.game = game;
            this.p1 = p1;
            this.p2 = p2;
        }
    }
}
