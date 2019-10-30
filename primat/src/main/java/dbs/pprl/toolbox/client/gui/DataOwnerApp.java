package dbs.pprl.toolbox.client.gui;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class DataOwnerApp extends Application {
	
	private Parent rootNode;
	
	
	@Override
	public void init() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/Main.fxml"));
		this.rootNode = loader.load();
//		this.rootNode.getStylesheets().add(getClass().getResource("/gui/style/application.css").toExternalForm());
	}
	
	
	@Override
	public void start(Stage stage) {
		try {			
			Scene scene = new Scene(this.rootNode);
//			scene.getStylesheets().add(getClass().getResource("/gui/style/application.css").toExternalForm());
			stage.setTitle("PRIMAT DO Module");
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/gui/image/linkage.png")));
			stage.setScene(scene);
			stage.show();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
