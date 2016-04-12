package editor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class LambdaAction extends AbstractAction {

	JTextComponent textComp = null;
	private static final char LAMBDA = (char) 955;
	
	public LambdaAction(JTextComponent textComp) {
		this.textComp = textComp;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (textComp == null) {
			try {
				textComp = (JTextComponent) e.getSource();
			} catch (ClassCastException ce) {
				System.err.println("No proper text component for the Lambda Action");
				return;
			}
		}
		textComp.replaceSelection("" + LAMBDA);
	}

}
