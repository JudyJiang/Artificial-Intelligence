import java.util.*;
import java.io.*;

public class sokoban {
	Solve solve;
	ArrayList<String> mapData;
	Board board;
	//Solution result
 	
	/*
	 * The program read the input file and make it the string contents 
	 * for further "Board initialization use"
	 */
	public sokoban(String filename){
		mapData = new ArrayList<String>();
		if(filename == null)
			return;
		
		File inputFile = new File(filename);
		if(!inputFile.exists()){
			System.out.println("File doesn't exist");
			return;
		}
		
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(inputFile)));
			String line = null;
			try{
				while((line = br.readLine()) != null)
					mapData.add(line);
				
			}catch (IOException e){
				e.printStackTrace();
			}
			
			}catch (FileNotFoundException e){
				e.printStackTrace();
			
		}
		
	}
	
	
	/*
	 * Read the file name input Board data and do further things such as keep
	 * record of goals, keeper, walls and boxes positions
	 */
	public void initialBoard(){
		int row = Integer.parseInt(mapData.get(0));
		int column = 0;
		for(String str: mapData){
			if(str.length() > column)
				column = str.length();
		}
		board = new Board(row, column, mapData);
	}
	
	/*
	 * The real solve procedure, return the solution string and the records of statistics data
	 */
	public void solve(int algorithm_method, int heuristic_method, String sta_options) throws Exception{
		initialBoard();
		int method = algorithm_method;
		String heuristic = "";
		boolean statistics = false;
		int[] node_statistics = new int[4];
		
		if(heuristic_method == 1)
			heuristic = "heu1";
		else if(heuristic_method == 2)
			heuristic = "heu2";
		if(sta_options.equals("y"))
			statistics = true;
		else if(sta_options.equals("n"))
			statistics = false;
		
		solve = new Solve(board, method, heuristic);
		String solution = solve.begin();
		System.out.println("Here is the solution: " + solution +"\n");
		if(statistics)
			solve.printStatistics();
		
	}
	
	/*
	 * The main entrance, give user input options to choose algorithm,
	 * to choose heuristic methods and whether or not to show the statistics
	 */
	public static void main(String[] args) throws Exception{
		String filename = args[0];
		sokoban game = new sokoban(filename);
		int algorithm_method = -1;
		int heuristic_method = -1;
		boolean statistics = false;
		String sta_options = "";
		System.out.println("Sokoban Agent");
		System.out.println("0 to exit;\n" + 
                "1 For BFS Solver\n" + 
		           "2 For DFS Solver\n" + 
                "3 For Uniform Cost Solver\n" + 
		           "4 For Greedy Best First Solver\n" + 
                "5 For A* Solver\n");
        Scanner in = new Scanner(System.in);
        algorithm_method = in.nextInt();
        while(algorithm_method > 5 || algorithm_method < 0){
        	System.out.println("Sokoban Solver method should between 1 and 5; Enter 0 to exit");
        	algorithm_method = in.nextInt();
        }
		if(algorithm_method == 0){
			System.out.println("End of Program");
			System.exit(0);
		}
		else if(algorithm_method == 4 || algorithm_method == 5){
			System.out.println("Choose a heuristic method: " +
					           "1 for Heuristic One and " +
					           "2 for Heuristic Two");
			heuristic_method = in.nextInt();
			while(heuristic_method != 1 && heuristic_method != 2){
				System.out.println("Heuristic method only 2 options, enter 1 or 2");
				heuristic_method = in.nextInt();
			}
		}
		
		
		sta_options = in.nextLine().toLowerCase();
		while(!sta_options.equals("y") && !sta_options.equals("n")){
			System.out.println("Show Statistics, enter Y for yes; N for No");
			sta_options = in.nextLine().toLowerCase();
		}
		
		while(algorithm_method != 0){
			game.solve(algorithm_method, heuristic_method, sta_options);
			System.out.println("0 to exit;\n" + 
		                       "1 For BFS Solver\n" + 
					           "2 For DFS Solver\n" + 
		                       "3 For Uniform Cost Solver\n" + 
					           "4 For Greedy Best First Solver\n" + 
		                       "5 For A* Solver\n");
			in = new Scanner(System.in);
			algorithm_method = in.nextInt();
	        while(algorithm_method > 5 || algorithm_method < 0){
	        	System.out.println("Sokoban Solver method should between 1 and 5; Enter 0 to exit");
	        	algorithm_method = in.nextInt();
	        }
			if(algorithm_method == 0){
				System.out.println("End of Program");
				System.exit(0);
			}
			else if(algorithm_method == 4 || algorithm_method == 5){
				System.out.println("Choose a heuristic method: " +
						           "1 for Heuristic One and " +
						           "2 for Heuristic Two");
				heuristic_method = in.nextInt();
				while(heuristic_method != 1 && heuristic_method != 2){
					System.out.println("Heuristic method only 2 options, enter 1 or 2");
					heuristic_method = in.nextInt();
				}
			}
			
			
			sta_options = in.nextLine().toLowerCase();
			while(!sta_options.equals("y") && !sta_options.equals("n")){
				System.out.println("Show Statistics, enter Y for yes; N for No");
				sta_options = in.nextLine().toLowerCase();
			}
		}
		
	}
	
}
