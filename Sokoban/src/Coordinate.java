
public class Coordinate {
	public int x;
	public int y;
	public int cost = 0;
	public int steps = 0;
	
	public Coordinate(int _x, int _y){
		x = _x; 
		y = _y;
	}
	
	public int getCost(){
		return cost;
	}
	
	public void setCost(int new_cost){
		cost = new_cost;
	}
	
	public int getSteps(){
		return steps;
	}
	
	public void setSteps(int new_step){
		steps = new_step;
	}
}
