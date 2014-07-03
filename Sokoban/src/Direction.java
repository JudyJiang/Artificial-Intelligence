
public class Direction{
	Coordinate direction = new Coordinate(0, 0);
	char[] moveType = new char[2];
	Direction[] dirs = new Direction[4];
	
	public Direction(){
	}
	
	public Direction[] getDirections(){
		Direction[] dirs = new Direction[4];
		dirs[0] = new Direction();
		dirs[0].direction = new Coordinate(-1, 0);
		dirs[0].moveType[0] = 'u';
		dirs[0].moveType[1] = 'U';
		
		dirs[1] = new Direction();
		dirs[1].direction = new Coordinate(1, 0);
		dirs[1].moveType[0] = 'd';
		dirs[1].moveType[1] = 'D';
		
		dirs[2] = new Direction();
		dirs[2].direction = new Coordinate(0, -1);
		dirs[2].moveType[0] = 'l';
		dirs[2].moveType[1] = 'L';
		
		dirs[3] = new Direction();
		dirs[3].direction = new Coordinate(0, 1);
		dirs[3].moveType[0] = 'r';
		dirs[3].moveType[1] = 'R';
		
		return dirs;
	}
}
