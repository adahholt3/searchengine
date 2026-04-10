package searchengine;

import java.util.*;

public class SearchSimulation {
	 // Simple revelance check/ review this
    private static int evaluateRelevance(List<String> results, String query) {
        int count = 0;

        String[] terms = query.toLowerCase().split(" ");

        for (String song : results) {
            for (String term : terms) {
                if (song.toLowerCase().contains(term)) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }
    public static void main(String[] args) {

        // Measure base time
        long startBuild = System.nanoTime();

        SearchEngine engine = new SearchEngine("songs.tsv"); 

        long endBuild = System.nanoTime();

        double buildTimeMs = (endBuild - startBuild) / 1000000.0;
        System.out.println("TF-IDF build time: " + buildTimeMs + " ms");

        //Testing queries
        String[] testQueries = {
            "love",
            "happy",
            "dark night",
            "broken heart",
            "dance",
            "lonely",
            "party",
            "rain",
            "dream",
            "fire"
        };

        System.out.println("\n--- Search Timing ---");

        for (String query : testQueries) {

            long start = System.nanoTime();

            List<String> results = engine.getTopResults(query, 5);

            long end = System.nanoTime();

            double timeMs = (end - start) / 1000000.0;

            System.out.println("Query: \"" + query + "\" took " + timeMs + " ms");

            // Precision@5
            int relevant = evaluateRelevance(results, query);
            double precision = relevant / 5.0;

            System.out.println("Precision@5: " + precision);
            System.out.println("----------------------");
        }

        // After UI
        UserInterface ui = new UserInterface(engine);
        ui.start();
    }

   
}