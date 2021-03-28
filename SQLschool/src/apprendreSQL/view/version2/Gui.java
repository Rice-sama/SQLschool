package apprendreSQL.view.version2;

<<<<<<< HEAD
import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import apprendreSQL.Controller.version1.EventManager;
import apprendreSQL.view.version2.fileXml.Config;
=======
>>>>>>> 94403e61c9b85c6732ccac968761e4a780b14e58
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application{ 

	private static final String title = "SQLSchool";

	@Override
	public void start(Stage primaryStage) throws Exception {
<<<<<<< HEAD
		
		Parent parent = FXMLLoader.load(getClass().getResource(Config.getHomaPage()));		
		Scene scene = new Scene(parent,Config.WIDTH, Config.HEIGHT);
=======
		FXMLLoader fxmlloader = new FXMLLoader();
		Parent parent = fxmlloader.load(getClass().getResource("fileXml/gui.fxml").openStream());
		Scene scene = new Scene(parent,1280,800);
>>>>>>> 94403e61c9b85c6732ccac968761e4a780b14e58
		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
		primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		  launch(args);
	}
}
