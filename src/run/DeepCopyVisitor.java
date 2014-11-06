package run;

public class DeepCopyVisitor extends BasicLambdaVisitor {

	private Expression passUp;
	
	@Override
	public void visit(Abstraction a) {
		visit(a.exp());
		passUp = new Abstraction(a.param(), passUp);
	}

	@Override
	public void visit(Application a) {
		visit(a.exp1());
		Expression exp1 = passUp;
		visit(a.exp2());
		passUp = new Application(exp1, passUp);
	}

	@Override
	public void visit(Variable v) {
		passUp = new Variable(v.id());
	}

	@Override
	public void visit(VariableHolder h) {
		visit(h.exp());
		passUp = new VariableHolder(passUp);
	}
	
	public Expression deepCopy(Expression e) {
		passUp = null;
		visit(e);
		return passUp;
	}

}
