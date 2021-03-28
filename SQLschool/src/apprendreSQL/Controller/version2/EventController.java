package apprendreSQL.Controller.version2;

import java.io.IOException;

import apprendreSQL.view.version2.fileXml.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class EventController {

	
	
	@FXML
	public void backOnHomePage(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
     	AnchorPane pan = FXMLLoader.load(getClass().getResource(Config.setFileName("home")));
     	Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}
	
	
	@FXML 
	public void openTutoriel(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
     	AnchorPane pan = FXMLLoader.load(getClass().getResource(Config.setFileName("chapitre")));
     	Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}	
	
	
	// à remplire 
	@FXML 
	public void openExcercice(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		AnchorPane pan = FXMLLoader.load(getClass().getResource(Config.setFileName("chapitre")));
     	Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}	
	

	// encours de construction 
	@FXML 
	public void openTest(MouseEvent event) {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		System.out.println("test is open");
	}	
	
	@FXML 
	public void openParametre(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		AnchorPane pan = FXMLLoader.load(getClass().getResource(Config.setFileName("parametre")));
		Scene scene = new Scene(pan);
     	stage.setScene(scene);
		
	}	
	
	@FXML
	public void openFormulaireCreate(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		AnchorPane pan = FXMLLoader.load(getClass().getResource(Config.setFileName("fomCreateQuestion")));
		Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}
	
	
	
	
}
