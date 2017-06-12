package utility;

import javafx.scene.control.TextInputControl;

/**
 * Classe che fa da wrapper ad un componente grafico per l'output di testo
 * @author stefanoandriolo
 *
 */
public class Logger {

	TextInputControl out;
	
	public Logger(TextInputControl out) {
		this.out = out;
	}
	
	/**
	 * Appende la stringa s al componente grafico
	 * @param s stringa da appendere.
	 */
	public void log(String s) {
		out.appendText(s + "\n");
	}
	
	/**
	 * Elimina tutto il testo all'interno del componente grafico
	 */
	public void clear() {
		out.clear();
	}
}
