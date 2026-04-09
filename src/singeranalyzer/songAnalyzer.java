package singeranalyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class songAnalyzer {

	public static String readFile(String filePath)
	{
		Path path = Paths.get(filePath);
		try {
			return Files.readString(path).strip();
		
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		return"";
	}
	
//Constructor
	Map<String, Map<String, Integer>>tf;
	private Map<String, String> titleLyricsMap;
	public songAnalyzer(String directorypath)
	{
		this.titleLyricsMap = songAnalyzer.songLyricsmap(directorypath);
		this.tf=getTF();
	}
	
	
	//Map
	public  static Map<String,String> songLyricsmap(String directoryPath)
	{
		HashMap<String, String> songLyricsMap = new HashMap<String, String>();
		File directory = new File(directoryPath);
		File[]files = directory.listFiles();
		for(File file: files)
		{
			if(file.isFile())
				{
					String fileName = file.getName();
					String title = fileName.split("\\.")[0];
					String lyrics = SingerAnalyzer.readFile(file.getAbsolutePath());
					songLyricsMap.put(title,lyrics);
				}
		}
		
		return songLyricsMap;
	}
	
	//Method
	public Set <String> getSongsOrdered()
	{
		return new TreeSet<String>(this.titleLyricsMap.keySet());
	}
	
	//Method
	
	private Map <String, Integer> getTermFrequency(String input)
	{
		/**
		 *Convert to lower case/ Anything that isnt a letter a-z 0-9 we drop it using regex (regular expression)
		 * 
		 */
		
		String cleanStr = input.toLowerCase().replaceAll("[a-z0-9]","");
		/**
		 *This regex chops sentence into an array of individual words/ matches agaisnt whitespace chaarcter(Spaces, tabs, newlines)
		 */
		String[] words = cleanStr.split("\\s+");
		Map<String,Integer>frequencyMap = new HashMap<>();
		for(String word : words)
		{
			/**
			 *Built in for hashMap that prevents your code from crashign when it sees a word for first time (getorDefault)
			 */
			frequencyMap.put(word, frequencyMap.getOrDefault(words, 0)+1);
		}
		return frequencyMap;
	}
	
	private HashMap<String, Map<String, Integer>> getTF()
	{ 
		HashMap<String, Map<String, Integer>> tf = new HashMap<>();
		for(String title : this.titleLyricsMap.keySet())
		{
			String lyrics = titleLyricsMap.get(title);
			Map<String, Integer> currentTF = getTermFrequency(lyrics);
			tf.put(title, currentTF);
		}
		return tf;
	}
	
	/**
	 *Calculate top 3
	 * 
	 */
	private List <String> topK (Map<String, Integer> songScores, int k)
	{
		List <String> songs = new ArrayList<String>();
		while(k>0)
		{
			int maxScore = -1;
			String maxSong = "";
			for(String song: songScores.keySet())
			{
				int currentScore = songScores.get(song);
				if(currentScore>maxScore)
					maxSong = song;
				maxScore = currentScore;
			}
			songs.add(maxSong);
			k--;
		}
		return songs;
	}
	
	
	/**
	 *Search method
	 * 
	 */
	
	public List<String> search (String query)
	{
		String cleanStr =query.toLowerCase().replaceAll("[a-z0-9]","");
		String[] queryTerms = cleanStr.split("\\s+");

//		String[] queryTerms = query.split("");
		Map<String, Integer> songScore = new HashMap <String, Integer>();
		for(String song: tf.keySet())
		{
			int score = 0;
			Map<String, Integer>currentTF = tf.get(song);
			for(String queryTerm : queryTerms)
			{
				if(currentTF.containsKey(queryTerm))
					score += currentTF.get(queryTerm);
				songScore.put(song, score);
			}
			
		}
		return topK(songScore,3);
	}
	
	
}	

