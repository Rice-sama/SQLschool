package apprendreSQL.Model.data;

import java.util.ArrayList;

import apprendreSQL.Model.job.semantique.Test;
import apprendreSQL.Model.job.semantique.TestSet;


/**
 * This class represents a Question object.
 *
 */
public class Question {

	private String titleQuestion;
	private String contentQuestion;
	private String answer;
	private String subject;
	private String database;
	private TestSet testSet;

	public Question(String database, String subject, String title_Question, String content_Question,
			String right_answer, ArrayList<Test> testList) {
		this.titleQuestion = title_Question;
		this.contentQuestion = content_Question;
		this.answer = right_answer;
		this.subject = subject;
		this.database = database;
		this.testSet = new TestSet(testList);
	}


	public String getTitleQuestion() {
		return titleQuestion;
	}

	public void setTitleQuestion(String title_Question) {
		this.titleQuestion = title_Question;
	}

	public String getContentQuestion() {
		return contentQuestion;
	}

	public void setContentQuestion(String content_Question) {
		this.contentQuestion = content_Question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String right_answer) {
		this.answer = right_answer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	
	public boolean hasTest() {
		if(testSet.isEmpty()) return false;
		return true;
	}
	
	public ArrayList<Test> getTestList(){
		return testSet.getTestList();
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj.getClass()==this.getClass()) {
			Question q = (Question) obj;
		
		return getTitleQuestion().contentEquals(q.getTitleQuestion()) && getSubject().contentEquals(q.getSubject()) && getDatabase().contentEquals(q.getDatabase()) ;
		}
		return false;
	}
}
