package run;

public class BetaReductionVisitor extends BasicLambdaVisitor {


	private String param;
	private Expression repl;
	private boolean isParam;
	
	public BetaReductionVisitor() {
		isParam = false;
	}

	@Override
	public void visit(Variable v) {
		if (v.id().equals(param))
			isParam = true;
	}
	
	@Override
	public void visit(VariableHolder h) {
		visit(h.exp());
		if (isParam) {
			h.betaReduce(repl);
			isParam = false;
		}
	}
	
	public void reduce(Abstraction a, Expression e) {
		param = a.param();
		repl = e;
		isParam = false;
		visit(a.exp());
	}

}
