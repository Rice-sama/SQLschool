package apprendreSQL.Model.job.syntaxique.general;

import java.io.InputStream;
import java.util.ArrayList;

import apprendreSQL.Controller.EventManager;
import apprendreSQL.Model.data.Observers;
import apprendreSQL.Model.data.Question;
import apprendreSQL.Model.job.syntaxique.particular.BillanID;

public interface ParserSQL {
	
	default  public void parserStart()  throws ParseException {};
	default  public void ReInit(InputStream stream) {};
	default public void  registerObserver(Observers o) {};
	default public void setDestination(String desti) {};
	default public void updateReponse(Question question) {};
	default public void setcontroller(EventManager controller) {};
	default public ArrayList<String> getIdTokens() {return new ArrayList<>();};

	default public void display() {};
	default public void reset() {};
	default public String getTypeRequete() {return "" ;};
	default  public void startAnalyseSemantic(boolean ordre) throws  ParseException {};
	default public String analysID() {
		return "";
	}
	
	
}
