package singleton;

public class Singleton {

	private static Singleton sharedInstance = null;
	
	protected Singleton() { }
	
	public static Singleton getInstance() {
		if (sharedInstance == null)
			sharedInstance = new Singleton();
		
		return sharedInstance;
	}
	
	// --------- OTHER DATA ------------
	
	boolean graphLoaded;
	boolean isAnimating;
}
