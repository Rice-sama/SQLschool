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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import apprendreSQL.Model.job.semantique.Table;

public class ConnectionSQLite {

	private String dBPathModif;
	private String dBPathOrigin = "";

	private Connection connection = null;
	private Statement statement = null;
	private String errorMessage = "";
	private ResultSet result;
	
	ArrayList<Table> tables = new ArrayList<Table>();

	public ConnectionSQLite(String dBPath) {
		dBPathOrigin = dBPath;
		resetDatabase();
		dBPathModif = dBPathOrigin.replace(".db", "") + "_versionReset.db";
		initTablesList();
	}

	/**
	 * Connects to the database.
	 * 
	 * @return
	 */
	public Boolean connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + dBPathModif);
			this.statement = connection.createStatement();
			System.out.println("Connected to " + dBPathModif + " successfully.");
			return true;
		} catch (ClassNotFoundException notFoundException) {
			notFoundException.printStackTrace();
			System.out.println("Erreur de connection0");
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.out.println("Erreur de connection1");
		}
		return false;
	}

	/**
	 * Closes the connection.
	 */
	public void close() {
		try {
			if(!connection.isClosed()) statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get all the tables' name and their columns' name from this db connection.
	 * 
	 * @param database target database
	 */
	public void initTablesList() {
		if(connect()) {
			try {
				ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;");
				while (resultSet.next()) {
					String tableName = resultSet.getString("name");
					tables.add(new Table(tableName));
				}
				
				for(Table t : tables) {
					ArrayList<String> colNames = new ArrayList<>();
					resultSet = statement.executeQuery("SELECT * FROM "+t.getName()+";");
					ResultSetMetaData meta = resultSet.getMetaData();
					int colCount = meta.getColumnCount();
					for (int i = 1; i <= colCount; i++) {
						colNames.add(meta.getColumnName(i));
					}
					t.setColumnNames(colNames);
					ResultSet pkColumns= connection.getMetaData().getPrimaryKeys(null,null,t.getName());
				    while(pkColumns.next()) {
				    	t.setPrimaryKey(pkColumns.getString("COLUMN_NAME"));
				    }
			    	System.out.println("Table info loaded : "+t.getName()+" | "+colNames+" | PK "+t.getPrimaryKey());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			close();
		}
	}
	
	public ArrayList<Table> getTables(){
		return tables;
	}
	
	
	public String existTable(ArrayList<String> tlist) throws Exception { //verification de l'existence de la table 
			for(String token : tlist) {
				try {
					statement.executeQuery("SELECT * FROM "+token+";");
					System.out.println("ID: "+token+" is a table");
					return token;
				} catch(SQLException e) {
					System.out.println("ID: "+token+" isn't a table");
				}
				
			}
			System.out.println("Err : no valid table");
			return "";
	}

	/**
	 * Creates a backup file of the database.
	 */
	public void resetDatabase() {
		if(connection!=null && statement!=null) close();

		FileInputStream sourceDirectory = null;
		FileOutputStream targetDirectory = null;

		try {
			File source = new File(dBPathOrigin);
			File destination = new File(dBPathOrigin.replace(".db", "") + "_versionReset.db");

			sourceDirectory = new FileInputStream(source);
			targetDirectory = new FileOutputStream(destination);
			byte[] buffer = new byte[1024];
			int length;

			while ((length = sourceDirectory.read(buffer)) > 0) {
				targetDirectory.write(buffer, 0, length);
			}

			sourceDirectory.close();
			targetDirectory.close();

			System.out.println("File copied successfully.");

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Statement getStatement() {
		return statement;
	}
	
	public String getdBPathOrigin() {
		return dBPathOrigin;
	}

	public Connection getConnection() {
		return connection;
	}

	
}
