package model.JSONFiles ;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Long ;
import model.graphs.*;
import model.JSONFiles.exceptions.*;
import model.node.visual.*;
import org.json.simple.*;
import org.json.simple.parser.*;


//TODO: metodi per generare il file 
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
	

	//TODO: gestire errori
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
			System.out.println("Impossibile leggere il valore associato alla chiave 'vl' ");
			System.out.print(e);
			throw new InvalidFileObject() ;
		}
		
		if (verticesList.isEmpty()) {		//se l'insieme dei nodi è vuoto
			throw new InvalidFileObject() ;
		}
		
		for(Object node : verticesList.keySet()) {
			
			Integer index = Integer.parseInt(node.toString()) ;
			
			JSONObject nodeVal = (JSONObject) verticesList.get(node) ;
			CoordinateNode infoN = new CoordinateNode(index, (double) Integer.parseInt(nodeVal.get("x").toString()),(double) Integer.parseInt(nodeVal.get("y").toString())) ;
			Node<CoordinateNode> CoNode = (Node<CoordinateNode>) new Node(infoN) ;
			
			G.insertNode(CoNode) ;
			
		}
		
		Object[] nodesEl =  G.V().toArray() ;
		
		JSONObject edgesList ;
		
		try {
			edgesList = (JSONObject) object.get("el") ;
		} catch (Exception e) {
			System.out.println("Impossibile leggere il valore associato alla chiave 'el' ");
			System.out.print(e);
			throw new InvalidFileObject() ;
		}
		
		if (edgesList.isEmpty()) {		//se l'insieme degli archi è vuoto
			throw new InvalidFileObject() ;
		}
		
		for (Object edge : edgesList.keySet()) {
			
			JSONObject edgeVal = (JSONObject) edgesList.get(edge) ;
			
			Node<CoordinateNode> u = null ;
			Node<CoordinateNode> v = null ;
			
			for (Object el : nodesEl) {
				
				if (el == edgeVal.get("u")) {
					u = (Node<CoordinateNode>)edgeVal.get("u") ;
				}
				
				else if (el == edgeVal.get("v")) {
					v = (Node<CoordinateNode>)edgeVal.get("v") ;
				}
			}
			
			G.insertEdge(u, v) ;
			
			if (!G.insertEdge(u,v)) {
				System.out.println("impossibile creare l'arco tra " + u + " e " +  v);
			}
			
		}
		
		
		
		return G ;
	} 

	/*
	 * Qeusto metodo verifica se l'oggetto passato come parametro rappresenta una 
	 * stringa di valori interi. In caso positivo restituisce l'intero in questione.
	 * In caso negativo restituisce null.
	 */
	public Integer isIntegerString(JSONObject obj) {
		
		Integer n ;
		String str = obj.toString() ;
		
	    if (str == null) {
	        n = null ;
	    }
	    
	    int length = str.length();
	    
	    if (length == 0) {
	    	n = null;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	        	n = null;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	        	n = null;
	        }
	    }
	    
	    try{
	    		n = Integer.parseInt(str) ;
	    } catch (NumberFormatException e) {
	    		n = null ;
	    }
	    
	    return n ;
		
		
	}
	
	
	public static void main(String[] args ) {
		
		ReadJSONFile reader ;
		
		try {
			reader = new ReadJSONFile("/home/melania/Scrivania/UNI BOH/INFORMATICA/II SEMESTRE/ALGORITMI/ALGA/examples.json") ;
		    System.out.println("Il file è stato aperto con successo.");
		} catch (WrongFileExtension exc1) {
			System.out.println(exc1);
			reader = null ;
			System.out.println("L'estensione del file selezionato non è corretta. Scegliere un file con estensione .json");
		} catch (EmptyFileName exc2) {
			System.out.println(exc2);
			reader = null ;
			System.out.println("Il percorso del file selezionato è vuoto. Scegliere un file con un percorso diverso");
		}
		
		System.out.println("Il contenuto del file è il seguente: ");
		System.out.println(reader.getFile().toString());
		
		System.out.println("Il grafo rappresentato è: ");
		try {
			reader.readGraph(reader.getFile()).print();
		} catch (InvalidFileObject e) {
			System.out.println(e);
		}
	}
	
}
	
	