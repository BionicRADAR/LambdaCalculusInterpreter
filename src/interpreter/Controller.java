package interpreter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

	private static Controller controller = new Controller();
	
	private Map<String, Expression> defs;
	
	private static final char ALPHA = (char) 945;
	
	private static final String HELPTEXT = "This is a lambda calculus interpreter.\n" +
			"Type a '" + (char) 955 + "' with the ';' (semicolon) key.\n" +
			"All applications must be parenthesized, as in (a b).\n" +
			"You can define a variable by placing its (alphanumeric) name left of\n" +
			"an expression, followed by an equal sign, as in var = expression.\n" +
			"All other variables must be bound (ie, placed in an abstraction).";
	
	private Controller() {
		defs = new HashMap<String, Expression>();
	}
	
	public static Controller getInstance() { return controller; }
	
	public String interpret(String expression) {
		if (expression.equals("exit"))
			System.exit(0);
		if (expression.equals(""))
			return "";
		if (expression.equals("help"))
			return HELPTEXT;
		String toReturn = "";
		int eqLoc = expression.indexOf('=');
		String defName = "";
		if (eqLoc != -1) {
			defName = expression.substring(0, eqLoc).trim();
			if (!defName.matches("\\w*"))
				return "Invalid receiever: " + defName;
			expression = expression.substring(eqLoc + 1);
		}
		Expression expr = null;
		try {
			expr = new ExpressionParser().parse(expression);
		} catch (ParseException e) {
			return e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "Parsing error";
		}
		
		DefReplacementVisitor drv = new DefReplacementVisitor(defs);
		drv.visit(expr);
		if (!drv.getErrors().equals("")) {
			return drv.getErrors();
		}
		
		CorrectnessVisitor cv = new CorrectnessVisitor();
		if (!(cv.check(expr))) 
			return cv.getErrors() + "Invalid Expression: static analysis failed";
		
		PrinterVisitor printer = new PrinterVisitor();
		new AlphaConversionVisitor().visit(expr);
		toReturn += ALPHA + ":" + printer.print(expr) + "\n";
		try {
			expr = new InterpreterVisitor().evaluate(expr);
		} catch (StackOverflowError e) {
			return "Error: infinite recursion";
		}
		if (defName != "")
			defs.put(defName, expr);
		return toReturn + printer.print(expr);
	}

	public Map<String, Expression> getDefs() {
		return defs;
	}
	
}
