#Search Engine Project

Overview:
This project implements a command-line search engine using the TF-IDF retrieval model. The system allows users to search for songs based on keywords and returns the most relevant results.

System Design:
The system is divided into multiple components:
- SearchEngine (Aamna): Handles TF calculation, IDF calculation, and scoring
- UserInterface (Adah): Provides a menu-based interface for users to search and interact
- SearchSimulation(Adah): Measures performance and evaluates results

Features:
- TF-IDF based ranking system
- Menu-based user interface using switch statements
- Allows users to enter multiple queries until they exit
- Displays top 5 most relevant songs
- Tracks user search history
- Measures execution time for building and searching
- Calculates precision@5 for evaluation

Enhancement:
(Aamna)
To improve search quality, we implemented a title-based boosting technique. If a query term appears in a song title, its score is increased. This improves relevance because titles often better represent the content of the song.
(Adah)
Enhancement: To improve the quality of search results, we implemented a score normalization technique. After computing TF-IDF scores, each song's score is divided by the number of words in its title. This reduces bias toward longer songs or titles that may naturally contain more words. This enhancement ensures that shorter, more relevant songs are not unfairly ranked lower simply because they contain fewer words.

Example:
For a query such as "love", longer song titles or songs with more words may originally receive higher scores. After normalization, songs with concise and relevant titles are ranked more fairly.

This improves the overall relevance and fairness of the search results.

Design Choices:
- Used HashMaps for efficient lookup of TF and IDF values
- Used a menu-driven interface for usability
- Applied enhancement outside the core engine to avoid modifying backend logic

How to Run:
1. Run SearchSimulation.java
2. View performance results in the console
3. Use the interactive menu to search songs

Conclusion:
The system efficiently retrieves relevant songs and demonstrates how TF-IDF can be applied in a real-world search engine.