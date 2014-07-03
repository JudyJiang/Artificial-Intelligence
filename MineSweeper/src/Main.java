public class Main {
	private static void test(int i,int j) {
		Ready ready=new Ready(null);
		ready.toShow();
		if(i>20){
			i=20;
		}
		if(i<3){
			i=3;
		}
		Mine model=new Mine(i,j);
		Controller controller=new Controller(model,i,j);
		ready.setCon(controller);
	}
	public static void main(String[] args) {
		test(18, 30);
	}
}
