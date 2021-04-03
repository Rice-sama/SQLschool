package apprendreSQL.Controller;

import apprendreSQL.Model.job.semantique.Test;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class TestViewController {

	@FXML Tab tab;
	@FXML TextArea s1;
	@FXML TextArea s2;
	
	
	public void initTitle(String name) {
		tab.setText(name);
	}
	
	public Test toTest(){
		return new Test(tab.getText(),s1.getText(),s2.getText());
	}

	public void setS1(String s) {
		s1.setText(s);
	}
	
	public void setS2(String s) {
		s2.setText(s);
	}
}
