package interpreter;

public class VariableHolder implements Expression {

	@Override
	public String type() {
		return "VarHolder";
	}
	
	public VariableHolder(Expression e) {
		exp = e;
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
	
	public void setExp(Expression e) {
		exp = e;
	}

}
