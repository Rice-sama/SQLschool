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
package apprendreSQL.Controller.version1;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import apprendreSQL.Model.DataBase;
import apprendreSQL.Model.Question;
import apprendreSQL.Model.analysisTypeMetier.semantique.Corrector;
import apprendreSQL.Model.analysisTypeMetier.semantique.Table;
import apprendreSQL.Model.analysisTypeMetier.semantique.TestCorrection;
import apprendreSQL.Model.analysisTypeMetier.syntax.general.ParseException;
import apprendreSQL.Model.analysisTypeMetier.syntax.general.ParserSQL;
import apprendreSQL.Model.analysisTypeMetier.syntax.particular.ParserSQL2;
import apprendreSQL.Model.data.Factory;
import apprendreSQL.Model.data.Observers;
import apprendreSQL.view.version1.GetInformation;
import apprendreSQL.view.version1.MainWindow;

public class EventManager implements GetInformation {

	private static CheckQueryManager checkQuery;
	private static JsonManager jsonManager;
	private static ParserSQL parserSQLGeneral, parserSQLParticulier;
	private static MainWindow mainWindow;
	private static Corrector corrector;
	private static Question currentQuestion;
	private static int compteurrep = 1;
	private ArrayList<String> answerColumns = new ArrayList<String>();
	private TreeMap<String, ConnectionSQLite> connectionsMap = new TreeMap<String, ConnectionSQLite>();

	private static ConnectionSQLite selectedConnection;

	public EventManager() {
		initialisationParser();
		dbConnections();
		jsonManager = new JsonManager();
		readJSONFiles();
		checkQuery = new CheckQueryManager();
		mainWindow = new MainWindow(this);
		new Watcher(this).observe();
		printNotice();
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
	private static String ifCorrect(String query) {
		if(currentQuestion == null) return null;
		String output_answer = null;
		if (currentQuestion.getAnswer() != null) {
			try {
				if (!corrector.correction(query, currentQuestion.getAnswer(), currentQuestion.getTestList(), currentQuestion.isMustOrder(), selectedConnection)) {

					output_answer = corrector.getCommentaire();
					
					//compteurrep++;

				} else {
					/*
					output_answer = "R�ponse correcte:";
					compteurrep = 1;
					System.out.println(query);
					output_answer = output_answer + "<br>" + submit(query, selectedConnection);*/
					
					
				}
				
				for(TestCorrection tc : corrector.getCorrectionList()) {
					output_answer += tc.getCompiledMessage();
					System.out.println(tc.getCompiledMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return output_answer;
	}

	/**
	 * Give the user a hint based on the his input.
	 * 
	 * @return the hint as a String
	 */
	public static String callHint() {
		if (!mainWindow.getInput().isEmpty())

			return ""; //corrector.definehint(currentQuestion.getAnswer());

		return "Il faut d'abord essayer d'ecrire une requete et de l'executer ;";
	}
	private static String text;

	/**
	 * This method is called when the "Executer" button is clicked.
	 */
	public static void callExecute() {
		if(currentQuestion == null) return;
		clearOutput();
		try {
			parserSQLParticulier.reset();
			parserSQLParticulier.updateReponse(currentQuestion.getAnswer());
			String query = mainWindow.getInput();
			parserSQLGeneral.ReInit(Factory.translateToStream(query));
			parserSQLGeneral.sqlStmtList();
			parserSQLParticulier.sqlStmtList();
			text = ifCorrect(query);
			mainWindow.setOutPut(text);
		} catch (ParseException e) {
			mainWindow.setOutPut(e.getMessage() + "\n");
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
     	mainWindow.updateTableModel();

	}

	/**
	 * This method is called when we click on a specific exercise.
	 * 
	 * @param dbName
	 * @param sujetName
	 * @param exerciceName
	 */
	public void callQuestion(String dbName, String sujetName, String exerciceName) {
		compteurrep = 1;
		clearOutput();
		corrector.setHint(callHint());
		int id = getIdExercise(dbName, sujetName, exerciceName, jsonManager);

		if (id < getQuestionsList().size() && id >= 0) {
			currentQuestion = getQuestionsList().get(id);
			
			
		}
		mainWindow.setDescription(currentQuestion.getContentQuestion(), exerciceName);
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

	public ArrayList<Question> getQuestionsList() {
		return jsonManager.getListQuestion();
	}

	/**
	 * This method check if there is any semicolon ";"at the end of our query and
	 * then call other method as depending on the the type of the query
	 * 
	 * @param inputQuery           the query inserted by the user
	 * @param mySelectedConnection the data base connection.
	 * @return the output result
	 */
	public static String submit(String inputQuery, ConnectionSQLite mySelectedConnection) {

		if (mySelectedConnection == null)
			return "ERR: Pas de base de donn�es";

		if (checkQuery.ifSelectQuery(inputQuery)) {

			// call the query on the selected database (in progress)
			return checkQuery.callSelect(inputQuery, mySelectedConnection, null);
		} else {
			return checkQuery.callOtherQuery(inputQuery, mySelectedConnection);
		}

	}

	/**
	 * Runs a query similar to '.tables'.
	 * 
	 * @param database target database
	 * @return a list of Table objects present in the database
	 */
	public ArrayList<Table> getTables(String database) {
		String inputQuery = "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;";
		ArrayList<Table> tables = new ArrayList<Table>();
		ConnectionSQLite s;
		if ((s = connectionsMap.get(database)) != null) {
			s.connect();
			tables = checkQuery.getTables(inputQuery, s, database);
		}
		return tables;

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

	public static void clearOutput() {
		mainWindow.setOutPut("");
	}

	public void clearInput() {
		mainWindow.setInput("");
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

	public void populateTablesView(DataBase database) {
		ArrayList<Table> tables = database.getTables();
		if (!tables.isEmpty())
			mainWindow.populateTablesView(tables);
	}

	public void updateExercisesView() throws IOException {
		readJSONFiles();
		mainWindow.updateExercisesView();

	}

	public void showEditor() {
		mainWindow.showEditor();

	}

	public void updateDiagram(boolean isInstalled) {
		mainWindow.updateDiagram(isInstalled);

	}

	public void notifyIncompatible() {
		mainWindow.notifyIncompatible();

	}

	public void notifyUnavailableDirectory() {
		mainWindow.notifyUnavailableDirectory();

	}

	public void showProgress() {
		mainWindow.showProgress();

	}

	public void hideProgress() {
		mainWindow.hideProgress();

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
