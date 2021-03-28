package apprendreSQL.view.version2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application{ 

	private static final String title = "SQLSchool";

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlloader = new FXMLLoader();
		Parent parent = fxmlloader.load(getClass().getResource("fileXml/gui.fxml").openStream());
		Scene scene = new Scene(parent,1280,800);
		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
		primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
