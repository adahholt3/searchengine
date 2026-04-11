package searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class SearchEngine {
	
	private Map<String, String>titleLyricsMap;
	private Map<String, Double> idf; 
	/**
	 * By - Aamna D.
	 */
		
	public static Map<String, String>readFiles(String filePath) {
		/**
		 * Reads the songg_lyrics tsv file 
		 */
		
		HashMap<String, String> songLyricsMap = new HashMap<String, String>();
		Path path = Paths.get(filePath);

		
		try (Scanner scn = new Scanner(path)) {

			if (scn.hasNextLine()) {
				scn.nextLine();
			}

			while (scn.hasNextLine()) {
				String line = scn.nextLine();
				String[] columns = line.split("\t");

				if (columns.length >= 6) {
					String title = columns[0];
					String lyrics = columns[5];
					songLyricsMap.put(title, lyrics);
				}
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}

		return songLyricsMap;
	}

	// Class activity code
	public static String readFile(String filePath) {
		Path path = Paths.get(filePath);

		try {
			return Files.readString(path).strip();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public Set<String> getSongsOrdered() {
		return new TreeSet<String>(this.titleLyricsMap.keySet());
	}

	// Search engine
	private Map<String, Integer> getTermFrequency(String input) {

		
		String cleanStr = input.toLowerCase().replaceAll("[^a-z0-9 ]", "");
		String[] words = cleanStr.split("\\s+");

		Map<String, Integer> frequencyMap = new HashMap<>();
		for (String word : words) {
			frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
			//getOrDefault built-in method for a HashMap prevents the code form crashing when see it for the first time
		}
		return frequencyMap;
	}

	private Map<String, Map<String, Integer>> calculateTF() {
		Map<String, Map<String, Integer>> tf = new HashMap<>();

		for (String songTitle : titleLyricsMap.keySet()) {
			String lyrics = titleLyricsMap.get(songTitle);
			Map<String, Integer> currentTF = getTermFrequency(lyrics);
			tf.put(songTitle, currentTF);
		}
		return tf;
	}

	Map<String, Map<String, Integer>> tf;

	//Constructor
	public SearchEngine(String directoryPath) {
		this.titleLyricsMap = SearchEngine.readFiles(directoryPath);
		this.tf = calculateTF();
		this.idf = computeIDF(); 
	}

	
	private Map<String, Double> computeIDF() {

		Map<String, Double> idf = new HashMap<>();
		Map<String, Integer> docFrequency = new HashMap<>();

		int N = titleLyricsMap.size();

		for (String lyrics : titleLyricsMap.values()) {

			String clean = lyrics.toLowerCase().replaceAll("[^a-z0-9 ]", "");
			String[] words = clean.split("\\s+");

			Set<String> uniqueWords = new HashSet<>(Arrays.asList(words));

			for (String word : uniqueWords) {
				docFrequency.put(word, docFrequency.getOrDefault(word, 0) + 1);
			}
		}

		for (String word : docFrequency.keySet()) {
			double df = docFrequency.get(word);
			idf.put(word, (double) N / df);
		}

		return idf;
	}

	// Search Method
	public List<String> search(String query) {

		
		String cleanStr = query.toLowerCase().replaceAll("[^a-z0-9 ]", "");
		String[] queryTerms = cleanStr.split("\\s+");
		//String[]queryTerms=query.split(" ");
		
		Map<String, Integer> songScore = new HashMap<>();

		for (String song : tf.keySet()) {
			int score = 0;
			Map<String, Integer> currentTF = tf.get(song);

			for (String queryTerm : queryTerms) {
				if (currentTF.containsKey(queryTerm)) {
					score += currentTF.get(queryTerm); 
				}
			}
			songScore.put(song, score);
		}

		return topK(songScore, 3);
	}

	// Top 3
	private List<String> topK(Map<String, Integer> songScores, int k) {

		List<String> top = new ArrayList<>();

		while (k > 0 && !songScores.isEmpty()) {

			int maxScore = -1; //max revelce score
			String maxSong = ""; //Keep maxsong title

			for (String song : songScores.keySet()) {
				int currentScore = songScores.get(song);

				// FIXED: removed bad condition
				if (currentScore > maxScore) {
					maxSong = song;
					maxScore = currentScore;
				}
			}

			top.add(maxSong);
			songScores.remove(maxSong);
			k--;
		}

		return top;
		//end of class activity code
	}

	/**
	 * 
	 * IDF for project
	 */
	public Map<String, Double> searchIDF(String query) {

		String cleanStr = query.toLowerCase().replaceAll("[^a-z0-9 ]", "");
		String[] queryTerms = cleanStr.split("\\s+");

		Map<String, Double> songScoreIDF = new HashMap<>();

		for (String song : tf.keySet()) {

			double currentSongScore = 0.0;
			Map<String, Integer> currentTF = tf.get(song);

			for (String queryTerm : queryTerms) {

				if (currentTF.containsKey(queryTerm)) { 

					int terms = currentTF.get(queryTerm);
					double idfScore = idf.getOrDefault(queryTerm, 0.0);

					double score = terms * idfScore;
					//Relevance score based on tf and idf


					
					if (song.toLowerCase().contains(queryTerm)) {
						score *= 5.0;
						// Score Enhancement if term is in title by 5 importance

					}

					currentSongScore += score; 
				}
			}

			songScoreIDF.put(song, currentSongScore);
		}

		return songScoreIDF;
	}

	public static void main(String[] args) {
		/**
		 * Testing File Reader
		 */


		String filePath = "/Users/aamnadhamdachhawala/Downloads/song_lyrics.tsv"; 

		Map<String, String> songlyricMap = SearchEngine.readFiles(filePath);
		System.out.println(songlyricMap.keySet());

		SearchEngine fileSongs = new SearchEngine(filePath);
		System.out.println(fileSongs.searchIDF("happy"));
	}
}

