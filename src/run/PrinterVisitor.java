package run;

public class PrinterVisitor extends BasicLambdaVisitor {

	String toPrint;

	@Override
	public void visit(Abstraction a) {
		toPrint += "fn" + a.param() + ".";
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
