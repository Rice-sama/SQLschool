package apprendreSQL.Controller;

import java.io.IOException;

import apprendreSQL.Controller.version1.EventManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RouterVersion {

	
	@FXML  RadioButton ChoiceVersion1;
	@FXML  RadioButton ChoiceVersion2;
	
	
	@FXML
	public void startHomePage(MouseEvent event) throws IOException {
		Stage stage =   (Stage) ((Node) event.getSource()).getScene().getWindow();
		if(ChoiceVersion1.isSelected())	{
			stage.close();
			new EventManager();
//			EventManager.printNotice(); // message de copyright
		}
		if(ChoiceVersion2.isSelected()){
			System.out.println("choix v2 ");
			//AnchorPane pan = FXMLLoader.load(getClass().getResource("/apprendreSQL/view/version2/fileXml/home.fxml"));
			Parent pan = FXMLLoader.load(getClass().getResource("/apprendreSQL/view/version2/fileXml/gui.fxml"));
			Scene scene = new Scene(pan,1280,800);
			stage.setScene(scene);
		}

	}

}
