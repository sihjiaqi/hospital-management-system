package utils.Utility;

/**
 * The Config class provides a simple utility to manage application configuration settings,
 * specifically for language preference.
 * <p>
 * This class allows getting and setting the language used in the application.
 * The default language is set to "en" (English).
 * </p>
 */
public class Config {
    
    /**
     * The current language setting for the application. 
     * Default is "en" (English).
     */
    public static String lang = "en";

    /**
     * Sets the language preference for the application.
     *
     * @param newLang The new language code to set. This should be a valid language code (e.g., "en", "fr").
     */
    public static void setLang(String newLang) {
        lang = newLang;
    }

    /**
     * Gets the current language preference of the application.
     *
     * @return The current language code, default is "en".
     */
    public static String getLang() {
        return lang;
    }
}
