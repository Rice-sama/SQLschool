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
import java.util.Map;

import apprendreSQL.Controller.ConnectionSQLite;
import apprendreSQL.Model.ParseException;
import apprendreSQL.Model.ParserSQL;
import apprendreSQL.Model.ParserSQLConstants;
import apprendreSQL.Model.Test;

public class Corrector {

	ArrayList<String> listType = new ArrayList<>();
	String hint = "";

	String type = "none";
	String table = "none";

	String comment = "";
	String commentO = "";
	
	ParserSQL parser = new ParserSQL(new ByteArrayInputStream("".getBytes()));
	SQLExecutionManager execManager = new SQLExecutionManager();

	public Corrector() {
		listType.add("select");
		listType.add("insert into");
		listType.add("update");
		listType.add("delete");

	}

	public String getHint() {
		return hint;
	}

	public String getCommentaire() {
		return comment;
	}

	public boolean correction(String inputUser, String right_Answer, ArrayList<Test> arrayList, boolean mustOrder, ConnectionSQLite dbconnection) {
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
			if(dbconnection.connect() && dbconnection.existTable(parser.idTokens.get(0))){
				
				ArrayList<String> r1= new ArrayList<String>();
				ArrayList<String> r2= new ArrayList<String>();
				
				switch(reqType) {
				case ParserSQLConstants.SELECT:
					r1= dbconnection.getSelectResult(inputUser);
					r2= dbconnection.getSelectResult(right_Answer);
					break;
				case ParserSQLConstants.INSERT:
				case ParserSQLConstants.UPDATE:
				case ParserSQLConstants.DELETE:
					r1= dbconnection.getUpdateResult(inputUser, parser.idTokens.get(0));
					r2= dbconnection.getUpdateResult(right_Answer, parser.idTokens.get(0));
					break;
				
				case ParserSQLConstants.TRIGGER:
					
					break;
				}
				//execManager.executeQueueOnDB(dbconnection.getConnection());
				System.out.println(r1);
				System.out.println(r2);
				if(r1.size()==r2.size()) {
					if(mustOrder) {
						for(int i = 0; i < r1.size(); i++) {
							if(!r1.get(i).equals(r2.get(i))) return false;
						}
					} else for(int i=0; i<r1.size();i++) if(Collections.frequency(r1,r1.get(i))!=Collections.frequency(r2,r1.get(i))) return false;
					return true;
				}
			}
		} catch (Exception e) {
			comment = e.getMessage();
		}
		return false;
	}	
	

	public void setHint(String hint) {
		this.hint = hint;
	}
}
