package apprendreSQL.Model;

public class Test {
	
	
	private String name;
	private String preExec;
	private String postExec;
	
	public Test(String name, String pre, String post) {
		this.name = name;
		this.preExec = pre;
		this.postExec = post;
	}
	
	public String getName() {
		return name;
	}

	public String getPreExecutionScript() {
		return preExec;
	}	
	
	public String getPostExecutionScript() {
		return postExec;
	}	
	
	public String toString() {
		return name;
	}
}
