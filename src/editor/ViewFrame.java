package editor;

import interpreter.Controller;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class ViewFrame extends JFrame {
	
	Font font;
	
	private JTextField input;
	private JTextArea history, defsText;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(20, 20, 800, 500);
		font = new Font("Monospaced", Font.PLAIN, 12);
		fillPane();
	}
	
	private void fillPane() {
		Container p = getContentPane();
		p.setLayout(new GridBagLayout());
		makeHistoryArea(p);
		makeDefsArea(p);
		makeInputArea(p);
	}
	
	private void makeHistoryArea(Container p) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.8;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		//c.ipady = 300;
		//c.ipadx = 300;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.gridheight = 1;
		JTextArea history = new JTextArea();
		history.setColumns(50);
		history.setRows(30);
		history.setLineWrap(true);
		history.setFont(font);
		history.setBackground(Color.BLACK);
		history.setForeground(Color.WHITE);
		history.setCaretColor(Color.WHITE);
		history.setEditable(false);
		this.history = history;
		//JPanel hist = new JPanel();
		//hist.add(history);
		p.add(history, c);
	}
	
	private void makeDefsArea(Container p) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.2;
		c.weighty = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.insets = new Insets(0, 1, 0, 0);
		//JPanel defs = new JPanel();
		JTextArea defsText = new JTextArea(30, 10);
		defsText.setLineWrap(true);
		defsText.setFont(font);
		defsText.setForeground(Color.WHITE);
		defsText.setBackground(Color.BLACK);
		defsText.setCaretColor(Color.WHITE);
		defsText.setEditable(false);
		this.defsText = defsText;
		//defs.add(defsText);
		p.add(defsText, c);
	}
	
	private void makeInputArea(Container p) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.6;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		JTextField input = new JTextField(50);
		input.setFont(font);
		input.setBackground(Color.BLACK);
		input.setForeground(Color.WHITE);
		//JPanel ins = new JPanel();
		//ins.add(input);
		//put keystroke redefinition here
		input.getInputMap().put(KeyStroke.getKeyStroke(';'), "putlambda");
		input.getActionMap().put("putlambda", new LambdaAction());
		input.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "interpret");
		input.getActionMap().put("interpret", new InterpretAction());
		input.setCaretColor(Color.WHITE);
		this.input = input;
		p.add(input, c);
	}
	
	private void updateDefs() {
		defsText.setText("");
		for (Iterator<String> it = Controller.getInstance().getDefs().keySet().iterator(); it.hasNext(); ) {
			defsText.append(it.next() + "\n");
		}
	}
	
	class LambdaAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String text = input.getText();
			int caret = input.getCaretPosition();
			input.setText(text.substring(0, caret) + (char) 955 + text.substring(caret));
			input.setCaretPosition(caret + 1);
		}
	}
	
	class InterpretAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String exp = input.getText();
			input.setText("");
			input.setEnabled(false);
			history.append("> " + exp + "\n");
			history.append(Controller.getInstance().interpret(exp) + "\n");
			input.setEnabled(true);
			updateDefs();
		}
		
	}
	
	
}
