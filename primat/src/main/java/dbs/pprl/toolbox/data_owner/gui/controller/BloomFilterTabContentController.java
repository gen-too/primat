package dbs.pprl.toolbox.data_owner.gui.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import dbs.pprl.toolbox.data_owner.encoding.BloomFilterDefinition;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.DoubleHashing;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.HashingMethod;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.RandomHashing;
import dbs.pprl.toolbox.data_owner.encoding.bloomfilter.HashUtils.HashingAlgorithm;
import dbs.pprl.toolbox.data_owner.encoding.extractor.SimpleExtractorDefinition;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

public class BloomFilterTabContentController extends VBox{
	
	@FXML
	private TitledPane featExtrPane;
	
	@FXML
	private TableView<SimpleExtractorDefinition> extractorRuleTableView;
	
	@FXML
	private Button addExtractorRuleButton;
	
	@FXML
	private Button removeExtractorRuleButton;
		
	@FXML
	private Button generateSeedButton;
	
	@FXML
	private ToggleGroup hashingMethodToggleGroup;
	
	@FXML
	private NumericTextField seedTextField;

	@FXML
	private RadioButton doubleHashingRadioButton;
	
	@FXML
	private RadioButton randomHashingRadioButton;
	
	@FXML
	private ChoiceBox<HashingAlgorithm> doubleHashingFunctionOneChoiceBox;
	
	@FXML
	private ChoiceBox<HashingAlgorithm> doubleHashingFunctionTwoChoiceBox;
	
	@FXML
	private Slider bloomFilterLengthSlider;
	
	@FXML
	private TextField bloomFilterLengthTextField;
	
	private EncodingTabController encodingTabController;
	
	public EncodingTabController getEncodingTabController() {
		return this.encodingTabController;
	}
	
	public BloomFilterTabContentController(EncodingTabController encodingTabController) { 
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/view/BloomFilterTabContent.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		try {
			fxmlLoader.load();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		this.encodingTabController = encodingTabController;
	
		this.doubleHashingFunctionOneChoiceBox.setItems(FXCollections.observableArrayList(HashingAlgorithm.values()));
		this.doubleHashingFunctionOneChoiceBox.setValue(HashingAlgorithm.SHA1);
		
		this.doubleHashingFunctionTwoChoiceBox.setItems(FXCollections.observableArrayList(HashingAlgorithm.values()));
		this.doubleHashingFunctionTwoChoiceBox.setValue(HashingAlgorithm.MD5);
		
		this.bloomFilterLengthTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateBloomFilterLength();	
			}
		});
		
		this.bloomFilterLengthSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				bloomFilterLengthTextField.setText(String.valueOf(newValue.intValue()));
			}
		});
				
		this.hashingMethodToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				final RadioButton selectedBtn = (RadioButton) hashingMethodToggleGroup.getSelectedToggle();
				if (selectedBtn.equals(doubleHashingRadioButton)) {
					doubleHashingFunctionOneChoiceBox.setDisable(false);
					doubleHashingFunctionTwoChoiceBox.setDisable(false);
				}
				else if (selectedBtn.equals(randomHashingRadioButton)) {
					doubleHashingFunctionOneChoiceBox.setDisable(true);
					doubleHashingFunctionTwoChoiceBox.setDisable(true);
				}
			}
		});
		this.setupExtractorRuleTable();
	}
	
	private void setupExtractorRuleTable() {
		final ReadOnlyDoubleProperty tableWidth = this.extractorRuleTableView.widthProperty();

		final TableColumn<SimpleExtractorDefinition, String> nameCol = 
				this.setupNameColumn(tableWidth);
		
		final TableColumn<SimpleExtractorDefinition, String> attrCol = 
				this.setupAttributeColumn(tableWidth);
		
		final TableColumn<SimpleExtractorDefinition, String> funcCol = 
				this.setupFunctionColumn(tableWidth);
		
		final TableColumn<SimpleExtractorDefinition, String> numFuncCol =
				this.setupNumFuncColumn(tableWidth);
		
		this.extractorRuleTableView.getColumns().add(nameCol);
		this.extractorRuleTableView.getColumns().add(attrCol);
		this.extractorRuleTableView.getColumns().add(funcCol);
		this.extractorRuleTableView.getColumns().add(numFuncCol);
	}
	
	private TableColumn<SimpleExtractorDefinition, String> setupNameColumn(ReadOnlyDoubleProperty tableWidth) {
		final TableColumn<SimpleExtractorDefinition, String> nameCol = new TableColumn<>("Rule");
		nameCol.setSortable(false);
		nameCol.prefWidthProperty().bind(tableWidth.divide(4));
		
		nameCol.setCellValueFactory(new PropertyValueFactory<SimpleExtractorDefinition, String>("name"));
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
			
		nameCol.setOnEditCommit(
			(CellEditEvent<SimpleExtractorDefinition, String> event) -> {
	            System.out.println("Name Column: On Edit commit");
				final TablePosition<SimpleExtractorDefinition, String> pos = event.getTablePosition();
	            final String newName = event.getNewValue();
	            final int row = pos.getRow();
	            final SimpleExtractorDefinition extractorDef = event.getTableView().getItems().get(row);
	            extractorDef.setName(newName);
	            event.getTableView().refresh();
	                        
	        });
		
		return nameCol;
	}
	
	private TableColumn<SimpleExtractorDefinition, String> setupAttributeColumn(ReadOnlyDoubleProperty tableWidth) {
		final TableColumn<SimpleExtractorDefinition, String> attrCol = new TableColumn<>("Attributes");
		attrCol.setCellValueFactory(param -> 
			new ReadOnlyStringWrapper(
					param.getValue().getColumnNames().toString()
			)
		);
		attrCol.setSortable(false);
		attrCol.prefWidthProperty().bind(tableWidth.divide(4));
		
		return attrCol;
	}
	
	private TableColumn<SimpleExtractorDefinition, String> setupFunctionColumn(ReadOnlyDoubleProperty tableWidth){
		final TableColumn<SimpleExtractorDefinition, String> funcCol = new TableColumn<>("Feature Extractors");
		funcCol.setCellValueFactory(param -> 
			new ReadOnlyStringWrapper(
				param.getValue().getExtractors().toString()
			)
		);
		funcCol.setSortable(false);
		funcCol.prefWidthProperty().bind(tableWidth.divide(4));
		
		return funcCol;
	}
	
	private TableColumn<SimpleExtractorDefinition, String> setupNumFuncColumn(ReadOnlyDoubleProperty tableWidth){
		final TableColumn<SimpleExtractorDefinition, String> funcCol = new TableColumn<>("# Hash Functions");
		funcCol.setCellValueFactory(param -> 
			new ReadOnlyStringWrapper(
				String.valueOf(param.getValue().getNumberOfHashFunctions())
			)
		);
		funcCol.setSortable(false);
		funcCol.prefWidthProperty().bind(tableWidth.divide(4));
		
		return funcCol;
	}
		
	public ExtractorRuleDialogController showExtractorRuleDialog() {
		final Alert infoAlert = new Alert(AlertType.CONFIRMATION);
		infoAlert.setTitle("Extractor Rule");
		infoAlert.setHeaderText("Please specify the following properties...");
		
		final DialogPane dialogPane = infoAlert.getDialogPane();
		infoAlert.getButtonTypes().retainAll(ButtonType.OK);
		
		final ExtractorRuleDialogController dialogContent = 
				new ExtractorRuleDialogController(this);
		
		dialogPane.setContent(dialogContent);
        dialogPane.getStylesheets().add(getClass().getResource("/gui/style/application.css").toExternalForm());
        dialogPane.getStylesheets().add(getClass().getResource("/gui/style/dialog.css").toExternalForm());
        Optional<ButtonType> res = infoAlert.showAndWait();
		
        if (res.isPresent() && res.get() == ButtonType.OK) {
        	return dialogContent;
        }
        else {
        	return null;
        }
	}
	
	@FXML
	public void addExtractorRule() {
		System.out.println("Add Extractor Rule");
		final ExtractorRuleDialogController extractorRuleDialog = this.showExtractorRuleDialog();
		if (extractorRuleDialog != null) {
			final SimpleExtractorDefinition extractorDef = extractorRuleDialog.getExtractorDefinition();	
			this.extractorRuleTableView.getItems().add(extractorDef);
			this.extractorRuleTableView.refresh();
		}
	}
	
	@FXML
	public void removeExtractorRule() {
		System.out.println("Remove Extractor Rule");
		final int indexOfSelectedRule = this.extractorRuleTableView.getSelectionModel().getSelectedIndex();
		
		if (indexOfSelectedRule != -1) {	
			this.extractorRuleTableView.getItems().remove(indexOfSelectedRule);
			this.extractorRuleTableView.refresh();
		}
	}
	
	@FXML
	public void generateSeed() {
		System.out.println("Generate seed.");
		final Random random = new Random();
		final int seed = random.nextInt();
		this.seedTextField.setText(String.valueOf(seed));
	}
	

	private void updateBloomFilterLength() {
		final String currentValue = bloomFilterLengthTextField.getText();
		try{
			final int newLength = Integer.parseInt(currentValue);
			if (newLength >= bloomFilterLengthSlider.getMin() && newLength <= bloomFilterLengthSlider.getMax()) {
				bloomFilterLengthSlider.setValue(newLength);
			}
		}
		catch (NumberFormatException e) {
			bloomFilterLengthTextField.setText(
				String.valueOf((int) bloomFilterLengthSlider.getValue())
			);
		}	
	}
	@FXML
	public void enterBloomFilterLength() {
		updateBloomFilterLength();
	}
	
	public BloomFilterDefinition getBloomFilterDefinition() {
		final BloomFilterDefinition bfDef = new BloomFilterDefinition();
		
		final List<SimpleExtractorDefinition> extractorDef = this.extractorRuleTableView.getItems();
		bfDef.setFeatureExtractors(extractorDef);
		
		final int bfSize = (int) this.bloomFilterLengthSlider.getValue();
		System.out.println("BF_SIZE: " + bfSize);
		bfDef.setBfLength(bfSize);
		
		final String seedString = this.seedTextField.getText();
		
		final Toggle selectedHashingMethodToggle = this.hashingMethodToggleGroup.getSelectedToggle();
		if (selectedHashingMethodToggle.equals(this.doubleHashingRadioButton)) {
			final HashingAlgorithm hash1 = this.doubleHashingFunctionOneChoiceBox.getValue();
			final HashingAlgorithm hash2 = this.doubleHashingFunctionTwoChoiceBox.getValue();
			final HashingMethod doubleHashing = new DoubleHashing(bfSize, seedString, hash1, hash2);
			bfDef.setHashingMethod(doubleHashing);
		}
		else {
			// Random hashing
			final HashingMethod randomHashing = new RandomHashing(bfSize, seedString);
			bfDef.setHashingMethod(randomHashing);
		}
		
		return bfDef;
	}

}
