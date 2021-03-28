package apprendreSQL.Controller.version2;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import apprendreSQL.Controller.version1.EventManager;
import apprendreSQL.Model.Question;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class GuiController {
	
	
	@FXML private Accordion exoExplorer;
	
	@FXML private Label qTitle;
	@FXML private Label qText;
	@FXML private TextArea queryText;
	
	
	@FXML private Label resultDisplay1;
	@FXML private Label resultDisplay2;
	
	/*
	public void updateExercisesView() throws IOException {
		readJSONFiles();
		
		exoExplorer.getPanes().clear();
		
		ArrayList<TitledPane> catList = new ArrayList<>();
		
		for(Question q : jsonManager.getListQuestion()) {
			Button exoButton = FXMLLoader.load(getClass().getResource("fileXml/exoButton.fxml"));
			exoButton.setText(q.getTitleQuestion());
			exoButton.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	currentQuestion = q;
			    }
			});
			boolean added = false;
			for(TitledPane cat : catList) {
				if(cat.getText().equals(q.getSubject())) {
					((VBox)cat.getContent()).getChildren().add(exoButton);
					added = true;
				}
			}
			if(!added) {
				TitledPane cat = FXMLLoader.load(getClass().getResource("fileXml/categorie.fxml"));
				catList.add(cat);
				((VBox)cat.getContent()).getChildren().add(exoButton);
			}
		}
		
		exoExplorer.getPanes().addAll(catList);
		//mainWindow.updateExercisesView();

	}*/
}
