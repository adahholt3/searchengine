package searchengine;

import java.util.*;
/**
 * UserInterface by Adah Holt
 * -Allows users to enter name and give welcome message
 * -Perform multiple search queries
 * -View previous search queries
 * -And then exit
 */
public class UserInterface {

    private SearchEngine engine;
    private List<String> userQueries;
    /**
     * Constructs a UserInterface with a given SearchEngine.
     *
     * @param engine the search engine used to process queries
     */
    public UserInterface(SearchEngine engine) {
        this.engine = engine;
        this.userQueries = new ArrayList<>();
    }
    
/**
 * Starts user interface loop
 * 
 * Allows user to:
 * -Search for songs
 * -View past queries
 * -Exit program
 * 
 * This method continues running until the user types "EXIT"
 */
    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Welcome, " + name + "!");

        boolean running = true;

        while (running) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Search songs");
            System.out.println("2. View past queries");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    while (true) {
                        System.out.print("\nEnter search query (type EXIT to stop): ");
                        String query = scanner.nextLine();

                        if (query.equalsIgnoreCase("EXIT")) {
                            break;
                        }

                        userQueries.add(query);

                        // Step 1: Get scores from  the backend
                        Map<String, Double> scores = engine.searchIDF(query);

                        // Step 2: apply enhancement
                        normalizeByLength(scores);

                        // Step 3: Get top 5
                        List<String> results = getTopK(scores, 5);

                        //Display results
                        System.out.println("\nTop Results:");
                        int rank = 1;
                        for (String song : results) {
                            System.out.println(rank + ". " + song);
                            rank++;
                        }
                    }
                    break;

                case 2:
                    System.out.println("\nYour past queries:");
                    for (String q : userQueries) {
                        System.out.println("- " + q);
                    }
                    break;

                case 3:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        // Print all queries before exit
        System.out.println("\nAll your queries:");
        for (String q : userQueries) {
            System.out.println("- " + q);
        }

        System.out.println("Goodbye!");
        scanner.close();
    }
/**
 * Applies a normalization enhancement to the TF-IDF scores
 * 
 * So each song's score is divided by the number of words in its titles. This is good because it reduces the bias toward longer titles
 * @param scores a map of song titles to their TF-IDF scores
 */
    //ENHANCEMENTn that Normalize scores by song title length
    private void normalizeByLength(Map<String, Double> scores) {
        for (String song : scores.keySet()) {
            int length = song.split(" ").length;

            if (length > 0) {
                scores.put(song, scores.get(song) / length);
            }
        }
    }
/**
 * Retrives the top k songs based on their scores
 * 
 * This method selects the highest scoring songs from the map and returns them in descending order
 * @param scores a map of song titles to their scores
 * @param k is the number of top results to return
 * @return then returns a list of the top k song titles
 */
    //Get top K songs
    private List<String> getTopK(Map<String, Double> scores, int k) {
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
}