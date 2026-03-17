import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Q2 {

    // HashMap for stock lookup
    private ConcurrentHashMap<String, AtomicInteger> inventory = new ConcurrentHashMap<>();

    // Waiting list FIFO
    private Map<String, Queue<Integer>> waitingList = new ConcurrentHashMap<>();

    // Initialize product stock
    public void addProduct(String productId, int stock) {
        inventory.put(productId, new AtomicInteger(stock));
        waitingList.put(productId, new LinkedList<>());
    }

    // Instant stock check O(1)
    public int checkStock(String productId) {
        AtomicInteger stock = inventory.get(productId);
        if (stock == null) return -1;
        return stock.get();
    }

    // Purchase item (thread safe)
    public synchronized String purchaseItem(String productId, int userId) {

        AtomicInteger stock = inventory.get(productId);

        if (stock == null)
            return "Product not found";

        if (stock.get() > 0) {
            int remaining = stock.decrementAndGet();
            return "Success, " + remaining + " units remaining";
        }

        // Stock finished → add to waiting list
        Queue<Integer> queue = waitingList.get(productId);
        queue.add(userId);

        return "Added to waiting list, position #" + queue.size();
    }

    // View waiting list
    public void printWaitingList(String productId) {
        Queue<Integer> queue = waitingList.get(productId);

        System.out.println("Waiting List for " + productId + ":");
        for (Integer user : queue) {
            System.out.println("User: " + user);
        }
    }

    // Main method for testing
    public static void main(String[] args) {

        Q2 manager = new Q2();

        manager.addProduct("IPHONE15_256GB", 100);

        System.out.println("Stock: " + manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate stock sold out
        for (int i = 0; i < 100; i++) {
            manager.purchaseItem(" IPHONE15_256GB", i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));

        manager.printWaitingList("IPHONE15_256GB");
    }
}
