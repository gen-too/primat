package dbs.pprl.toolbox.client.gui.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import dbs.pprl.toolbox.client.data.records.Record;
import dbs.pprl.toolbox.client.encoding.extractor.BigramExtractor;
import dbs.pprl.toolbox.client.encoding.extractor.FeatureExtractor;
import dbs.pprl.toolbox.client.encoding.extractor.IdentityExtractor;
import dbs.pprl.toolbox.client.encoding.extractor.SimpleExtractorDefinition;
import dbs.pprl.toolbox.client.encoding.extractor.TrigramExtractor;
import dbs.pprl.toolbox.client.encoding.extractor.UnigramExtractor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class ExtractorRuleDialogController extends GridPane{
	
	@FXML
	private TextField ruleNameTextField; 
	
	@FXML
	private CheckComboBox<TableColumn<Record, ?>> columnsToExtractCheckComboBox;
	
	@FXML
	private CheckComboBox<FeatureExtractor> extractorFunctionsToUseCheckComboBox;
	
	@FXML
	private Slider hashFunctionsSlider;
	
	@FXML
	private TextField hashFunctionsTextField;
	
	private BloomFilterTabContentController bloomFilterTabContentController;
	
	public static List<FeatureExtractor> getAvailableExtractors(){
		return FXCollections.observableArrayList(
				new UnigramExtractor(),
				new BigramExtractor(),
				new TrigramExtractor(),
				new IdentityExtractor()
		);
	}	

	public ExtractorRuleDialogController(BloomFilterTabContentController bloomFilterTabContentController) { 
		final FXMLLoader fxmlLoader = 
				new FXMLLoader(getClass().getResource("/gui/view/ExtractorRuleDialog.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();	
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		this.bloomFilterTabContentController = bloomFilterTabContentController;
	
		final ObservableList<TableColumn<Record, ?>> columns = 
			this.bloomFilterTabContentController
				.getEncodingTabController()
				.getMainController()
				.getDataPaneController()
				.getColumnsInDataTable();
			
		this.columnsToExtractCheckComboBox.setConverter(new StringConverter<TableColumn<Record,?>>() {
			@Override
			public String toString(TableColumn<Record, ?> object) {
				return object.getUserData().toString();
			}
			
			@Override
			public TableColumn<Record, ?> fromString(String string) {
				final ObservableList<TableColumn<Record, ?>> columns = 
						bloomFilterTabContentController
						.getEncodingTabController()
						.getMainController()
						.getDataPaneController()
						.getColumnsInDataTable();
				
				for (final TableColumn<Record, ?> col : columns) {
					if (col.getId().equals(string)) {
						return col;
					}
				}
				
				return null;
			}
		});
		
		
		
		this.columnsToExtractCheckComboBox.getItems().addAll(columns);
		this.extractorFunctionsToUseCheckComboBox.getItems().addAll(getAvailableExtractors());	
		
		this.columnsToExtractCheckComboBox
			.getCheckModel()
			.getCheckedItems()
			.addListener(new ListChangeListener<TableColumn<Record, ?>>() {
				 public void onChanged(ListChangeListener.Change<? extends TableColumn<Record, ?>> c) {
			         final ObservableList<TableColumn<Record, ?>> selectedColumns = 
			        		 columnsToExtractCheckComboBox.getCheckModel().getCheckedItems();
					 System.out.println(selectedColumns);
					 columnsToExtractCheckComboBox.setTitle(null);
			     }
			});
		
		this.extractorFunctionsToUseCheckComboBox
		.getCheckModel()
		.getCheckedItems()
		.addListener(new ListChangeListener<FeatureExtractor>() {
			 public void onChanged(ListChangeListener.Change<? extends FeatureExtractor> c) {
		         final ObservableList<FeatureExtractor> selectedNormalizer = 
		        		 extractorFunctionsToUseCheckComboBox.getCheckModel().getCheckedItems();
		         System.out.println(selectedNormalizer);
		         extractorFunctionsToUseCheckComboBox.setTitle(null);
		     }
		});	
		
		this.hashFunctionsTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateHashFunctionNumber();	
			}
		});
		
		this.hashFunctionsSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				hashFunctionsTextField.setText(String.valueOf(newValue.intValue()));
			}
		});
	}
	
	private void updateHashFunctionNumber() {
		final String currentValue = hashFunctionsTextField.getText();
		try{
			final int newLength = Integer.parseInt(currentValue);
			if (newLength >= hashFunctionsSlider.getMin() && newLength <= hashFunctionsSlider.getMax()) {
				hashFunctionsSlider.setValue(newLength);
			}
		}
		catch (NumberFormatException e) {
			hashFunctionsTextField.setText(
				String.valueOf((int) hashFunctionsSlider.getValue())
			);
		}	
	}
	
	@FXML
	public void enterHashFunctionNumber() {
		updateHashFunctionNumber();
	}

	public SimpleExtractorDefinition getExtractorDefinition() {
		final String name = this.ruleNameTextField.getText();
		
		final List<TableColumn<Record, ?>> columns = 
				this.columnsToExtractCheckComboBox.getCheckModel().getCheckedItems();
		
		final List<FeatureExtractor> extractors =
				this.extractorFunctionsToUseCheckComboBox.getCheckModel().getCheckedItems();
		
		final SimpleExtractorDefinition extractorDef = new SimpleExtractorDefinition(name);
		extractorDef.setExtractors(extractors);
		
		final int hashes = (int) this.hashFunctionsSlider.getValue();
		System.out.println("HASHES: " + hashes);
		extractorDef.setNumberOfHashFunctions(hashes);
		
		//TODO: separate this function as it is used multiple times in different classes
		final Set<Integer> intCols = columns
				.stream()
				.map(i -> {
					return i.getId();
				})
				.map(Integer::parseInt)
				.collect(Collectors.toSet());
			
		extractorDef.setColumns(intCols);
			
		final Set<String> colNames = columns
			.stream()
			.map(i -> {
				return i.getUserData().toString();
			})
			.collect(Collectors.toSet());
		
		extractorDef.setColumNames(colNames);
		
		return extractorDef;
	}
}