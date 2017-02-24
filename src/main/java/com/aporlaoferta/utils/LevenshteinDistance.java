package com.aporlaoferta.utils;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 18/02/16
 * Time: 22:55
 */
public class LevenshteinDistance {

    /**
     * Process both words and check whether the max threshold is  reached
     *
     * @param first
     * @param second
     * @param threshold
     * @return
     */
    public static boolean process(String first, String second, int threshold) {
        String word = first.toUpperCase().replaceAll("\\s+","");
        String original = second.toUpperCase().replaceAll("\\s+","");
        if (isPartOfWord(word, original) || isPartOfWord(original, word)) {
            return true;
        }
        return distance(word, original) < threshold;
    }

    /**
     * Checks whether 'original' is part of 'word' or not
     * Minimum length is 3 to avoid unique char
     * Checks for char length to avoid having same word in a different (longer/shorter) word
     *
     * @param word
     * @param original
     * @return
     */
    private static boolean isPartOfWord(String word, String original) {
        return similarLength(word, original) && original.length() > 2 && word.indexOf(original) >= 0;
    }

    private static boolean similarLength(String word, String original) {
        return Math.abs(word.length() - original.length()) <= 2;
    }

    /**
     * https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
     *
     * @param lhs
     * @param rhs
     * @return
     */
    private static int distance(CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;
        int[] cost = new int[len0];
        int[] newcost = new int[len0];
        for (int i = 0; i < len0; i++) cost[i] = i;
        for (int j = 1; j < len1; j++) {
            newcost[0] = j;
            for (int i = 1; i < len0; i++) {
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }
        return cost[len0 - 1];
    }
}
