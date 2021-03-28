package apprendreSQL.view.version2.fileXml;

public interface Config {
	String pathToXml = "/apprendreSQL/view/version2/fileXml/";
	
	public static String setFileName(String name) {
		return pathToXml + name+ ".fxml";
	}
	public static String getHomaPage() {
		return "fileXml/home.fxml";
	}
	
   static int WIDTH = 1280;
   static int HEIGHT = 800;
	
}
