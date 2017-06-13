package model.JSONFiles ;

import java.io.FileWriter ;
import java.io.IOException ;
import model.node.visual.*;
import model.graphs.*;
import org.json.simple.* ;

/*
 * Questa classe permette di generare un file con estensione .json 
 * dove memorizzare l'attuale grafo creato dall'utente.
 */
public class JSONFileWriter {
	
	String fileName ;
	
	public JSONFileWriter() {
		this.fileName = "" ;
	}
	
	/*
	 * Questo metodo converte il Grafo passato come parametro in un 
	 * JSONObject.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject (Graph<CoordinateNode> G) {
		
		JSONObject obj = new JSONObject() ;
		
		Integer edgeIndex = 0 ;
		
		JSONObject verticesListObj = new JSONObject() ;
		JSONObject edgesListObj = new JSONObject() ;
		
		
		for (Node<CoordinateNode> node : G.V()) {
		
			System.out.println("nodo: " + node.getElement().getIndex());
			
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
	
}