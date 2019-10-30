package dbs.pprl.toolbox.lu.gui;



import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import org.controlsfx.control.ToggleSwitch;

import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.matching.Matcher;
import dbs.pprl.toolbox.data_owner.gui.controller.NumericTextField;
import dbs.pprl.toolbox.lu.blocking.Blocker;
import dbs.pprl.toolbox.lu.blocking.CandidatePair;
import dbs.pprl.toolbox.lu.blocking.standard.lsh.HLshBlocker;
import dbs.pprl.toolbox.lu.blocking.standard.lsh.HLshBlocker.HLshBlockerBuilder;
import dbs.pprl.toolbox.lu.blocking.standard.lsh.JLshBlocker;
import dbs.pprl.toolbox.lu.blocking.standard.lsh.JLshBlocker.JLshBlockerBuilder;
import dbs.pprl.toolbox.lu.classification.Classificator;
import dbs.pprl.toolbox.lu.classification.ThresholdClassificator;
import dbs.pprl.toolbox.lu.evaluation.IdQualityEvaluator;
import dbs.pprl.toolbox.lu.evaluation.QualityEvaluator;
import dbs.pprl.toolbox.lu.postprocessing.HungarianPostprocessor;
import dbs.pprl.toolbox.lu.postprocessing.MaxBothPostprocessor;
import dbs.pprl.toolbox.lu.postprocessing.Postprocessor;
import dbs.pprl.toolbox.lu.postprocessing.StableMarriagePostprocessor;
import dbs.pprl.toolbox.lu.similarityCalculation.BinarySimilarityCalculator;
import dbs.pprl.toolbox.lu.similarityCalculation.CandidatePairWithSimilarity;
import dbs.pprl.toolbox.lu.similarityCalculation.SimilarityCalculator;
import dbs.pprl.toolbox.lu.similarityFunctions.DiceSimilarity;
import dbs.pprl.toolbox.lu.similarityFunctions.HammingSimilarity;
import dbs.pprl.toolbox.lu.similarityFunctions.JaccardSimilarity;
import dbs.pprl.toolbox.lu.similarityFunctions.SimilarityFunction;
import dbs.pprl.toolbox.utils.CSVReader;
import dbs.pprl.toolbox.utils.CSVToEncodedRecordTransformer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import dbs.pprl.toolbox.lu.matching.BasicMatcher.BasicMatcherBuilder;


public class LUController implements Initializable {
	
	@FXML
	private TabPane menuTabPane;
	
	@FXML
	private TextField datasetATextField;
	
	@FXML
	private TextField datasetBTextField;
	
	@FXML
	private ComboBox<Blocker> blockingMethodComboBox;
	
	@FXML
	private Slider bloomFilterLengthSlider;
	
	@FXML
	private TextField bloomFilterLengthTextField;
	
	@FXML
	private Slider lshKeyLengthSlider;
	
	@FXML
	private TextField lshKeyLengthTextField;
	
	@FXML
	private Slider lshKeySlider;
	
	@FXML
	private TextField lshKeyTextField;
	
	@FXML
	private ComboBox<SimilarityFunction> similarityFunctionComboBox;
	
	@FXML
	private Slider lowerThresholdSlider;
	
	@FXML
	private TextField lowerThresholdTextField;
	
	@FXML
	private Slider upperThresholdSlider;
	
	@FXML
	private TextField upperThresholdTextField;
	
	@FXML
	private ToggleSwitch postprocessingToggleSwitch;
	
	@FXML
	private Label postprocessingMethodLabel;
	
	@FXML
	private ComboBox<Postprocessor> postprocessingMethodComboBox;
	
	@FXML
	private ToggleSwitch collectMetricsToggleSwitch;
	
	@FXML
	private ToggleSwitch qualityEvaluationToggleSwitch;
	
	@FXML
	private ComboBox<QualityEvaluator> evaluatorComboBox;
	
	@FXML
	private Label evaluatorTextField;
	
	@FXML
	private Label noMatchesLabel;
	
	@FXML
	private NumericTextField noMatchesTextField;
	
	private List<EncodedRecord> datasetA;
	
	private List<EncodedRecord> datasetB;
	
	@FXML
	private Tab resultsTab;
	
	@FXML
	private TableView<CandidatePair> matchMappingTableView;
	
	@FXML
	private TableView<Map.Entry<String, Number>> metricsTableView;
	
	@FXML
	private Label matchMappingLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		this.datasetATextField.setMouseTransparent(true);
		this.datasetATextField.setFocusTraversable(false);
		
		this.datasetBTextField.setMouseTransparent(true);
		this.datasetBTextField.setFocusTraversable(false);
		
		this.blockingMethodComboBox.getItems().addAll(
				new HLshBlocker(new HLshBlockerBuilder()),
				new JLshBlocker(new JLshBlockerBuilder())
		);
		
		this.blockingMethodComboBox.getSelectionModel().select(0);
		
		this.similarityFunctionComboBox.getItems().addAll(
			new JaccardSimilarity(),
			new DiceSimilarity(),
			new HammingSimilarity()
		);
		
		this.similarityFunctionComboBox.getSelectionModel().select(0);
		
		//TODO: Use method below for encoding tab in data owner application
		this.bloomFilterLengthTextField.textProperty().bindBidirectional(
			this.bloomFilterLengthSlider.valueProperty(), NumberFormat.getIntegerInstance());
		
		this.lshKeyLengthTextField.textProperty().bindBidirectional(
			this.lshKeyLengthSlider.valueProperty(), NumberFormat.getIntegerInstance());
		
		this.lshKeyTextField.textProperty().bindBidirectional(
			this.lshKeySlider.valueProperty(), NumberFormat.getIntegerInstance());
		
		this.lowerThresholdTextField.textProperty().bindBidirectional(
			this.lowerThresholdSlider.valueProperty(), NumberFormat.getIntegerInstance());
		
		this.upperThresholdTextField.textProperty().bindBidirectional(
			this.upperThresholdSlider.valueProperty(), NumberFormat.getIntegerInstance());
		
		
		this.postprocessingMethodComboBox.disableProperty().bind(
			this.postprocessingToggleSwitch.selectedProperty().not()
		);
		this.postprocessingMethodLabel.disableProperty().bind(
			this.postprocessingToggleSwitch.selectedProperty().not()
		);
			
		this.postprocessingMethodComboBox.getItems().addAll(
			new MaxBothPostprocessor(),
			new StableMarriagePostprocessor(),
			new HungarianPostprocessor()
		);
		
		this.postprocessingMethodComboBox.getSelectionModel().select(0);
		
		this.evaluatorComboBox.getItems().add(new IdQualityEvaluator(0));
		this.evaluatorComboBox.getSelectionModel().select(0);
		
		
		this.evaluatorComboBox.disableProperty().bind(
			this.qualityEvaluationToggleSwitch.selectedProperty().not()
		);
		
		this.evaluatorTextField.disableProperty().bind(
			this.qualityEvaluationToggleSwitch.selectedProperty().not()
		);
		
		this.noMatchesLabel.disableProperty().bind(
			this.qualityEvaluationToggleSwitch.selectedProperty().not()
		);
		
		this.noMatchesTextField.disableProperty().bind(
			this.qualityEvaluationToggleSwitch.selectedProperty().not()
		);
		
		
		final TableColumn<CandidatePair, String> leftRecordIdCol = new TableColumn<>("ID Record A");
		leftRecordIdCol.setCellValueFactory(new PropertyValueFactory<>("leftRecord"));
		
		final TableColumn<CandidatePair, String> rightRecordIdCol = new TableColumn<>("ID Record B");
		rightRecordIdCol.setCellValueFactory(new PropertyValueFactory<>("rightRecord"));
		
		final TableColumn<CandidatePair, String> similarityCol = new TableColumn<>("Similarity");
		similarityCol.setCellValueFactory(new PropertyValueFactory<>("similarity"));

		this.matchMappingTableView.getColumns().add(leftRecordIdCol);
		this.matchMappingTableView.getColumns().add(rightRecordIdCol);
		this.matchMappingTableView.getColumns().add(similarityCol);
		
		
		final TableColumn<Map.Entry<String, Number>, String> metricNameCol = new TableColumn<>("Metric");
		metricNameCol.setCellValueFactory(
			new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Number>, String>, ObservableValue<String>>() {
	            @Override
	            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Number>, String> p) {
	                return new SimpleStringProperty(p.getValue().getKey());
	            }
        });

        final TableColumn<Map.Entry<String, Number>, String> metricValueCol = new TableColumn<>("Value");
        metricValueCol.setCellValueFactory(
        	new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Number>, String>, ObservableValue<String>>() {
	            @Override
	            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, Number>, String> p) {
	                return new SimpleStringProperty(p.getValue().getValue().toString());
	            }
        });

        this.metricsTableView.getColumns().add(metricNameCol);
        this.metricsTableView.getColumns().add(metricValueCol);
	}
	
	@FXML
	public void browseForDatasetA() {
		System.out.println("Browse for dataset A");
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("File for data owner A");
		final File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			final String filePath = file.getAbsolutePath();
			this.datasetATextField.setText(filePath);
			try {
				final CSVReader csvReader = new CSVReader(filePath, false);
				final List<CSVRecord> csvRecords = csvReader.read();
				final CSVToEncodedRecordTransformer trans = new CSVToEncodedRecordTransformer();
				this.datasetA = trans.transform(csvRecords);
			}
			catch (Exception e) {
				//TODO: excepction handling
			}
		}
	}
	
	@FXML
	public void browseForDatasetB() {
		System.out.println("Browse for dataset B");
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("File for data owner B");
		final File file = fileChooser.showOpenDialog(null);

		if (file != null) {
			final String filePath = file.getAbsolutePath();
			this.datasetBTextField.setText(filePath);
			try {
				final CSVReader csvReader = new CSVReader(filePath, false);
				final List<CSVRecord> csvRecords = csvReader.read();
				final CSVToEncodedRecordTransformer trans = new CSVToEncodedRecordTransformer();
				this.datasetB = trans.transform(csvRecords);
			}
			catch (Exception e) {
				//TODO: excepction handling
			}
		}
	}
	 
	private Blocker getBlocker() {
		final int bfSize = (int) this.bloomFilterLengthSlider.getValue();
		final int lshKeys = (int) this.lshKeySlider.getValue();
		final int lshKeyLength = (int) this.lshKeyLengthSlider.getValue();
		
		final Blocker blocker = 
				new HLshBlockerBuilder()
					.setValueRange(bfSize)
					.setKeyRestriction(false)
					.setKeys(lshKeys)
					.setKeySize(lshKeyLength)
					.build();
		return blocker;
	}

	
	private SimilarityCalculator getSimilarityCalculator() {
		final SimilarityFunction simFunc = this.similarityFunctionComboBox.getValue();
		return new BinarySimilarityCalculator(simFunc);		
	}
	 
	private Classificator getClassificator() {
		final double threshold = this.upperThresholdSlider.getValue() / 100;
		final Classificator classificator = new ThresholdClassificator(threshold);
		return classificator;
	}
	
	private Postprocessor getPostprocessor() {
		if (this.postprocessingToggleSwitch.isSelected()) {
			return this.postprocessingMethodComboBox.getValue();
		}
		else {
			return null;
		}
	}
	
	private QualityEvaluator getQualityEvaluator() {
		final QualityEvaluator qualityEvaluator;
		if (this.qualityEvaluationToggleSwitch.isSelected()) {
			try {
				final int realMatches = Integer.valueOf(this.noMatchesTextField.getText());
				qualityEvaluator = new IdQualityEvaluator(realMatches);
			}
			catch (NumberFormatException e) {
				//TODO: Show error dialog
				return null;
			}
		}
		else {
			qualityEvaluator = null;
		}
		return qualityEvaluator;
	}
	
	private void showResult(Set<CandidatePairWithSimilarity> linkageResult, Map<String, Number> metrics) {
		this.showMatches(linkageResult);
		this.showMetrics(metrics);
        this.menuTabPane.getSelectionModel().selectLast();
	}
	
	private void showMatches(Set<CandidatePairWithSimilarity> linkageResult) {
		this.matchMappingTableView.getItems().clear();
		this.matchMappingTableView.getItems().addAll(FXCollections.observableArrayList(linkageResult));
		this.matchMappingTableView.refresh();
		this.matchMappingLabel.setText("#Matches: " + linkageResult.size());
	}
	
	private void showMetrics(Map<String, Number> metrics) {
		final ObservableList<Map.Entry<String, Number>> items = 
        		FXCollections.observableArrayList(metrics.entrySet());
        this.metricsTableView.getItems().clear();
        this.metricsTableView.getItems().addAll(items);
        this.metricsTableView.refresh();
	}
	
	@FXML
	public void startLinkage() {
		if (this.datasetA != null && this.datasetB != null) {
			final Blocker blocker = this.getBlocker();		
			final SimilarityCalculator similarityCalculator = this.getSimilarityCalculator();
			final Classificator classificator = this.getClassificator();
			final Postprocessor postprocessor = this.getPostprocessor();
			final QualityEvaluator qualityEvaluator = this.getQualityEvaluator();
			
			final Matcher matcher = new BasicMatcherBuilder()
				.setBlocker(blocker)
				.setSimilarityCalculator(similarityCalculator)
				.setClassificator(classificator)
				.setPostprocessor(postprocessor)
				.setQualityEvaluator(qualityEvaluator)
				.build();
			
			final Set<CandidatePairWithSimilarity> linkageResult = 
					matcher.match(this.datasetA, this.datasetB);

			final Map<String, Number> metrics = 
					matcher.getMetrics();
			
			this.showResult(linkageResult, metrics);		
		}
		else {
			this.showNoDataError();
		}
	}
	
	private void showNoDataError() {
		final Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText("Datasets not specified!");
		errorAlert.setContentText("Please select the dataset files in the input data section.");
		errorAlert.getDialogPane().getStylesheets().add(
				getClass().getResource("/gui/style/application.css").toExternalForm()
		);
		errorAlert.getDialogPane().getStylesheets().add(
				getClass().getResource("/gui/style/dialog.css").toExternalForm()
		);
		//TIP: Necessary to make alert wrap message text. 
		errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		errorAlert.showAndWait();
	}
}