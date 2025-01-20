package utils.Utility;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to calculate similarity between strings based on the Jaro-Winkler distance metric.
 * Provides functionality to find objects from a database whose string field values are similar 
 * to the given input string, based on a similarity threshold.
 */
public class SimilarityCalculator {
    /**
     * Finds and returns a map of objects from a database that have a string field value similar to the input string.
     * <p>
     * This method calculates the similarity of the input string to the specified field of each object in the
     * database using the Jaro-Winkler distance metric. Only objects whose field values meet or exceed the
     * similarity threshold are included in the returned map.
     * 
     * @param input The input string to compare against the field values of objects in the database.
     * @param objectDB The database of objects to search through, with the object keys as the map key and the
     *                 objects themselves as the map value.
     * @param fieldName The name of the string field in the objects that will be used for the comparison.
     * @param similarityThreshold The minimum similarity threshold for including an object in the result map.
     * @param <K> The type of the key in the object database.
     * @param <T> The type of the objects in the database.
     * @return A map of objects from the database whose field values are similar to the input string.
     */
    public static <K, T> HashMap<K, T> findSimilarObjects(
        String input,
        HashMap<K, T> objectDB,
        String fieldName,
        double similarityThreshold) {

        // Get list of similar names based on the provided name and field
        List<String> result = getStringsWithHighSimilarity(input, objectDB, fieldName, similarityThreshold);

        // Create a HashMap to store the objects whose names are in the result list
        HashMap<K, T> similarObjects = new HashMap<>();

        // Iterate over the objectDB and select objects whose field value is in the
        // result list
        for (Map.Entry<K, T> entry : objectDB.entrySet()) {
            String fieldValue = getFieldStringValue(entry.getValue(), fieldName);
            if (fieldValue != null && result.contains(fieldValue)) {
                similarObjects.put(entry.getKey(), entry.getValue()); // Add the matching object to the similarObjects
                                                                      // map
            }
        }

        // Return the map of similar objects
        return similarObjects;
    }

    /**
     * Retrieves a list of strings from the object database that have a high similarity to the input string.
     * <p>
     * This method calculates the Jaro-Winkler distance between the input string and the field values of each object
     * in the database, normalizes the distance using Min-Max scaling, and filters out field values below the specified
     * similarity threshold.
     * 
     * @param input The input string to compare against the field values of objects in the database.
     * @param objectDB The database of objects to search through.
     * @param fieldName The name of the string field to be compared.
     * @param similarityThreshold The minimum normalized similarity distance for a string to be included in the result.
     * @param <K> The type of the key in the object database.
     * @param <T> The type of the objects in the database.
     * @return A list of strings (field values) that are similar to the input string, sorted by similarity.
     */
    public static <K, T> List<String> getStringsWithHighSimilarity(
            String input,
            HashMap<K, T> objectDB,
            String fieldName,
            double similarityThreshold) {

        // List to hold pairs of field values and their normalized distances
        List<SimpleEntry<String, Double>> fieldValueWithDistance = new ArrayList<>();

        // Step 1: Calculate Jaro-Winkler distances
        double minDist = Double.MAX_VALUE;
        double maxDist = Double.MIN_VALUE;

        // Calculate Jaro-Winkler distance for each object in the DB
        for (Map.Entry<K, T> entry : objectDB.entrySet()) {
            String fieldValue = getFieldStringValue(entry.getValue(), fieldName);
            if (fieldValue != null) {
                double jaroWinklerDistance = calculateJaroWinklerDistance(input, fieldValue);

                // Find min and max Jaro-Winkler distances to apply Min-Max scaling
                if (jaroWinklerDistance < minDist) {
                    minDist = jaroWinklerDistance;
                }
                if (jaroWinklerDistance > maxDist) {
                    maxDist = jaroWinklerDistance;
                }

                // Store the field value and its raw Jaro-Winkler distance
                fieldValueWithDistance.add(new SimpleEntry<>(fieldValue, jaroWinklerDistance));
            }
        }

        // Step 2: Normalize the Jaro-Winkler distances using Min-Max scaling
        List<SimpleEntry<String, Double>> filteredAndNormalized = new ArrayList<>();
        for (SimpleEntry<String, Double> pair : fieldValueWithDistance) {
            double jaroWinklerDistance = pair.getValue();
            double normalizedDistance = calculateMinMaxNormalizedDistance(jaroWinklerDistance, minDist, maxDist);

            // Only add field values with normalized distance >= threshold
            if (normalizedDistance >= similarityThreshold) {
                filteredAndNormalized.add(new SimpleEntry<>(pair.getKey(), normalizedDistance));
            }
        }

        // Step 3: Sort the list in descending order by normalized distance
        filteredAndNormalized.sort((pair1, pair2) -> Double.compare(pair2.getValue(), pair1.getValue()));

        // Step 4: Extract only the field values, now sorted by normalized distance
        List<String> result = new ArrayList<>();
        for (SimpleEntry<String, Double> pair : filteredAndNormalized) {
            result.add(pair.getKey());
        }

        return result;
    }

    // Helper method to get a string field value from any object using reflection
    /**
     * Retrieves the value of a string field from an object using reflection.
     * <p>
     * This helper method dynamically calls the getter method for the specified field name in the given object.
     * 
     * @param object The object from which to retrieve the field value.
     * @param fieldName The name of the field to retrieve.
     * @param <T> The type of the object.
     * @return The value of the specified field as a string, or null if the field is not found.
     */
    private static <T> String getFieldStringValue(T object, String fieldName) {
        try {
            // Dynamically call the getter method for the specified field
            Method method = object.getClass().getMethod(fieldName);
            return (String) method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace(); // Handle reflection errors
        }
        return null;
    }

    // Jaro-Winkler Distance Calculation
    /**
     * Calculates the Jaro-Winkler distance between two strings.
     * <p>
     * This method calculates the similarity between two strings based on the Jaro-Winkler distance metric, 
     * which is particularly effective for short strings and name matching.
     * 
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return A double representing the Jaro-Winkler similarity between the two strings (range 0 to 1).
     */
    private static double calculateJaroWinklerDistance(String s1, String s2) {
        double jaro = calculateJaroDistance(s1, s2);
        int l = 0;
        int maxPrefixLength = 4; // Limit the length of the common prefix
        while (l < Math.min(s1.length(), s2.length()) && s1.charAt(l) == s2.charAt(l) && l < maxPrefixLength) {
            l++;
        }
        double p = 0.1; // Scaling factor
        return jaro + (l * p * (1 - jaro));
    }

    // Jaro Distance Calculation
    /**
     * Calculates the Jaro distance between two strings.
     * <p>
     * The Jaro distance is a string comparison algorithm that measures the similarity between two strings,
     * with a range of 0 (no similarity) to 1 (exact match).
     * 
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return A double representing the Jaro similarity between the two strings.
     */
    private static double calculateJaroDistance(String s1, String s2) {
        if (s1.equals(s2)) {
            return 1.0;
        }

        int len1 = s1.length();
        int len2 = s2.length();

        int matchDistance = Math.max(len1, len2) / 2 - 1;

        boolean[] s1Matches = new boolean[len1];
        boolean[] s2Matches = new boolean[len2];

        int matches = 0;
        for (int i = 0; i < len1; i++) {
            for (int j = Math.max(0, i - matchDistance); j < Math.min(len2, i + matchDistance + 1); j++) {
                if (s1.charAt(i) == s2.charAt(j) && !s2Matches[j]) {
                    s1Matches[i] = true;
                    s2Matches[j] = true;
                    matches++;
                    break;
                }
            }
        }

        if (matches == 0) {
            return 0.0;
        }

        int t = 0;
        int k = 0;
        for (int i = 0; i < len1; i++) {
            if (s1Matches[i]) {
                while (!s2Matches[k]) {
                    k++;
                }
                if (s1.charAt(i) != s2.charAt(k)) {
                    t++;
                }
                k++;
            }
        }

        t /= 2;

        return ((matches / (double) len1) + (matches / (double) len2) + ((matches - t) / (double) matches)) / 3.0;
    }

    // Normalize the distance using Min-Max normalization
    /**
     * Normalizes the distance using Min-Max normalization.
     * <p>
     * This method scales the distance value into a range of 0 to 1 based on the provided 
     * minimum and maximum distances.
     * 
     * @param distance The distance to normalize.
     * @param minDist The minimum distance in the dataset.
     * @param maxDist The maximum distance in the dataset.
     * @return The normalized distance.
     */
    private static double calculateMinMaxNormalizedDistance(double distance, double minDist, double maxDist) {
        return (distance - minDist) / (maxDist - minDist);
    }
}
