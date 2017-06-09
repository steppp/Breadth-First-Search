package model.randomGraph ;

import java.util.Random ;
import model.graphs.Graph;
import model.graphs.Node;

<<<<<<< HEAD
=======

>>>>>>> 278a73a5c5662fc229629d96190dc060056d3116
public class randomGraph<T extends Comparable<T>> extends Graph<T>{
	
	Graph<T> G ;
	Integer nodesNumber ;
	

	/*
	 * Costruttore che genera un grafo casuale
	 */
	public randomGraph() {
		this.G = new Graph<T>() ;	
		this.nodesNumber = 0 ;
		this.randomNodes();
		this.randomEdges();
	}
	
	/*
	 * Costruttore che genera un grafo casuale di n nodi(A SCELTA DELL'UTENTE)
	 */
	public randomGraph(int n) {
		this.G = new Graph<T>() ;
		this.nodesNumber = 0 ;
		this.nNodes(n) ;
		this.randomEdges();
		
<<<<<<< HEAD
	}
		
=======
>>>>>>> 278a73a5c5662fc229629d96190dc060056d3116
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
		
		
		for (i=0;i<m;i++) {
			
			Node<T> x = new Node((T)i) ;
		
			this.G.insertNode(x);
			this.nodesNumber ++ ;
			
		}
		
	}
	
	/*
	 * Crea n nodi 
	 */
	public boolean nNodes(int n){
		
		Integer i ;
		
<<<<<<< HEAD
		if (n>1) {
			for (i=0;i<n;i++) {
				
				Node<T> x = new Node((T)i) ;
			
				this.G.insertNode(x);
				this.nodesNumber ++ ;
				
			}
			return true ;
		}
		return false ;
	}
		
=======
>>>>>>> 278a73a5c5662fc229629d96190dc060056d3116
	/*
	 * Crea un numero casuale di archi(MAX 2n)
	 * Assumiamo che venga applicato ad un grafo di Interi
	 */
	public void randomEdges(){
		
		Random random1 = new Random() ;
		
		boolean tmp ;
		
		if (this.nodesNumber > 1) {
			
			
			int n = this.nodesNumber +  random1.nextInt(this.nodesNumber) ;
			
<<<<<<< HEAD
			Object[] nodesEl =  this.G.V().toArray() ;
			
			Integer i ;
			
			for (i=0;i<n;i++){
=======
			Object[] nodesEl =  this.G.V().toArray() ; ;
			
			Integer i ;
			
		    for (i=0;i<n;i++){
>>>>>>> 278a73a5c5662fc229629d96190dc060056d3116
		            
		    	Node<T> u = (Node<T>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
		    	Node<T> v = (Node<T>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
		    	
<<<<<<< HEAD
		    	tmp = this.G.insertEdge(u, v) ;
		    	
		    	if (!tmp ) {		//se non è stato possibile creare l'arco faccio un nuovo tentativo
		    		
		    		u = (Node<T>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
			    	v = (Node<T>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
			    	
			    	
			    	this.G.insertEdge(u, v) ;
			    	
		    	}
=======
		    	this.G.insertEdge(u, v) ;
		    	
>>>>>>> 278a73a5c5662fc229629d96190dc060056d3116
		    }
		}
	}
	
	@Override
	public void print() {
		this.G.print();
	}

	
}
<<<<<<< HEAD



=======
>>>>>>> 278a73a5c5662fc229629d96190dc060056d3116
