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
import java.sql.ResultSet;
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
import apprendreSQL.Model.job.syntaxique.particular.ParserSQL2;
import apprendreSQL.View.GetInformation;
import apprendreSQL.View.Gui;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.StringConverter;

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

	private static CheckQueryManager checkQuery;
	private static JsonManager jsonManager;
	private static ParserSQL parserSQLGeneral, parserSQLParticulier;
	private static Corrector corrector;
	private static Question currentQuestion;
	private ArrayList<String> answerColumns = new ArrayList<String>();
	private TreeMap<String, ConnectionSQLite> connectionsMap = new TreeMap<String, ConnectionSQLite>();

	private static  ConnectionSQLite selectedConnection;
	
	

	public EventManager() {
		initialisationParser();
		dbConnections();
		jsonManager = new JsonManager();
		readJSONFiles();
		checkQuery = new CheckQueryManager();
		//new Watcher(this).observe();
		printNotice();
	}
	
	@FXML 
	public void initialize() {
		try {
			updateExercisesView();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void initialisationParser() {
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
	 * @return a string that represent the result in the output jText
	 */
	
	

	private void ifCorrect(String query) {
		if (currentQuestion.getAnswer() != null) {
			try {
				if (!corrector.correction(query, currentQuestion.getAnswer(), currentQuestion.getTestList(), selectedConnection)) {
					resultDisplay1.setVisible(false);
					resultDisplay2.setVisible(true);
					
					String msg = corrector.getCommentaire();
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
					
					resultText.setText("Les tests ont éte passés avec succés.");
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
	 * Give the user a hint based on the his input.
	 * 
	 * @return the hint as a String
	 */
	public static String callHint() {
		/*
		if (!mainWindow.getInput().isEmpty())

			return ""; //corrector.definehint(currentQuestion.getAnswer());
		*/
		return "Il faut d'abord essayer d'ecrire une requete et de l'executer ;";
	}
	private static String text;

	/**
	 * This method is called when the "Executer" button is clicked.
	 */
	
	@FXML
	public void callExecute() {
		if(currentQuestion == null) {
			setText("Veuillez selectionner une question.", YELLOW_PAINT);
			return;
		}else if(selectedConnection == null) {
			setText("Base de données introuvable.", RED_PAINT);
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
			//mainWindow.setOutPut(text);
		} catch (ParseException e) {
			setText(e.getMessage(),Paint.valueOf("ef476f"));
			//mainWindow.setOutPut(e.getMessage() + "\n");
		}

		//  tu peux laisser ça après car c'est le nombre de tentative 
//		if (compteurrep == 4) {
//			mainWindow.setOutPut("Vous avez fait 3 tentatives. Voil la bonne reponse <br> " + currentQuestion.getAnswer().replaceAll("<","&lt;")
//					+ " <br> Essayez de l'ecrire et de l'executer");
//			compteurrep = 1;
//
//		} else {
//			mainWindow.setOutPut(text + "\n");
//		}
//
     	//mainWindow.updateTableModel();

	}

	/**
	 * This method is called when we click on a specific exercise.
	 * 
	 * @param dbName
	 * @param sujetName
	 * @param exerciceName
	 */
	public void callQuestion(String dbName, String sujetName, String exerciceName) {
		clearOutput();
		corrector.setHint(callHint());
		int id = getIdExercise(dbName, sujetName, exerciceName, jsonManager);

		if (id < getQuestionsList().size() && id >= 0) {
			currentQuestion = getQuestionsList().get(id);
			
			
		}
		qTitle.setText(exerciceName);
		qText.setText(currentQuestion.getContentQuestion());
		selectedConnection = connectionsMap.get(dbName);

		answerColumns.clear();
		for (int i = 0; i < checkQuery.getTableColumns().size(); i++) {
			String column = checkQuery.getTableColumns().get(i);
			if (currentQuestion.getAnswer().contains(column)) {
				answerColumns.add(column);
				System.out.println(column);
			}
		}

	}
	
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
	 * Returns a ResultSet object of the table from the database passed as
	 * arguments.
	 * 
	 * @param database the database where the table exists
	 * @param table    the table we want to retrieve
	 * @return
	 */
	public ResultSet getTable(String database, String table) {
		String inputQuery = "SELECT * FROM " + table;
		ResultSet rs = null;
		ConnectionSQLite connectionSQLite;
		if ((connectionSQLite = connectionsMap.get(database)) != null) {
			connectionSQLite.connect();
			rs = connectionSQLite.queryExecution(inputQuery);
		}
		return rs;
	}

	public ConnectionSQLite getSelectedConnection() {
		return selectedConnection;
	}

	public void setselectedConnection(ConnectionSQLite selectedConnection) {
		EventManager.selectedConnection = selectedConnection;
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

	public ArrayList<String> getAnswerColumns() {
		return answerColumns;
	}

	public void setAnswerColumns(ArrayList<String> answerColumns) {
		this.answerColumns = answerColumns;
	}

	public TreeMap<String, ConnectionSQLite> getConnectionsMap() {
		return connectionsMap;
	}



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
		//mainWindow.updateExercisesView();

	}

	public void showEditor() {
		//mainWindow.showEditor();

	}

	public void updateDiagram(boolean isInstalled) {
		//mainWindow.updateDiagram(isInstalled);

	}

	public void notifyIncompatible() {
		//mainWindow.notifyIncompatible();

	}

	public void notifyUnavailableDirectory() {
		//mainWindow.notifyUnavailableDirectory();

	}

	public void showProgress() {
		//mainWindow.showProgress();

	}

	public void hideProgress() {
		//mainWindow.hideProgress();

	}

	public static void printNotice() {
		System.out.println("############################################################################################## \r\n"
				+ "    Sqlschool  Copyright (C) 2020  Bayad Nasr-eddine, Bayol Thibaud, Benazzi Naima, \r\n"
				+ "    Douma Fatima Ezzahra, Chaouche Sonia, Kanyamibwa Blandine \r\n"
				+ "    (thesqlschool@hotmail.com) \r\n\n"
				+ "    This program comes with ABSOLUTELY NO WARRANTY; \r\n"
				+ "    This is free software, and you are welcome to redistribute it \r\n"
				+ "    under certain conditions; refer to the GNU General Public License for details. \r\n"
				+ "############################################################################################## \n\n\n\n");
	}

}
