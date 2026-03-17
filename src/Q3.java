import java.util.*;

public class Q3
{

    // Entry class
    static class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, int ttl) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + ttl * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    // Cache using LinkedHashMap for LRU
    private LinkedHashMap<String, DNSEntry> cache;
    private int capacity;

    private int hits = 0;
    private int misses = 0;

    public Q3(int capacity) {
        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > Q3.this.capacity;
            }
        };

        startCleanupThread();
    }

    // Resolve DNS
    public synchronized String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null) {
            if (!entry.isExpired()) {
                hits++;
                System.out.println("Cache HIT → " + entry.ipAddress);
                return entry.ipAddress;
            } else {
                cache.remove(domain);
                System.out.println("Cache EXPIRED");
            }
        }

        misses++;

        String ip = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, ip, 300));

        System.out.println("Cache MISS → Query upstream → " + ip);

        return ip;
    }

    // Simulate upstream DNS lookup
    private String queryUpstreamDNS(String domain) {
        Random rand = new Random();
        return "172.217.14." + rand.nextInt(255);
    }

    // Cache statistics
    public void getCacheStats() {
        int total = hits + misses;
        double hitRate = (total == 0) ? 0 : ((double) hits / total) * 100;

        System.out.println("\nCache Hits: " + hits);
        System.out.println(" Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    // Background thread to remove expired entries
    private void startCleanupThread() {

        Thread cleaner = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(5000);

                    synchronized (this) {
                        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

                        while (it.hasNext()) {
                            Map.Entry<String, DNSEntry> entry = it.next();

                            if (entry.getValue().isExpired()) {
                                it.remove();
                            }
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        cleaner.setDaemon(true);
        cleaner.start();
    }

    // Main method
    public static void main(String[] args) throws Exception {

        Q3 dns = new Q3(5);

        dns.resolve("google.com");
        dns.resolve("google.com");

        Thread.sleep(2000);

        dns.resolve("yahoo.com");
        dns.resolve("google.com");

        dns.getCacheStats();
    }
}
