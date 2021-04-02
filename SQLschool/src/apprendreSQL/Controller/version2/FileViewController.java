package apprendreSQL.Controller.version2;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import apprendreSQL.Controller.version1.JsonManager;
import apprendreSQL.Model.analysisTypeMetier.semantique.Test;
import apprendreSQL.view.version1.HighlightListener;
import apprendreSQL.view.version2.fileXml.Config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FileViewController {
	
	private ArrayList<QuestionViewController> questions = new ArrayList<>();
	
	private JsonManager jsonManager;
	
	@FXML VBox editBox;
	
	@FXML TextField fileName;
	@FXML TextField questionName;
	@FXML Accordion questionExplorer;
	
	@FXML Label saveInfo;
	
	@FXML Label successText;
	
	@FXML
	private void addSubject() {
		FXMLLoader loader = new FXMLLoader();
		try {
			TitledPane tp = loader.load(getClass().getResource(Config.setFileName("chapitre")).openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void backToHomePage(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
     	Parent pan = FXMLLoader.load(getClass().getResource(Config.setFileName("home")));
     	Scene scene = new Scene(pan);
     	stage.setScene(scene);
	}

	@FXML
	private void addQuestion() {
     	try {
     		if(questionName.getText().isEmpty()) return;
     		FXMLLoader loader = new FXMLLoader();
			TitledPane pane = loader.load(getClass().getResource(Config.setFileName("questionView")).openStream());
			QuestionViewController qvc = loader.getController();
			qvc.initTitle(questionName.getText());
			questionName.setText("");
			questions.add(qvc);
			questionExplorer.getPanes().add(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onSave() {
		String name = fileName.getText();
		if(!name.endsWith(".json")) name +=".json";
		if(name.length() > 5) {
			for(QuestionViewController qvc : questions) {
				
			}
			if (jsonManager.getListQuestion().size() > 0) {
				jsonManager.createJSON("resource/" + name);
				editBox.setVisible(false);
				successText.setVisible(true);
			} else {
				saveInfo.setText("Vous devez ajouter au moins une question.");
			}
		} else {
			saveInfo.setText("Le nom du fichier est trop court.");
		}
	}
}
