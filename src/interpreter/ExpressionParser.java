package interpreter;

import java.text.ParseException;




public class ExpressionParser {
	
	private String errors;
	
	public ExpressionParser() {
		errors = "";
	}
	
	public static final char LAMBDA = 955;

	public Expression parse(String toParse) throws ParseException {
		errors = "";
		toParse = toParse.trim();
		if (toParse.charAt(0) == '(') { //It is an application
			toParse = toParse.substring(1, toParse.length() - 1).trim();
			int split = findSplit(toParse);
			Expression exp1 = parse(toParse.substring(0, split));
			Expression exp2 = parse(toParse.substring(split + 1));
			return new Application(exp1, exp2);
		}
		if (toParse.startsWith("fn") || toParse.charAt(0) == LAMBDA || toParse.charAt(0) == ';') { //It is an abstraction				
			int split = toParse.indexOf('.');
			String param = toParse.substring(1, split);
			if (toParse.charAt(0) != LAMBDA && toParse.charAt(0) != ';')
				param = toParse.substring(2, split);
			if (!param.matches("\\w*")) {
				throw new ParseException("Bad variable name: " + param, 0);
			}
			Expression exp = parse(toParse.substring(split + 1));
			return new Abstraction(param, exp);
		}
		//Otherwise it's a variable (unless it's an error)
		if (!toParse.matches("\\w*")) {
			throw new ParseException("Bad variable name or expression: " + toParse, 0);
		}
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
	
	public String getErrors() {
		return errors;
	}
}
