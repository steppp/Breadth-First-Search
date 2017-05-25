package model.randomGraph ;

import java.util.Random ;
import java.util.Iterator;
import model.graphs.Graph;
import model.graphs.Node;

public class randomGraph<T extends Comparable<T>> extends Graph<T>{
	
	Graph<Integer> G ;
	Integer nodesNumber ;
	

	public randomGraph() {
		this.G = new Graph<Integer>() ;	
		this.nodesNumber = 0 ;
	}
		
	//TODO: escludere il caso in cui ci sono 0 nodi
	//Crea un numero casuale di nodi (MAX 10)	
	public void randomNodes() {
		
		Random random = new Random() ;
		
		Integer i ;
		int n = 10 ;
		int m = random.nextInt(n) ;
		
		System.out.println("Il numero di nodi è " + m );
	 	
		for (i=0;i<m;i++) {
			
			Node<Integer> x = new Node<Integer>(i) ;
		
			this.G.insertNode(x);
			this.nodesNumber ++ ;
		}
		
		this.G.print() ;
	}
		
	public void randomEdges(){
		
		Integer[] A = new Integer[this.nodesNumber] ;
		
        Iterator<Node<Integer>> I = this.G.V().iterator() ;
		
	    Integer i = 0 ;
	    
	    
	    while (I.hasNext()) {
	    	
	    	A[i] = I.next().getElement() ;
	    	i++ ;
	    	
	    }
	    
	   
		Node<Integer> u ;
		Node<Integer> v ;
		
		Random random1 = new Random() ;
		
		if (this.nodesNumber > 1) {
			
			int m = this.nodesNumber ;		//MAX_N edges
			
			int n = random1.nextInt(m) ;
			
			System.out.println("Il numero di archi è " + n );
			
			
	        for (i=0;i<n;i++){
				
				u = (Node<Integer>) new Node(A[random1.nextInt(this.nodesNumber - 1)]) ;
				v = (Node<Integer>) new Node(A[random1.nextInt(this.nodesNumber - 1)]) ;
				
				//TODO: verificare che l'arco non sia stato già creato
				if (u.getElement() != v.getElement()) {
					this.G.insertEdge(u, v) ;
					System.out.println("è stato appena creato un arco da " + u + " a " + v );
				}
				
				
				
			}
		}
	}

	
	//TODO: perqualecazzodimotivo non stampa il grafo
	public static void main (String[] args) {
		
		randomGraph<Integer> g1 = new randomGraph<Integer>() ;
		
		g1.randomNodes();
		g1.randomEdges();
		
		g1.print() ;
		
	}
}

