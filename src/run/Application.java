package run;

public class Application implements Expression {

	private Expression exp1, exp2;
	
	public Application(Expression exp1, Expression exp2) {
		this.exp1 = exp1;
		this.exp2 = exp2;
	}
	
	public Expression exp1() { return exp1; }
	public Expression exp2() { return exp2; }
	
	public String type() { return "App"; }
	public void accept(LambdaVisitor v) { v.visit(this); }
	public void setExp1(Expression e) { exp1 = e; }
	public void setExp2(Expression e) { exp2 = e; }
	
}
