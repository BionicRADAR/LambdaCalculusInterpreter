package interpreter;

public class Abstraction implements Expression{

	private String param;
	private Expression exp;
	public Abstraction(String param, Expression exp) {
		this.param = param;
		this.exp = exp;
	}
	
	public String param() { return param; }
	public Expression exp() { return exp; }
	public String type() { return "Abs"; }
	public void accept(LambdaVisitor v) { v.visit(this); }
	public void alphaConvert(String newParam) {
		param = newParam;
	}
	public void setExp(Expression e) {
		exp = e;
	}
	
}
