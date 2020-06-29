package edu.upf.taln.welcome.slas.commons.exceptions;

/**
 * This class represents an application domain exception.
 *
 */
public class WelcomeException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public WelcomeException() {
		super();
	}
	
	public WelcomeException(Throwable throwable) {
        super(throwable);
    }
	
	public WelcomeException(String message) {
        super(message);
	}
	
	public WelcomeException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
