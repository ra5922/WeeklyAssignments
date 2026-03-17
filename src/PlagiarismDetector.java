import java.util.*;

public class PlagiarismDetector {

    // n-gram → set of document IDs
    private Map<String, Set<String>> index = new HashMap<>();

    int n = 5; // size of n-gram

    // Add document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        for (String gram : ngrams) {

            index.putIfAbsent(gram, new HashSet<>());

            index.get(gram).add(docId);
        }
    }

    // Generate n-grams
    private List<String> generateNGrams(String text) {

        List<String> result = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < n; j++) {
                gram.append(words[i + j]).append(" ");
            }

            result.add(gram.toString().trim());
        }

        return result;
    }

    // Analyze document
    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (index.containsKey(gram)) {

                for (String doc : index.get(gram)) {

                    matchCount.put(doc, matchCount.getOrDefault(doc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches + " matching n-grams with \"" + doc + "\"");

            System.out.println("Similarity: " + similarity + "%");

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED");
            } else if (similarity > 10) {
                System.out.println("Suspicious similarity");
            }

            System.out.println();
        }
    }

    // Main method
    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String doc1 = "data structures and algorithms are important for computer science students";
        String doc2 = "data structures and algorithms are very important for programming students";

        detector.addDocument("essay_089.txt", doc1);
        detector.addDocument("essay_092.txt", doc2);

        String newEssay = " data structures and algorithms are important for computer science";

        detector.analyzeDocument("essay_123.txt", newEssay);
    }
}