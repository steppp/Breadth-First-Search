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
	

	/**
	 * Costruttore che genera un grafo casuale
	 */
	public RandomGraph() {
		this.G = new Graph<CoordinateNode>() ;	
		this.nodesNumber = 0 ;
		this.randomNodes();
		this.randomEdges();
	}
	
	/**
	 * Costruttore che genera un grafo casuale di n nodi(A SCELTA DELL'UTENTE)
	 * MAX_n = 20 ;
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
	
		
    /**
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
			
		}
		
	}
	
	/**
	 * Crea un numero fisso e stabilito dall'utente di nodi etichettati con numeri interi
	 * da 0 a n.
	 * MAX_n = 20.
	 * Assumiamo che venga applicato ad un grafo di Interi.
	 * @param n numero di nodi
	 * @return valore booleano ad indicare se è stato possibile generare i nodi
	 */
	public boolean nNodes(int n){
		
		Integer i ;
		
		if (n>1) {
			
			double posX = this.posX[0] ;
			double posY = this.posY[0] ;
			
			if(n == 4) {
				
				posX = this.posX[2] ;
				posY = this.posY[2] ;
			}
			
			else if(n == 8) {
				
				posX = this.posX[1] ;
				posY = this.posY[1] ;
			}

			for (i=0;i<n;i++) {
				
				CoordinateNode coN = new CoordinateNode(i, posX, posY) ;
				
				Node<CoordinateNode> x = new Node<CoordinateNode>(coN) ;
			
				this.G.insertNode(x);
				this.nodesNumber ++ ;
				
				if (i != n-1 && i < 9) {		//aggiorno la posizione del prossimo nodo
					posX = this.posX[getDelta(i+1,n) + i + 1] ;
					posY = this.posY[getDelta(i+1,n) + i + 1] ;
				}
				
				else if (i >= 9 && i < n-1) {
					
					int j ;
					
					if (i == 9) {
						posX = this.posX[0] ;
						posY = this.posY[0] + 50.0 ;
					}
					
					else if (i == 10) {	
						j = i%10 ;
						posX = this.posX[getDelta(j,n) + j + 1] + 50.0 ;
						posY = this.posY[getDelta(j,n) + j + 1] + 50.0 ;
					}
					
					else if (i == 13) {
						j = i%10 ;
						posX = this.posX[getDelta(j,n) + j + 1] + 50.0 ;
						posY = this.posY[getDelta(j,n) + j + 1] - 50.0 ;
					}
					
					else if (i == 14) {
						j = i%10 ;
						posX = this.posX[getDelta(j,n) + j + 1] ;
						posY = this.posY[getDelta(j,n) + j + 1] - 50.0 ;
					}
					
					else if (i == 15) {
						j = i%10 ;
						posX = this.posX[getDelta(j,n) + j + 1] - 50.0 ;
						posY = this.posY[getDelta(j,n) + j + 1] - 50.0 ;
					}
					
					
					else if (i == 18) {
						j = i%10 ;
						posX = this.posX[getDelta(j,n) + j + 1] - 50.0 ;
						posY = this.posY[getDelta(j,n) + j + 1] + 50.0 ;
					}
					
					
					else if (i == 11 || i == 12) {
						j = i%10 ;
						posX = this.posX[getDelta(j,n) + j + 1] + 100.0 ;
						posY = this.posY[getDelta(j,n) + j + 1] ;
					}
					
					else if (i == 16 || i == 17) {
						j = i%10 ;
						posX = this.posX[getDelta(j,n) + j + 1] - 100.0 ;
						posY = this.posY[getDelta(j,n) + j + 1];
					}
				}
			}
			return true ;
		}
		return false ;
	}
		
	/**
	 * Crea un numero casuale di archi(MAX 2n)
	 * Assumiamo che venga applicato ad un grafo di Interi.
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


	/**
	 * Questo metodo resituisce lo spiazzamento della posizione che
	 * un nodo deve occupare. 
	 * Prende in input l'indice del nodo la cui posizione stiamo aggiornando, e
	 * il numero di nodi totali.
	 * @param indice del nodo di cui determinare le coordinate 
	 * @param massimo numero totale dei nodi da creare
	 * @return spiazzamento della posizione che
	 * un nodo deve occupare. 
	 */
	public int getDelta(int indice, int massimo) {
		
		if (massimo == 2 ) {
			return 4 ;
		}
		
		else if (massimo == 3 || massimo == 4) {
			if (indice < 2) {
				return 2 ;
			} else {
				return 5 ;
			}
		}
		
		else if (massimo == 5) {
			return massimo ;
		}
		
		else if (massimo == 6) {
			if (indice < 5) {
				return 2 ;
			} else {
				return 4 ;
			}
		}
		
		else if (massimo == 7 || massimo == 8) {
			if (indice < 4) {
				return 1 ;
			} else {
				return 2 ;
			}
		}
		
		else if (massimo == 9) {
			if (indice < 5) {
				return 0 ;
			} else {
				return 1 ;
			}
		}
		
		else if (massimo > 10 && massimo < 20) {
			//
		}
		
		return 0 ; //se in totale i nodi sono 10
		
	}
	
}

