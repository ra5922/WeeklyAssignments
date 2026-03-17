import java.util.*;

public class analyticdashboard {

    // username -> userId
    private HashMap<String, Integer> users = new HashMap<>();

    // username -> number of attempts
    private HashMap<String, Integer> attempts = new HashMap<>();

    // Check if username is available
    public boolean checkAvailability(String username) {

        // track attempt frequency
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        return !users.containsKey(username);
    }

    // Register username
    public void registerUser(String username, int userId) {
        users.put(username, userId);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username.replace("_", ""));

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String most = null;
        int max = 0;

        for (String user : attempts.keySet()) {
            if (attempts.get(user) > max) {
                max = attempts.get(user);
                most = user;
            }
        }

        return most + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        analyticdashboard checker = new analyticdashboard();

        // Existing users
        checker.registerUser("john_doe", 1);
        checker.registerUser("admin", 2);

        // Check availability
        System.out.println("john_doe available: " + checker.checkAvailability("john_doe"));
        System.out.println("jane_smith available: " + checker.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("Suggestions for john_doe: " + checker.suggestAlternatives("john_doe"));

        // Simulate attempts
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");
        checker.checkAvailability("admin");

        // Most attempted username
        System.out.println(" Most attempted username: " + checker.getMostAttempted());
    }
}
