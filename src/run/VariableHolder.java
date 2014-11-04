package run;

public class VariableHolder implements Expression {

	@Override
	public String type() {
		return "VarHolder";
	}
	
	public VariableHolder(Variable v) {
		exp = v;
	}
	
	private Expression exp;

	@Override
	public void accept(LambdaVisitor v) {
		v.visit(this);
	}
	
	public Expression exp() {
		return exp;
	}
	
	public void betaReduce(Expression newExp) {
		exp = newExp;
	}

}
