package dtu.thebestprice;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class LevenshteinDistance {
    public static int dist(char[] s1, char[] s2) {

        // distance matrix - to memoize distances between substrings
        // needed to avoid recursion
        int[][] d = new int[s1.length + 1][s2.length + 1];

        // d[i][j] - would contain distance between such substrings:
        // s1.subString(0, i) and s2.subString(0, j)

        for (int i = 0; i < s1.length + 1; i++) {
            d[i][0] = i;
        }

        for (int j = 0; j < s2.length + 1; j++) {
            d[0][j] = j;
        }

        for (int i = 1; i < s1.length + 1; i++) {
            for (int j = 1; j < s2.length + 1; j++) {
                int d1 = d[i - 1][j] + 1;
                int d2 = d[i][j - 1] + 1;
                int d3 = d[i - 1][j - 1];
                if (s1[i - 1] != s2[j - 1]) {
                    d3 += 1;
                }
                d[i][j] = Math.min(Math.min(d1, d2), d3);
            }
        }
        return d[s1.length][s2.length];
    }

    public static void main(String[] args) {
        String s1 = "iphone xs";
        String s2 = "xr";

        JaroWinkler jw = new JaroWinkler();
        System.out.println(jw.similarity(s2, s1) * 100);
        if (s1.trim().length() == s2.trim().length() && jw.similarity(s1, s2) * 100 > 99)
            System.out.println("Trung lap");
        else System.out.println("Không trung lặp");
    }
}
