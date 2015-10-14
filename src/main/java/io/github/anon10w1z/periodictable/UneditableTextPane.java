package io.github.anon10w1z.periodictable;

import javax.swing.JTextPane;

public class UneditableTextPane extends JTextPane {
	public UneditableTextPane(String text) {
		super();
		this.setText(text);
		this.setEditable(false);
	}
}
