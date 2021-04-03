package apprendreSQL.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import apprendreSQL.Model.job.semantique.Test;
import apprendreSQL.Model.job.syntaxique.general.ParseException;
import apprendreSQL.Model.job.syntaxique.general.ParserSQL1;
import apprendreSQL.View.Config;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class QuestionViewController {
	
	private ArrayList<TestViewController> tests = new ArrayList<>();

	@FXML TitledPane pane;
	@FXML TextField title;
	@FXML ComboBox<String> subject;
	@FXML ComboBox<String> db;
	@FXML TextArea question;
	@FXML TextArea answer;
	@FXML TextField testName;
	@FXML Label deleteConfirm;
	@FXML TabPane testExplorer;
	
	@FXML
	private void initialize() {
		db.getItems().addAll(FileViewController.retrieveDBs());
	}
	
	@FXML 
	public void checkValid() throws Exception {
		if(title.getText().isBlank()) throw new Exception("Une question n'a pas de titre.");
		if(db.getValue()==null) throw new Exception(title.getText()+" n'a pas de base de données.");
		if(subject.getValue()==null) throw new Exception(title.getText()+" n'a pas de catégorie.");
		if(question.getText().isBlank()) throw new Exception(title.getText()+" n'a pas d'énoncé.");
		if(answer.getText().isBlank()) throw new Exception(title.getText()+" n'a pas de réponse.");
		else {
			ParserSQL1 parser = new ParserSQL1(new ByteArrayInputStream(answer.getText().getBytes()));
			try {
				parser.sqlStmtList();
			} catch (ParseException e) {
				throw new Exception(title.getText()+" n'a pas une réponde valide.");
			}
		}
	}
	
	@FXML
	private void updateName() {
		pane.setText(title.getText());
	}
	
	@FXML
	private void addTest() {
		if(testName.getText().isBlank()) return;
		try {
     		FXMLLoader loader = new FXMLLoader();
			Tab tab = loader.load(getClass().getResource(Config.setFileName("testTab")).openStream());
			TestViewController tvc = loader.getController();
			tab.setOnClosed(new EventHandler<Event>()
			{
			    @Override
			    public void handle(Event arg0) 
			    {
			        tests.remove(tvc);
			    }
			});
			tvc.initTitle(testName.getText());
			testName.setText("");
			tests.add(tvc);
			testExplorer.getTabs().add(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void delete() {
		if(deleteConfirm.getText().isBlank()) deleteConfirm.setText("Supprimer?");
		else {
			FileViewController.removeQuestion(this);
			((Accordion) pane.getParent()).getPanes().remove(pane);
		}
	}
	
	public void initTitle(String s) {
		pane.setText(s);
		title.setText(s);
	}

	public void updateSubjectList(ArrayList<String> subjects) {
		String current = subject.getValue();
		subject.getItems().setAll(subjects);
		if(subject.getItems().contains(current)) subject.setValue(current);
	}

	public String getDB() {
		return db.getValue();
	}

	public String getSubject() {
		return subject.getValue();
	}

	public String getTitle() {
		return title.getText();
	}

	public String getContent() {
		return question.getText();
	}

	public String getAnswer() {
		return answer.getText();
	}

	public ArrayList<Test> getTestList() {
		ArrayList<Test> tl = new ArrayList<Test>();
		for(TestViewController tvc : tests) {
			tl.add(tvc.toTest());
		}
		return tl;
	}
}
