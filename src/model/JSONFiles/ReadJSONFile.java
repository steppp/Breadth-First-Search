package model.JSONFiles ;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map ;
import java.util.Set ;
import java.util.Iterator ;
import java.io.IOException;
import model.graphs.*;
import model.JSONFiles.exceptions.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/* Questo file permette di leggere un file con estensione .json 
   contentente l'implementazione di un grafo. 
   dati caricati da un file di testo
    •  strutturato secondo regole stabilite dall’applicazione
    •  ALGA verifica se il file è valido ed è possibile caricare i dati; in caso
       contrario notifica all’utente la presenza di errori
*/

public class ReadJSONFile {
	
	String fileName ;
	JSONParser parser = new JSONParser();
	
	/*
	 * Costruttore.
	 * Memorizza il percorso del file da prendere in input.
	 */
	public ReadJSONFile(String filePath) throws WrongFileExtension,EmptyFileName {
		
		if (!filePath.endsWith(".json")) {
			throw new WrongFileExtension() ;
		}
		
		if (filePath.isEmpty()){
			throw new EmptyFileName() ;
		}
		
		this.fileName = filePath ;
	}
	
    /*
     * Questo metodo ritorna il JSONObject contenuto nel file.
     * Assumiamo che il file con estensione .json contenga un unico oggetto.
     */
	public JSONObject getFile () {
		
		JSONObject jsonObject ;
		
        try {
			
			Object obj = this.parser.parse(new FileReader(fileName)) ;
			
			jsonObject = (JSONObject) obj ;
			
			return jsonObject ;
	
		} catch (FileNotFoundException e) {
            
			e.printStackTrace(); 
        
		} catch (IOException e) {
            
			e.printStackTrace();
        
		} catch (ParseException e) {
            
			System.out.println("Error occurred while parsing " + fileName + " file.");
			e.printStackTrace();
	    
		}
        
        return null ;
        
	}
	
	/*
	 * Questo metodo apre lo stream con il file di input, ne legge 
	 * e copia il contenuto in una stringa che viene restituita dallo stesso.
	 * 
	 */
	public String getFileContent() {
		
		String FileC = "" ;
		
		try {
			
			Object obj = this.parser.parse(new FileReader(fileName)) ;
			
			JSONObject jsonObject = (JSONObject) obj ;
			
			FileC = jsonObject.toString() ;
	
		} catch (FileNotFoundException e) {
            
			e.printStackTrace(); 
        
		} catch (IOException e) {
            
			e.printStackTrace();
        
		} catch (ParseException e) {
            
			System.out.println("Error occurred while parsing " + fileName + " file.");
			e.printStackTrace();
	    
		}
		
		return FileC ;
		
	}
	

	/*
	 * Questo metodo restituisce un grafo a partire da un JSONObject.
	 * Solleva un'eccezione nel caso in cui l'oggetto non rispetti le regole sintattiche
	 * notificate dal software.
	 */
	public Graph<Integer> readGraph(JSONObject object) throws InvalidFileObject {
		
		Graph<Integer> G = new Graph<Integer>() ;
		
		if (object.isEmpty() || object.keySet().size() != 2) {		//se l'oggetto è vuoto oppure non ha esattamente due chiavi
			throw new InvalidFileObject() ;
			
		}
		
		if(!object.containsKey("vl") || !object.containsKey("el")) {		//se non ci sono entrambe le chiavi vl ed el
			throw new InvalidFileObject() ;
		}
		
		//TODO: type check - try/catch which exception?
		JSONObject verticesList = (JSONObject) object.get("vl") ;
		JSONObject edgesList = (JSONObject) object.get("el") ;
		
		if (verticesList.isEmpty() || edgesList.isEmpty()) {		//se l'insieme dei nodi o quello degli archi sono vuoti
			throw new InvalidFileObject() ;
		}
		
		
		
		return G ;
	} 
	
	
}
	
	