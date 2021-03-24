package apprendreSQL.view.version2;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Component extends VBox {
	
	private HBox header;
	private VBox body;
	private VBox footer;
	private Text titleHeader;
	private ImageView icon;
	private Text titleContainer;
	private Text levelEnChiffre;
	private ProgressBar levelBar; 
	
	public Component(int width, int height) {
		init( width,  height);
	}
	
	
	private void init(int width, int height) {
		this.setPrefSize(width, height);
		header = new HBox();
		body   = new VBox();
		footer = new VBox();
		titleHeader = new Text();
		titleContainer = new Text();
		levelEnChiffre = new Text();
		levelBar = new ProgressBar(0);
	}




	





	public void setTitleHeader(String titleHeader) {
		this.titleHeader.setText(titleHeader);
		this.titleHeader.setFill(Color.WHITE);
		this.titleHeader.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
		
	}


	public void setIcon(String images) throws MalformedURLException {
		String path = "/ressources/images/"+images;
		Path ap = Path.of("");
		File file = new File(ap.toAbsolutePath() + path);
		String url = file.toURI().toURL().toString();
		this.icon = new ImageView(url);
		this.icon.setFitWidth(50);
		this.icon.setFitHeight(50);
	}


	public void setTitleContainer(String titleContainer) {
		this.titleContainer.setText(titleContainer);
		
	}


	public void setLevelEnChiffre(String levelEnChiffre) {
		this.levelEnChiffre.setText(levelEnChiffre+" %");
	}

    private void createHeader() {
    	header.setPrefHeight(50);
		header.setStyle("-fx-background-color: blue;");
        header.setAlignment(Pos.CENTER);
    	header.getChildren().add(titleHeader);
    }
    
    private void createBody(){
		body.setAlignment(Pos.CENTER);
		body.setPadding(new Insets(25));
		body.setSpacing(10);
		body.getChildren().addAll(icon,titleContainer);
    	
    }
    
    private void creatFooter() {
    	levelBar.setPrefHeight(30);
		levelBar.setPrefWidth(250);
		levelBar.setProgress(0.2);
		footer.getChildren().addAll(levelEnChiffre,levelBar);
		footer.setAlignment(Pos.BOTTOM_RIGHT);
		footer.setSpacing(5);
    }
    
    public void creatComponent() {  
    	createHeader();
    	createBody();
    	creatFooter();
    	this.getChildren().addAll(header,body,footer);
    	this.setStyle("-fx-background-color: red;");
		this.snapSpaceY(10);
    }
	
	
	
	
}
