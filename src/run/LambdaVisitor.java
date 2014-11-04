package run;

public interface LambdaVisitor {

	public void visit(Expression e);
	public void visit(Abstraction a);
	public void visit(Application a);
	public void visit(Variable v);
	public void visit(VariableHolder h);
	
}
