package apprendreSQL.Model.analysisTypeMetier.syntax.particular;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import apprendreSQL.Controller.version1.EventManager;
import apprendreSQL.Model.analysisTypeMetier.syntax.general.ParseException;
import apprendreSQL.Model.analysisTypeMetier.syntax.general.ParserSQL;
import apprendreSQL.Model.analysisTypeMetier.syntax.particular.Tokens.TypePArtie;
import apprendreSQL.Model.data.Factory;
import apprendreSQL.Model.data.Observers;

/**
 * 
 *  Cette a comme responsabilité la compilation d'une requête relativement à une question
 *
 */


class TokenFix {

	private static boolean compteurIDAfterAS = false;

	public static  boolean isFix(String token) {
		for(String tkn : tokenImage) {
			if(tkn.equals(token)) 
				return true;
		}
		return false; 
	}


	public static boolean ignorer(String tokenImage) {
		if(tokenImage.equals("\"AS\"")){
			compteurIDAfterAS = true;
			return true;
		}
		if(compteurIDAfterAS) {
			System.out.println("check id as "+tokenImage);
			compteurIDAfterAS = false;
			return true;
		}
		for(String s : tokenIgnorer) {
			if(s.equals(tokenImage))
				return true;
		}
		return false;
	}
	
	
	static String[] tokenIgnorer = {
		    "\",\"",
		    "\"\"\"",
		    "\"'\"",
		    "\"AS\""
	};
	
	static String[] tokenImage = {
			"\"fun\"",
		    "\"ABORT\"",
		    "\"ACTION\"",
		    "\"AS\"",
		    "\"ADD\"",
		    "\"AFTER\"",
		    "\"ALL\"",
		    "\"ALTER\"",
		    "\"ANALYZE\"",
		    "\"ATTACH\"",
		    "\"AUTOINCREMENT\"",
		    "\"'\"",
		    "\"BEFORE\"",
		    "\"BEGIN\"",
		    "\"BY\"",
		    "\"CASCADE\"",
		    "\"CASE\"",
		    "\"CAST\"",
		    "\"CHECK\"",
		    "\"COLLATE\"",
		    "\"\"\"",
		    "\"COLUMN\"",
		    "\"COMMIT\"",
		    "\"CONFLICT\"",
		    "\"CONSTRAINT\"",
		    "\"CREATE\"",
		    "\"CROSS\"",
		    "\"CURRENT_DATE\"",
		    "\"CURRENT_TIME\"",
		    "\"CURRENT_TIMESTAMP\"",
		    "\"DATABASE\"",
		    "\"DEFAULT\"",
		    "\"DEFERRABLE\"",
		    "\"DEFERRED\"",
		    "\"DELETE\"",
		    "\"DESC\"",
		    "\"DETACH\"",
		    "\"DISTINCT\"",
		    "\"DROP\"",
		    "\"EACH\"",
		    "\"ELSE\"",
		    "\"END\"",
		    "\"ESCAPE\"",
		    "\"EXCLUSIVE\"",
		    "\"EXPLAIN\"",
		    "\"FAIL\"",
		    "\"FOR\"",
		    "\"FOREIGN\"",
		    "\"FROM\"",
		    "\"FULL\"",
		    "\"GLOB\"",
		    "\"GROUP\"",
		    "\"HAVING\"",
		    "\"IF\"",
		    "\"IGNORE\"",
		    "\"IMMEDIATE\"",
		    "\"INDEX\"",
		    "\"INDEXED\"",
		    "\"INITIALLY\"",
		    "\"INSERT\"",
		    "\"INSTEAD\"",
		    "\"INTERSECT\"",
		    "\"INTO\"",
		    "\"KEY\"",
		    "\"LEFT\"",
		    "\"MATCH\"",
		    "\"NATURAL\"",
		    "\"OF\"",
		    "\"ORDER\"",
		    "\"PLAN\"",
		    "\"PRAGMA\"",
		    "\"PRIMARY\"",
		    "\"QUERY\"",
		    "\"RAISE\"",
		    "\"REFERENCES\"",
		    "\"REGEXP\"",
		    "\"REINDEX\"",
		    "\"RELEASE\"",
		    "\"RENAME\"",
		    "\"REPLACE\"",
		    "\"RESTRICT\"",
		    "\"RIGHT\"",
		    "\"ROLLBACK\"",
		    "\"ROW\"",
		    "\"SAVEPOINT\"",
		    "\"SELECT\"",
		    "\"SET\"",
		    "\"TABLE\"",
		    "\"TEMP\"",
		    "\"TEMPORARY\"",
		    "\"THEN\"",
		    "\"TO\"",
		    "\"TRANSACTION\"",
		    "\"TRIGGER\"",
		    "\"UNION\"",
		    "\"UNIQUE\"",
		    "\"UPDATE\"",
		    "\"USING\"",
		    "\"VACUUM\"",
		    "\"VALUES\"",
		    "\"VIEW\"",
		    "\"VIRTUAL\"",
		    "\"WHEN\"",
		    "\"WHERE\"",
		    "<BLOB>",
		    "\"(\"",
		    "\")\"",
		    "\";\"",
		    "\",\"",
		    "\".\"",
		  };
}

public class ParserSQL2 implements  Observers, ParserSQL {
	
	private Stack<Tokens> p_token_eleve;
	private Stack<Tokens> p_token_prof;
	private List<Stack<Tokens>>  list_p_token;
	private String reponse;
	private ParserSQL parser;
	private EventManager controller;
	private int numero_pile = 0;
	private Tokens pairToken = new TokensPermutables();
	private Stack<Tokens> p_token_accepted;
	private Stack<Paire<String>> p_ID = new Stack<Paire<String>>();
	private boolean flag = false;
    private boolean debut = false;
    private Stack<Paire<String>> p_projecttion_id = new Stack<>();
	private List<String> tokenswithOrdre = new ArrayList<String>();
	private List<Integer> p_nu = new ArrayList<>();
	private int var;
	private boolean isCorrect;
	private List<String> token_ID = new ArrayList<>();
	private Tokens typeRequete;
	
	
	public ParserSQL2() {
		initiation();
	}
	
	/**
	 *  setter 
	 */
	@Override
	public void setcontroller(EventManager controller) {
			this.controller = controller;
	}
	
	/**
	 *  mis à jour de l'attribut reponses et automatiquement la reprise de l'analyse
	 */
	@Override
	public void updateReponse(String reponse) {
		this.reponse = reponse;
		start();
	}
	
	/**
	 *  analyse de la requête du prof
	 */
	private void start() {
		try {
			parser.ReInit(Factory.translateToStream(reponse));
			parser.sqlStmtList();
		} catch (ParseException e){
		//	controller.getView().sendMessage(" Requette prof  : "+e.getMessage());
		}
	}
	
	/**
	 *  initiation des attributes
	 */
	private void initiation() {
		
		this.p_token_eleve = Factory.makeStack();
		this.p_token_prof = Factory.makeStack();
		this.p_token_accepted = Factory.makeStack();
		
		this.list_p_token =  Factory.makeList();
		this.parser = Factory.makeParserSQL("general");
		this.parser.registerObserver(this);
		this.parser.setDestination("prof");
	}
	
	/**
	 *  ( token, imagesToken)  couple de token relié à son image
	 *  envoyé par le parseur général au sujet de la requête de l'élève 
	 */
	@Override
	public void notifyEventEleve(String token, String tokenImage) {

		if(!TokenFix.ignorer(tokenImage)) {
			this.p_token_eleve.add(new TokenNoPermutable(token, tokenImage));
		}
	}
	
	/**
	 * ( token, imagesToken)  couple de token relié à son image
	 *  envoyé par le parseur général au sujet de la requête du professeur 
	 */
	@Override
	public void notifyEventProf(String token, String tokenImage) {
		System.out.println("test notif prof ");
		if(token.equals("fin")) {
			addQueryInStack();
		} else {
			try {
				addToken(token, tokenImage);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void notifyEventProjection(boolean b) {
		debut = b;		
	}
	
	private void addPartyFix(String token, String tokenImage) throws CloneNotSupportedException {
		if(flag) 
			p_token_prof.add(pairToken.clone());
		p_token_prof.add(new TokenNoPermutable(token, tokenImage));
	}
	private void addPartyVariable(String token, String tokenImage) {
		if(!flag) 
			pairToken = new TokensPermutables();
		pairToken.addToken(token, tokenImage);
	}
	
	// methode traite les token selon leur nature positionnelle dans la requête 
	private void addToken(String token, String tokenImage) throws CloneNotSupportedException {
		if(TokenFix.isFix(tokenImage)) {
			if(!TokenFix.ignorer(tokenImage)) {
				addPartyFix(token, tokenImage);
				flag = false;
			}
		} else {
			if(debut) 
				p_projecttion_id.add(new Paire<String>(token, tokenImage));			
			addPartyVariable(token, tokenImage);
			flag = true;
		}
	}
	
	/**
	 *  ajoute une requête composé d'un ensemble de token dans une pile 
	 */
	@SuppressWarnings("unchecked")
	private void addQueryInStack() {
		p_token_prof.add(new TokenNoPermutable("fin"));
		list_p_token.add((Stack<Tokens>) p_token_prof.clone());
		p_token_prof.removeAllElements();
	}
	/**
	 *  renitialisation des atttributs 
	 */	
	@Override
	public void reset() {
		debut = false;
		p_ID.removeAllElements();
		tokenswithOrdre.removeAll(tokenswithOrdre);
		p_projecttion_id.removeAllElements();
		token_ID.removeAll(token_ID);
		this.p_token_eleve.removeAllElements();
		this.p_token_prof.removeAllElements();
		this.p_token_accepted.removeAllElements();
		this.list_p_token.removeAll(list_p_token);
		p_nu.removeAll(p_nu);
		initiation();
	}
	
	/**
	 *  affichage en console  [  utile pour le test ]
	 */
	@Override
	public void display() {
		for(Tokens t :p_token_accepted){
			t.display();
		}
	}
	

	private void consumeStaticToken(Paire<String> pair,  Stack<Tokens> p) {
		p.remove(p.firstElement());	
		if(pair.containToken("\"SELECT\"")) {
			debut = true; 
		}
		else if(pair.containToken("\"FROM\"")) {
			debut = false;
		}
	}
	
	private void consumePermutableToken(Paire<String> pair , Stack<Tokens> p, String t) {
		p.firstElement().remove(pair.getImageToken(), pair.getToken());
		if(p.firstElement().isEmpty()) 
			p.remove(p.firstElement());
		if(debut) {
			token_ID.add(t);
		}
		if(pair.getImageToken().equals("<ID>")) {
			p_ID.add(pair);
		}
	}
	

	/**
	 * 
	 * @param token  associé à son image   ( year , <ID> ) 
	 * @param tokenImage
	 * @return
	 */
	private boolean consume(String tokenImage, String token) {
		update(false, 0);
		for(Stack<Tokens> pileToken : list_p_token){
			if(!pileToken.isEmpty()){
				if(pileToken.firstElement().contain(tokenImage)) { 
					check(tokenImage, var, pileToken.firstElement(), pileToken, token);
					numero_pile = var;
					p_nu.add(var);
				}
				var++;
			}	
		}
		return isCorrect;
	}
	
	private void update(boolean isCorrect, int va) {
		this.isCorrect = isCorrect;
		this.var = va; 
	}
	
	
	private void check(String tokenImage, int v, Tokens tokenTmp, Stack<Tokens> pileToken, String token) {
		update(true, var);
		if(tokenTmp.getTypeToken().equals(TypePArtie.STATIC)) 					
			consumeStaticToken(tokenTmp.getCoupleToken("",""), pileToken);
		 else 
			consumePermutableToken(tokenTmp.getCoupleToken(tokenImage, token), pileToken, token);
	}
	
	
	private void updatelistePile() {
		if(list_p_token.size() != p_nu.size()){
			for(int i = 0; i < list_p_token.size(); i++){
				if(!p_nu.contains(i))
					list_p_token.remove(list_p_token.get(i));
			}
		}
		p_nu.removeAll(p_nu);
	}
	
	//
	private void checkSameRequete() throws ParseException {
		if(!p_token_eleve.firstElement().getTokenImage().equals(list_p_token.get(0).firstElement().getTokenImage())){
			throw new ParseException("Attend en premier le mot clef suivant : "+ list_p_token.get(0).firstElement().getToken());
		}
	}
	/**
	 *   parseur relatif
	 */
	@Override
	public void sqlStmtList() throws ParseException {
	    typeRequete = p_token_eleve.firstElement();
	    checkSameRequete();
		while(!p_token_eleve.isEmpty()) {
			Tokens tmp = p_token_eleve.firstElement();
			if(consume(tmp.getTokenImage(), tmp.getToken())) {
				updatePile(tmp);
				updatelistePile();
			} else {
				//dans le cas où il y a pas une décision
				break;
				//manageError();
			}
		}
		checkID();
	}
	
	@Override
	public String getTypeRequete() {
		return typeRequete.getTokenImage();
	}	
	
	
	
	/**
	 *  gestion d'erreur
	 * @throws ParseException
	 */
	private void manageError() throws ParseException  {
		Tokens nextToken = list_p_token.get(numero_pile).firstElement();
		Tokens lastToken = p_token_accepted.lastElement();
		throw new ParseException(nextToken.getTokenImage(), lastToken.getToken(), numero_pile, list_p_token.get(numero_pile).size());
	}
	
	private void checkID(){
		for(Paire<String> p : p_projecttion_id){
			if(token_ID.contains(p.getToken())) 
				tokenswithOrdre.add(p.getToken());
		}	
	}
	
	/**
	 *   mis à jour de la pile des token de l'eleve
	 *   
	 * @param tokenEleve
	 */
	private void updatePile(Tokens tokenEleve) {
		p_token_eleve.remove(tokenEleve);
		p_token_accepted.add(new TokenNoPermutable(tokenEleve.getToken(), tokenEleve.getTokenImage()));
	}
	
	/**
	 *  check l'exitance des ids dans le l'espace des id 
	 * @param token
	 * @return
	 */
	private boolean isExistID(String token) {
		for(Paire<String> pair : p_ID) {
			if(pair.getToken().equals(token))
				return true;
		}
		return false;
	}
	/**
	 *   analyse  sémantique des ID s  
	 */
	public void startAnalyseSemantic(boolean ordre) throws  ParseException {
		for(Tokens token : p_token_accepted) {
			if(token.getTokenImage().equals("<ID>")) {
				if(!isExistID(token.getToken())) 
					throw new ParseException("error semantique  :=  \n   unknown : "+ token.getToken());
			}
		}
		if(ordre) 
			checkOrdre();
	
	}
	
	/**
	 *  check l'ordre des colonnes selon l'ordre de shémas de la table 
	 * @throws ParseException
	 */
	private void checkOrdre() throws ParseException {
		for(int i = 0; i < tokenswithOrdre.size(); i++) {
			if( i < token_ID.size()) {
			  if(!tokenswithOrdre.get(i).equals(token_ID.get(i)))
				throw new ParseException("Error , l'ordre des colonnes ne respecte pas l'ordre indiqué !!! ");
			}
		}
	}
}



	
/*
 *  cette classe représente la partie non permutable
 */
class TokenNoPermutable extends Tokens {


	private Paire<String> pair;
	public TokenNoPermutable(String token) {
		pair = new Paire<String>(token, "fin");
		typePArtie = TypePArtie.STATIC;
	}
	
	@Override
	public String getToken() {
		return pair.getToken();
	}




	public TokenNoPermutable(String token, String tokenImage) {
		addToken(token, tokenImage);
		typePArtie = TypePArtie.STATIC;
	}
	
	@Override
	public void addToken(String token, String tokenImage) {
		pair = new Paire<String>(token, tokenImage);
	}
	
	@Override
	protected Tokens clone() throws CloneNotSupportedException {
		return (TokenNoPermutable)super.clone();
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	protected boolean contain(String tokenImage) {	
		return this.pair.containToken(tokenImage);
	}

	@Override
	public boolean isExist(String token) {
		return false;
	}

	@Override
	public void display() {
		pair.display();
	}


	@Override
	public String getTokenImage() {
		return pair.getImageToken();
	}

	@Override
	public void remove(String tokenImage, String token) {}

	@Override
	public Paire<String> getCoupleToken(String tokenImage, String token) {
		return pair;
	}

	@Override
	public int size() {
		return 0;
	}
}


/**
 * 
 * @author 
 * cette class represente la partie permutable
 */
class TokensPermutables extends Tokens {
	
	private List<Paire<String>> tokens;
	
	public TokensPermutables() {
		tokens = new ArrayList<>();
		typePArtie = TypePArtie.PERMUTABLE;
	}
	
	@Override
	public void addToken(String token, String tokenImage) {
		tokens.add(new Paire<String>(token, tokenImage));
	}

	@Override
	public boolean isEmpty() {
		return tokens.isEmpty();
	}

	@Override
	protected boolean contain(String tokenImage) {
		for(Paire<String> pair : tokens){
			if(pair.containToken(tokenImage)){
				return true;
			}
		}
		return false;
	}
	@Override
	protected Tokens clone() throws CloneNotSupportedException {
		return (TokensPermutables)super.clone();
	}
	@Override
	public boolean isExist(String token) {
		return false;
	}

	@Override
	public void display() {
		for(Paire<String> pair : tokens) 
			pair.display();
	}

	@Override
	public String getToken() {
		return tokens.get(0).getToken();
	}

	@Override
	public String getTokenImage() {
		
		return tokens.get(0).getImageToken();
	}
	
	@Override
	public void remove(String tokenImage, String token) {
		tokens.remove(getCoupleToken(tokenImage, token));
	}

	@Override   // to do pour l erreur syntaxique 
	public Paire<String> getCoupleToken(String tokenImage,String token) throws IllegalArgumentException {
		for(Paire<String> pair : tokens){ 
			if(pair.containToken(tokenImage) && pair.getToken().equals(token))
				return pair;
			else if(pair.containToken(tokenImage))
				return pair;
		}
		throw new IllegalArgumentException("");
	}

	@Override
	public int size() {
		return tokens.size();
	}
	

}



//	/**
//	 * methode consume un token dans la requête de l'élève si les règles sont satisfaite
//	 *  les règles : respecte l'une des solutions proposés par le prof
//	 * @param token
//	 * @return
//	 */

//	
