package model.JSONFiles ;

import java.io.FileWriter ;
import java.io.IOException ;
import model.JSONFiles.exceptions.*;
import model.node.visual.*;
import model.graphs.*;
import org.json.simple.* ;

/**
 * Questa classe permette di generare un file con estensione .json 
 * dove memorizzare l'attuale grafo creato dall'utente.
 * @author melania
 *
 */
public class JSONFileWriter {
	
	String fileName ;
	
	public JSONFileWriter() {
		this.fileName = "" ;
	}
	
	
	/**
	 * Costruttore.
	 * Se passati come argomenti un percorso di un file con estensione .json
	 * ed un grafo, questo scrive all'interno del file il grafo in formato .json .
	 * Se il file non esiste, ne crea uno nuovo.
	 * @param FilePath percorso del file .json su cui scrivere
	 * @param G grafo attuale da convertire e scrivere su file
	 * @throws WrongFileExtension
	 * @throws EmptyFileName
	 */
	public JSONFileWriter(String FilePath, Graph<CoordinateNode> G) throws WrongFileExtension, EmptyFileName{
		
		if (!FilePath.endsWith(".json")) {
			throw new WrongFileExtension() ;
		}
		
		if (FilePath.isEmpty()){
			throw new EmptyFileName() ;
		}
		
		this.fileName = FilePath ;
	    
		try {
			
			writeFile(toJSONObject(G)) ;
		
		} catch (IOException e) {
			
			System.out.println("Impossibile creare il file " + this.fileName) ;
		
		}
	}
	
	
	/**
	 * Questo metodo converte il Grafo passato come parametro in un JSONObject.
	 * @param G grafo da convertire in JSONObject
	 * @return JSONObject corrispondente
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject (Graph<CoordinateNode> G) {
		
		JSONObject obj = new JSONObject() ;
		
		Integer edgeIndex = 0 ;
		
		JSONObject verticesListObj = new JSONObject() ;
		JSONObject edgesListObj = new JSONObject() ;
		
		
		for (Node<CoordinateNode> node : G.V()) {
		
			//Creo l'insieme dei nodi
			
			JSONObject nodeObj = new JSONObject() ;
			
			nodeObj.put("x", node.getElement().getxPos()) ;
			nodeObj.put("y", node.getElement().getyPos()) ;
			
			verticesListObj.put(Integer.toString(node.getElement().getIndex()), nodeObj) ;
			
			//Creo l'insieme degli archi
			
			for (Node<CoordinateNode> nodeV : G.adj(node)) {
				
				JSONObject vertexObj = new JSONObject();
				
				vertexObj.put("u", node.getElement().getIndex()) ;
				vertexObj.put("v", nodeV.getElement().getIndex()) ;
				vertexObj.put("w", 1) ;
				
				edgesListObj.put(String.valueOf(edgeIndex), vertexObj) ;
				edgeIndex ++ ;
				
			}
		}
		
		obj.put("vl", verticesListObj) ;
		obj.put("el", edgesListObj) ;
		
		return obj ;
	}
	
	
	/**
	 * Questo metodo scrive sul file un oggetto di tipo JSON passato come parametro.
	 * @param obj JSONObject da scrivere su file
	 * @throws IOException
	 */
	public void writeFile(JSONObject obj) throws IOException {
		
		FileWriter file = new FileWriter(this.fileName) ;
		
		try {
			file.write(obj.toJSONString()) ;
			System.out.println("File salvato con successo.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			file.flush() ;
			file.close() ;
		}
		
	}
	
	
	/**
	 * Metodo principale da richiamare per scrivere su file .json
	 * @param FilePath percorso del file .json 
	 * @param G grafo da scrivere
	 */
	public void writeFileWithJSONFileWriter(String FilePath, Graph<CoordinateNode> G) {
        
		JSONFileWriter writer = new JSONFileWriter() ;
		
		try {
			
			writer = new JSONFileWriter(FilePath, G);
		
		} catch (WrongFileExtension e) {
			
			System.out.println("Impossibile aprire lo stream con il file " + writer.fileName ) ;
		
		} catch (EmptyFileName e) {
			
			System.out.println("Impossibile aprire lo stream con il file " + writer.fileName ) ;
		
		}
	}
}