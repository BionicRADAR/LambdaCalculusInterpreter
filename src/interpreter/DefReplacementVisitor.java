package interpreter;

import java.util.Map;

public class DefReplacementVisitor extends BasicLambdaVisitor{

	private boolean isParam;
	private String paramName;
	private Map<String, Expression> map;
	private DeepCopyVisitor dcv;
	private CorrectnessStack stack;
	private String errors;
	
	public DefReplacementVisitor(Map<String, Expression> m) {
		isParam = false;
		map = m;
		dcv = new DeepCopyVisitor();
		stack = new CorrectnessStack();
		errors = "";
	}
	
	@Override
	public void visit(Variable v) {
		if (map.containsKey(v.id()) && !stack.contains(v.id())) {
			isParam = true;
			paramName = v.id();
		}
	}
	
	@Override
	public void visit(Abstraction a) {
		if (map.containsKey(a.param())) {
			stack.push(a.param());
			visit(a.exp());
			if (!stack.pop().equals(a.param()))
				errors += "Stack check error on " + a.param() + "\n";
		}
		visit(a.exp());
	}

	@Override
	public void visit(VariableHolder h) {
		visit(h.exp());
		if (isParam) {
			h.setExp(dcv.deepCopy(map.get(paramName)));
			isParam = false;
			paramName = "";
		}
	}
	
	public String getErrors() {
		return errors;
	}

}
