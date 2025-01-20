package utils.libraries;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Users.*;

/**
 * Utility class for string operations, including calculation of
 * Levenshtein distance and identifying similar strings based on 
 * Min-Max normalized distances.
 */
public class StringUtil {

    // Method to calculate the Levenshtein distance between two strings
    /**
     * Calculates the Levenshtein distance between two strings.
     *
     * @param str1 the first string
     * @param str2 the second string
     * @return the Levenshtein distance between the two strings
     *         (number of single-character edits needed to transform one string to the other)
     */
    public static int calculateLevenshteinDistance(String str1, String str2) {
        if (str1 == null || str1.isEmpty()) {
            return str2.length();
        }
        if (str2 == null || str2.isEmpty()) {
            return str1.length();
        }

        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= str2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(
                        dp[i - 1][j] + 1, // Deletion
                        dp[i][j - 1] + 1), // Insertion
                        dp[i - 1][j - 1] + cost // Substitution
                );
            }
        }

        return dp[str1.length()][str2.length()];
    }

    // Method to calculate the Min-Max scaled Levenshtein distance
    /**
     * Calculates the Min-Max normalized distance given the raw Levenshtein distance,
     * minimum distance, and maximum distance.
     *
     * @param levDistance the raw Levenshtein distance
     * @param minDist the minimum Levenshtein distance observed in the dataset
     * @param maxDist the maximum Levenshtein distance observed in the dataset
     * @return the normalized distance scaled between 0 and 1
     */
    public static double calculateMinMaxNormalizedDistance(int levDistance, int minDist, int maxDist) {
        // Min-Max normalization formula
        return (double) (levDistance - minDist) / (maxDist - minDist);
    }

    // Method to compare the input string to the userDB and extract strings with
    // high similarity based on Min-Max normalized Levenshtein distance
    /**
     * Compares the input string to the user database and extracts strings with high similarity
     * based on Min-Max normalized Levenshtein distance.
     *
     * @param input the input string to be compared
     * @param userDB a HashMap representing the user database, where keys are user IDs
     *               and values are User objects
     * @return a list of user names with high similarity to the input string
     */
    public static List<String> getStringsWithHighSimilarity(String input, HashMap<String, User> userDB) {
        // List to hold the names of users that have high similarity with the input
        List<String> result = new ArrayList<>();

        // Step 1: Calculate the Levenshtein distances
        List<Integer> levDistances = new ArrayList<>();
        int minDist = Integer.MAX_VALUE;
        int maxDist = Integer.MIN_VALUE;

        for (Map.Entry<String, User> entry : userDB.entrySet()) {
            String name = entry.getValue().getName();
            int levDistance = calculateLevenshteinDistance(input, name);
            levDistances.add(levDistance);

            // Find min and max Levenshtein distances to apply Min-Max scaling
            if (levDistance < minDist) {
                minDist = levDistance;
            }
            if (levDistance > maxDist) {
                maxDist = levDistance;
            }
        }

        // Step 2: Normalize the Levenshtein distances using Min-Max scaling
        for (Map.Entry<String, User> entry : userDB.entrySet()) {
            String name = entry.getValue().getName();
            int levDistance = calculateLevenshteinDistance(input, name);
            double normalizedDistance = calculateMinMaxNormalizedDistance(levDistance, minDist, maxDist);
            
            // Step 3: Add to result if the normalized distance is greater than a threshold
            // (e.g., 0.6 for high similarity)
            if (normalizedDistance >= 0.6) { // You can adjust this threshold to suit your needs
                result.add(name);
            }
        }

        return result;
    }

}
