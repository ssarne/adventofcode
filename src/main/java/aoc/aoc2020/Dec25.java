package aoc.aoc2020;

import static aoc.utils.Utils.*;
import static aoc.utils.MathAlgos.*;

public class Dec25 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1(5764801, 17807724), 14897079);
    }

    public static void task1() {
        var result = solve1(14012298, 74241);
        check(result, 18608573);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        System.out.println("Result: Merry Christmas!");
    }

    public static long solve1(long cardPubKey, long doorPubKey) {

        long subject = 7;
        long mod = 20201227L;

        int cardLoops = 0;
        for (long value = 1; value != cardPubKey; cardLoops++) {
            value = (subject * value) % mod;
        }
        //System.out.println("Card loops=" + cardLoops);

        int doorLoops = 0;
        for (long value = 1; value != doorPubKey; doorLoops++) {
            value = (subject * value) % mod;
        }
        //System.out.println("Door loops=" + doorLoops);

        //System.out.println("Encryption key: " + pow(doorPubKey, cardLoops, mod));
        ensure(pow(doorPubKey, cardLoops, mod) == pow(cardPubKey, doorLoops, mod));

        return pow(cardPubKey, doorLoops, mod);
    }
}
