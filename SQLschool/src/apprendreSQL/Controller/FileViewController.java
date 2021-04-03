package apprendreSQL.Controller;

import java.io.IOException;
import java.util.ArrayList;
import apprendreSQL.View.Config;
import apprendreSQL.View.GetInformation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FileViewController implements GetInformation{
	
	private static ArrayList<String> dbs = new ArrayList<>();
	private static ArrayList<String> subjects = new ArrayList<String>();	
	private static ArrayList<QuestionViewController> questions = new ArrayList<>();
	private JsonManager jsonManager;
	
	@FXML VBox editBox;
	@FXML TextField fileName;
	@FXML TextField questionName;
	@FXML Accordion questionExplorer;
	@FXML Label saveInfo;
	@FXML Label successText;
	@FXML TextField subjectName;
	@FXML VBox subjectExplorer;
	
	@FXML
	private void initialize() {
		dbs = getDbFiles();
		questions.clear();
		subjects.clear();
		jsonManager = new JsonManager();
	}
	
	@FXML
	private void addSubject() {
		if(subjectName.getText().isBlank()) return;
		try {
			HBox subject = FXMLLoader.load(getClass().getResource(Config.setFileName("subjectItem")));
			String sbj = subjectName.getText();
			subjects.add(sbj);
			subjectName.setText("");
			((Label)((VBox) subject.getChildren().get(0)).getChildren().get(0)).setText(sbj);
			((Button)subject.getChildren().get(1)).setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			        subjectExplorer.getChildren().remove(subject);
			        subjects.remove(sbj);
			        updateSubjectLists();
			    }
			});
			subjectExplorer.getChildren().add(subject);
	        updateSubjectLists();
		} catch (IOException e) {
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
     		if(questionName.getText().isBlank()) return;
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
			try {
				for(QuestionViewController qvc : questions) {
					qvc.checkValid();
				}
			} catch (Exception e) {
				saveInfo.setText(e.getMessage());
				return;
			}
			for(QuestionViewController qvc : questions) {
				jsonManager.addQuestion(qvc.getDB(), qvc.getSubject(), qvc.getTitle(), qvc.getContent(), qvc.getAnswer(), qvc.getTestList());
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
	
	public static ArrayList<String> retrieveDBs(){
		return dbs;
	}
	
	public static void removeQuestion(QuestionViewController qvc) {
		questions.remove(qvc);
	}
	
	private static void updateSubjectLists() {
		for(QuestionViewController qvc : questions) {
			qvc.updateSubjectList(subjects);
		}
		
	}
}
