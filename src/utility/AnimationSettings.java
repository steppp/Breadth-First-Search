package utility;

import model.graphs.Node;
import model.node.visual.CoordinateNode;

public class AnimationSettings {

	public static final String THREAD_NAME = "BFS_THREAD";
	long millisInterval = 500L;
	Node<CoordinateNode> root = null;
	
	
	/**
	 * @return the speed
	 */
	public long getInterval() {
		return millisInterval;
	}
	
	/**
	 * @param speed the speed to set
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
