package model.JSONFiles ;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import model.JSONFiles.exceptions.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/* Questo file permette di leggere un file con estensione .json 
   contentente l'implementazione di un grafo. 
   
   
   dati caricati da un file di testo
   strutturato secondo regole stabilite dall’applicazione
   ALGA verifica se il file è valido ed è possibile caricare i dati; in caso
   contrario notifica all’utente la presenza di errori
*/

public class ReadJSONFile {
	
	String fileName ;
	JSONParser parser = new JSONParser();
	
	/*
	 * Constructor. 
	 * Memorizes the file path in a local parameter.
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
     * This method returns the JSON Object contained in the file.
     * Use only if the .json file contains one object.	
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
	 * This method opens an input file stream, reads it content
	 * and returns a String with the same content.
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
	
}
	
	