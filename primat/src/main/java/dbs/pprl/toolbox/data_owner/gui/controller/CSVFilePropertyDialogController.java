package dbs.pprl.toolbox.data_owner.gui.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class CSVFilePropertyDialogController extends GridPane{
	
	@FXML
	private RadioButton commaRadioBtn;
	
	@FXML
	private RadioButton semicolonRadioBtn;
	
	@FXML
	private RadioButton tabRadioBtn;
	
	@FXML CheckBox hasHeaderCheckbox;	
	
	@FXML
	private ToggleGroup fieldSeparatorGroup;
	
	public CSVFilePropertyDialogController()  { 
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/CsvFilePropertyDialog.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();	
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		this.commaRadioBtn.setUserData(',');
		this.semicolonRadioBtn.setUserData(';');
		this.tabRadioBtn.setUserData('\t');
	}
	
	public char getSelectedFieldSeparator() {
		final Toggle selectedToggle = this.fieldSeparatorGroup.getSelectedToggle();
		return (Character) selectedToggle.getUserData();
	}

	public boolean csvFileHasHeader() {
		return this.hasHeaderCheckbox.isSelected();
	}
}
