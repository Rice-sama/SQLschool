package apprendreSQL.Controller.version2;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class QuestionViewController {

	@FXML TitledPane pane;
	@FXML TextField title;
	@FXML ComboBox<String> subject;
	@FXML ComboBox<String> db;
	@FXML TextArea question;
	@FXML TextArea answer;
	@FXML TextField testName;
	
	@FXML 
	private void checkValid() {
		
	}
	
	@FXML
	private void updateName() {
		pane.setText(title.getText());
	}
	
	@FXML
	private void addTest() {
		//create test
		testName.setText("");
	}
	
	public void initTitle(String s) {
		pane.setText(s);
		title.setText(s);
	}
}
