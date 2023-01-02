package aoc.aoc2020;

import java.util.*;
import static aoc.utils.Utils.*;

public class Dec21 {

    public static void main(String[] args) throws Exception {
        test();
        task1();
        task2();
    }

    public static void test() {
        check(solve1("aoc2020/dec21_test.txt"), 5);
        check(solve2("aoc2020/dec21_test.txt"), "mxmxvkd,sqjhc,fvjkl");
    }

    public static void task1() {
        var result = solve1("aoc2020/dec21.txt");
        check(result, 2265);
        System.out.println("Result: " + result);
    }

    public static void task2() {
        var result = solve2("aoc2020/dec21.txt");
        check(result, "dtb,zgk,pxr,cqnl,xkclg,xtzh,jpnv,lsvlx");
        System.out.println("Result: " + result);
    }

    public static long solve1(String input) {

        var lines = getLines(input);
        var ingredientsCount = new HashMap<String, Integer>(); // map of all ingredients and their occurrences
        var allergensToIngredientLists = parseFoodList(lines, ingredientsCount); // allergen to (multiple) list of ingredients
        var allergensMap = mergeAllergens(allergensToIngredientLists);

        reduceAllergens(allergensMap);

        var allergenicIngredients = getAllergenicIngredients(allergensMap);
        return countNonallergenicIngredients(ingredientsCount, allergenicIngredients);
    }

    public static String solve2(String input) {

        var lines = getLines(input);
        var ingredientsCount = new HashMap<String, Integer>(); // map of all ingredients and their occurrences
        var allergensToIngredientLists = parseFoodList(lines, ingredientsCount); // allergen to (multiple) list of ingredients
        var allergensMap = mergeAllergens(allergensToIngredientLists);

        reduceAllergens(allergensMap);

        return getSortedIngredients(allergensMap);
    }

    private static String getSortedIngredients(HashMap<String, List<String>> allergensMap) {
        var al = new ArrayList<String>(allergensMap.keySet());
        Collections.sort(al);

        StringBuilder sb = new StringBuilder(allergensMap.get(al.get(0)).get(0));
        for (int i = 1; i < al.size(); i++) {
            sb.append(',').append(allergensMap.get(al.get(i)).get(0));
        }
        // System.out.println(sb.toString());
        return sb.toString();
    }


    private static int countNonallergenicIngredients(HashMap<String, Integer> ingredientsCount, HashSet<String> allergenicIngredients) {
        int tot = 0;
        for (String i : ingredientsCount.keySet()) {
            if (!allergenicIngredients.contains(i)) {
                tot += ingredientsCount.get(i);
            }
        }
        return tot;
    }

    private static HashSet<String> getAllergenicIngredients(HashMap<String, List<String>> allergensMap) {
        var allergenicIngredients = new HashSet<String>();
        for (String a : allergensMap.keySet()) {
            // System.out.println(a + ": " + allergensMap.get(a));
            allergenicIngredients.addAll(allergensMap.get(a));
        }
        return allergenicIngredients;
    }

    private static void reduceAllergens(HashMap<String, List<String>> aToI2) {
        for (boolean changed = true; changed; ) {
            changed = false;
            for (String a : aToI2.keySet()) {
                var is = aToI2.get(a);
                if (is.size() == 1) {
                    String i = is.get(0);
                    for (String a2 : aToI2.keySet()) {
                        if (!a2.equals(a)) {
                            if (aToI2.get(a2).contains(i)) {
                                aToI2.get(a2).remove(i);
                                changed = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private static HashMap<String, List<String>> mergeAllergens(HashMap<String, List<List<String>>> allergensMap) {
        var aToI2 = new HashMap<String, List<String>>();
        for (String a : allergensMap.keySet()) {
            var ais = allergensMap.get(a);
            var is = new ArrayList<String>();
            for (String i : ais.get(0)) {
                boolean match = true;
                for (List<String> il : ais) {
                    if (!il.contains(i)) {
                        match = false;
                    }
                }
                if (match) {
                    is.add(i);
                }
            }
            ensure (is.size() > 0);
            // System.out.println("Found " + (is.size() == 1 ? "" : "several for ") + a + ": " + is);
            aToI2.put(a, is);
        }
        return aToI2;
    }

    private static HashMap<String, List<List<String>>> parseFoodList(List<String> lines, HashMap<String, Integer> allIngredients) {
        var allergensMap = new HashMap<String, List<List<String>>>();
        for (String line : lines) {
            int ip1 = line.indexOf('(');
            String [] ingredients = ((ip1 == -1) ? line : line.substring(0, ip1-1)).split(" ");
            String [] allergens = (ip1 == -1) ? new String[0] : (line.substring(ip1 + "(contains ".length(), line.length() - 1)).split(", ");
            for (String i : ingredients) {
                int n = allIngredients.containsKey(i) ? allIngredients.get(i) + 1 : 1;
                allIngredients.put(i, n);
            }
            for (String a : allergens) {
                var il = allergensMap.get(a);
                if (il == null) {
                    il = new ArrayList<>();
                    allergensMap.put(a, il);
                }
                il.add(Arrays.asList(ingredients));
            }
        }
        return allergensMap;
    }
}
