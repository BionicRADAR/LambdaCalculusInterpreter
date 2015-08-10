package editor;

import interpreter.Controller;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class EditorFrame extends JFrame {

	private final static String[] CLOSING_OPTIONS = new String[4];
	
	private InterpreterView parentFrame;
	
	private JMenuItem runAll;
	
	private JTextArea codeTextArea;
	
	private Font font;
	
	private File currentFile;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EditorFrame() {
		this(null);
	}
	
	public EditorFrame(InterpreterView parent) {
		super("Lambda Editor");
		parentFrame = parent;
		this.setBounds(820, 20, 500, 300);
		font = new Font("Monospaced", Font.PLAIN, 12);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosed(WindowEvent arg0) {}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (closingFile())
					EditorFrame.this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowOpened(WindowEvent arg0) {}
			
		});
		Container p = this.getContentPane();
		p.setLayout(new GridBagLayout());
		makeMenu();
		makeTextArea(p);
		currentFile = null;
		CLOSING_OPTIONS[0] = "Save";
		CLOSING_OPTIONS[1] = "Don't Save";
		CLOSING_OPTIONS[2] = "Save As";
		CLOSING_OPTIONS[3] = "Cancel";
	}
	
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		menuBar.add(file);
		
		JMenuItem newClear = new JMenuItem("New", KeyEvent.VK_N);
		file.add(newClear);
		newClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (closingFile()) {
					codeTextArea.setText("");
					setCurrentFile(null);
				}
			}
		});
		
		JMenuItem save = new JMenuItem("Save", KeyEvent.VK_S);
		file.add(save);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		
		JMenuItem saveAs = new JMenuItem("Save As", KeyEvent.VK_A);
		file.add(saveAs);
		saveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAs();
			}
		});
		
		JMenuItem load = new JMenuItem("Load", KeyEvent.VK_L);
		file.add(load);
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}
		});
		
		JMenuItem close = new JMenuItem("Close Editor");
		file.add(close);
		close.addActionListener(new CloseListener(this));
		
		JMenu run = new JMenu("Run");
		run.setMnemonic(KeyEvent.VK_R);
		menuBar.add(run);
		
		runAll = new JMenuItem("Run All", KeyEvent.VK_A);
		runAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
		run.add(runAll);
		runAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runAll();
			}
		});
	}
	
	private void makeTextArea(Container p) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.gridheight = 1;
		codeTextArea = new JTextArea(12, 30);
		codeTextArea.setLineWrap(true);
		codeTextArea.setFont(font);
		codeTextArea.setBackground(Color.BLACK);
		codeTextArea.setForeground(Color.WHITE);
		codeTextArea.setCaretColor(Color.WHITE);
		codeTextArea.setPreferredSize(new Dimension(50, 30));
		JScrollPane codeScroll = new JScrollPane(codeTextArea);
		p.add(codeScroll, c);
		codeTextArea.getInputMap().put(KeyStroke.getKeyStroke(';'), "putlambda");
		codeTextArea.getActionMap().put("putlambda", new LambdaAction(codeTextArea));
	}
	
	private void runAll() {
		runAll.setEnabled(false);
		if (parentFrame == null) {
			parentFrame = Controller.getInstance().multiLineInterpret(codeTextArea.getText());
		}
		else {
			Controller.getInstance().multiLineInterpret(codeTextArea.getText(), parentFrame);
		}
		runAll.setEnabled(true);
	}
	
	private boolean save() {
		if (currentFile == null) {
			return saveAs();
		}
		else {
			writeTo(currentFile, codeTextArea.getText());
			return true;
		}
	}
	
	private boolean saveAs() {
		String toOutput = codeTextArea.getText();
		JFileChooser chooser = new JFileChooser();
		int accept = chooser.showSaveDialog(this);
		if (accept == JFileChooser.APPROVE_OPTION) {
			while (chooser.getSelectedFile().exists()) {
				int confirm = JOptionPane.showConfirmDialog(chooser, 
						"A file by this name already exists. Would you like to replace it?",
						"File Name Taken", JOptionPane.YES_NO_CANCEL_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					break;
				}
				if (confirm == JOptionPane.NO_OPTION) {
					accept = chooser.showSaveDialog(this);
					if (accept != JFileChooser.APPROVE_OPTION)
						return false;
				}
				else if (confirm == JOptionPane.CANCEL_OPTION) {
					return false;
				}
			}
			writeTo(chooser.getSelectedFile(), toOutput);
			setCurrentFile(chooser.getSelectedFile());
			return true;
		}
		return false;
	}
	
	private void writeTo(File outputFile, String toOutput) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			writer.write(toOutput);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Error: File not found", "Save Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error: I/O error while writing to file", "Save Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				//Ignore
			}
		}
	}
	
	private void load() {
		closingFile();
		JFileChooser chooser = new JFileChooser();
		int accept = chooser.showOpenDialog(this);
		if (accept == JFileChooser.APPROVE_OPTION) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(chooser.getSelectedFile())));
				codeTextArea.setText("");
				String line = "";
				while ((line = reader.readLine()) != null) {
					codeTextArea.append(line + "\n");
				}
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "Error: File not found", "Load Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error: I/O error while reading from file", "Load Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					//Ignore
				}
			}
			setCurrentFile(chooser.getSelectedFile());
		}
	}
	
	private void setCurrentFile(File newCurrentFile) {
		currentFile = newCurrentFile;
		if (newCurrentFile == null) {
			this.setTitle("Lambda Editor");
		}
		else {
			this.setTitle("Lambda Editor - " + currentFile.getName());
		}
	}
	
	private boolean closingFile() {
		int option = JOptionPane.showOptionDialog(this, "Do you want to save this work before closing it?", 
				"Closing File", JOptionPane.DEFAULT_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, CLOSING_OPTIONS, CLOSING_OPTIONS[0]);
		switch (option) {
		case 0: return save();
		case 1: return true;
		case 2: return saveAs();
		case 3: return false;
		default: return false;
		}
	}

}
