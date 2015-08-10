package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class CloseListener implements ActionListener {

	JFrame toClose = null;
	
	public CloseListener(JFrame toClose) {
		this.toClose = toClose;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		toClose.dispatchEvent(new WindowEvent(toClose, WindowEvent.WINDOW_CLOSING));
	}

}
