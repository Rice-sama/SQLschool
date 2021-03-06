/*******************************************************************************
 * 	Java tool with a GUI to help learn SQL
 * 	
 *     Copyright (C) 2020  Bayad Nasr-eddine, Bayol Thibaud, Benazzi Naima, 
 *     Douma Fatima Ezzahra, Chaouche Sonia, Kanyamibwa Blandine
 *     (thesqlschool@hotmail.com)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package apprendreSQL.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import apprendreSQL.Model.data.Factory;
import apprendreSQL.Model.data.Observers;
import apprendreSQL.Model.data.Question;
import apprendreSQL.Model.job.semantique.Corrector;
import apprendreSQL.Model.job.semantique.Table;
import apprendreSQL.Model.job.semantique.TestCorrection;
import apprendreSQL.Model.job.syntaxique.general.ParseException;
import apprendreSQL.Model.job.syntaxique.general.ParserSQL;
import apprendreSQL.View.GetInformation;
import apprendreSQL.View.Gui;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class EventManager implements GetInformation {
	
	@FXML private Accordion exoExplorer;
	@FXML private VBox inputBox;
	@FXML private Label qTitle;
	@FXML private Label qText;
	@FXML private TextArea queryText;
	@FXML private Label resultDisplay1;
	@FXML private Label resultDisplay2;
	@FXML private Label resultText;
	@FXML private Accordion testExplorer;
	@FXML private VBox tableView;

	private static final Paint GREEN_PAINT = Paint.valueOf("06d6a0");
	private static final Paint YELLOW_PAINT = Paint.valueOf("ffc300");
	private static final Paint RED_PAINT = Paint.valueOf("ef476f");
	private static final Paint BLACK_PAINT = Paint.valueOf("000000");

	private static JsonManager jsonManager;
	private static ParserSQL parserSQLGeneral, parserSQLParticulier;
	private static Corrector corrector;
	private static Question currentQuestion;
	private static  ConnectionSQLite selectedConnection;

	private TreeMap<String, ConnectionSQLite> connectionsMap = new TreeMap<String, ConnectionSQLite>();

	public EventManager() {
		initializeParser();
		dbConnections();
		jsonManager = new JsonManager();
		readJSONFiles();
	}
	
	@FXML 
	public void initialize() {
		try {
			updateExercisesView();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * initialize parsers
	 * 
	 */
	
	private void initializeParser() {
		parserSQLGeneral      = Factory.makeParserSQL("general");
		parserSQLParticulier  = Factory.makeParserSQL("particulier");
		parserSQLParticulier.setcontroller(this);
		
		parserSQLGeneral.registerObserver((Observers) parserSQLParticulier);
		parserSQLGeneral.setDestination("eleve");
		corrector = new Corrector(parserSQLGeneral,parserSQLParticulier);

	}
	/**
	 * A function that connects the database with sqlite.
	 */
	private void dbConnections() {
		ConnectionSQLite c;
		for (String database : getDbFiles()) {
			c = new ConnectionSQLite("resource/" + database);
			c.connect();
			connectionsMap.put(database, c);
		}
		// selectedConnection =connectionsMap.firstEntry().getValue();

	}

	/**
	 * A function that reads the available json files.
	 */
	private void readJSONFiles() {
		jsonManager.clear();
		for (String jsonFile : getJSONFiles())
			jsonManager.readFileQuestion("resource/" + jsonFile, true);
	}

	/**
	 * This method tests if the query is correct or not.
	 * 
	 * @param query the query inserted by the user
	 */

	private void ifCorrect(String query) {
		if (currentQuestion.getAnswer() != null) {
			try {
				if (!corrector.correction(query, currentQuestion.getAnswer(), currentQuestion.getTestList(), selectedConnection)) {
					resultDisplay1.setVisible(false);
					resultDisplay2.setVisible(true);
					
					String msg = corrector.getComment();
					if(msg.isEmpty()) {
						resultText.setText(
								"Une erreur est survenue lors de la correction d'un ou plusieurs tests : \n"
								+ parserSQLParticulier.analysID());
					}
					
					else resultText.setText(msg);
					resultText.setTextFill(YELLOW_PAINT);
				} else {	
					resultDisplay1.setVisible(true);
					resultDisplay2.setVisible(false);
					
					resultText.setText("Les tests ont �t� pass�s avec succ�s.");
					resultText.setTextFill(GREEN_PAINT);
				}
				
				for(TestCorrection tc : corrector.getCorrectionList()) {
					TitledPane testpane = FXMLLoader.load(Gui.class.getResource("fileXml/testItem.fxml"));
					testpane.setText(tc.getTest().getName());
					if(tc.isCorrect()) testpane.setTextFill(GREEN_PAINT);
					else testpane.setTextFill(RED_PAINT);
					VBox content = (VBox) testpane.getContent();
					((Label) content.getChildren().get(0)).setText(tc.getCompiledMessage());
					
					ArrayList<LinkedHashMap<String, Object>> res = tc.getUserTest().getResult();
					@SuppressWarnings("unchecked")
					TableView<Map<String,Object>> tv = (TableView<Map<String,Object>>) content.getChildren().get(1);
					
					if(!res.isEmpty()) {
						for(String colname : res.get(0).keySet()) {
							TableColumn<Map<String,Object>, String> col = new TableColumn<>(colname);
							col.setSortable(false);
							col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(colname).toString()));
					        tv.getColumns().add(col);
						}

						ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();					
						
						for(LinkedHashMap<String, Object> row : res) {
							items.add(row);
						}
						
						tv.getItems().addAll(items);
					}
					testExplorer.getPanes().add(testpane);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}


	/**
	 * This method is called when the "Executer" button is clicked.
	 */
	
	@FXML
	public void callExecute() {
		if(currentQuestion == null) {
			setText("Veuillez selectionner une question.", YELLOW_PAINT);
			return;
		}else if(selectedConnection == null) {
			setText("Base de donn�es introuvable.", RED_PAINT);
			return;
		}
		clearOutput();
		try {
			parserSQLParticulier.reset();
			parserSQLParticulier.updateReponse(currentQuestion);
			String query = queryText.getText();
			parserSQLGeneral.ReInit(Factory.translateToStream(query));
			parserSQLGeneral.parserStart();
			parserSQLParticulier.parserStart();
			ifCorrect(query);
		} catch (ParseException e) {
			setText(e.getMessage(),Paint.valueOf("ef476f"));
		}
	}
	
	/**
	 * Set the current question to q
	 * 
	 * @param question to change into
	 */
	public void setQuestion(Question q) {
		clearInput();
		clearOutput();
		if(!inputBox.isVisible()) inputBox.setVisible(true);
		currentQuestion = q;
		qTitle.setText(q.getTitleQuestion());
		qText.setText(q.getContentQuestion());
		selectedConnection = connectionsMap.get(q.getDatabase());
		drawTables();
		
	}

	public ArrayList<Question> getQuestionsList() {
		return jsonManager.getListQuestion();
	}

	
	/**
	 * draw the tables' info view 
	 * 
	 */
	
	private void drawTables() {
		tableView.getChildren().clear();
		ArrayList<Table> tables = selectedConnection.getTables();
		for(Table t : tables) {
			try {
				VBox tableBox = FXMLLoader.load(Gui.class.getResource("fileXml/table.fxml"));
				Label name = (Label) tableBox.getChildren().get(0);
				HBox cols = (HBox) tableBox.getChildren().get(1);
				name.setText(t.getName());
				for(String colName : t.getColumnNames()) {
					Label col = new Label(colName);
					Paint p = BLACK_PAINT;
					if(t.getPrimaryKey()!=null) if(colName.toLowerCase().equals(t.getPrimaryKey().toLowerCase())) p = RED_PAINT;
					col.setBorder(new Border(new BorderStroke(p, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
					col.setPadding(new Insets(4));
					cols.getChildren().add(col);
				}
				tableView.getChildren().add(tableBox);
			} catch (IOException e) {
				System.out.println("Error while loading the table view : "+ e);
			}

		}
	}

	/**
	 * Closes all open database connections.
	 */
	public void close() {
		Iterator<Entry<String, ConnectionSQLite>> iterator = connectionsMap.entrySet().iterator();
		while (iterator.hasNext()) {
			iterator.next().getValue().close();
		}
	}
	
	public void setText(String msg, Paint p) {
		resultText.setText(msg);
		resultText.setTextFill(p);
	}

	public void clearOutput() {
		setText("",Paint.valueOf("000000"));
		testExplorer.getPanes().clear();
	}

	public void clearInput() {
		queryText.setText("");
	}

	public JsonManager getJsonManager() {
		return jsonManager;
	}

	public TreeMap<String, ConnectionSQLite> getConnectionsMap() {
		return connectionsMap;
	}


	/**
	 * update the exercises explorer
	 * 
	 */
	@FXML
	public void updateExercisesView() throws IOException {
		readJSONFiles();
				
		exoExplorer.getPanes().clear();
		
		ArrayList<TitledPane> catList = new ArrayList<>();
		for(Question q : jsonManager.getListQuestion()) {
			Button exoButton = FXMLLoader.load(Gui.class.getResource("fileXml/exoButton.fxml"));
			exoButton.setText(q.getTitleQuestion());
			exoButton.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	setQuestion(q);
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
				TitledPane cat = FXMLLoader.load(Gui.class.getResource("fileXml/categorie.fxml"));
				cat.setText(q.getSubject());
				catList.add(cat);
				((VBox)cat.getContent()).getChildren().add(exoButton);
			}
		}
		
		exoExplorer.getPanes().addAll(catList);
	}
}
