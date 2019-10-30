package dbs.pprl.toolbox.data_owner.gui.controller;

import java.util.List;
import java.util.stream.Collectors;

import dbs.pprl.toolbox.data_owner.data.records.Record;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

public class SelectionTabController {
	
	private MainController mainController;
	
	@FXML
	private Button confirmButton;
	
	@FXML
	private TableView<Record> selectionTable;
	
	public TableView<Record> getSelectionTable(){
		return this.selectionTable;
	}
	
	@FXML
	public void initialize() {
		this.selectionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	@FXML 
	public void addRecordsToSample() {
		final ObservableList<Record> selectedRecords = this.mainController.getDataPaneController().getSelectedRecordsInDataTable();
		
		if (selectedRecords != null && selectedRecords.size() > 0) {			
			final List<Record> recCopy = FXCollections.observableArrayList();
			for (final Record rec : selectedRecords) {
				System.out.println("Record copy");
				recCopy.add(rec.copy());
			}
			this.selectionTable.getItems().addAll(recCopy);
		}
	}
	
	@FXML
	public void addAllRecordsToSample() {
		final ObservableList<Record> allRecords = this.mainController.getDataPaneController().getRecordsInDataTable();
		this.selectionTable.getItems().addAll(allRecords);
	}
	
	@FXML
	public void removeRecordsFromSample() {
		ObservableList<Integer> selectedRecordsIndices = this.selectionTable.getSelectionModel().getSelectedIndices();
		
		for (final int index : selectedRecordsIndices) {
			this.selectionTable.getItems().remove(index);
		}
	}
	
	@FXML
	public void removeAllRecordsFromSample() {
		this.selectionTable.getItems().clear();
	}
	
	@FXML
	public void recordInSelectionTableClicked(MouseEvent event) {		
		if (event.getClickCount() == 1) {
			// do nothing
		}
		else if (event.getClickCount() == 2) {
			final int indexSelectedRecord = this.selectionTable.getSelectionModel().getSelectedIndex();
			if (indexSelectedRecord != -1) {
				this.selectionTable.getItems().remove(indexSelectedRecord);
			}
			else {
				// Empty table
			}
		}
	}
	
	@FXML
	public void removeDuplicates() {
		final ObservableList<Record> selectedRecords = this.selectionTable.getItems();
		
		final ObservableList<Record> duplicateFreeRecords = selectedRecords
				.stream()
				.distinct()
				.collect(Collectors.toCollection(FXCollections::observableArrayList));
		
		this.selectionTable.setItems(duplicateFreeRecords);
	}
	
	@FXML
	public void confirm() {
		System.out.println("Confirm button pressed");
		
		final ObservableList<Record> selectedRecords = this.selectionTable.getItems();
		final ObservableList<TableColumn<Record, ?>> selectedColumns = this.selectionTable.getColumns();
		
		if (selectedRecords != null && selectedRecords.size() > 0) {
			this.mainController.getDataPaneController().setSelectedRecords(selectedRecords, selectedColumns);
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No records selected.");
            alert.setContentText("Please selected records from the data table.");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/gui/style/application.css").toExternalForm());
            alert.showAndWait();
		}
	}
	
	
	public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }

	public TableColumn<Record, String> buildTableColumn(String columnName, int columnId){
		final TableColumn<Record, String> tcol = new TableColumn<>(columnName);
		tcol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getAttribute(columnId).getStringValue()));
		tcol.setSortable(false);
		tcol.setId(Integer.toString(columnId));
		tcol.setMinWidth(150);
		
//		final ColumnHeaderController chc = new ColumnHeaderController(columnName, columnId, this);
//		tcol.setGraphic(chc);
		return tcol;
	}

	public void addColumnToSelectionTable(String columnName, int columnId) {
		final TableColumn<Record, String> tcol = this.buildTableColumn(columnName, columnId);
		this.selectionTable.getColumns().add(tcol);
		
	}
}
