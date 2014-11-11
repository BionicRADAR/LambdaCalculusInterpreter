package interpreter;



public class ExpressionParser {
	
	public static final char LAMBDA = 955;

	public Expression parse(String toParse) {
		toParse = toParse.trim();
		if (toParse.charAt(0) == '(') { //It is an application
			toParse = toParse.substring(1, toParse.length() - 1).trim();
			int split = findSplit(toParse);
			Expression exp1 = parse(toParse.substring(0, split));
			Expression exp2 = parse(toParse.substring(split + 1));
			return new Application(exp1, exp2);
		}
		if (toParse.startsWith("fn") || toParse.charAt(0) == LAMBDA) { //It is an abstraction				
			int split = toParse.indexOf('.');
			String param = toParse.substring(1, split);
			if (toParse.charAt(0) != LAMBDA)
				param = toParse.substring(2, split);
			Expression exp = parse(toParse.substring(split + 1));
			return new Abstraction(param, exp);
		}
		//Otherwise it's a variable
		return new VariableHolder(new Variable(toParse));
	}
	
	private int findSplit(String appl) {
		int parens = 0;
		for (int i = 0; i < appl.length(); i++) {
			if (appl.charAt(i) == ' ' && parens == 0)
				return i;
			if (appl.charAt(i) == '(')
				parens++;
			if (appl.charAt(i) == ')')
				parens--;
		}
		return -1;
	}
}
