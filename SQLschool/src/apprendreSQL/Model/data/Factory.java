package apprendreSQL.Model.data;

import static java.util.stream.Collectors.toList;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		return  new ByteArrayInputStream(str.getBytes());
	}
	

	
	public static <T> List<T> makeList(){
		return new ArrayList<T>();
	}
	
	public static  <T> Stack<T> makeStack() {
		return new Stack<T>();
	}
	
}
