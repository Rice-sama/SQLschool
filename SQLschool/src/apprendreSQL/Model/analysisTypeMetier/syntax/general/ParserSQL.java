package apprendreSQL.Model.analysisTypeMetier.syntax.general;

import java.io.InputStream;
import java.util.ArrayList;

import apprendreSQL.Controller.version1.EventManager;
import apprendreSQL.Model.analysisTypeMetier.syntax.particular.Tokens;
import apprendreSQL.Model.data.Observers;

public interface ParserSQL {
	
	default  public void sqlStmtList()  throws ParseException {};
	default  public void ReInit(InputStream stream) {};
	default public void  registerObserver(Observers o) {};
	default public void setDestination(String desti) {};
	default public void updateReponse(String reponse) {};
	//default public void checkSameRequete() throws ParseException {};
	default public void setcontroller(EventManager controller) {};
	default public void display() {};
	default public void reset() {};
	default public ArrayList<String> getIdTokens() {return new ArrayList<>();};
	default public String getTypeRequete() {return "" ;};
	default  public void startAnalyseSemantic(boolean ordre) throws  ParseException {};
	
	
}
