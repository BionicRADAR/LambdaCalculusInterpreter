package interpreter;

public class CorrectnessVisitor extends BasicLambdaVisitor {

	
	private CorrectnessStack fr;
	private boolean correct;
	private String errors;
	private Expression passUp;
	
	public CorrectnessVisitor() {
		fr = new CorrectnessStack();
		correct = true;
		errors = "";
		passUp = null;
	}
	
	@Override
	public void visit(Abstraction a) {
		fr.push(a.param());
		visit(a.exp());
		if (a.param() != fr.pop()) {
			errors += "Stack check error on param " + a.param() + "\n";
			correct = false;
		}
	}

	@Override
	public void visit(Variable v) {
		if (!fr.contains(v.id())) {
			errors += "Free Variable " + v.id() + "\n";
			correct = false;
		}
	}
	
	public boolean check(Expression e) {
		errors = "";
		visit(e);
		return correct;
	}
	
	public String getErrors() {
		return errors;
	}
	
}
