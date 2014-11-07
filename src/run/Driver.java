package run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;




public class Driver {

	public static void main(String[] args) {
		System.out.print("> ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String expression = "";
		try {
			expression = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Expression expr = null;
		try {
			expr = new Parser().parse(expression);
		} catch (Exception e) {
			System.out.println("Invalid expression: could not parse");
			return;
		}
//		String toPrint = new PrinterVisitor().print(expr);
//		System.out.println(toPrint);
		boolean frees = new CorrectnessVisitor().check(expr);
		if (!frees) {
			System.out.println("Invalid expression: free variable");
			return;
		}
//		System.out.println(frees);
		new AlphaConversionVisitor().visit(expr);
//		System.out.println(new PrinterVisitor().print(expr));
		Expression reduced = new InterpreterVisitor().evaluate(expr);
		System.out.println(new PrinterVisitor().print(reduced));
	}
	
}
