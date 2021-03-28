package apprendreSQL.Model.analysisTypeMetier.semantique;

public class TestCorrection {
	
	private TestResult testUser;
	private TestResult testAnswer;
	private boolean correct;
	private String message;
	private Test test;
	
	public TestCorrection() {
		correct = false;
		message = "";
	}
	
	public void setTestResults(TestResult t1, TestResult t2) {
		testUser = t1;
		testAnswer = t2;
	}
	
	public void setCorrect(boolean b) {
		correct = b;
	}
	
	public boolean isCorrect() {
		return correct;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String m) {
		message = m;
	}
	
	public String getCompiledMessage() {
		String m;
		if(correct) m = "Correction validé." + message;
		else m = "Correction erreur: \n" + message +"\n"
											   + ((testUser.getMessage()!=null) ? "Test utilisateur : " + testUser.getMessage() + "\n": "")
											   + ((testAnswer.getMessage()!=null) ? "Test rï¿½ponse : " + testAnswer.getMessage() + "\n": "");
		return m;
	}
	
	public TestResult getUserTest() {
		return testUser;
	}
	
	public TestResult getAnswerTest() {
		return testAnswer;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
	
}
