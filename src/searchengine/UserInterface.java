package searchengine;

import java.util.*;

public class UserInterface {
	
	private SearchEngine engine;
	private List<String> userQueries;
	
	public UserInterface(SearchEngine engine)
	{
		this.engine=engine;
		this.userQueries = new ArrayList<>();
	}
	
	public void start()
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter your name: ");
		String name = scanner.nextLine();
		
		System.out.println("Welcome " + name + " to Song Search Engine!");
		
		boolean running = true;
		
		while(true)
		{
			System.out.println("\n-----MENU-----");
			System.out.println("1. Search for a song");
			System.out.println("2. Previous searches");
			System.out.println("3. Exit");
			System.out.println("Choose an option (enter the number): ");
			
			int choice = scanner.nextInt();
			scanner.nextLine();
			
			switch(choice)
			{
			case 1:
			    while (true) {
			        System.out.print("Enter search query (type EXIT to stop searching): ");
			        String query = scanner.nextLine();

			        if (query.equalsIgnoreCase("EXIT")) {
			            break; // exit
			        }

			        userQueries.add(query);

			        List<String> results = engine.getTopResults(query, 5);

			        System.out.println("\nTop Results:");
			        for (String song : results) {
			            System.out.println("- " + song);
			        }
			        System.out.println();
			    }
			    break;
				case 2:
					System.out.println("Past queries: ");
					
					for(String j : userQueries)
					{
						System.out.println("--" + j);
					}
					break;
				case 3:
					running = false;
					break;
				default:
					System.out.println("Invalid Choice. Try again.");	
			}
			
		}

		 System.out.println("\nAll your queries:");
	        for (String q : userQueries) {
	            System.out.println("- " + q);
	        }

	        System.out.println("Goodbye!");
	        scanner.close();
	}

}
