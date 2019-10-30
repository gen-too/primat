package dbs.pprl.toolbox.data_owner.gui.controller;

import javafx.scene.control.TextField;

public class NumericTextField extends TextField {
	
	@Override
	public void replaceText(int start, int end, String text) {
		if (!text.matches("[a-z, A-Z]")) {
			super.replaceText(start, end, text);
		}
		this.setPromptText("Enter a numeric value");
	}
	
	@Override
	public void replaceSelection(String text) {
		if (!text.matches("[a-z, A-Z]")) {
			super.replaceSelection(text);
		}
	}

}
