package apprendreSQL.Controller;

import java.io.IOException;

import apprendreSQL.View.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GuiController {
	
	@FXML
	public void backToHomePage(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
     	Parent pan = FXMLLoader.load(getClass().getResource(Config.setFileName("home")));
     	Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}
	
	
	@FXML 
	public void openTutoriel(MouseEvent event) throws IOException {

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
	public void openCreate(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		FXMLLoader loader  = new FXMLLoader(getClass().getResource(Config.setFileName("fileView")));
		loader.setController(new FileViewController(0));
		Parent pan = loader.load();
		Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}
	
	@FXML
	public void openModify(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		FXMLLoader loader  = new FXMLLoader(getClass().getResource(Config.setFileName("fileView")));
		loader.setController(new FileViewController(1));
		Parent pan = loader.load();
		Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}
	
	
	
}
