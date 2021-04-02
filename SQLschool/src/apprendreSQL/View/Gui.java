package apprendreSQL.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application{ 

	private static final String title = "SQLSchool";

	@Override
	public void start(Stage primaryStage) throws Exception {

        Parent parent = FXMLLoader.load(getClass().getResource(Config.getHomaPage()));       
        Scene scene = new Scene(parent,Config.WIDTH, Config.HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		  launch(args);
	}
}
