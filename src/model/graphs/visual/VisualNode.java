package model.graphs.visual;

import model.graphs.Node;

public class VisualNode<T extends Comparable<T>> extends Node<T> {
	
	public VisualNode(T el) {
		super(el);
		// TODO Auto-generated constructor stub
	}

	// posizione in cui disegnare il nodo
	int xPos;
	int yPos;
	
	// indice del nodo
	int index;
}
