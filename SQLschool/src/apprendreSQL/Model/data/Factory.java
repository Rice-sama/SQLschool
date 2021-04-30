package apprendreSQL.Model.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import apprendreSQL.Model.job.syntaxique.general.ParserSQL;
import apprendreSQL.Model.job.syntaxique.general.ParserSQL1;
import apprendreSQL.Model.job.syntaxique.particular.ParserSQL2;

public class Factory  {
	
	


	
	/**
	 * lecture d'un fichier json avec streaming et de créer fur et à mesure un objet question et de l'ajouter dans 
	 *  l objet banqueQuesiton qui gère ce dernier
	 * @param s
	 * @return  
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */   


	
	
	
	/**
	 *    
	 * @return 
	 */
	public static ParserSQL makeParserSQL(String type) throws IllegalArgumentException {
		switch(type) {
			case "general" : return  new ParserSQL1(new ByteArrayInputStream("".getBytes()));
			case "particulier" :        return  new ParserSQL2();
			default :  throw new IllegalArgumentException("Invalide argument");
		}
	}
	
	public static InputStream translateToStream(String str) {
		return  new ByteArrayInputStream(str.toLowerCase().getBytes());
	}
	

	
	public static <T> List<T> makeList(){
		return new ArrayList<T>();
	}
	
	public static  <T> Stack<T> makeStack() {
		return new Stack<T>();
	}
	
}
