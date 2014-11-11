package interpreter;


public class CorrectnessStack {

	
	private Node top;
	public CorrectnessStack() {	}
	
	public void push(String datum) {
		top = new Node(datum, top);
	}
	
	public String pop() {
		if (!isEmpty()) {
			String popped = top.datum;
			top = top.next;
			return popped;
		}
		return null;	
	}
	
	public boolean isEmpty() {
		return top == null;
	}
	
	public boolean contains(String s) {
		for (Node n = top; n != null; n = n.next){
			if (n.datum.equals(s))
				return true;
		}
		return false;
	}
	
	private class Node {
		Node(String dat, Node nex) {
			datum = dat;
			next = nex;
		}
		private String datum;
		private Node next;
	}
}
