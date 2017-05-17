package utility;

public class Trigonometry {

	/**
	 * Calcola il coefficiente angolare di una retta passante per due punti.
	 * @param x1 coordinata x del primo punto.
	 * @param x2 coordinata x del secondo punto. 
	 * @param y1 coordinata y del primo punto.
	 * @param y2 coordinata y del secondo punto.
	 * @return valore in radianti dell'angolo compreso tra la retta e l'asse x.
	 */
	public static Double angularCoeffiecientInRadians(Double x1, Double x2, Double y1, Double y2) {
		return Math.atan2(y2 - y1, x2 - x1);
	}
	
	
	/**
	 * Calcola il valore del lato opposto in un triangolo rettangolo rispetto all'angolo alpha partendo dall'ipotenusa.
	 * @param alpha valore dell'angolo in radianti.
	 * @param hyp lunghezza dell'ipotenusa.
	 * @return lunghezza del lato opposto rispetto all'angolo alpha.
	 */
	public static Double getOppositeEdgeLength(Double alpha, Double hyp) {
		return Math.abs(hyp * Math.sin(alpha));
	}
	
	
	/**
	 * Calcola il valore del lato adiacente in un triangolo rettangolo rispetto all'angolo alpha partendo dall'ipotenusa.
	 * @param alpha valore dell'angolo in radianti.
	 * @param hyp lunghezza dell'ipotenusa.
	 * @return lunghezza del lato adiacente rispetto all'angolo alpha.
	 */
	public static Double getAdjacentEdgeLength(Double alpha, Double hyp) {
		return Math.abs(hyp * Math.cos(alpha));
	}
	
	
	/**
	 * Calcola il valore in radianti dell'angolo complementare ad alpha
	 * @param angle valore in radianti dell'angolo di cui calcolare il complementare
	 * @return valore espresso in radianti dell'angolo complementare di alpha.
	 */
	public static Double getComplementaryAngle(Double angle) {
		return Math.PI / 2 - angle;
	}
}
