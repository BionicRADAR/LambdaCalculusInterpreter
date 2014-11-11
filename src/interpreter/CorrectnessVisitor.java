package interpreter;

public class CorrectnessVisitor extends BasicLambdaVisitor {

	
	private CorrectnessStack fr;
	private boolean correct;
	public CorrectnessVisitor() {
		fr = new CorrectnessStack();
		correct = true;
	}
	
	@Override
	public void visit(Abstraction a) {
		if (fr.contains(a.param())) {
			System.out.println("Conflicting variable " + a.param());
			correct = false;
		}
		fr.push(a.param());
		visit(a.exp());
		if (a.param() != fr.pop()) {
			System.out.println("Stack check error on param " + a.param());
			correct = false;
		}
	}

	@Override
	public void visit(Variable v) {
		if (!fr.contains(v.id())) {
			System.out.println("Free Variable " + v.id());
			correct = false;
		}
	}

	public boolean check(Expression e) {
		e.accept(this);
		return correct;
	}
	
}
