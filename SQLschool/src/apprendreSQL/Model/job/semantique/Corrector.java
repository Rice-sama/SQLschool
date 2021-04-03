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
package apprendreSQL.Model.job.semantique;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import apprendreSQL.Controller.ConnectionSQLite;
import apprendreSQL.Controller.SQLExecutionManager;
import apprendreSQL.Model.job.syntaxique.general.ParserSQL;

public class Corrector {

	String hint = "";

	String type = "none";
	String table = "none";

	String comment = "";
	String commentO = "";
	private ParserSQL parserSQLParticulier;
	private ParserSQL parserSQLGeneral;
	
	SQLExecutionManager execManager = new SQLExecutionManager();
	
	ArrayList<TestCorrection> tclist = new ArrayList<>();

	public Corrector(ParserSQL parser1, ParserSQL parser2) {
		parserSQLParticulier = parser2;
		parserSQLGeneral = parser1;
	}

	public String getHint() {
		return hint;
	}

	public String getCommentaire() {
		return comment;
	}
	
	public boolean correction(String inputUser, String right_Answer, ArrayList<Test> testList, ConnectionSQLite dbconnection) {
		comment = "";
		try {
			if(dbconnection.connect()){
				String tableEleve = dbconnection.existTable(parserSQLGeneral.getIdTokens()).toLowerCase();
				if(tableEleve.equals("")){
					comment = "La table n'existe pas.";
					return false;
				}
				if(!tableEleve.equals(dbconnection.existTable(parserSQLParticulier.getIdTokens()).toLowerCase())){
					comment = "La table ne correspond pas.";
					System.out.println("Err : tables don't match");
					return false;
				}
				
				String[] inputs = {inputUser, right_Answer};
				String select = "SELECT * FROM " + tableEleve;
				
				tclist.clear();
				if(testList.isEmpty()) testList.add(new Test("R�sultat", "", ""));
				for(Test t : testList) {
					
					ArrayList<ArrayList<String>> sl = new ArrayList<>();
					sl.add(new ArrayList<String>());
					sl.add(new ArrayList<String>());
					
					TestCorrection tc = new TestCorrection();
					tc.setTest(t);
					TestResult[] trlist= {new TestResult(),new TestResult()};
					boolean test = true;
					
					for(int i = 0; i < inputs.length; i++) {
						execManager.addToQueue(t.getPreExecutionScript());
						
						switch(parserSQLParticulier.getTypeRequete()) {
						
						case "\"SELECT\"":
							select = inputs[i];
							break;
							
						default:
							System.out.println(inputs[i]);
							execManager.addToQueue(inputs[i]);
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
										
					String m = compare(sl.get(0),sl.get(1));
					if(!m.equals("")) test = false;
					tc.setMessage(m);
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
	
	private String compare(ArrayList<String> r1, ArrayList<String> r2) {
		String s = "";
		if(r1.size()==r2.size()) {
			for(int i = 0; i < r1.size() && s.equals(""); i++) {
				if(!r1.get(i).equals(r2.get(i))) {
					boolean orderTest = true;
					for(int j=0; j<r1.size() && orderTest; j++) 
						if(Collections.frequency(r1,r1.get(i))!=Collections.frequency(r2,r1.get(i))) {
							s = "Ordre des tuples incorrect.";
							orderTest = false;
						}
					s = "Les tuples ne correspondent pas.";
				}
			}
		} else {
			s = "Nombre de tuples incorrect.";
		}
		return s;
	}
	
	public ArrayList<TestCorrection> getCorrectionList(){
		return tclist;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
}
