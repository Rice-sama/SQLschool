package apprendreSQL.Model.analysisTypeMetier.semantique;

import java.util.ArrayList;

public class TestSet {
	
	private ArrayList<Test> testList;
	
	public TestSet(ArrayList<Test> testList) {
		this.testList = testList;
	}

	public void addTest(String name, String pre, String post) {
		testList.add(new Test(name, pre, post));
	}
	
	public void removeTest(String name) {
		for(int i = 0; i < testList.size(); i++) {
			if(testList.get(i).getName().equals(name)) testList.remove(i);
		}
	}
	
	public ArrayList<Test> getTestList(){
		return testList;
	}

	public boolean isEmpty() {
		return testList.isEmpty();
	}
}
