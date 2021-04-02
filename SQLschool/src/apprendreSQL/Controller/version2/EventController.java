package apprendreSQL.Controller.version2;

import java.io.IOException;
import apprendreSQL.view.version2.fileXml.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EventController {
	
	@FXML
	public void backToHomePage(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
     	Parent pan = FXMLLoader.load(getClass().getResource(Config.setFileName("home")));
     	Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}
	
	
	@FXML 
	public void openTutoriel(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
     	Parent pan = FXMLLoader.load(getClass().getResource(Config.setFileName("chapitre")));
     	Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}	
	
	
	@FXML 
	public void openExercice(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		Parent pan = FXMLLoader.load(getClass().getResource(Config.setFileName("gui")));
     	Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}	
	
	@FXML 
	public void openParametre(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		Parent pan = FXMLLoader.load(getClass().getResource(Config.setFileName("parametre")));
		Scene scene = new Scene(pan);
     	stage.setScene(scene);
		
	}	
	
	@FXML
	public void openFormulaireCreate(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		Parent pan = FXMLLoader.load(getClass().getResource(Config.setFileName("fileView")));
		Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}
	
	
	
	
}
