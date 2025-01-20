package gcloud;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is used to interact with a Cloud Function that translates text to a specified target language.
 * It sends a POST request to the Cloud Function, receives a JSON response, and extracts the translated text.
 */
public class CloudFunctionQuery {
    /**
     * The entry point of the program. It sends a POST request to the Cloud Function and processes the response.
     *
     * @param args Command line arguments (not used in this program).
     */
    public static void main(String[] args) {
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
            String jsonPayload = "{\"text\": \"testing function\", \"targetLanguage\": \"es\"}";
            
            // Send the JSON payload in the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Get the HTTP response code
            int responseCode = connection.getResponseCode();
            //System.out.println("Response Code: " + responseCode);
            
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
                    System.out.println(result);  // Output: ¡Hola Mundo!



                }
            } else {
                System.out.println("Error: Could not contact Cloud Function. HTTP code " + responseCode);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Decodes Unicode escape sequences (e.g., \u00E9) in the input string and returns the resulting string.
     *
     * @param input The string containing Unicode escape sequences.
     * @return A string with the decoded Unicode characters.
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
                output.append((char) codePoint);  // Convert to the corresponding character
                i += 6;  
            } else {
                output.append(currentChar);
                i++;
            }
        }
        return output.toString();
    }
}
