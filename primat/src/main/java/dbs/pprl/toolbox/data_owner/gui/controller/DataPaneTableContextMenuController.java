package dbs.pprl.toolbox.data_owner.gui.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Alert.AlertType;

public class DataPaneTableContextMenuController extends ContextMenu{

	public DataPaneTableContextMenuController() {
		final FXMLLoader fxmlLoader = 
				new FXMLLoader(getClass().getResource("/gui/view/DataAnalyzerContextMenu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void showResultInformation(String headerText) {
		final Alert resultAlert = new Alert(AlertType.INFORMATION);
		resultAlert.setTitle("Count Operation");
		resultAlert.setHeaderText(headerText);
		resultAlert.getDialogPane().getStylesheets().add(getClass().getResource("/gui/style/application.css").toExternalForm());
		resultAlert.getDialogPane().getStylesheets().add(getClass().getResource("/gui/style/dialog.css").toExternalForm());
		resultAlert.showAndWait();
	}
	
	@FXML
	public void countDistinctValues() {
		System.out.println("Count Distinct Values");
		//TODO: Service call
		final int distinctValues = 42; 
		this.showResultInformation("Distinct Values: " + distinctValues);
	}
	
	@FXML
	public void countNullValues() {
		System.out.println("Count Null Values");
		//TODO: Service call
		final int nullValues = 42; 
		this.showResultInformation("Null Values: " + nullValues);
	}
	
	@FXML
	public void countMinOccuringValue() {
		System.out.println("Count Min. Occuring Value");
		//TODO: Service call
		final int minValues = 42; 
		this.showResultInformation("Minimum Value Occurence " + minValues);
	}
	
	@FXML
	public void countAverageOccuringValue() {
		System.out.println("Count Avg. Occuring Value");
		//TODO: Service call
		final int avgValues = 42; 
		this.showResultInformation("Average Value Occurence: " + avgValues);
	}
	
	@FXML
	public void countMaxOccuringValue() {
		System.out.println("Count Max. Occuring Value");
		//TODO: Service call
		final int maxValues = 42; 
		this.showResultInformation("Maximum Value Occurence: " + maxValues);
	}
		
	@FXML
	public void countValuesContaining() {
		System.out.println("Count Values Containing ...");
	}
	
}