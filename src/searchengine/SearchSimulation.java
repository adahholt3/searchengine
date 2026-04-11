package searchengine;

import java.util.*;
/**
 * Done by Adah holt
 * 
 * The SearchSimulation class evaluates he efficiency and effectiveness of the search engine
 * 
 * It measures
 * -Time required to compute TF-IDF values
 * -Time required to process search queries
 * 
 * It evaluates effectiveness using precision@5
 * 
 * After running simulations, it launchs the userInterface for interactive searching
 * 
 */
public class SearchSimulation {
/**
 * evaluateRevelance evaluates the relevance of search results for a query
 * 
 * A result is considered relevant if the song title contain at least one term from the query
 * 
 * This method uses precision@5
 * 
 * @param results a list of top search results
 * @param query the search query
 * @return the number of relevant results in the list
 * 
 */
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
    
    
    //Helper method
    private static List<String> getTopK(Map<String, Double> scores, int k) {
        List<String> top = new ArrayList<>();

        while (k > 0 && !scores.isEmpty()) {
            double maxScore = -1;
            String maxSong = "";

            for (String song : scores.keySet()) {
                double score = scores.get(song);

                if (score > maxScore) {
                    maxScore = score;
                    maxSong = song;
                }
            }

            top.add(maxSong);
            scores.remove(maxSong);
            k--;
        }

        return top;
    }
    
    /**
     * Main method that runs search simulation
     * 
     * This method:
     * -Measures time to initialize search engine
     * -Executes multiple test queries
     * -Measures the time taken for each query
     * -Calculates precision@5 for each query
     * -Displays results and performance metrics
     * -Launches the user interface
     * 
     * 
     * @param args
     */
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
            //raw scores
            Map<String, Double> scores = engine.searchIDF(query);

           

            List<String> results = getTopK(scores, 5);
            
            long end = System.nanoTime();

            double timeMs = (end - start) / 1000000.0;

            System.out.println("Query: \"" + query + "\" took " + timeMs + " ms");

            // Precision@5
            int relevant = evaluateRelevance(results, query);
            double precision = relevant / 5.0;

            System.out.println("Precision@5: " + precision);
            System.out.println("----------------------");
        }

        //UI after simulation
        UserInterface ui = new UserInterface(engine);
        ui.start();
    }

   
}