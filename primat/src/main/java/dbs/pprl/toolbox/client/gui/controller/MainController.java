package dbs.pprl.toolbox.client.gui.controller;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import dbs.pprl.toolbox.client.data.records.Record;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class MainController implements Initializable {
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private Tab selectionTabPaneTab;
	
	@FXML
	private Tab dataCleaningTabPaneTab;
	
	@FXML
	private Tab encodingTabPaneTab;
	
	@FXML
	private MenuBarController menuBarController;
	
	@FXML
	private DataPaneController dataPaneController;

	@FXML
	private SelectionTabController selectionTabController;
	
	@FXML
	private DataCleaningTabController dataCleaningTabController;
	
	@FXML
	private EncodingTabController encodingTabController;
	
	private Stage stage;
	
	
	public void doTransitionFromSelectionTabToDataCleaningTab() {
		this.disableSelectionTab();
		this.enableDataCleaningTab();
		this.dataCleaningTabController.setup();
		this.focusDataCleaningTab();
	}
	
	public void doTransitionFromDataCleaningTabToEncodingTab() {
		System.out.println("Transition from data cleaning to encoding.");
		this.disableDataCleaningTab();
		this.encodingTabController.setupFirstTab();
		this.enableEncodingTab();
		this.focusEncodingTab();
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.menuBarController.injectMainController(this);
		this.dataPaneController.injectMainController(this);
		this.selectionTabController.injectMainController(this);
		this.dataCleaningTabController.injectMainController(this);
		this.encodingTabController.injectMainController(this);
	}
	
	public void disableSelectionTab() {
		this.selectionTabPaneTab.setDisable(true);
	}
	
	public void disableDataCleaningTab() {
		this.dataCleaningTabPaneTab.setDisable(true);
	}
	
	public void enableDataCleaningTab() {
		this.dataCleaningTabPaneTab.setDisable(false);
	}
	
	public void enableEncodingTab() {
		this.encodingTabPaneTab.setDisable(false);
	}
	
	public void disableEncodingTab() {
		this.encodingTabPaneTab.setDisable(true);
	}
	
	public SelectionTabController getSelectionTabController() {
		return this.selectionTabController;
	}
	
	public DataPaneController getDataPaneController() {
		return this.dataPaneController;
	}
	
	public MenuBarController getMenuBarController() {
		return this.menuBarController;
	}
	
	public boolean isSelectionTabFocused() {
		return this.tabPane.getSelectionModel().getSelectedItem().equals(this.selectionTabPaneTab);
	}
		
	public void afterDataImportSetup(List<Record> records, List<String> header) {
		int columnId = 0;
		for (String columnName : header) {
			this.dataPaneController.addColumn(columnName, columnId);
			this.selectionTabController.addColumnToSelectionTable(columnName, columnId);
			columnId++;
		}
		
		this.dataPaneController.addItems(records);
		this.tabPane.setDisable(false);
	}
	
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void focusDataCleaningTab() {
		this.tabPane.getSelectionModel().select(this.dataCleaningTabPaneTab);		
	}

	public Stage getStage() {
		return this.stage;
	}


	public void focusEncodingTab() {
		this.tabPane.getSelectionModel().select(this.encodingTabPaneTab);
	}
}