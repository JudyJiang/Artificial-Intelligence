import java.util.*;

public class Node{
	public static long start = 0;
	public static long end = 0;
	
	public static long period = 0;
	
	public static void main(String[] args) throws InterruptedException{
		start = System.currentTimeMillis();
		period = 1000; //1000 1s.
		end = start + period;
		
		//long current = System.currentTimeMillis();
		while(start < end){
			start = System.currentTimeMillis();
		}
		System.out.println("End");
	}
}