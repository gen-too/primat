package dbs.pprl.toolbox.data_owner.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import dbs.pprl.toolbox.data_owner.data.records.EncodedRecord;
import dbs.pprl.toolbox.data_owner.data.records.Record;
import dbs.pprl.toolbox.data_owner.encoding.BloomFilterDefinition;
import dbs.pprl.toolbox.data_owner.encoding.GenericBloomFilterEncoder;
import dbs.pprl.toolbox.utils.BitSetUtils;
import dbs.pprl.toolbox.utils.CSVWriter;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;


public class EncodingTabController implements Initializable {

	@FXML
	private Button confirmButton;
	
	@FXML
	private Button addBloomFilterDefinitionButton;
	
	@FXML
	private TabPane bloomFilterTabPane;
	
	
	private MainController mainController;
	
	
	public EncodingTabController() {}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	
	@FXML
	public void confirm(ActionEvent ev) {
		System.out.println("Confirm in EncodingTab pressed.");
		
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File");
		fileChooser.setInitialFileName(".csv");
		final File file = fileChooser.showSaveDialog(this.mainController.getStage());
		
		if (file != null) {
			String filePath = file.getAbsolutePath();
			System.out.println("Save data to: " + filePath);	
		
			final List<BloomFilterDefinition> bloomFilterDefinitions = 
					this.bloomFilterTabPane
					.getTabs()
					.stream()
					.map(tab -> tab.getContent())
					.map(node -> (BloomFilterTabContentController) node)
					.map(bfTab -> bfTab.getBloomFilterDefinition())
					.collect(Collectors.toList());
					
			final GenericBloomFilterEncoder bfEncoder = new GenericBloomFilterEncoder(bloomFilterDefinitions);
			final List<Record> records = this.mainController.getDataPaneController().getRecordsInDataTable();
							
			final List<EncodedRecord> encodedRecords = bfEncoder.encode(records);
			
			try {
				final CSVWriter csvWriter = new CSVWriter(filePath);
				csvWriter.writeRecords(encodedRecords);
				//TODO: Dialog with TableView and EncodedRecords.
				
				final Alert encodedRecordsAlert = new Alert(AlertType.INFORMATION);
				encodedRecordsAlert.setTitle("Encoding done.");
				encodedRecordsAlert.setHeaderText("Encoded records:");
				
				
				final DialogPane dialogPane = encodedRecordsAlert.getDialogPane();

				
				final TableView<EncodedRecord> recordTable = new TableView<>(FXCollections.observableArrayList(encodedRecords));
				recordTable.setMaxWidth(Double.MAX_VALUE);
				recordTable.setMaxHeight(Double.MAX_VALUE);
				
				final TableColumn<EncodedRecord, String> idColumn = new TableColumn<>("Id");
				idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
				
				final TableColumn<EncodedRecord, String> bfColumn = new TableColumn<>("Bit Vector(s)");
				bfColumn.setCellValueFactory(i -> {
					final StringJoiner res = new StringJoiner(",");
					i.getValue().getBitVectors().forEach(bv -> {
						res.add(BitSetUtils.toBase64LittleEndian(bv));
					});
					return new ReadOnlyStringWrapper(res.toString());
				});
				bfColumn.setStyle("-fx-alignment: CENTER-LEFT;");
				
				recordTable.getColumns().add(idColumn);
				recordTable.getColumns().add(bfColumn);
				
				dialogPane.setContent(recordTable);
				dialogPane.setMinHeight(Region.USE_PREF_SIZE);
				dialogPane.setMinWidth(800);
				
				
		        dialogPane.getStylesheets().add(getClass().getResource("/gui/style/application.css").toExternalForm());
		        dialogPane.getStylesheets().add(getClass().getResource("/gui/style/dialog.css").toExternalForm());
		        encodedRecordsAlert.showAndWait();
				
				
			} 
			catch (IOException e) {
				// TODO: Show alert dialog.
				e.printStackTrace();
			}
			
		}
	}
	
	public void setupFirstTab() {
		this.addBloomFilterDefinition(true);
	}
	
	private void addBloomFilterDefinition(boolean first) {
		final Tab bfTab = new Tab();
		
		if (first) {
			bfTab.setClosable(false);
		}
		
		bfTab.setContent(new BloomFilterTabContentController(this));
		
		final Label label = new Label("New BF");
		//TODO: ID = CSS ID
		label.setId("bfTabLabel");
		bfTab.setGraphic(label);
		final TextField textField = new TextField();
		textField.setPrefWidth(100);
	
		label.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {  
					textField.setText(label.getText());  
					bfTab.setGraphic(textField);  
				    textField.selectAll();  
				    textField.requestFocus();  
				}  
			}	
		});
		
		textField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				label.setText(textField.getText());
				bfTab.setGraphic(label);
			}
		});
		
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					System.out.println("Tab TextField focused");
				}
				else {
					label.setText(textField.getText());
					bfTab.setGraphic(label);
				}
			}
		});
		
		this.bloomFilterTabPane.getTabs().add(bfTab);
	}
	
	@FXML
	public void addBloomFilterDefinition() {
		System.out.println("Add Bloom filter definition.");
		this.addBloomFilterDefinition(false);
	}
	
	public void injectMainController(MainController mainController) {
		this.mainController = mainController;
	}
	
	public MainController getMainController() {
		return this.mainController;
	}
	
}