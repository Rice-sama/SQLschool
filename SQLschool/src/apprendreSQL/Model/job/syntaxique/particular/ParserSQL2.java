package apprendreSQL.Model.job.syntaxique.particular;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import apprendreSQL.Controller.EventManager;
import apprendreSQL.Model.data.Factory;
import apprendreSQL.Model.data.Observers;
import apprendreSQL.Model.data.Question;
import apprendreSQL.Model.job.syntaxique.general.ParseException;
import apprendreSQL.Model.job.syntaxique.general.ParserSQL;
import apprendreSQL.Model.job.syntaxique.particular.Tokens.TypePArtie;



/**
 * c'est une classe interne 
 * 
 *
 */
class TokenFix {

	private static boolean isIDAfterAS = false;

	public static  boolean isFix(String token) {
		for(String tkn : tokenImage) {
			if(tkn.equals(token)) 
				return true;
		}
		return false; 
	}


	public static boolean ignorer(String tokenImage) {
		if(tokenImage.equals("\"AS\"")){
			isIDAfterAS = true;
			return true;
		}
		if(isIDAfterAS) {
			System.out.println("check id as "+tokenImage);
			isIDAfterAS = false;
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

/**
 * 
 *  Cette a comme responsabilité la compilation d'une requête relativement à une question
 *
 */
public class ParserSQL2 implements  Observers, ParserSQL {
	
	private Question question;
	private ParserSQL parser;
	
	private Stack<Tokens> pileTokenEleve;
	private Stack<Tokens> pileTokenProf;
	private List<Stack<Tokens>>  listPileToken; 
    private Tokens pairToken = new TokensPermutables();
	private BillanID billanID;
	private Stack<Tokens> pileTokenAcceptedEleve;
	private Stack<Paire<String>> pileIDConsumedProf = Factory.makeStack();
	
	private boolean flag = false;
    private boolean debutAttribut = false;
    
    private Stack<Paire<String>> pileAttributIDProf = Factory.makeStack();
    private Stack<Paire<String>> pileAttributIDElev = Factory.makeStack();
    private Stack<String> pileIDEleve = Factory.makeStack();
    private Stack<String> pileIDProf = Factory.makeStack();


	private List<String> tokenswithOrdre = Factory.makeList(); 
	private boolean isCorrect;
	private List<String> envAttributIDProf = Factory.makeList();
	private Tokens typeRequete;
	private List<String> idTokensProf = Factory.makeList();

	
	public ParserSQL2() {
		initiation();
	}
	
	/**
	 *  setter 
	 */
	@Override
	public void setcontroller(EventManager controller) {}
	
	/**
	 *  mis à jour de l'attribut reponses et automatiquement la reprise de l'analyse
	 */
	@Override
	public void updateReponse(Question question) {
		this.question = question;
		startParserReponsesProf();
	}
	
	/**
	 *  analyse de la requête du prof
	 */
	private void startParserReponsesProf() {
		try {
			parser.ReInit(Factory.translateToStream(question.getAnswer()));
			parser.parserStart();
			idTokensProf = parser.getIdTokens();
		} catch (ParseException e){}
	}
	
	/**
	 *  initiation des attributes
	 */
	private void initiation() {
		this.pileTokenEleve = Factory.makeStack();
		this.pileTokenProf = Factory.makeStack();
		this.pileTokenAcceptedEleve = Factory.makeStack();
		this.listPileToken =  Factory.makeList();
		this.parser = Factory.makeParserSQL("general");
		this.parser.registerObserver(this);
		this.parser.setDestination("prof");
		billanID = new BillanID();
	}
	
	/**
	 *  ( token, imagesToken)  couple de token relié à son image
	 *  envoyé par le parseur général au sujet de la requête de l'élève 
	 */
	@Override
	public void notifyEventEleve(String token, String tokenImage) {
		 if(tokenImage.equals("<ID>")) {
			pileIDEleve.add(token);
		}
		if(!TokenFix.ignorer(tokenImage)) {
			this.pileTokenEleve.add(new TokenNoPermutable(token, tokenImage));
		}
	}
	
	/**
	 * ( token, imagesToken)  couple de token relié à son image
	 *  envoyé par le parseur général au sujet de la requête du professeur 
	 */
	@Override
	public void notifyEventProf(String token, String tokenImage) {
		if(tokenImage.equals("<ID>")) {
				pileIDProf.add(token);
		}
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
	public ArrayList<String> getIdTokens(){
		return (ArrayList<String>) idTokensProf;
	}
	
	
	@Override
	public void notifyEventProjection(boolean firstEnd) {
		debutAttribut = firstEnd;		
	}
	
	private void addPartyFix(String token, String tokenImage) throws CloneNotSupportedException {
		if(flag) 
			pileTokenProf.add(pairToken.clone());
		pileTokenProf.add(new TokenNoPermutable(token, tokenImage));
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
			if(debutAttribut) 
				pileAttributIDProf.add(new Paire<String>(token, tokenImage));			
			addPartyVariable(token, tokenImage);
			flag = true;
		}
	}
	
	/**
	 *  ajoute une requête composé d'un ensemble de token dans une pile 
	 */
	@SuppressWarnings("unchecked")
	private void addQueryInStack() {
		pileTokenProf.add(new TokenNoPermutable("fin"));
		listPileToken.add((Stack<Tokens>) pileTokenProf.clone());
		pileTokenProf.removeAllElements();
	}
	/**
	 *  renitialisation des atttributs 
	 */	
	@Override
	public void reset() {
		debutAttribut = false;
		pileIDConsumedProf.removeAllElements();
		tokenswithOrdre.removeAll(tokenswithOrdre);
		this.pileIDEleve.removeAllElements();
		this.pileIDProf.removeAllElements();
		this.pileAttributIDProf.removeAllElements();
		envAttributIDProf.removeAll(envAttributIDProf);
		pileAttributIDElev.removeAllElements();
		this.pileTokenEleve.removeAllElements();
		this.pileTokenProf.removeAllElements();
		this.pileTokenAcceptedEleve.removeAllElements();
		this.listPileToken.removeAll(listPileToken);
		initiation();
	}
	
	
	
	/**
	 *  affichage en console  [  utile pour le test ]
	 */
	@Override
	public void display() {
		for(Tokens t :pileTokenAcceptedEleve){
			t.display();
		}
	}
	

	private void consumeStaticToken(Paire<String> pair,  Stack<Tokens> p) {
		p.remove(p.firstElement());	
		if(pair.containToken("\"SELECT\"")) {
			debutAttribut = true; 
		}
		else if(pair.containToken("\"FROM\"")) {
			debutAttribut = false;
		}
	}
	
	private void consumePermutableToken(Paire<String> pair , Stack<Tokens> p, String t) {
		p.firstElement().remove(pair.getImageToken(), pair.getToken());
		if(p.firstElement().isEmpty()) 
			p.remove(p.firstElement());
		if(debutAttribut) {
			envAttributIDProf.add(t);
		}
		if(pair.getImageToken().equals("<ID>")) {
			pileIDConsumedProf.add(pair);
		}
	}
	

	/**
	 * 
	 * @param token  associé à son image   ( year , <ID> ) 
	 * @param tokenImage
	 * @return
	 */
	private boolean consume(String tokenImage, String token) {
		update(false);
		for(Stack<Tokens> pileToken : listPileToken){
			if(!pileToken.isEmpty()){
				if(pileToken.firstElement().contain(tokenImage)) 
					check(tokenImage,pileToken.firstElement(), pileToken, token);
			}	
		}
		return isCorrect;
	}
	
	private void update(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	
	private void check(String tokenImage,Tokens tokenTmp, Stack<Tokens> pileToken, String token) {
		update(true);
		if(tokenTmp.getTypeToken().equals(TypePArtie.STATIC)) 					
			consumeStaticToken(tokenTmp.getCoupleToken("",""), pileToken);
		 else 
			consumePermutableToken(tokenTmp.getCoupleToken(tokenImage, token), pileToken, token);
	}
	
	public String analysID() {
		if(pileIDEleve.size() > pileIDProf.size()) {
			billanID.addIDEQ(pileIDEleve, pileIDProf);
			return "vous avez beaucoup des identifiants dans votre requête, il faut enlever  "+ billanID.toString();
		}else if(pileIDEleve.size() < pileIDProf.size()) {
			billanID.addIDEQ(pileIDProf, pileIDEleve);
			return " vous avez moins des identifiants dans votre requête, il vous manque : " + billanID.toString(); 
		}
		return "il y a des identifiants pas demandée à ecrire dans votre requête " + billanID.toString();
	}

	
	// methode qui vérifier est ce que les deux requête (éléve, prof) ont le même debut concernant la requete 
	private void checkSameRequete() throws ParseException {
		if(!pileTokenEleve.firstElement().getTokenImage().equals(listPileToken.get(0).firstElement().getTokenImage())){
			throw new ParseException("Attention ! la requête doit commencer par : "+ listPileToken.get(0).firstElement().getToken());
		}
	}
	
	/**
	 *   parseur relatif
	 */
	@Override
	public void parserStart() throws ParseException {
	    typeRequete = pileTokenEleve.firstElement();
	    checkSameRequete();
		while(!pileTokenEleve.isEmpty()) {
			Tokens tmp = pileTokenEleve.firstElement();
			if(consume(tmp.getTokenImage(), tmp.getToken())) {
				updatePile(tmp);
			} else {
				return;  			   	//dans le cas où il y a pas une décision
			}
		}
		if(typeRequete.getTokenImage().equals("\"SELECT\"")){
			checkID();		   
			startAnalyseSemantic(true);
		}
	}
	

	@Override
	public String getTypeRequete() {
		return typeRequete.getTokenImage();
	}	
	
	
	

	

	/**
	 *   mis à jour de la pile des token de l'eleve
	 *   
	 * @param tokenEleve
	 */
	private void updatePile(Tokens tokenEleve) {
		pileTokenEleve.remove(tokenEleve);
		pileTokenAcceptedEleve.add(new TokenNoPermutable(tokenEleve.getToken(), tokenEleve.getTokenImage()));
	}
	
	
	
	/**
	 *  check l'exitance des ids dans le l'espace des id 
	 * @param token
	 * @return
	 */
	private boolean isExistID(String token) {
		for(Paire<String> pair : pileIDConsumedProf) {
			if(pair.getToken().equals(token))
				return true;
		}
		return false;
	}
	/**
	 *   analyse  sémantique des ID s  
	 */
	public void startAnalyseSemantic(boolean ordre) throws  ParseException {
		for(Tokens token : pileTokenAcceptedEleve) {
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
			if( i < envAttributIDProf.size()) {
			  if(!tokenswithOrdre.get(i).equals(envAttributIDProf.get(i)))
				throw new ParseException("Erreur : l'ordre des colonnes ne respecte pas l'ordre indiqué dans la question ?. ");
			}
		}
	}
	private void checkID(){
		for(Paire<String> p : pileAttributIDProf){
			if(envAttributIDProf.contains(p.getToken())) 
				tokenswithOrdre.add(p.getToken());
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

class Paire<T> {
	private T token;
	private T imageToken;
	public Paire (T token, T imageToken){
		this.token      = token;
		this.imageToken = imageToken;
	}
	
	public T getToken() {
		return token;
	}
	
	public T getImageToken() {
		return imageToken;
	}
	
	public void display() {
		System.out.print("( "+token+" , "+imageToken+" )");
	}
	
	public boolean containToken(T tokenIm) {
		return this.imageToken == tokenIm;
	}
	
	
}
abstract class Tokens implements Cloneable {
	
	protected TypePArtie typePArtie;
	
	static enum TypePArtie {STATIC, PERMUTABLE};
	public abstract void addToken(String imageToken, String token) ;
	public abstract boolean isEmpty() ;
	protected abstract boolean contain(String token) ;		
	public abstract boolean isExist(String token) ;
	public abstract void display();
	public abstract String getToken();
	public abstract String getTokenImage();
	public abstract void remove(String tokenImage, String token);
	public abstract Paire<String> getCoupleToken(String tokenImahe, String token);
	public abstract int size();
	public TypePArtie getTypeToken() {
		return typePArtie;
	}
	@Override
	protected Tokens clone() throws CloneNotSupportedException {
		return (Tokens) super.clone();
	}

}


//	/**
//	 * methode consume un token dans la requête de l'élève si les règles sont satisfaite
//	 *  les règles : respecte l'une des solutions proposés par le prof
//	 * @param token
//	 * @return
//	 */

//	
