package utils.Translator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for translating text using a deployed Cloud Function.
 * It sends text to a translation service and decodes the returned translated text.
 */
public class Translate {
    /**
     * Translates the input text into the specified target language using an HTTP POST
     * request to a cloud-based translation service.
     *
     * @param text the text to be translated
     * @param lang the target language code (e.g., "es" for Spanish, "fr" for French)
     * @return the translated text, or {@code null} if an error occurs during translation
     */
    public static String translate(String text, String lang) {
        if (lang.equals("en")) {
            return text;
        } else {
            return translateText(text, lang);
        }
    }

    /**
     * Translates a list of strings into the specified language.
     * 
     * This method iterates through the given list of strings and translates each string
     * to the target language using an external translation service. If the target language
     * is "en" (English), the original list is returned without modification.
     * 
     * @param text The list of strings to be translated.
     * @param lang The target language code (e.g., "en" for English, "es" for Spanish).
     * @return A list of translated strings if the target language is not English, otherwise the original list.
     */
    public static List<String> translate(List<String> text, String lang) {
        if (lang.equals("en")) {
            return text;
        } else {
            List<String> translatedList = new ArrayList<>();
            for (String str : text) {
                translatedList.add(translateText(str, lang));
            }
            return translatedList;
        }
    }

    /**
     * Translates a single string into the specified language using an external translation service.
     * 
     * This method sends a POST request to a cloud function that performs text translation. The translation
     * is performed using the target language specified by the `lang` parameter. The response from the 
     * translation service is parsed to extract the translated text.
     * 
     * @param text The string to be translated.
     * @param lang The target language code (e.g., "en" for English, "es" for Spanish).
     * @return The translated string, or `null` if an error occurs during translation.
     */
    public static String translateText(String text, String lang) {
        try {
            // URL of your deployed Cloud Function
            String urlString = "https://us-central1-calcium-spanner-439414-b5.cloudfunctions.net/translate_text";
            URL url = new URL(urlString);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the HTTP method to POST
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // Allow sending data in the request body
            connection.setRequestProperty("Content-Type", "application/json");

            // Prepare the JSON payload
            String jsonPayload = "{\"text\": \"" + text + "\", \"targetLanguage\": \"" + lang + "\"}";

            // Send the JSON payload in the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();
            // System.out.println("Response Code: " + responseCode);

            // If response code is OK (200), read the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    // Print the response

                    String jsonResponse = response.toString();

                    // Step 1: Find the position of the translatedText field
                    int startIndex = jsonResponse.indexOf("\"translatedText\": \"") + "\"translatedText\": \"".length();
                    int endIndex = jsonResponse.indexOf("\"", startIndex);

                    // Step 2: Extract the translated text substring
                    String translatedText = jsonResponse.substring(startIndex, endIndex);

                    // Step 3: Convert Unicode escape sequences to actual characters
                    String result = decodeUnicode(translatedText);

                    // Print the result
                    // System.out.println(result); // Output: ¡Hola Mundo!
                    return result;

                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }

        return "";
    }

    /**
     * Decodes a string containing Unicode escape sequences (e.g., "\u0041") into
     * its corresponding characters.
     *
     * @param input the input string containing Unicode escape sequences
     * @return the decoded string with all escape sequences converted to characters
     */
    public static String decodeUnicode(String input) {
        StringBuilder output = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            char currentChar = input.charAt(i);
            if (currentChar == '\\' && i + 1 < input.length() && input.charAt(i + 1) == 'u') {
                // Unicode escape sequence found, decode it
                String hex = input.substring(i + 2, i + 6); // Get the 4-digit hex code
                int codePoint = Integer.parseInt(hex, 16);
                output.append((char) codePoint); // Convert to the corresponding character
                i += 6;
            } else {
                output.append(currentChar);
                i++;
            }
        }
        return output.toString();
    }
}
