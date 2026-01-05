package com.techconnect.opportunity.util;

public class SimilarityUtil {

    /**
     * Calculates the Levenshtein distance between two strings.
     */
    public static int calculateLevenshteinDistance(String x, String y) {
        if (x == null && y == null)
            return 0;
        if (x == null || y == null)
            return Integer.MAX_VALUE;

        int m = x.length();
        int n = y.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++)
            dp[i][0] = i;
        for (int j = 0; j <= n; j++)
            dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (x.charAt(i - 1) == y.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[m][n];
    }

    /**
     * Calculates similarity score (0.0 to 1.0) based on Levenshtein distance.
     */
    public static double calculateSimilarity(String x, String y) {
        if (x == null || y == null)
            return 0.0;
        String s1 = x.toLowerCase();
        String s2 = y.toLowerCase();
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0)
            return 1.0;
        return 1.0 - (double) calculateLevenshteinDistance(s1, s2) / maxLength;
    }
}
