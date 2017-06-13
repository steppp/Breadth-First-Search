package model.JSONFiles.exceptions;

/**
 * Eccezione che viene sollevata nel caso in cui si tenti di utilizzare
 * un file con estensione diversa da .json .
 * @author melania
 *
 */
public class WrongFileExtension extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongFileExtension() {
	}
	
}