package run;

public class BasicLambdaVisitor implements LambdaVisitor {

	@Override
	public void visit(Expression e) {
		e.accept(this);
	}

	@Override
	public void visit(Abstraction a) {
		visit(a.exp());
	}

	@Override
	public void visit(Application a) {
		visit(a.exp1());
		visit(a.exp2());
	}

	@Override
	public void visit(Variable v) {	}
	
	@Override
	public void visit(VariableHolder h) { 
		visit(h.exp());
	}

}
