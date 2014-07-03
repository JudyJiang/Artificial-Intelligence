import java.util.*;

public class Sentence {
	int premice_count = 0;
	Symbol infered;
	public LinkedList<Symbol> symbols;
	public LinkedList<String> variables;
	
	public Sentence(){
		symbols = new LinkedList<Symbol>();
		infered = null;
	}
}
