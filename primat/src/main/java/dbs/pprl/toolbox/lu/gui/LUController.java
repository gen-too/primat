package dbs.pprl.toolbox.lu.gui;


import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;
import org.controlsfx.control.ToggleSwitch;

import dbs.pprl.toolbox.lu.blocking.Blocker;
import dbs.pprl.toolbox.lu.blocking.standard.lsh.HLshBlocker.HLshBlockerBuilder;
import dbs.pprl.toolbox.lu.classification.Classificator;
import dbs.pprl.toolbox.lu.classification.ThresholdClassificator;
import dbs.pprl.toolbox.lu.evaluation.IdQualityEvaluator;
import dbs.pprl.toolbox.lu.evaluation.QualityEvaluator;
import dbs.pprl.toolbox.lu.matching.EncodedRecord;
import dbs.pprl.toolbox.lu.matching.Matcher;
import dbs.pprl.toolbox.lu.matching.BasicMatcher.BasicMatcherBuilder;
import dbs.pprl.toolbox.lu.postprocessing.BypassPostprocessor;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;




public class LUController implements Initializable {
	
			
	@FXML
	private TextField datasetATextField;
	
	@FXML
	private TextField datasetBTextField;
	
	@FXML
	private ComboBox<String> blockingMethodComboBox;
	
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
	private ComboBox<Postprocessor> postprocessingMethodComboBox;
	
	/*
	@FXML
	private Pane inputDataPane;
	
	@FXML
	private Pane blockingPane;
	
	@FXML
	private Pane comparisonClassificationPane;
	
	@FXML
	private Pane postprocessingPane;
	
	@FXML
	private Pane evaluationPane;
	
	
	@FXML
	private ToggleButton inputDataToggleButton;
	
	@FXML
	private ToggleButton blockingToggleButton;
	
	@FXML
	private ToggleButton comparisonClassificationToggleButton;
	
	@FXML
	private ToggleButton postprocessingToggleButton;
	
	@FXML
	private ToggleButton evaluationToggleButton;
	
	@FXML
	private ToggleGroup leftMenuToggleGroup;
	*/
	
	@FXML
	private ToggleSwitch collectMetricsToggleSwitch;
	
	@FXML
	private ComboBox<String> evaluatorComboBox;
	
	private List<EncodedRecord> datasetA;
	
	private List<EncodedRecord> datasetB;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		/*this.leftMenuToggleGroup.selectToggle(this.inputDataToggleButton);
		
		this.leftMenuToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (newValue == null) {
					leftMenuToggleGroup.selectToggle(oldValue);
				}
				else if (newValue.equals(inputDataToggleButton)) {
					inputDataPane.toFront();
				}
				else if (newValue.equals(blockingToggleButton)) {
					blockingPane.toFront();
				}
				else if (newValue.equals(comparisonClassificationToggleButton)) {
					comparisonClassificationPane.toFront();
				}
				else if (newValue.equals(postprocessingToggleButton)) {
					postprocessingPane.toFront();
				}
				else if (newValue.equals(evaluationToggleButton)) {
					evaluationPane.toFront();
				}
			}
	});
		*/
		this.datasetATextField.setMouseTransparent(true);
		this.datasetATextField.setFocusTraversable(false);
		
		this.datasetBTextField.setMouseTransparent(true);
		this.datasetBTextField.setFocusTraversable(false);
		
		this.blockingMethodComboBox.getItems().add("Locality-sensitive Hashing (HLSH)");
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
		
		this.postprocessingMethodComboBox.getItems().addAll(
			new BypassPostprocessor(),
			new MaxBothPostprocessor(),
			new StableMarriagePostprocessor(),
			new HungarianPostprocessor()
		);
		
		this.postprocessingMethodComboBox.getSelectionModel().select(0);
		
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
				final CSVToEncodedRecordTransformer csvPojoTransformer = new CSVToEncodedRecordTransformer();
				this.datasetA = csvPojoTransformer.transform(csvRecords);
			}
			catch (Exception e) {
				
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
				final CSVToEncodedRecordTransformer csvPojoTransformer = new CSVToEncodedRecordTransformer();
				this.datasetB = csvPojoTransformer.transform(csvRecords);
			}
			catch (Exception e) {
				
			}
		}
	}
	
	@FXML
	public void startLinkage() {
		System.out.println(this.postprocessingMethodComboBox.getValue());
		if (this.datasetA != null && this.datasetB != null) {
			final double threshold = this.upperThresholdSlider.getValue() / 100;
			final int bfSize = (int) this.bloomFilterLengthSlider.getValue();
			final int lshKeys = (int) this.lshKeySlider.getValue();
			final int lshKeyLength = (int) this.lshKeyLengthSlider.getValue();
			final SimilarityFunction simFunc = this.similarityFunctionComboBox.getValue();
			
			final Blocker blocker = 
					new HLshBlockerBuilder()
						.setValueRange(bfSize)
						.setKeyRestriction(false)
						.setKeys(lshKeys)
						.setKeySize(lshKeyLength)
						.build();
					
			final SimilarityCalculator similarityCalculator = new BinarySimilarityCalculator(simFunc);
			final Classificator classificator = new ThresholdClassificator(threshold);
			final Postprocessor postprocessor = this.postprocessingMethodComboBox.getValue();
			final QualityEvaluator qualityEvaluator = new IdQualityEvaluator(1); 
			
			final Matcher matcher = new BasicMatcherBuilder()
				.setBlocker(blocker)
				.setSimilarityCalculator(similarityCalculator)
				.setClassificator(classificator)
				.setPostprocessor(postprocessor)
				.setQualityEvaluator(qualityEvaluator)
				.build();
			
			final Set<CandidatePairWithSimilarity> linkageResult = matcher.match(this.datasetA, this.datasetB);
			System.out.println(matcher.getMetrics());
			System.out.println(linkageResult.size());
//			linkageResult.forEach(pair -> {
//				System.out.println(pair.getLeftRecord().getId() + ", " + pair.getRightRecord().getId() + ", " + pair.getSimilarity());
//			});

		}
	}
}