package interpreter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import editor.EditorFrame;
import editor.InterpreterView;

public class Controller {

	private static Controller controller = new Controller();
	
	private Map<String, Expression> defs;
	
	private String newDef;
	
	private int maxReductions = 100000;
	
	private InputRecord ir;
	
	private boolean errorsPresent = false;
	
	private static final char ALPHA = (char) 945;
	
	private static final String HELPTEXT = "This is a lambda calculus interpreter.\n" +
			"Type a '" + (char) 955 + "' with the ';' (semicolon) key.\n" +
			"All applications must be parenthesized, as in (a b).\n" +
			"You can define a variable by placing its (alphanumeric) name left of\n" +
			"an expression, followed by an equal sign, as in var = expression.\n" +
			"All other variables must be bound (ie, placed in an abstraction).\n" +
			"Other special inputs: setMaxReds n, where n is the new maximum\n" +
			"number of reductions for one line of input before it gives up;\n" +
			"editor, which opens an editor for writing and recording lambda calculus programs;\n" +
			"and exit, which closes the whole program.";
	
	private Controller() {
		defs = new HashMap<String, Expression>();
		maxReductions = 100000;
		ir = new InputRecord();
	}
	
	public static Controller getInstance() { return controller; }
	
	/*
	public String cmdLineInterpret(String expression) {
		ir.newInput(expression);
		if (expression.equals("exit"))
			System.exit(0);
		if (expression.equals(""))
			return "";
		if (expression.equals("help"))
			return HELPTEXT;
		if (expression.length() > 10 && expression.substring(0, 10).equals("setMaxReds")) {
			return setMaxReds(expression.substring(10));
		}
		return interpret(expression);
	}
	*/
	
	
	/**
	 * Interprets single-line input from the command-line-like view.
	 * @param The view that contains the input and to which the output will be sent
	 */
	public synchronized void cmdLineInterpret(InterpreterView view) {
		
		String input = view.getInput().getText();
		view.getInput().setText("");
		if (input.equals("")) {
			return;
		}
		if (input.equals("exit"))
			System.exit(0);
		ir.newInput(input);
		view.appendToHistory("> " + input);
		if (input.equals("help")) {
			view.appendToHistory(HELPTEXT);
			return;
		}
		if (input.length() > 10 && input.substring(0, 10).equals("setMaxReds")) {
			view.appendToHistory(setMaxReds(input.substring(10)));
			return;
		}
		if (input.toLowerCase().equals("editor")) {
			new EditorFrame(view).setVisible(true);
			return;
		}
		String result = interpret(input);
		view.appendToHistory(result);
		if (newDef != null) {
			view.addDef(newDef);
		}
	}
	
	public InterpreterView multiLineInterpret(String input) {
		InterpreterView newView = new InterpreterView();
		newView.setVisible(true);
		multiLineInterpret(input, newView);
		return newView;
	}
	
	public synchronized void multiLineInterpret(String input, InterpreterView view) {
		view.getInput().setEnabled(false);
		String[] lines = input.split("\n");
		String lastResult = "";
		int lineno = 0;
		for (String line: lines) {
			line = line.trim();
			if (line.equals(""))
				continue;
			if (input.length() > 10 && input.substring(0, 10).equals("setMaxReds")) {
				view.appendToHistory(setMaxReds(input.substring(10)));
			}
			lastResult = interpret(line);
			if (errorsPresent) {
				view.appendToHistory("Error in line " + lineno + ":\n" + lastResult);
				return;
			}
			if (newDef != null) {
				view.addDef(newDef);
			}
			lineno++;
		}
		view.appendToHistory(lastResult);
		view.getInput().setEnabled(true);
	}
	
	/**
	 * Essentially, interprets the string input as a lambda expression,
	 * outputting the result to the view and potentially storing the resulting
	 * Expression in the defs map.
	 *  
	 * @param A string representing the lambda expression to be interpreted
	 * @return A string containing the results of the computation or an error message
	 */
	public String interpret(String expression) {
		String toReturn = ""; //contains the eventual result of the computation
		newDef = null; //If the result will be stored in a def and the defname is new, it will be stored in newDef
		errorsPresent = true;
		
		//Find the defname if there is one.
		int eqLoc = expression.indexOf('='); //Everything left of the equal sign (if there is one) is the def name
		String defName = ""; //If there is a def, its name will be stored in defName.
		if (eqLoc != -1) { //if there is an equal sign, we find the def
			defName = expression.substring(0, eqLoc).trim();
			if (!defName.matches("\\w*"))
				return "Invalid receiever: " + defName;
			expression = expression.substring(eqLoc + 1);
		}
		
		//Attempt to parse the expression String into an Expression (the datatype)
		Expression expr = null;
		try {
			expr = new ExpressionParser().parse(expression);
		} catch (ParseException e) {
			return "Parse error: " + e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "Unknown error during parsing";
		}
		
		//Replace all defs in the expression with the lambda expressions they represent
		DefReplacementVisitor drv = new DefReplacementVisitor(defs);
		drv.visit(expr);
		if (!drv.getErrors().equals("")) {
			return drv.getErrors();
		}
		
		//Check to see if the lambda expression is correct (ie, no free variables)
		CorrectnessVisitor cv = new CorrectnessVisitor();
		if (!(cv.check(expr))) 
			return cv.getErrors() + "Invalid Expression: static analysis failed";
		

		//The PrinterVisitor will be used to convert Expressions back into Strings
		PrinterVisitor printer = new PrinterVisitor();
		
		//Convert the Expression so that all its variables have distinct names, then output the converted expression
		new AlphaConversionVisitor().visit(expr);
		toReturn += ALPHA + ":" + printer.print(expr) + "\n";
		
		//Attempt to actually evaluate the expression, stopping only if the lambda expression explodes or reduces too many times
		try {
			expr = new InterpreterVisitor(maxReductions).evaluate(expr);
		} catch (StackOverflowError e) {
			return toReturn + "Error: infinite recursion";
		} catch (ReductionLimitException e) {
			return toReturn + "Error: maximum reductions exceeded";
		}
		//Store the expression in the defs map, if a def is provided
		if (defName != "") {
			if (!defs.containsKey(defName)) {
				newDef = defName;
			}
			defs.put(defName, expr);
		}
		errorsPresent = false;
		return toReturn + printer.print(expr);
	}

	public Map<String, Expression> getDefs() {
		return defs;
	}
	
	public String newDef() {
		return newDef;
	}
	
	/**
	 * Attempts to set the maximum reductions for an expression's computation to a new value;
	 * fails if the input String does not parse to an int, in which case it does not change the maximum
	 * number of reductions and returns an error message.
	 * 
	 * @param newMax The new maximum number of reductions
	 * @return a String describing the result; either stating the new maximum if it was set or an error message
	 */
	private String setMaxReds(String newMax) {
		try {
			int newMaxReds = Integer.parseInt(newMax.trim());
			if (newMaxReds < 1)
				throw new NumberFormatException();
			maxReductions = newMaxReds;
		} catch (NumberFormatException e) {
			return "Failed to change max reductions: new max provided is not a valid number"; 
		}
		return "New maximum reductions: " + maxReductions;
	}
	
	public String getLast() {
		return ir.getLast();
	}
	
	public String getNext() {
		return ir.getNext();
	}
	
}
