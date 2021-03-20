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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import apprendreSQL.Controller.ConnectionSQLite;
import apprendreSQL.Model.ParseException;
import apprendreSQL.Model.ParserSQL;
import apprendreSQL.Model.ParserSQLConstants;
import apprendreSQL.Model.Test;
import apprendreSQL.Model.TestCorrection;
import apprendreSQL.Model.TestResult;

public class Corrector {

	String hint = "";

	String type = "none";
	String table = "none";

	String comment = "";
	String commentO = "";
	
	ParserSQL parser = new ParserSQL(new ByteArrayInputStream("".getBytes()));
	SQLExecutionManager execManager = new SQLExecutionManager();
	
	ArrayList<TestCorrection> tclist = new ArrayList<>();

	public Corrector() {

	}

	public String getHint() {
		return hint;
	}

	public String getCommentaire() {
		return comment;
	}

	public boolean correction(String inputUser, String right_Answer, ArrayList<Test> testList, boolean mustOrder, ConnectionSQLite dbconnection) {
		comment = "";
		parser.ReInit(new ByteArrayInputStream(inputUser.getBytes()));
		int reqType;
		try {
			reqType = parser.sqlStmtList();
		} catch (ParseException e1) {
			comment = e1.getMessage();
			return false;
		}

		try {
			if(dbconnection.connect()){
				
				if(!dbconnection.existTable(parser.idTokens.get(0))) {
					comment = "La table "+parser.idTokens.get(0)+" n'existe pas.";
					return false;
				}
				
				String[] inputs = {inputUser, right_Answer};
				String select = "SELECT * FROM " + parser.idTokens.get(0);
				
				tclist.clear();
				
				for(Test t : testList) {
					
					ArrayList<ArrayList<String>> sl = new ArrayList<>();
					sl.add(new ArrayList<String>());
					sl.add(new ArrayList<String>());
					
					TestCorrection tc = new TestCorrection();
					
					TestResult[] trlist= {new TestResult(),new TestResult()};
					
					boolean test = true;
					
					for(int i = 0; i < inputs.length; i++) {
						
						execManager.addToQueue(t.getPreExecutionScript());
						
						switch(reqType) {
						
						case ParserSQLConstants.SELECT:
							select = inputs[i];
							break;
							
						case ParserSQLConstants.INSERT:
						case ParserSQLConstants.UPDATE:
						case ParserSQLConstants.DELETE:
							System.out.println(inputs[i]);
							execManager.addToQueue(inputs[i]);
							break;
						
						case ParserSQLConstants.TRIGGER:
							
							break;
						}
						
						execManager.addToQueue(t.getPostExecutionScript());
						
						try {
							execManager.executeQueueOnDB(dbconnection.getConnection());
						} catch(SQLException e) {
							trlist[i].setMessage(e.getMessage());
						}
						
						ResultSet rs = execManager.executeSelect(dbconnection.getConnection(), select);
						trlist[i].setResult(rs);
						rs.close();
						
						for(LinkedHashMap<String, Object> map : trlist[i].getResult()) {
							String row = "";
							ArrayList<Object> l = new ArrayList<Object>(map.values());
							for(Object o : l) {
								row += o.toString()+" ";
							}
						    sl.get(i).add(row);
						}
						
						dbconnection.resetDatabase();
						dbconnection.connect();
					}
										
					tc.setTestResults(trlist[0], trlist[1]);
										
					if(sl.get(0).size()==sl.get(1).size()) {
						if(mustOrder) {
							for(int i = 0; i < sl.get(0).size() && test; i++) {
								if(!sl.get(0).get(i).equals(sl.get(1).get(i))) {
									test = false;
									boolean orderTest = true;
									for(int j=0; j<sl.get(0).size() && orderTest; j++) 
										if(Collections.frequency(sl.get(0),sl.get(0).get(i))!=Collections.frequency(sl.get(1),sl.get(0).get(i))) {
											tc.setMessage("Ordre des tuples incorrect.");
											orderTest = false;
										}
									else tc.setMessage("Les tuples ne correspondent pas.");
								}
							}
						}
					} else {
						test = false;
						tc.setMessage("Nombre de tuples incorrect.");
					}
					
					tc.setCorrect(test);
					tclist.add(tc);
				}
				
				for(TestCorrection tcRes : tclist) {
					if(!tcRes.isCorrect()) return false;
				}
				return true;
			}
		} catch (Exception e) {
			comment += e.getMessage();
			System.out.println(e);
		}
		return false;
	}	
	
	public ArrayList<TestCorrection> getCorrectionList(){
		return tclist;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
}
