package apprendreSQL.Model.job.syntaxique.particular;

import java.util.List;


import apprendreSQL.Model.data.Factory;

public class BillanID {
	
	List<String> ids = Factory.makeList();


	
	

	
	public void addIDEQ(List<String> idE, List<String> idP) {
		reset();
		for(String id : idE) {
			if(!idP.contains(id)) {
				ids.add(id);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		ids.forEach( e -> builder.append(e+" ") );
		return builder.toString();
	}
	private void reset() {
		ids.removeAll(ids);
	}
	

	
}
