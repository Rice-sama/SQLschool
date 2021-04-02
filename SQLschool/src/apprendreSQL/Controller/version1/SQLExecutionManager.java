package apprendreSQL.Controller.version1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLExecutionManager {
	
	private ArrayList<String> executionQueue = new ArrayList<>();

	public void addToQueue(String script) {
		if(!script.isBlank()) executionQueue.add(script);
	}
	
	
	public void clearQueue() {
		executionQueue.clear();
	}
	
	public void executeQueueOnDB(Connection conn) throws SQLException {
		
		Statement stmt = conn.createStatement();
		for(String sql : executionQueue) {
			String[] commands = sql.split(";");
			for(int i = 0; i < commands.length; i++) {
				stmt.addBatch(commands[i]);
			}
		}
		clearQueue();
		int[] res = stmt.executeBatch();
		// get errors from res
	}
	
	public ResultSet executeSelect(Connection conn, String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet res = stmt.executeQuery(sql);
		return res;
	}
}
