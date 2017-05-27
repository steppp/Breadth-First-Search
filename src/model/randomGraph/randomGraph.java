package model.randomGraph ;

import java.util.Random ;
import java.util.Iterator;
import model.graphs.Graph;
import model.graphs.Node;

//TODO:parametrizza
//TODO:verificare perchè AlreadyExist restituisce sempre false 
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
		
		while (m == 0) {
			m = random.nextInt(n) ;
		}
		
		for (i=0;i<m;i++) {
			
			Node<Integer> x = new Node<Integer>(i) ;
		
			this.G.insertNode(x);
			this.nodesNumber ++ ;
		}
		
	}
		
	public void randomEdges(){
		
		Random random1 = new Random() ;
		
		if (this.nodesNumber > 1) {
			
			
			int n = this.nodesNumber +  random1.nextInt(this.nodesNumber) ;
			
			while (n == 0) {
				n = random1.nextInt(this.nodesNumber) ;
			}
			
			Integer[] nodesEl = getNodesArray() ;
			
			Integer i ;
			
			
		    for (i=0;i<n;i++){
		            
		            Node<Integer> u = (Node<Integer>) new Node(nodesEl[random1.nextInt(this.nodesNumber - 1)]) ;
					Node<Integer> v = (Node<Integer>) new Node(nodesEl[random1.nextInt(this.nodesNumber - 1)]) ;
					
					if (u.getElement() == v.getElement()) {
						
						while(u.getElement() == v.getElement()) {
							
							u = (Node<Integer>) new Node(nodesEl[random1.nextInt(this.nodesNumber - 1)]) ;
							v = (Node<Integer>) new Node(nodesEl[random1.nextInt(this.nodesNumber - 1)]) ;
							

					     }
					}
					
					if (alreadyExist(u,v)) {
						
						Integer tmp = u.getElement() ;
						u = new Node(v.getElement()) ;
						v = new Node(tmp) ;
						
					}
					
					if (!alreadyExist(u,v)) {
						
						this.G.insertEdge(u, v) ;
						
			        }
		    }
		}
	}

	
	public Boolean alreadyExist(Node<Integer> x, Node<Integer> y) {
		
		Iterator<Node<Integer>> I = this.G.adj(x).iterator() ;
		
		Boolean tmp = false ;
		
		while (I.hasNext()) {
			
			if (I.next() == y) {
				tmp = true ;
			}
			
		}
		
		return tmp ;
	}
	
	public Integer[] getNodesArray() {
      
		Integer[] A = new Integer[this.nodesNumber] ;
		
      Iterator<Node<Integer>> I = this.G.V().iterator() ;
		
	    Integer i = 0 ;
	    
	    while (I.hasNext()) {
	    	
	    	A[i] = I.next().getElement() ;
	    	i++ ;
	    	
	    }
	    
	    return A ;
	}

}

