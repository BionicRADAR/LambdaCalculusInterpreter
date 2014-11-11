package interpreter;

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
			h.betaReduce(new DeepCopyVisitor().deepCopy(repl));
			isParam = false;
		}
	}
	
	public Expression reduce(Application a) {
		Expression e1 = a.exp1();
		while (e1.type().equals("VarHolder")) {
			e1 = ((VariableHolder) e1).exp();
		}
		if (!e1.type().equals("Abs")) {
			System.out.println("Not a valid beta-reduction");
			return null;
		}
		Abstraction abs = (Abstraction) e1;
		param = abs.param();
		repl = a.exp2();
		isParam = false;
		visit(abs.exp());
//		System.out.println(new PrinterVisitor().print(abs.exp()));
		return abs.exp();
	}

}
