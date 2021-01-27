package apprendreSQL.Model;

import java.util.Map;

public class TestSet {
	
	private Map<String,String> testList;
	
	public TestSet(Map<String, String> testList) {
		this.testList = testList;
	}

	public void addTest(String name, String content) {
		testList.put(name, content);
	}
	
	public void removeTest(String name) {
		testList.remove(name);
	}
	
	public Map<String,String> getTestList(){
		return testList;
	}

	public boolean isEmpty() {
		return testList.isEmpty();
	}
}
