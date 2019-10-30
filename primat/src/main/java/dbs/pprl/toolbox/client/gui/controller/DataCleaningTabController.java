package dbs.pprl.toolbox.client.gui.controller;


import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.controlsfx.dialog.ProgressDialog;

import dbs.pprl.toolbox.client.preprocessing.preprocessors.fieldnormalizer.*;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


public class DataCleaningTabController implements Initializable {
		
	@FXML
	private TableView<SimpleNormalizeDefinition> ruleTableView;
	
	@FXML
	private Button addRuleButton;
	
	@FXML
	private Button removeRuleButton;
	
	@FXML
	private Button ruleUpButton;
	
	@FXML
	private Button ruleDownButton;
	
	@FXML
	private Button confirmButton;
		
	private MainController mainController;

	public MainController getMainController() {
		return this.mainController;
	}
		
	private void setupRuleTable() {
		final ReadOnlyDoubleProperty tableWidth = this.ruleTableView.widthProperty();

		final TableColumn<SimpleNormalizeDefinition, String> nameCol = 
				this.setupNameColumn(tableWidth);
		
		final TableColumn<SimpleNormalizeDefinition, String> attrCol = 
				this.setupAttributeColumn(tableWidth);
		
		final TableColumn<SimpleNormalizeDefinition, String> funcCol = 
				this.setupFunctionColumn(tableWidth);
		
		this.ruleTableView.getColumns().add(nameCol);
		this.ruleTableView.getColumns().add(attrCol);
		this.ruleTableView.getColumns().add(funcCol);
	}
	
	private TableColumn<SimpleNormalizeDefinition, String> setupNameColumn(ReadOnlyDoubleProperty tableWidth) {
		final TableColumn<SimpleNormalizeDefinition, String> nameCol = new TableColumn<>("Rule");
		nameCol.setSortable(false);
		nameCol.prefWidthProperty().bind(tableWidth.divide(4));
		
		nameCol.setCellValueFactory(new PropertyValueFactory<SimpleNormalizeDefinition, String>("name"));
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
			
		nameCol.setOnEditCommit(
			(CellEditEvent<SimpleNormalizeDefinition, String> event) -> {
	            System.out.println("Name Column: On Edit commit");
				final TablePosition<SimpleNormalizeDefinition, String> pos = event.getTablePosition();
	            final String newName = event.getNewValue();
	            final int row = pos.getRow();
	            final SimpleNormalizeDefinition normDef = event.getTableView().getItems().get(row);
	            normDef.setName(newName);
	            event.getTableView().refresh();
	                        
	        });
		
		return nameCol;
	}
	
	private TableColumn<SimpleNormalizeDefinition, String> setupAttributeColumn(ReadOnlyDoubleProperty tableWidth) {
		final TableColumn<SimpleNormalizeDefinition, String> attrCol = new TableColumn<>("Attributes");
		attrCol.setCellValueFactory(param -> 
			new ReadOnlyStringWrapper(
					param.getValue().getColumnNames().toString()
			)
		);
		attrCol.setSortable(false);
		attrCol.prefWidthProperty().bind(tableWidth.divide(4));
		
		return attrCol;
	}
	
	private TableColumn<SimpleNormalizeDefinition, String> setupFunctionColumn(ReadOnlyDoubleProperty tableWidth){
		final TableColumn<SimpleNormalizeDefinition, String> funcCol = new TableColumn<>("Normalizer Functions");
		funcCol.setCellValueFactory(param -> 
			new ReadOnlyStringWrapper(
				param.getValue().getNormalizer().toString()
			)
		);
		funcCol.setSortable(false);
		funcCol.prefWidthProperty().bind(tableWidth.divide(2));
		
		return funcCol;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.setupRuleTable();	
	}
	
	
	public void setup() {}
	
	@FXML
	public void skipDataCleaning() {
		this.mainController.doTransitionFromDataCleaningTabToEncodingTab();
	}
	
	public DataCleaningRuleDialogController showCleaningRuleDialog() {
		final Alert infoAlert = new Alert(AlertType.CONFIRMATION);
		infoAlert.setTitle("Data Cleaning Rule");
		infoAlert.setHeaderText("Please specify the following properties...");
		
		final DialogPane dialogPane = infoAlert.getDialogPane();
		infoAlert.getButtonTypes().retainAll(ButtonType.OK);
		
		final DataCleaningRuleDialogController dialogContent = 
				new DataCleaningRuleDialogController(this);
		
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
	public void addRule() {
		System.out.println("Add Rule.");				
		
		final DataCleaningRuleDialogController ruleDialog = this.showCleaningRuleDialog();
		if (ruleDialog != null) {
			final SimpleNormalizeDefinition simpleNormDef = ruleDialog.getSimpleNormalizeDefinition();	
			this.ruleTableView.getItems().add(simpleNormDef);
			this.ruleTableView.refresh();
		}
	}
	
	@FXML
	public void removeRule() {
		System.out.println("Remove rule clicked");
		final int indexOfSelectedRule = this.ruleTableView.getSelectionModel().getSelectedIndex();
		
		if (indexOfSelectedRule != -1) {	
			this.ruleTableView.getItems().remove(indexOfSelectedRule);
			this.ruleTableView.refresh();
		}
	}
	
	@FXML
	public void moveRuleUp() {
		System.out.println("Move Role Up.");
		final int index = this.ruleTableView.getSelectionModel().getSelectedIndex();
		if (index > 0) {
			final SimpleNormalizeDefinition row = this.ruleTableView.getItems().remove(index);
			this.ruleTableView.getItems().add(index-1, row);
			this.ruleTableView.getSelectionModel().selectPrevious();
		}
	}
	
	@FXML
	public void moveRuleDown() {
		System.out.println("Move Role Down");
		final int index = this.ruleTableView.getSelectionModel().getSelectedIndex();
		if (index < this.ruleTableView.getItems().size() - 1) {
			final SimpleNormalizeDefinition row = this.ruleTableView.getItems().remove(index);
			this.ruleTableView.getItems().add(index+1, row);
			this.ruleTableView.getSelectionModel().select(index+1);
		}
	}

	@FXML
	public void confirm() {
		System.out.println("Confirm data cleaning");
	
		final List<NormalizeDefinition> normDefs = 
				this.ruleTableView
					.getItems()
					.stream()
					.map(NormalizeDefinition::from)
					.collect(Collectors.toList());
		
		
		final NormalizeDefinition normDef = new NormalizeDefinition();
		normDefs.forEach(def -> {
			normDef.merge(def);
		});
		
		final FieldNormalizer fNormalizer = new FieldNormalizer(normDef);
		
		final Task<Void> preprocessor = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				fNormalizer.apply(mainController.getDataPaneController().getRecordsInDataTable());
				return null;
			}
			
		};
		preprocessor.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				mainController.getDataPaneController().refreshDataTable();
				mainController.doTransitionFromDataCleaningTabToEncodingTab();
			}
		});
		
		preprocessor.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("Data cleaning failed!");
			}
		});
		
		final ProgressDialog dialog = new ProgressDialog(preprocessor);        
		dialog.setTitle("Normalization in progress...");
		new Thread(preprocessor).start();
		dialog.showAndWait();
	}
		
	public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }	
}