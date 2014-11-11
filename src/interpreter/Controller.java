package interpreter;

import java.util.HashMap;
import java.util.Map;


public class Controller {

	private static Controller controller = new Controller();
	
	private Map<String, Expression> defs;
	
	private Controller() {
		defs = new HashMap<String, Expression>();
	}
	
	public static Controller getInstance() { return controller; }
	
	public String interpret(String expression) {
		int eqLoc = expression.indexOf('=');
		String defName = "";
		if (eqLoc != -1) {
			defName = expression.substring(0, eqLoc).trim();
			if (defName.matches("[^a-zA-Z0-9]"))
				return "Invalid receiever: " + defName;
			expression = expression.substring(eqLoc + 1);
		}
		Expression expr = null;
		try {
			expr = new ExpressionParser().parse(expression);
		} catch (Exception e) {
			e.printStackTrace();
			return "Parsing error. Invalid input string.";
		}
		
		if (!(new CorrectnessVisitor().check(expr)))
			return "Invalid Expression: static analysis failed";
		
		expr = new InterpreterVisitor().evaluate(expr);
		if (defName != "")
			defs.put(defName, expr);
		return new PrinterVisitor().print(expr);
	}
	
	public Map<String, Expression> getDefs() {
		return defs;
	}
	
}
