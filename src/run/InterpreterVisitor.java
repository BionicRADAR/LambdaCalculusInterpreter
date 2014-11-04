package run;

public class InterpreterVisitor extends BasicLambdaVisitor {

	Expression passUp;

	public InterpreterVisitor() {
		passUp = null;
	}
	
	@Override
	public void visit(Abstraction a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Application a) {
		if (a.exp1().type().equals("Abs")) {
			Expression e = new BetaReductionVisitor().reduce(a);
			
		}
	}

	@Override
	public void visit(VariableHolder h) {
		// TODO Auto-generated method stub
		
	}

}
