package editor;

import interpreter.Controller;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

public class InterpreterView extends JFrame {
	
	private Font font;
	
	private JTextField input;
	private JTextArea history, defsText;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InterpreterView() {
		super("Lambda Calculus Interpreter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(20, 20, 800, 500);
		font = new Font("Monospaced", Font.PLAIN, 12);
		fillPane();
		input.requestFocusInWindow();
	}
	
	private void fillPane() {
		Container p = getContentPane();
		p.setLayout(new GridBagLayout());
		makeHistoryArea(p);
		makeInputArea(p);
		makeDefsArea(p);
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
		JTextArea history = new JTextArea(25, 30);
		history.setLineWrap(true);
		history.setFont(font);
		history.setBackground(Color.BLACK);
		history.setForeground(Color.WHITE);
		history.setCaretColor(Color.WHITE);
		history.setEditable(false);
		history.setPreferredSize(new Dimension(50, 30));
		((DefaultCaret) history.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		this.history = history;
		JScrollPane hist = new JScrollPane(history);
		//JPanel hist = new JPanel();
		//hist.add(history);
		p.add(hist, c);
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
		JTextArea defsText = new JTextArea(25, 10);
		defsText.setLineWrap(true);
		defsText.setFont(font);
		defsText.setForeground(Color.WHITE);
		defsText.setBackground(Color.BLACK);
		defsText.setCaretColor(Color.WHITE);
		defsText.setEditable(false);
		defsText.setPreferredSize(new Dimension(10, 30));
		this.defsText = defsText;
		JScrollPane defs = new JScrollPane(defsText);
		//defs.add(defsText);
		p.add(defs, c);
	}
	
	private void makeInputArea(Container p) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.8;
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
		//Make typing ';' input a lambda
		input.getInputMap().put(KeyStroke.getKeyStroke(';'), "putlambda");
		input.getActionMap().put("putlambda", new LambdaAction(input));
		//Make the enter key call the interpret method
		input.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "interpret");
		input.getActionMap().put("interpret", new InterpretAction());
		//Make the up and down keys access the recent actions list
		input.getInputMap().put(KeyStroke.getKeyStroke("UP"), "getLast");
		input.getActionMap().put("getLast", new GetLastAction());
		input.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "getNext");
		input.getActionMap().put("getNext", new GetNextAction());
		input.setCaretColor(Color.WHITE);
		this.input = input;
		p.add(input, c);
	}
	
	private void callInterpret() {
		Controller.getInstance().cmdLineInterpret(this);
		input.setEnabled(true);
		input.requestFocusInWindow();
	}
	
	public void appendToHistory(String toAppend) {
		history.append(toAppend + "\n");
		if (historyLineCount() > 90) {
			history.setRows(100);
			while (history.getLineCount() > 90) {
				try {
					history.replaceRange("", 0, history.getLineStartOffset(1));
				} catch (BadLocationException e) {
					System.out.println("Problem with history: text location not found");
					e.printStackTrace();
				}
			}
			return;
		}
		while (historyLineCount() + 10 > history.getRows()) {
			history.setRows(history.getRows() + 10);
		}
	}
	
	private int historyLineCount() {
		String historyText = history.getText();
		int totalLines = 0;
		for (int i = 0; i < history.getLineCount(); i++) {
			int split = historyText.indexOf("\n");
			String line = historyText;
			if (split != -1) {
				line = historyText.substring(0, split);
				historyText = historyText.substring(split + 1);
			}
			totalLines += (1 + line.length() / (3 * history.getColumns()));
		}
		return totalLines;
	}
	
	public void addDef(String newDef) {
		defsText.append(newDef + "\n");
		while (defsText.getLineCount() + 3 > defsText.getRows()) {
			defsText.setRows(defsText.getRows() + 10);
		}
	}

	public JTextField getInput() {
		return input;
	}
	
	class InterpretAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			input.setEnabled(false);
			callInterpret();
		}
		
	}
	
	class NoAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {}
		
	}
	
	class GetLastAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			input.setText(Controller.getInstance().getLast());
		}
		
	}
	
	class GetNextAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			input.setText(Controller.getInstance().getNext());
		}
		
	}
	
	
}
