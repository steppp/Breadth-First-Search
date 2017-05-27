package model.JSONFiles ;

import org.json.simple.*;
import java.util.Map ;
import java.util.Set ;
import java.util.Iterator ;
import org.json.JSONString;

/*
dati caricati da un file di testo
strutturato secondo regole stabilite dall’applicazione
ALGA verifica se il file è valido ed è possibile caricare i dati; in caso
contrario notifica all’utente la presenza di errori
*/

//TODO: completare la funzione isValidObject
public class ManipulateJSONFile {
	
	
	public ManipulateJSONFile() {
	}	
	
	/*
	 * This method verifies that the input JSON Object represents a graph.
	 */
	public Boolean isValidObject(JSONObject object) throws InvalidFileObject {
	      
		Boolean tmp = true ;
		
		if (object.isEmpty() || 
				object.keySet().size() != 2 ||
				object.values() != Collection<JSONObject> ) {
			throw new InvalidFileObject() ;
			tmp = false ;
		}
		
		if(!object.containsKey("vl") || !object.containsKey("el")) {
			throw new InvalidFileObject() ;
			tmp = false ;
		}
		
		JSONObject verticesList = object.get("vl") ;
		JSONObject edgesList = object.get("el") ;
		
		if (verticesList.isEmpty() || edgesList.isEmpty() ||
				verticesList.values() != Collection<JSONObject> || 
				edgesList.values() != Collection<JSONObject>) {
			throw new InvalidFileObject() ;
			tmp = false ;
		}
		
		for (JSONString node : verticesList.keySet()) {
			
		}
		
		for (JSONString edge : edgesList.keySet()) {
			
		}
		
	    return tmp ;
	}
	
	
}