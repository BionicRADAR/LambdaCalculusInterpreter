package test;

import javax.swing.JFrame;

import editor.EditorFrame;

public class EditorFrameTest {

	public static void main(String[] args) {
		EditorFrame toTest = new EditorFrame();
		toTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		toTest.setVisible(true);
	}
	
}
