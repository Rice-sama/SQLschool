package apprendreSQL.view.version2;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import apprendreSQL.Controller.version1.EventManager;
import apprendreSQL.view.version2.fileXml.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Gui extends Application{ 

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent parent = FXMLLoader.load(getClass().getResource(Config.getHomaPage()));		
		Scene scene = new Scene(parent,Config.WIDTH, Config.HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		  launch(args);
	}
}
