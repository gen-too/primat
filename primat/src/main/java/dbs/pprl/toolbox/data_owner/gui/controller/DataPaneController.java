package dbs.pprl.toolbox.data_owner.gui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dbs.pprl.toolbox.data_owner.data.records.Record;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class DataPaneController implements Initializable{

	@FXML
	private Label noRowsLabel;
	
	@FXML
	private TableView<Record> dataTable;
	
	
	private MainController mainController;
	

	public void showFileSystemOpenDialog() {
		this.mainController.getMenuBarController().showFileSystemOpenDialog();
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void refreshDataTable() {
		this.dataTable.refresh();
	}
	
	public ObservableList<Record> getSelectedRecordsInDataTable() {
		return this.dataTable.getSelectionModel().getSelectedItems();
	}
	
	public ObservableList<Record> getRecordsInDataTable() {
		return this.dataTable.getItems();
	}	
	
	public ObservableList<TableColumn<Record, ?>> getColumnsInDataTable() {
		return this.dataTable.getColumns();
	}
	
	public void setSelectedRecords(ObservableList<Record> selectedRecords, ObservableList<TableColumn<Record, ?>> selectedColumns) {
		
		final ObservableList<TableColumn<Record, ?>> cols = this.dataTable.getColumns();			
		
		cols.removeIf(col -> {
			for (TableColumn<Record, ?> selCol : selectedColumns) {
				if (selCol.getId().equals(col.getId())) {
					return false;
				}
			}
			return true;
		});
		
		final List<Record> recCopy = new ArrayList<>(selectedRecords);
		final ObservableList<Record> selectedRecCopy = FXCollections.observableArrayList(recCopy);
		this.addItems(selectedRecCopy);	
		
		cols.forEach(t -> {
			final ColumnHeaderController chc = new ColumnHeaderController(t.getText(), t.getId(), this);
			t.setGraphic(chc);
			t.setText("");
		});
		
		this.mainController.doTransitionFromSelectionTabToDataCleaningTab();
	}
	
	
	@FXML
	public void recordInDataTableClicked(MouseEvent event){
		System.out.println("Record in tableview clicked");
		
		if (!this.mainController.isSelectionTabFocused()) {
			return;
		}
		
		System.out.println(event.getClickCount());
		System.out.println(this.dataTable.getSelectionModel().getSelectedItems());
		
		if (event.getClickCount() == 2) {
			this.mainController.getSelectionTabController().addRecordsToSample();
		}
	}
	/*
	public TableView<Record> getDataTable(){
		return this.dataTable;
	}
	*/
	
	public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }
	
	public void addColumn(String columnName, int columnId) {
		final TableColumn<Record, String> tcol = new TableColumn<>(columnName);
		tcol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getAttribute(columnId).getStringValue()));
		tcol.setSortable(false);
		tcol.setId(Integer.toString(columnId));
		tcol.setUserData(columnName);
		tcol.setMinWidth(150);
		//TODO change id i.e. how columns can be identified
		// necessary for split and merge to keep column arrangement
		
		final DataPaneTableContextMenuController contextMenu = new DataPaneTableContextMenuController();		
		tcol.setContextMenu(contextMenu);
			
		this.dataTable.getColumns().add(tcol);
	}


	public void addItems(List<Record> records) {
		final ObservableList<Record> items = FXCollections.observableArrayList(records);
		this.dataTable.setItems(items);
		this.noRowsLabel.setText(Integer.toString(items.size()));		
	}


	public void removeColumnInDataTable(String columnId) {
		this.dataTable.getColumns()
			.removeIf(col -> 
				col.getId().equals(columnId)
			);	
	}
}
