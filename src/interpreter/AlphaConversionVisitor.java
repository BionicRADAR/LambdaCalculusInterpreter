package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlphaConversionVisitor extends BasicLambdaVisitor {

	List<String> ids;
	Map<String, String> conv;
	public AlphaConversionVisitor() {
		ids = new ArrayList<String>();
		conv = new HashMap<String, String>();
	}

	@Override
	public void visit(Abstraction a) {
		if (ids.contains(a.param())) {
			String newID = a.param();
			for (int i = 0; ids.contains(newID); i++) {
				newID = a.param() + i;
			}
			ids.add(newID);
			conv.put(a.param(), newID);
			visit(a.exp());
			conv.remove(a.param());
			a.alphaConvert(newID);
			return;
		}
		ids.add(a.param());
		visit(a.exp());
	}

	@Override
	public void visit(Variable v) {
		if (conv.containsKey(v.id()))
			v.alphaConvert(conv.get(v.id()));
	}
	
}
