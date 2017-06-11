package model.JSONFiles ;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator ;
import model.graphs.*;
import model.JSONFiles.exceptions.*;
import model.node.visual.*;
import org.json.simple.*;
import org.json.simple.parser.*;


//TODO: metodi per generare il file 
/* permette di leggere un file con estensione .json 
   contentente l'implementazione di un grafo. 
*/

public class ReadJSONFile {
	
	String fileName ;
	JSONParser parser;
	
	/*
	 * Costruttore.
	 */
	public ReadJSONFile() {
		
		this.fileName = "" ;
		this.parser = new JSONParser() ;
	}
	
	/*
	 * Costruttore:
	 * memorizza il percorso del file da leggere, che deve essere passato come parametro.
	 */
	public ReadJSONFile(String filePath) {
		
	    this.fileName = filePath ;
		this.parser = new JSONParser() ;
	}
	
    /*
     * Questo metodo ritorna il JSONObject contenuto nel file.
     */
	public JSONObject getFile () throws WrongFileExtension,EmptyFileName {
		
		if (!this.fileName.endsWith(".json")) {
			throw new WrongFileExtension() ;
		}
		
		if (this.fileName.isEmpty()){
			throw new EmptyFileName() ;
		}
		
		JSONObject jsonObject ;
		
        try {
			
        	FileReader reader = new FileReader(fileName) ;
        	
        	Object obj = this.parser.parse(reader) ;
     		
     		jsonObject = (JSONObject) obj ;
     		
     		return jsonObject ;
             
		} catch (FileNotFoundException e) {
            
			e.printStackTrace();
        
		} catch (IOException e) {
            
			e.printStackTrace();
        
		} catch (ParseException e) {
            
			System.out.println("Il contenuto del file non è corretto");
			
		}
        
        return null ;
    }
	
	/*
	 * Questo metodo restituisce un grafo a partire da un JSONObject.
	 * Solleva un'eccezione nel caso in cui l'oggetto non rispetti le regole sintattiche
	 * notificate dal software.
	 */
	public Graph<CoordinateNode> readGraph(JSONObject object) throws InvalidFileObject {
		
		Graph<CoordinateNode> G = new Graph<CoordinateNode>() ;
		
		if (object.isEmpty() || object.keySet().size() != 2) {		//se l'oggetto è vuoto oppure non ha esattamente due chiavi
			throw new InvalidFileObject() ;
		}
		
		if(!object.containsKey("vl") || !object.containsKey("el")) {		//se non ci sono entrambe le chiavi vl ed el
			throw new InvalidFileObject() ;
		}
		
		JSONObject verticesList ;
		
		
		try {
			verticesList = (JSONObject) object.get("vl") ;
		} catch (Exception e) {
			throw new InvalidFileObject() ;
		}
		
		if (verticesList.isEmpty()) {		//se l'insieme dei nodi è vuoto
			throw new InvalidFileObject() ;
		}
		
		for(Object node : verticesList.keySet()) {		//per ogni valore creo un nodo e lo inserisco nel grafo
			
			try {
				Integer index = Integer.parseInt(node.toString()) ;
				
				JSONObject nodeVal = (JSONObject) verticesList.get(node) ;
				CoordinateNode infoN = new CoordinateNode(index, (double) Integer.parseInt(nodeVal.get("x").toString()),(double) Integer.parseInt(nodeVal.get("y").toString())) ;
				Node<CoordinateNode> CoNode = (Node<CoordinateNode>) new Node(infoN) ;
				
				G.insertNode(CoNode) ;
			} catch (NumberFormatException e) {		//se i nodi non sono etichettati a valori interi
				throw new InvalidFileObject() ;
			}
		}
		
	
		JSONObject edgesList ;
		
		try {
			edgesList = (JSONObject) object.get("el") ;
		} catch (Exception e) {
			throw new InvalidFileObject() ;
		}
		
		if (edgesList.isEmpty()) {		//se l'insieme degli archi è vuoto
			throw new InvalidFileObject() ;
		}
		
		for (Object edge : edgesList.keySet()) {
			
			JSONObject edgeVal = (JSONObject) edgesList.get(edge) ;
			
			Node<CoordinateNode> u = null ;
			Node<CoordinateNode> v = null ;
			
	        Iterator<Node<CoordinateNode>> I = G.V().iterator() ;
			
			while (I.hasNext()) {		//cerco i nodi corrispondenti ad u e v
				
				Node<CoordinateNode> nodeToC = I.next() ; 
				
				try {
					
					Integer uInd = Integer.parseInt(edgeVal.get("u").toString()) ;
					Integer vInd = Integer.parseInt(edgeVal.get("v").toString()) ;
					
					if (nodeToC.getElement().getIndex() == uInd) {
						u = nodeToC ;
					}
					
					else if (nodeToC.getElement().getIndex() == vInd) {
						v = nodeToC;
					}
					
				} catch (NumberFormatException e) {		//se i nodi non sono associati ad un valore intero
					
					throw new InvalidFileObject() ;
					
				}
			}
			
			G.insertEdge(u, v) ;
		}
		
		return G ;
	} 

	/*
	 * Questo è il metodo da invocare nel MainController.
	 * Richiama tutti gli altri metodi facendo sì che venga restituito il 
	 * Grafo contenuto nel file .json, gestendo tutti gli eventuali errori.
	 */
	public Graph<CoordinateNode> readGraphFromJSONFilereader(){
		
     	try {
			
     		JSONObject fileObj = getFile() ;
			
			if (fileObj != null) {
				
				return readGraph(fileObj);
			}
			
			
		} catch (WrongFileExtension e) {
			
			System.out.println("Impossibile aprire lo stream con il file " + this.fileName +".");
		
		} catch (EmptyFileName e){
			
			System.out.println("Impossibile aprire lo stream con il file " + this.fileName +".");
		
		} catch (InvalidFileObject e) {
			
			System.out.println("Il contenuto del file non è corretto");
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
	
}
	
	