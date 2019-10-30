package dbs.pprl.toolbox.data_owner.gui.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckComboBox;

import dbs.pprl.toolbox.data_owner.data.records.Record;
import dbs.pprl.toolbox.data_owner.preprocessing.preprocessors.fieldnormalizer.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class DataCleaningRuleDialogController extends GridPane{
	
	@FXML
	private TextField ruleNameTextField;
	
	@FXML
	private CheckComboBox<TableColumn<Record, ?>> columnCheckComboBox;
	
	@FXML
	private CheckComboBox<Normalizer> normalizerCheckComboBox;
	
	private DataCleaningTabController dataCleaningTabController;
	
	public static List<Normalizer> getAvailableNormalizer(){
		return FXCollections.observableArrayList(
				new AccentRemover(),
				new DigitRemover(),
				new GenderNormalizer(),
				new LetterLowerCaseToNumberNormalizer(),
				new LetterUpperCaseToNumberNormalizer(),
				new LowerCaseNormalizer(),
				new NonDigitRemover(), 
				new NullRemover(),
				new NumberToLetterLowerCaseNormalizer(),
				new NumberToLetterUpperCaseNormalizer(),
				new PunctuationRemover(),
				new SpecialCharacterRemover(),
				new StandardStringNormalizer(),
				new StandardNumberNormalizer(),
				new SubstringNormalizer(0, 1),
				new SubstringNormalizer(0, 2),
				new SubstringNormalizer(0, 3),
				new SubstringNormalizer(0, 4),
				new SubstringNormalizer(0, 5),
				new SubstringNormalizer(0, 6),
				new SubstringNormalizer(0, 7),
				new SubstringNormalizer(0, 8),
				new TrimNormalizer(),
				new UmlautNormalizer(),
				new UpperCaseNormalizer(),
				new WhitespaceRemover()
		);
	}	

	public DataCleaningRuleDialogController(DataCleaningTabController dataCleaningTabController) { 
		final FXMLLoader fxmlLoader = 
				new FXMLLoader(getClass().getResource("/gui/view/DataCleaningRuleDialog.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();	
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		this.dataCleaningTabController = dataCleaningTabController;
	
		final ObservableList<TableColumn<Record, ?>> columns = 
			this.dataCleaningTabController.getMainController().getDataPaneController().getColumnsInDataTable();
			
		this.columnCheckComboBox.setConverter(new StringConverter<TableColumn<Record,?>>() {
			@Override
			public String toString(TableColumn<Record, ?> object) {
				return object.getUserData().toString();
			}
			
			@Override
			public TableColumn<Record, ?> fromString(String string) {
				final ObservableList<TableColumn<Record, ?>> columns = 
						dataCleaningTabController.getMainController()
						.getDataPaneController().getColumnsInDataTable();
				
				for (final TableColumn<Record, ?> col : columns) {
					if (col.getId().equals(string)) {
						return col;
					}
				}
				
				return null;
			}
		});
		
		this.columnCheckComboBox.getItems().addAll(columns);
		this.normalizerCheckComboBox.getItems().addAll(getAvailableNormalizer());	
		
		this.columnCheckComboBox
			.getCheckModel()
			.getCheckedItems()
			.addListener(new ListChangeListener<TableColumn<Record, ?>>() {
				 public void onChanged(ListChangeListener.Change<? extends TableColumn<Record, ?>> c) {
			         final ObservableList<TableColumn<Record, ?>> selectedColumns = columnCheckComboBox.getCheckModel().getCheckedItems();
					 System.out.println(selectedColumns);
			         columnCheckComboBox.setTitle(null);
			     }
			});
		
		this.normalizerCheckComboBox
		.getCheckModel()
		.getCheckedItems()
		.addListener(new ListChangeListener<Normalizer>() {
			 public void onChanged(ListChangeListener.Change<? extends Normalizer> c) {
		         final ObservableList<Normalizer> selectedNormalizer = normalizerCheckComboBox.getCheckModel().getCheckedItems();
		         System.out.println(selectedNormalizer);
		         normalizerCheckComboBox.setTitle(null);
		     }
		});		
	}
	
	public SimpleNormalizeDefinition getSimpleNormalizeDefinition() {
		final String name = this.ruleNameTextField.getText();
		
		final List<TableColumn<Record, ?>> cols = 
				this.columnCheckComboBox.getCheckModel().getCheckedItems();
		
		final List<Normalizer> norms = 
				this.normalizerCheckComboBox.getCheckModel().getCheckedItems();
		
		final SimpleNormalizeDefinition normDef = new SimpleNormalizeDefinition(name);
		normDef.setNormalizer(norms);
		
		final Set<Integer> intCols = cols
			.stream()
			.map(i -> {
				return i.getId();
			})
			.map(Integer::parseInt)
			.collect(Collectors.toSet());
		
		normDef.setColumns(intCols);
		
		final Set<String> colNames = cols
			.stream()
			.map(i -> {
				return i.getUserData().toString();
			})
			.collect(Collectors.toSet());
		
		normDef.setColumNames(colNames);
				
		return normDef;
	}
}