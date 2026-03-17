import java.util.*;

public class RealTimeAnalytics {

    // Page → visit count
    private Map<String, Integer> pageViews = new HashMap<>();

    // Page → unique visitors
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();

    // Traffic source → count
    private Map<String, Integer> trafficSources = new HashMap<>();


    // Process incoming event
    public void processEvent(String url, String userId, String source) {

        // Update page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // Update unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Update traffic source count
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }


    // Display dashboard
    public void getDashboard() {

        System.out.println("\nTop Pages:");

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        int rank = 1;

        while (!pq.isEmpty() && rank <= 10) {

            Map.Entry<String, Integer> entry = pq.poll();

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println(rank + " " + page + " - "
                    + views + " views (" + unique + " unique)");

            rank++;
        }


        System.out.println("\nTraffic Sources: ");

        int total = trafficSources.values().stream().mapToInt(i -> i).sum();

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);

            double percent = (count * 100.0) / total;

            System.out.println(source + ": " + String.format("%.1f", percent) + "%");
        }
    }


    // Main method
    public static void main(String[] args) {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent("/article/breaking-news", "user_123", "Google");
        analytics.processEvent("/article/breaking-news", "user_456", "Facebook");
        analytics.processEvent("/sports/championship", "user_789", "Direct");
        analytics.processEvent("/article/breaking-news", "user_123", "Google");
        analytics.processEvent("/sports/championship", "user_222", "Google");

        analytics.getDashboard();
    }
}