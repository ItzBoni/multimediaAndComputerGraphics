package api;

/**
 * Base class for API client classes.
 *
 * Provides utility methods for JSON string escaping to prevent
 * malformed JSON when embedding user-generated content in API requests.
 */
public class Connectable {

    /**
     * Escapes special characters in a string for safe JSON embedding.
     *
     * Handles:
     * - Backslashes
     * - Double quotes
     * - Newlines
     * - Carriage returns
     * - Tabs
     *
     * @param s the string to escape
     * @return escaped string safe for JSON embedding
     */
    public String escapeJson(String s){
        return s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
