package searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class SearchEngine {
	
	private Map<String, String>titleLyricsMap;
	/**
	 * By - Aamna D.
	 */
		
	public static Map<String, String>readFiles(String filePath) {
		/**
		 * Reads the songg_lyrics tsv file 
		 */
		
		HashMap<String, String> songLyricsMap = new HashMap <String, String>();
		
		Path path = Paths.get(filePath);
		
	
			try (Scanner scn = new Scanner (filePath)){
				
				if (scn.hasNextLine()) {
				scn.nextLine();
				}
				while(scn.hasNextLine()) {
					String line = scn.nextLine();
					String[] columns = line.split("\t");
				
					if (columns.length >= 6) {
						String title = columns[0];
						String lyrics = columns[5];
					
						songLyricsMap.put(title,lyrics);
				}
				
			} 
		}
		
		return songLyricsMap;
	}
	
	// Class activity code
	public static String readFile(String filePath) {
		
		Path path = Paths.get(filePath);
		
		try {
			return Files.readString(path).strip();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		return"";
	}
	
	
	public Set<String> getSongsOrdered() {
		
		return new TreeSet<String>(this.titleLyricsMap.keySet());
		
	}
	
	// Search engine
	private Map<String, Integer> getTermFrequency(String input) {
		
		String cleanStr = input.toLowerCase().replaceAll("[^a-z0-9]","");
		String[] words = cleanStr.split("\\s+");
		
		Map<String, Integer> frequencyMap = new HashMap<>();
		for(String word : words) {
			frequencyMap.put(word,frequencyMap.getOrDefault(word,0)+ 1);  //getOrDefault built-in method for a HashMap prevents the code form crashing when see it for the first time
		}
		return frequencyMap;
		
		}
	
		private Map<String,Map<String,Integer>>calculateTF(){
			Map<String,Map<String,Integer>>tf = new HashMap<String,Map<String,Integer>>();
			
			for(String songTitle:titleLyricsMap.keySet()) {
				String lyrics = titleLyricsMap.get(songTitle);
				Map<String,Integer>currentTF = getTermFrequency(lyrics);
				tf.put(songTitle,currentTF);
			}
			
			return tf;
		}
		
		Map<String,Map<String,Integer>>tf;
		public SearchEngine(String directoryPath) {
			this.titleLyricsMap = SearchEngine.readFiles(directoryPath); 		//Constructor
			this.tf = calculateTF();
		}
		
		
		
		//Search Method
		public List<String> search(String query) {
			String cleanStr = query.toLowerCase().replaceAll("[^a-z0-9]","");
			String[] queryTerms = cleanStr.split("\\s+");
			//String[]queryTerms=query.split(" ");
		
			Map <String,Integer>songScore = new HashMap <String,Integer>();
			
			for(String song : tf.keySet()) {
				int score = 0;
				Map<String,Integer>currentTF = tf.get(song);
				for(String queryTerm: queryTerms) {
					
					if(currentTF.containsKey(queryTerm))
						score+=currentTF.get(queryTerm);
						songScore.put(song,score); 
				}
				
			  }
			return topK(songScore,3);
			}	
			

		
		//Top 3
				
				private List<String>topK(Map<String,Integer>songScores,int k) {
					
					List<String>top = new ArrayList<String>();
					while(k>0) {
						int maxScore =-1; // max revelce score
						String maxSong = ""; // Keep maxsong title
						for(String Song: songScores.keySet()) {
							int currentScore = songScores.get(Song);
							if (currentScore > songScores.get(Song));
							if (currentScore > maxScore) {
								maxSong = Song;
								maxScore = currentScore;
							
							}
							
						}
						top.add(maxSong);
						songScores.remove(maxSong);
						k --;
					}
					return top;
				}		// end of class activity code
				
	/**
	 * IDF for project
	 */
	private Map<String, Double>idf;
	public Map<String,Double>getTopResults(String query, int frequncy){
		//Sets up the maps and variable N as the counter for total number of songs
		
		Map<String,Double>idf = new HashMap<String,Double>();
		Map<String,Integer>DocFrequency = new HashMap<String,Integer>();
		
		int N = titleLyricsMap.size(); // number of songs
		
		
		for(String lyricsIDF : titleLyricsMap.values()) {
			
			Set<String>WordCount = new HashSet<String>();
			
			for(String word : WordCount) {
				DocFrequency.put(word,DocFrequency.getOrDefault(word,0) + 1);
			}
			
		// Calculates the score for idf
			
			for(String word : DocFrequency.keySet()) {
				double df = DocFrequency.get(word);
				idf.put(word, N/df); // N/Number of document having that term
			}
		}
		return idf;
	}
	
	/**
	 * Search Method for IDF 
	 */
	public Map<String, Double> searchIDF(String query) {
		String cleanStr = query.toLowerCase().replaceAll("[^a-z0-9]",""); //Regex to clean the query
		String[] queryTerms = cleanStr.split("\\s+");
		
		
		Map <String,Double>SongScoreIDF = new HashMap <String,Double>();
		
		for(String SongDoc : tf.keySet()) {
		
			double currentSongScore = 0.0;
			int terms = 0;
			
			Map<String,Integer>getTopResults = tf.get(SongDoc);
			
			for(String queryTerm: queryTerms) {
				
				if(getTopResults.containsKey(queryTerm))
					
					terms = getTopResults.get(queryTerm);
					double idfScore = idf.getOrDefault(queryTerm,0.0);
					
					double ScoreEnchancementByWeight = terms * idfScore;
					if(SongDoc.contains(queryTerm)) {		// Score Enhancement if term is in title by 5 importance
						ScoreEnchancementByWeight = ScoreEnchancementByWeight* 5.0;
					}
				
					currentSongScore += (terms * idfScore);  //Relevance score based on tf and idf
					SongScoreIDF.put(SongDoc,currentSongScore); 
			}
			
		}
		return SongScoreIDF;
	}	


		public static void main(String[] args) {
			/**
			 * Testing File Reader 
			 */

			String filePath ="/Users/aamnadhamdachhawala/Downloads/song_lyrics.tsv";
			
			Map<String,String> songlyricMap = SearchEngine.readFiles(filePath); 		
			System.out.println(songlyricMap.keySet());
			
			SearchEngine FileSongs = new SearchEngine(filePath);
			System.out.println(FileSongs.searchIDF("happy").toString());
			

			
		}

	}
	


