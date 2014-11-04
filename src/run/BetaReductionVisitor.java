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
	
	public Expression reduce(Application a) {
		if (!a.exp1().type().equals("Abs")) {
			System.out.println("Not a valid beta-reduction");
			return null;
		}
		Abstraction abs = (Abstraction) a.exp1();
		param = abs.param();
		repl = a.exp2();
		isParam = false;
		visit(abs.exp());
		return abs.exp();
	}

}
