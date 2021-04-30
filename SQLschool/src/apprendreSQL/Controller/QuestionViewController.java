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
	
	/**
	 * check if all inputs are valid
	 * 
	 */
	@FXML 
	public void checkValid() throws Exception {
		if(title.getText().equals("")) throw new Exception("Une question n'a pas de titre.");
		if(db.getValue()==null) throw new Exception(title.getText()+" n'a pas de base de donn�es.");
		if(subject.getValue()==null) throw new Exception(title.getText()+" n'a pas de cat�gorie.");
		if(question.getText().equals("")) throw new Exception(title.getText()+" n'a pas d'�nonc�.");
		if(answer.getText().equals("")) throw new Exception(title.getText()+" n'a pas de r�ponse.");
		else {
			ParserSQL1 parser = new ParserSQL1(new ByteArrayInputStream(answer.getText().getBytes()));
			try {
				parser.parserStart();
			} catch (ParseException e) {
				throw new Exception(title.getText()+" n'a pas une r�ponde valide.");
			}
		}
	}
	
	@FXML
	private void updateName() {
		pane.setText(title.getText());
	}
	
	@FXML
	private void addTest() {
		if(testName.getText().equals("")) return;
		addTest(testName.getText(),"","");
	}
	
	/**
	 * add a test to the test view
	 * 
	 * @param name
	 * @param script1
	 * @param script2
	 */
	public void addTest(String s, String s1, String s2) {
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
			tvc.initTitle(s);
			tvc.setS1(s1);
			tvc.setS2(s2);
			tests.add(tvc);
			testExplorer.getTabs().add(tab);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void delete() {
		if(deleteConfirm.getText().equals("")) deleteConfirm.setText("Supprimer?");
		else {
			FileViewController.removeQuestion(this);
			((Accordion) pane.getParent()).getPanes().remove(pane);
		}
	}
	
	public void initTitle(String s) {
		pane.setText(s);
		title.setText(s);
	}

	/**
	 * update the subjects' combobox with the new list
	 * 
	 * @param subject list
	 */
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

	public void setSubject(String s) {
		subject.setValue(s);
	}
	
	public void setDb(String s) {
		db.setValue(s);
	}

	public void setContent(String s) {
		question.setText(s);
	}

	public void setAnswer(String s) {
		answer.setText(s);
	}
}
