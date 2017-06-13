package utility;

import model.graphs.Node;
import model.node.visual.CoordinateNode;

public class AnimationSettings {

	public static final String THREAD_NAME = "BFS_THREAD";
	Long millisInterval = null;
	Node<CoordinateNode> root = null;
	
	
	/**
	 * @return the speed
	 */
	public Long getInterval() {
		return millisInterval;
	}
	
	/**
	 * @param millisInterval the speed to set
	 */
	public void setInterval(long millisInterval) {
		this.millisInterval = millisInterval;
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
