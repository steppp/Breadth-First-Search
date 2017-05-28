package utility;

import model.graphs.Node;
import model.node.visual.CoordinateNode;

public class AnimationSettings {

	public static final String THREAD_NAME = "BFS_THREAD";
	Double speed = 1.0;
	Node<CoordinateNode> root = null;
	
	
	/**
	 * @return the speed
	 */
	public Double getSpeed() {
		return speed;
	}
	
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	
	/**
	 * @return the root
	 */
	public Node<CoordinateNode> getRoot() {
		return root;
	}
	
	/**
	 * @param root the root to set
	 */
	public void setRoot(Node<CoordinateNode> root) {
		this.root = root;
	}
}
