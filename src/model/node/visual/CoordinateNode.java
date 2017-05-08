package model.node.visual;

/**
 * Classe che descrive un oggetto con un indice e le coordinate x ed y. Verrà usato come tipo inserito all'interno del grafo.
 * @author stefanoandriolo
 */
public class CoordinateNode implements Comparable<CoordinateNode> {
	
	public CoordinateNode(int index, double xPos, double yPos) {
		this.index = index;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public CoordinateNode(double xPos, double yPos) {
		this(0, xPos, yPos);
	}
	
	public CoordinateNode() {
		this(0.0, 0.0);
	}
	
	// indice del nodo corrente
	int index;
	
	// coordinate x ed y del nodo
	double xPos;
	double yPos;

	@Override
	public int compareTo(CoordinateNode other) {
		
		// ritorno -1 se l'indice del nodo da confrontare è maggiore di quello del nodo corrente
		if (other.index > this.index)
			return -1;
		
		// ritorno 0 se i due indici sono uguali
		if (other.index == this.index)
			return 0;
		
		// ritorno 1, l'indice del nodo sa confrontare è minore di quello del nodo corrente
		return 1;
	}

	/**
	 * @return indice
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return posizione x
	 */
	public double getxPos() {
		return xPos;
	}

	/**
	 * @return posizione y
	 */
	public double getyPos() {
		return yPos;
	}

	
	@Override
	public String toString() {
		
		return index + "";
	}
	
	
}
