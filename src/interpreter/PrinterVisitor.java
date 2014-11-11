package interpreter;

public class PrinterVisitor extends BasicLambdaVisitor {

	String toPrint;
	private static final char LAMBDA = (char) 955;

	@Override
	public void visit(Abstraction a) {
		toPrint += LAMBDA + a.param() + ".";
		visit(a.exp());
	}

	@Override
	public void visit(Application a) {
		toPrint += "(";
		visit(a.exp1());
		toPrint += " ";
		visit(a.exp2());
		toPrint += ")";
	}

	@Override
	public void visit(Variable v) {
		toPrint += v.id();
	}
	
	public String print(Expression e) {
		toPrint = "";
		visit(e);
		return toPrint;
	}

}
