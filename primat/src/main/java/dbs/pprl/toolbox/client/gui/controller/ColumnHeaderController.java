package dbs.pprl.toolbox.client.gui.controller;

import java.io.IOException;
import java.util.Optional;


import dbs.pprl.toolbox.client.data.attributes.AttributeType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

public class ColumnHeaderController extends MenuButton {
	
	@FXML
	private MenuItem renameColumnItem;
	
	@FXML
	private MenuItem deleteColumnItem;
	
	@FXML
	private MenuItem mergeColumnItem;
	
	@FXML
	private MenuItem splitColumnItem;
	
	@FXML
	private ToggleGroup attributeTypeToggleGroup;
	
	private DataPaneController dataPaneControl;
	
	private String columnName;
	
	private String columnId;
	
	public ColumnHeaderController(String columnName, String columnId, DataPaneController dataPaneControl) { 
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/ColumnHeader.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	
//		this.getStylesheets().add(getClass().getResource("/gui/style/application.css").toExternalForm());	
		this.columnName = columnName;
		this.setText(this.columnName);
		this.columnId = columnId;
		this.dataPaneControl = dataPaneControl;
	}
	
	public AttributeType getAttributeType() {
		final RadioMenuItem selectedType = (RadioMenuItem) this.attributeTypeToggleGroup.getSelectedToggle();
		final String stringAttributeType = selectedType.getText();
		return AttributeType.from(stringAttributeType);
	}
	
	@FXML
	public void renameColumn(ActionEvent event) {
		System.out.println("Rename column clicked!");
	}
	
	@FXML
	public void deleteColumn(ActionEvent event) {
		System.out.println("Delete column clicked");
		System.out.println(this.getAttributeType());
		
		final Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Column");
        alert.setContentText("Are you sure want to remove that column?");
        
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/gui/style/application.css").toExternalForm());
        
        final Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
        	this.dataPaneControl.removeColumnInDataTable(this.columnId);
        }
        
	}
	
	@FXML
	public void splitColumn(ActionEvent event) {
		System.out.println("split column clicked");
		/*
		Splitter splitter = new PositionSplitter(2);
		SplitDefinition splitDef = new SplitDefinition();
		splitDef.setSplitter(this.columnId, splitter);
		FieldSplitter fieldSplitter = new FieldSplitter(splitDef);
		final ObservableList<Record> records = this.selectionTabControl.getSelectionTable().getItems();
		
		for (int i = 1; i < splitter.parts(); i++) {
			final String newColumnName = columnName + "_" + i;
			final int newPos = this.columnId + i;
			final TableColumn<Record, String> t = this.selectionTabControl.buildTableColumn(newColumnName, newPos);
			t.setGraphic(new ColumnHeaderController(newColumnName, newPos, this.selectionTabControl));
			this.selectionTabControl.getSelectionTable().getColumns().add(newPos, t);
		}
		
		fieldSplitter.apply(records, null);
		*/
	}
	
	@FXML
	public void mergeColumn(ActionEvent event) {
		System.out.println("merge column clicked");
	}
}
