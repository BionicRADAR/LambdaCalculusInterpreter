package editor;

import java.awt.Font;

import javax.swing.JFrame;

public class EditorFrame extends JFrame {

	private Font font;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EditorFrame() {
		super("Lambda Editor");
		this.setBounds(820, 20, 500, 300);
		font = new Font("Monospaced", Font.PLAIN, 12);
	}

}
