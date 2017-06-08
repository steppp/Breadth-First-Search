package model.randomGraph ;

import java.util.Random ;
import model.graphs.Graph;
import model.graphs.Node;


public class randomGraph<T extends Comparable<T>> extends Graph<T>{
	
	Graph<T> G ;
	Integer nodesNumber ;
	

	public randomGraph() {
		this.G = new Graph<T>() ;	
		this.nodesNumber = 0 ;
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
		
		while (m == 0) {
			m = random.nextInt(n) ;
		}
		
		
		for (i=0;i<m;i++) {
			
			Node<T> x = new Node((T)i) ;
		
			this.G.insertNode(x);
			this.nodesNumber ++ ;
			
		}
		
	}
		
	/*
	 * Crea un numero casuale di archi(MAX 2n)
	 * Assumiamo che venga applicato ad un grafo di Interi
	 */
	public void randomEdges(){
		
		Random random1 = new Random() ;
		
		if (this.nodesNumber > 1) {
			
			
			int n = this.nodesNumber +  random1.nextInt(this.nodesNumber) ;
			
			Object[] nodesEl =  this.G.V().toArray() ; ;
			
			Integer i ;
			
		    for (i=0;i<n;i++){
		            
		    	Node<T> u = (Node<T>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
		    	Node<T> v = (Node<T>)nodesEl[random1.nextInt(this.nodesNumber - 1)] ;
		    	
		    	this.G.insertEdge(u, v) ;
		    	
		    }
		}
	}
	
	@Override
	public void print() {
		this.G.print();
	}

	
}
