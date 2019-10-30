package dbs.pprl.toolbox.client.gui.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import dbs.pprl.toolbox.client.data.CSVRecordWrapper;
import dbs.pprl.toolbox.client.data.attributes.AttributeParseException;
import dbs.pprl.toolbox.client.data.attributes.AttributeType;
import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.utils.CSVReader;
import dbs.pprl.toolbox.utils.SetUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MenuBarController {
	
	public static ObservableList<ExtensionFilter> fileExtensions =
			FXCollections.observableArrayList(
					new FileChooser.ExtensionFilter("CSV", "*.csv"),
					new FileChooser.ExtensionFilter("TXT", "*.txt")
			);	
	
	private MainController mainController;
	
	@FXML
	private MenuItem saveFileMenuItem;
	
	
	public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }
	
	@FXML
	public void showFileSystemSaveDialog() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(fileExtensions);
		fileChooser.setTitle("Save Data File");
		fileChooser.setInitialFileName(".csv");
		final File file = fileChooser.showSaveDialog(this.mainController.getStage());
		
		if (file != null) {
			String filePath = file.getAbsolutePath();
			System.out.println("Save data to: " + filePath);	
		}
		else {
			// No file specified.
		}
	}
	
	public File showFileChooserDialog() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(fileExtensions);
		fileChooser.setTitle("Open Data File");
		return fileChooser.showOpenDialog(this.mainController.getStage());
	}
	
	public CSVFilePropertyDialogController showCSVPropertyDialog() {
		final Alert fileInfoAlert = new Alert(AlertType.CONFIRMATION);
		fileInfoAlert.setTitle("File Information");
		fileInfoAlert.setHeaderText("Please specify the following details...");
		
		final DialogPane dialogPane = fileInfoAlert.getDialogPane();
		fileInfoAlert.getButtonTypes().retainAll(ButtonType.OK);
		CSVFilePropertyDialogController dialogContent = 
				new CSVFilePropertyDialogController();
		dialogPane.setContent(dialogContent);
		
        dialogPane.getStylesheets().add(getClass().getResource("/gui/style/application.css").toExternalForm());
        dialogPane.getStylesheets().add(getClass().getResource("/gui/style/dialog.css").toExternalForm());
        fileInfoAlert.showAndWait();
		
        return dialogContent;
	}
	
	public static void checkEmptyFile(List<CSVRecord> records) throws IOException {
		if (records == null || records.size() == 0) {
			throw new IOException("File is empty!");
		}
	}
	
	@FXML
	public void showFileSystemOpenDialog() {	
		final File choosenFile = this.showFileChooserDialog();
		
		if (choosenFile != null) {		
			final CSVFilePropertyDialogController csvProperties = this.showCSVPropertyDialog();
			final boolean hasHeader = csvProperties.csvFileHasHeader();
			final char fieldSeparator = csvProperties.getSelectedFieldSeparator();
			
			System.out.println("CSV file header: " + hasHeader);
			System.out.println("Field separator: " + fieldSeparator);
			
			final String filePath = choosenFile.getAbsolutePath();
		
			try {
				final CSVReader csvReader = new CSVReader(filePath, hasHeader, fieldSeparator);
				final List<CSVRecord> csvRecords = csvReader.read();
							
				checkEmptyFile(csvRecords);
				
				final List<String> header;
				
				if (hasHeader) {
					final List<String> fileHeader = csvReader.getHeader();
					final boolean duplicatesInFileHeader = SetUtils.hasDuplicates(fileHeader);
					if (duplicatesInFileHeader) {
						header = CSVReader.getDefaultHeader(csvRecords);
					}
					else {
						header = fileHeader;
					}
				}
				else {
					header = CSVReader.getDefaultHeader(csvRecords);
				}
				
				
				final int columns = header.size();
				final Map<Integer, AttributeType> attrMap = new HashMap<>();
				
				//TODO: handle id column / attribute
				attrMap.put(0, AttributeType.ID);
				
				for (int i = 1; i < columns; i++) {
					attrMap.put(i, AttributeType.STRING);
				}
				
				final CSVRecordWrapper wrapper = new CSVRecordWrapper(attrMap);
				final List<Record> records = wrapper.from(csvRecords);
				
				this.mainController.afterDataImportSetup(records, header);			
				this.saveFileMenuItem.setDisable(false);
				//TODO disable open menu or clear before
			} 
			catch (IOException | AttributeParseException e) {
				e.printStackTrace();
			}
		}
		else {
			// No file chosen.
		}
	}
}
