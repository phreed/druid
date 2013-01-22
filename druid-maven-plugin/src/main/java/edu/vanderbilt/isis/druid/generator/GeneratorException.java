package edu.vanderbilt.isis.druid.generator;


public class GeneratorException extends Exception {

	public GeneratorException(Exception ex) {
		super(ex);
	}
	public GeneratorException(String msg) {
        super(msg);
    }
	public GeneratorException(String msg, Exception ex) {
        super(msg, ex);
    }
	/**
	 * 
	 */
	private static final long serialVersionUID = 3365003357977422251L;

}
