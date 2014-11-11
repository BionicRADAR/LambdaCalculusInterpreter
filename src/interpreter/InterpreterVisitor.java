package interpreter;

public class InterpreterVisitor extends BasicLambdaVisitor {

	Expression passUp;
	Expression startPoint;

	public InterpreterVisitor() {
		passUp = null;
		startPoint = null;
	}

	@Override
	public void visit(Application a) {
		if (new CheckAbstractionVisitor().isAbstraction(a.exp1())) {
			passUp = new BetaReductionVisitor().reduce(a);
			if (a == startPoint) {
				startPoint = passUp;
				passUp = null;
				visit(startPoint);
			}
			return;
		}
		visit(a.exp1());
		if (passUp != null) {
			a.setExp1(passUp);
			passUp = null;
			visit(a);
			return;
		}
		visit(a.exp2());
		while (passUp != null) {
			a.setExp2(passUp);
			passUp = null;
			visit(a.exp2());
		}
	}
	
	@Override
	public void visit(Abstraction a) {
		visit(a.exp());
		if (passUp != null) {
			a.setExp(passUp);
			passUp = null;
		}
	}
	
	@Override
	public void visit(VariableHolder h) {
		visit(h.exp());
		if (passUp != null) {
			h.setExp(passUp);
			passUp = null;
		}
	}
	
	public Expression evaluate(Expression e) {
		startPoint = e;
		visit(startPoint);
		return startPoint;
	}
	
	private class CheckAbstractionVisitor extends BasicLambdaVisitor {
		
		private boolean isAbstraction;
		
		public CheckAbstractionVisitor() {
			isAbstraction = false;
		}
		
		@Override
		public void visit(Abstraction a) {
			isAbstraction = true;
		}
		
		@Override
		public void visit(Application a) { }
		
		@Override
		public void visit(Variable v) { }

		public boolean isAbstraction(Expression e) {
			isAbstraction = false;
			visit(e);
			return isAbstraction;
		}
		
	}

}


