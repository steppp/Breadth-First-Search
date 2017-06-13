package model.randomGraph ;

import java.util.Random ;
import model.node.visual.*;
import model.graphs.Graph;
import model.graphs.Node;

/**
 * Classe che permette di generare un grafo casuale.
 * @author melania
 *
 * @param <T>
 */
public class RandomGraph<T extends Comparable<T>> extends Graph<T>{
	
	Graph<CoordinateNode> G ;
	Integer nodesNumber ;
	
	double posX[] = {440.0, 293.0, 220.0, 220.0, 293.0, 440.0, 586.0, 659.0, 659.0, 586.0} ;
	double posY[] = {69.0, 106.0, 206.0, 274.0, 373.0, 411.0, 373.0, 274.0, 206.0, 106.0} ;
	

	/*
	 * Costruttore che genera un grafo casuale
	 */
	public RandomGraph() {
		this.G = new Graph<CoordinateNode>() ;	
		this.nodesNumber = 0 ;
		this.randomNodes();
		this.randomEdges();
	}
	
	/*
	 * Costruttore che genera un grafo casuale di n nodi(A SCELTA DELL'UTENTE)
	 */
	public RandomGraph(int n) {
		this.G = new Graph<CoordinateNode>() ;
		this.nodesNumber = 0 ;
		this.nNodes(n) ;
		this.randomEdges();
		
	}
	
	/**
	 * Ritorna il grafo creato.
	 * @return grafo creato.
	 */
	public Graph<CoordinateNode> getGraph() {
		return G;
	}
	
		
    /*
	 * Crea un numero casuale di nodi (MAX 10) etichettati con numeri interi da 0 a 10	
	 * Assumiamo che venga applicato ad un grafo di Interi
	 */
	public void randomNodes() {
		
		Random random = new Random() ;
		
		Integer i ;
		int n = 10 ;
		int m = random.nextInt(n) ;
		
		while (m <= 1) {
			m = random.nextInt(n) ;
		}
		
		double posX = this.posX[0] ;
		double posY = this.posY[0] ;
		
		if(m == 4) {
			
			posX = this.posX[2] ;
			posY = this.posY[2] ;
		}
		
		else if(m == 8) {
			
			posX = this.posX[1] ;
			posY = this.posY[1] ;
		}
		
		for (i=0;i<m;i++) {
			
			CoordinateNode coN = new CoordinateNode(i, posX, posY ) ;
			
			Node<CoordinateNode> x = new Node<CoordinateNode>(coN) ;
		
			this.G.insertNode(x);
			this.nodesNumber ++ ;
			
			if (i != m-1) {		//aggiorno la posizione del prossimo nodo
				posX = this.posX[getDelta(i+1,m) + i + 1] ;
				posY = this.posY[getDelta(i+1,m) + i + 1] ;
			}
			
		}
		
	}
	
	/*
	 * Crea n nodi 
	 */
	public boolean nNodes(int n){
		
		Integer i ;
		
		double posX = 0.0 ;
		double posY = 0.0 ;

        if (n>1) {
			for (i=0;i<n;i++) {
				
				Node<CoordinateNode> x =(Node<CoordinateNode>) new Node<CoordinateNode>(new CoordinateNode(i, posX, posY)) ;
			
				this.G.insertNode(x);
				this.nodesNumber ++ ;
				
			}
			return true ;
		}
		return false ;
	}
		
	/*
	 * Crea un numero casuale di archi(MAX 2n)
	 * Assumiamo che venga applicato ad un grafo di Interi
	 */
	@SuppressWarnings("unchecked")
	public void randomEdges(){
		
		Random random1 = new Random() ;
		
		boolean tmp ;
		
		if (this.nodesNumber > 1) {
			
			
			int n = this.nodesNumber +  random1.nextInt(this.nodesNumber) ;
			
			Object[] nodesEl =  this.G.V().toArray() ; ;
			
			Integer i ;
			
		    for (i=0;i<n;i++){
		            
		    	Node<CoordinateNode> u = (Node<CoordinateNode>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
		    	Node<CoordinateNode> v = (Node<CoordinateNode>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
		    	

		    	tmp = this.G.insertEdge(u, v) ;
		    	
		    	if (!tmp ) {		//se non è stato possibile creare l'arco faccio un nuovo tentativo
		    		
		    		u = (Node<CoordinateNode>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
			    	v = (Node<CoordinateNode>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
			    	
			    	
			    	this.G.insertEdge(u, v) ;
			    	
		    	}

		    	this.G.insertEdge(u, v) ;
		    }
		}
	}
	

	@Override
	public void print() {
		this.G.print();
	}


	/*
	 * Questo metodo resituisce lo spiazzamento della posizione che
	 * un nodo deve occupare. 
	 * Prende in input l'indice del nodo la cui posizione stiamo aggiornando, e
	 * il numero di nodi totali.
	 */
	public int getDelta(int index, int maximum) {
		
		if (maximum == 2 ) {
			return 4 ;
		}
		
		else if (maximum == 3 || maximum == 4) {
			if (index < 2) {
				return 2 ;
			} else {
				return 5 ;
			}
		}
		
		else if (maximum == 5) {
			return maximum ;
		}
		
		else if (maximum == 6) {
			if (index < 5) {
				return 2 ;
			} else {
				return 4 ;
			}
		}
		
		else if (maximum == 7 || maximum == 8) {
			if (index < 4) {
				return 1 ;
			} else {
				return 2 ;
			}
		}
		
		else if (maximum == 9) {
			if (index < 5) {
				return 0 ;
			} else {
				return 1 ;
			}
		}
		
		return 0 ; //se in totale i nodi sono 10
		
	}
	
}

