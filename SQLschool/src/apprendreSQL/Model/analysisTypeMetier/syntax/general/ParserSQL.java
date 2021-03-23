package apprendreSQL.Model.analysisTypeMetier.syntax.general;

import java.io.InputStream;

import apprendreSQL.Controller.EventManager;
import apprendreSQL.Model.analysisTypeMetier.syntax.particular.Tokens;
import apprendreSQL.Model.data.Observers;

public interface ParserSQL {
	
	default  public void sqlStmtList()  throws ParseException {};
	default  public void ReInit(InputStream stream) {};
	default public void  registerObserver(Observers o) {};
	default public void setDestination(String desti) {};
	default public void updateReponse(String reponse) {};
	default public void setcontroller(EventManager controller) {};
	default public void display() {};
	default public void reset() {};
	default public String getIdTables() {return "";};
	default public String getTypeRequete() {return "" ;};
	default  public void startAnalyseSemantic(boolean ordre) throws  ParseException {};
	
	
}
