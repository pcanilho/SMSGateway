package ch.cern.it.cs.cs.assets;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class HintTextField extends JTextField implements FocusListener {

	private static final long serialVersionUID = -5866396716264791841L;

	private final String hint;
	private boolean showingHint;

	public HintTextField(final String hint) {
		super(hint);
		this.hint = hint;
		this.showingHint = true;
		super.addFocusListener(this);
		setFont(new Font("Verdana", 2, 12));
		setForeground(Color.GRAY);
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText("");
			setFont(new Font("Verdana", 0, 12));
			showingHint = false;
			setForeground(Color.BLACK);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText(hint);
			setFont(new Font("Verdana", 2, 12));
			showingHint = true;
			setForeground(Color.GRAY);
		}
	}

	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}
}