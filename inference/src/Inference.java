import java.io.*;
import java.util.*;

public class Inference {
	Scanner in;
	String algorithm;
	String kb;
	String[] sentences;
	String query;
	LinkedList<Symbol> agenda;
	LinkedList<Sentence> Sentences;
	Hashtable<Symbol, Boolean> infered;

	
	Inference(String method, String file, String q){
		in = new Scanner(System.in);
		System.out.println("Parameters in: method <forward, backward>; KB file; Query Symbol");
		algorithm = method;
		query = q;
		kb = file;
		readFile();
	}
	
	public void infer(){
		ArrayList<String> rawSentences = new ArrayList<String>();
		rawSentences = removeQuotes();//rawSentences is the list contains all the facts and rules.
		
		
		for(int i = 0; i < rawSentences.size(); i++){
			if(rawSentences.get(i).contains("V"))
				rawSentences.set(i, convert2(rawSentences.get(i)));
		}
		
		
		construct(rawSentences);
		
		/*for(Symbol str: agenda)
			System.out.println("Facts are " + str.sym);
		
		Set<Symbol> keys = infered.keySet();
		for(Symbol s: keys)
			System.out.print(s.sym + " ");
		System.out.println();
		for(Sentence sen: Sentences){
			System.out.print("Symbols are " );
			for(Symbol sym: sen.symbols)
				System.out.print(sym.sym + " ");
			System.out.println("Infered is " + sen.infered.sym);
			System.out.println();
		}*/
		
		if(algorithm.equals("forward")){
			if(forward()){
				System.out.println("-->True");
			}
			else
				System.out.println("-->False");
		}
		if(algorithm.equals("backward")){
			if(backward()){
				System.out.println("-->True");
			}
			else{
				System.out.println("-->False");
			}
		}
	}
	
	/* convert all sentences to implication form*/
	public String convert2(String raw){
		//System.out.println(raw);
		String str = "";
		String pos = "";
		String[] strlist = raw.split("V");
		
		for(String s: strlist){
			if(!s.contains("~")){
				pos = s;
				continue;
			}
			else{
				int j;
				for(j = 0; j < s.length(); j++)
					if(s.charAt(j) == '~')
						break;
				
				str += s.substring(j + 1) + "^";
			}
		}
		if(pos.equals("")){
			str = str.substring(0, str.length() - 1);
			int j;
			for(j = str.length() - 1; j >= 0; j--)
				if(str.charAt(j) == '^')
					break;
			str = str.substring(0, j) + "=>" + "~" + str.substring(j + 1, str.length());
		}
		else{
			str = str.substring(0, str.length() - 1) + "=>" + pos;
		}
		System.out.println(str);
		return str;
	}
	public String convert(String raw){
		//System.out.println(raw);
		String str = "";
		String pos = "";
		for(int i = 0; i < raw.length(); i++){
			char c = raw.charAt(i);
			if(c == ' ')
				continue;
			if(c != 'V' && c != ' '){
				if((i == 0 && c != '~') || (i >= 1 && c != '~' && raw.charAt(i - 1) != '~')){
					int j;
					for(j = i; j < raw.length(); j++){
						if(raw.charAt(j) == 'V' || raw.charAt(j) == '~')
							break;
					}
					
					pos = raw.substring(i, j);
				}
				if(c == '~'){
					int j;
					for(j = i; j < raw.length(); j++){
						if(raw.charAt(j) == ' ' || raw.charAt(j) == 'V')
							break;
					}
					
					str += raw.substring(i + 1, j) + "^";
				}
			}
		}
		
		if(pos.equals("")){
			int j, k;
			str = str.substring(0, str.length() - 1);
			for(j = str.length() - 1; j >= 0; j--){
				if(str.charAt(j) == '^'){
					pos = "~" + str.substring(j + 1, str.length());
					break;
				}
			}
			for(k = j; k >= 0; k--){
				if(str.charAt(k) == '^')
					break;
			}
			
			str = str.substring(0, k) + "=>" + pos;
		}
		
		else{
			int j;
			for(j = str.length() - 1; j>= 0; j--){
				if(str.charAt(j) == '^'){
					break;
				}
			}
			str = str.substring(0, j) + "=>" + pos;
			
		}
		System.out.println(str);
		return str;
	}
	
	public boolean forward(){
		while(!agenda.isEmpty()){
			Symbol p = agenda.pop();
			//System.out.println("This time pop " + p.sym + " ");
			
			if(infered.containsKey(p) && (!infered.get(p))){
				//System.out.println(p.sym + "in loop");
				infered.put(p, false);
				
				for(Sentence sen: Sentences){
					for(Symbol symbol: sen.symbols){
						if(p.sym.replaceAll("\\s", "").equals(symbol.sym.replaceAll("\\s", "")) && p.value == symbol.value){
							//System.out.println("Equal...");
							sen.premice_count--;
							if(sen.premice_count == 0){
								printProcedure(sen);
						    	//System.out.println("Equals zero " + sen.infered.sym + " " + query);
								if(sen.infered.sym.equals(query)){
									//System.out.println("should return.....");
									return true;
								}
								else{
									agenda.add(sen.infered);
									infered.put(sen.infered, false);
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean backward(){
		System.out.println();
		LinkedList<Symbol> proved = new LinkedList<Symbol>();
		proved = agenda;
		
		Symbol s;
		if(query.contains("~")){
			s = new Symbol(query.substring(1));
			s.setValue(false);
		}
		else{
			s = new Symbol(query);
		}
		
		boolean result = prove(s, proved);
		return result;
	}
	
	public boolean prove(Symbol q, LinkedList<Symbol> proved){
		System.out.println("To prove " + q.sym);
		for(Sentence sen: Sentences){
			boolean inferedProved = false;
			if(sen.infered.sym.equals(q.sym) && sen.infered.value == q.value){
				
				printProcedure(sen);
				LinkedList<Symbol> list = toProve(sen, proved);
				if(list == null || list.size() == 0){//all premice proved
					proved.add(sen.infered);
					return true;
				}
				else{
					for(Symbol sol: list){
						inferedProved = prove(sol, proved);
						if(inferedProved)
							proved.add(sol);
						//else
							//return false;
					}
					return true;
				}
			}
		}
		return false;
	}
	
	
	public LinkedList<Symbol> toProve(Sentence sen, LinkedList<Symbol> proved){
		boolean add = true;
		LinkedList<Symbol> list = new LinkedList<Symbol>();
		for(Symbol sol: sen.symbols){
			//System.out.println("match symbol is " + sol.sym);
			for(Symbol sol2: proved){
				if(sol.sym.equals(sol2.sym) && sol.value == sol2.value){
					add = false; 
					break;
				}
			}
			if(add){
				list.add(sol);
			}
		}
		//System.out.println(list.size());
		return list;
	}
	
	public void printProcedure(Sentence sen){
		String s = "";
		for(int i = 0; i < sen.symbols.size(); i++){
			Symbol ss = sen.symbols.get(i);
			if(!ss.value)
				s += "~" + ss.sym;
			else
				s += ss.sym;
			if(i != sen.symbols.size() - 1)
				s += "^";
		}
		
		s += "=>" + sen.infered.sym;
		System.out.println(s);
	}
	/*
	 * This function is to construct all the tables or list if needed(as steps in book)
	 * Sentences which contains a list of symbols of the clause, how many symbols there're (in book count)
	 * If the count is 0 then check if the infered value is reached. 
	 * 
	 * and the infered table
	 * and the agenda, initially all the known to true values. 
	 */
	public void construct(ArrayList<String> raw){
		agenda = new LinkedList<Symbol>();
		Sentences = new LinkedList<Sentence>();
		infered = new Hashtable<Symbol, Boolean>();
		
		
		for(String str: raw){
			//System.out.println("String list is " + str);
			if(notOperator(str)){
				Symbol s;
				if(str.charAt(0) == '~'){
					//s = new Symbol(String.valueOf(str.charAt(1)));
					s = new Symbol(str.substring(1));
					System.out.println(s.sym);
					s.setValue(false);
				}
				else{
					//s = new Symbol(String.valueOf(str.charAt(0)));
					s = new Symbol(str);
					//System.out.println(s.sym);
				}
				agenda.push(s);
				infered.put(s, false);
			 }
			else{
				//System.out.println("Contains Operator");
				str = str.replaceAll("\\s+", "");
				Sentence sen = new Sentence();
				int count = 0;
				for(int i = 0; i < str.length(); i++){
					char c = str.charAt(i);
					if(c == '>'){
						if(str.charAt(i + 1) == '~'){
							//sen.infered = new Symbol(String.valueOf(str.charAt(i + 2)));
							sen.infered = new Symbol(str.substring(i + 2));
							sen.infered.setValue(false);
						}
						else{
							//sen.infered = new Symbol(String.valueOf(str.charAt(i + 1)));
							sen.infered = new Symbol(str.substring(i + 1));
						}
						break;
					}//It's for each sentence's inferend value P=>Q's Q.
					else if(c == '^' || c == 'V' || c == '=' || c == ' ')
						continue;
					//Each sentence's premice list A^B=>P's A and B
					else if(c == '~' && str.charAt(i - 1) != '>'){
						int j;
						for(j = i; j < str.length(); j++)
							if(str.charAt(j) == '^' || str.charAt(j) == '=' || str.charAt(j) == ' ')
								break;
						//Symbol sym = new Symbol(String.valueOf(str.charAt(i + 1)));
						Symbol sym = new Symbol(str.substring(i + 1, j));
						//System.out.println("Put value " + sym.sym);
						infered.put(sym, false);
						sym.setValue(false);
						sen.symbols.push(sym);
						count++;
					}
					else if(i == 0 || (i - 1 >= 0 && str.charAt(i - 1) != '>')){
						int j;
						for(j = i; j < str.length(); j++)
							if(str.charAt(j) == '^' || str.charAt(j) == '=' || str.charAt(j) == ' ')
								break;
						//Symbol sym = new Symbol(String.valueOf(str.charAt(i)));
						Symbol sym = new Symbol(str.substring(i, j));
						//System.out.println("Put value " + sym.sym);
						infered.put(sym, false);
						sen.symbols.push(sym);
						count++;
						i = j;
					}
				}
				sen.premice_count = count;
				Sentences.add(sen);
			}
		}
	}
	
	public boolean notOperator(String str){
		return (!str.contains("^") && !str.contains("V") && !str.contains("=>"));
	}
	
	
	/*
	 * This function + removal 
	 * is to remove all the parents within a horn form
	 * So, for case as: (P => Q) ^ (L ^ M => P) ^ A
	 * will return P=>Q, L^M=>P and A.
	 */
	public ArrayList<String> removeQuotes(){
		ArrayList<String> process = new ArrayList<String>();
		LinkedList<String> q = new LinkedList<String>();
		for(String str: sentences){
			if(str.contains("(")){
				q.clear();
				q = removal(str);
				for(String sstr: q){
					process.add(sstr);
				}
			}
			else
				process.add(str);
		}
		return process;
	}
	
	
	public LinkedList<String> removal(String str){
		String temp = "";
		int end = 0;
		LinkedList<String> q = new LinkedList<String>();
		for(int i = 0; i < str.length(); i++){
			char c = str.charAt(i);
			if(c == ' ')
				continue;
			else if(c != '(' && c != ')'){
				if(end == 0){
					if(c != '^'){
						temp = String.valueOf(c);
						q.push(temp);
						temp = "";
					}
				}
				else if(end != 0){
					temp += c;
				}
			}
			else if(c == '('){
				end++;
				continue;
			}
			
			else if(c == ')'){
				end--;
				if(end == 0){
					q.push(temp);
					temp = "";
					continue;
				}
			}
		}
		return q;
	}
	
	public void readFile(){
		try {
			String line = "";
			String raw = "";
			int count = 0;
			BufferedReader br = new BufferedReader(new FileReader(new File(kb)));
			while((line = br.readLine()) != null){
				raw += line + "#";
			}
			sentences = raw.split("#");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		String method = args[0];
		String file = args[1];
		String query = args[2];
		Inference run = new Inference(method, file, query);
		run.infer();
	}
}
