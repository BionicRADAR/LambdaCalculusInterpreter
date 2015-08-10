package interpreter;

public class InputRecord {

	String[] inputs;
	int maxLength = 50;
	int length = 0;
	int position = -1;
	int mostRecent = -1;
	
	public InputRecord(int max) {
		maxLength = max;
		inputs = new String[maxLength];
	}
	
	public InputRecord() {
		maxLength = 50;
		inputs = new String[maxLength];
	}
	
	public void newInput(String input) {
		if (length >= maxLength) {
			mostRecent = (mostRecent + 1) % maxLength;
			position = mostRecent;
			inputs[position] = input;
			return;
		}
		length++;
		mostRecent = length - 1;
		position = mostRecent;
		inputs[position] = input;
	}
	
	public String getLast() {
		if (length == 0) {
			return "";
		}
		String toReturn = inputs[position];
		int oldPos = position--;
		if (position < 0)
			position += length;
		if (position == mostRecent) {
			position = oldPos;
		}
		return toReturn;
	}
	
	public String getNext() {
		if (position == mostRecent)
			return "";
		position = (position + 1) % length;
		return inputs[position];
	}
	
}
