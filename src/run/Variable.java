package run;

public class Variable implements Expression {

	private String id;
	public Variable(String id) {
		this.id = id;
	}
	
	public String id() { return id; }
	public String type() { return "Var"; }
	public void accept(LambdaVisitor v) { v.visit(this); }
	public void alphaConvert(String newID) { id = newID; }
	
}
