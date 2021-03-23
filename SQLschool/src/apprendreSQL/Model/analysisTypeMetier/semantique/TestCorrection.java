package apprendreSQL.Model.analysisTypeMetier.semantique;

public class TestCorrection {
	
	private TestResult testUser;
	private TestResult testAnswer;
	private boolean correct;
	private String message;
	
	
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
		if(correct) m = "Correction valid�." + message;
		else m = "Correction erreur: \n" + message +"\n"
											   + ((testUser.getMessage()!=null) ? "Test utilisateur : " + testUser.getMessage() + "\n": "")
											   + ((testAnswer.getMessage()!=null) ? "Test r�ponse : " + testAnswer.getMessage() + "\n": "");
		return m;
	}
	
	public TestResult getUserTest() {
		return testUser;
	}
	
	public TestResult getAnswerTest() {
		return testAnswer;
	}
	
}
