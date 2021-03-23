package apprendreSQL.Model.analysisTypeMetier.semantique;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class TestResult {
	
	
	private ArrayList<LinkedHashMap<String, Object>> data;
	
	private String message;
	
	public TestResult() {
		data = new ArrayList<>();
	}
	
	public void setResult(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList<LinkedHashMap<String, Object>> list = new ArrayList<>();
		while (rs.next()){
			LinkedHashMap<String, Object> col = new LinkedHashMap<>(columns);
			for(int i=1; i<=columns; ++i){           
				col.put(md.getColumnName(i),rs.getObject(i));
			}
			list.add(col);
		}
		data = list;
	}
	
	public ArrayList<LinkedHashMap<String, Object>> getResult(){
		return data;
	}
	
	
	public void setMessage(String m) {
		message = m ;
	}
	
	public String getMessage() {
		return message;
	}
	
}
