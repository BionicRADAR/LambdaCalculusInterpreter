package interpreter;

public interface Expression {

	public String type();
	
	public void accept(LambdaVisitor v) ;
	
}
