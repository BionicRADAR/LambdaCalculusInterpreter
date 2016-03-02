package interpreter;

public class BetaReductionVisitor extends BasicLambdaVisitor {


	private String param;
	private Expression repl;
	private boolean isParam;
	private String errors;
	
	public BetaReductionVisitor() {
		errors = "";
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
			h.betaReduce(new DeepCopyVisitor().deepCopy(repl));
			isParam = false;
		}
	}
	
	public Expression reduce(Application a) {
		errors = "";
		Expression e1 = a.exp1();
		while (e1.type().equals("VarHolder")) {
			e1 = ((VariableHolder) e1).exp();
		}
		if (!e1.type().equals("Abs")) {
			errors += ("Not a valid beta-reduction\n");
			return null;
		}
		Abstraction abs = (Abstraction) e1;
		param = abs.param();
		repl = a.exp2();
		isParam = false;
		visit(abs.exp());
		return abs.exp();
	}
	
	public String getErrors() {
		return errors;
	}

}
