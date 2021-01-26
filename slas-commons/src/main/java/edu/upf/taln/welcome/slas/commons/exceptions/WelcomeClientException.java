package edu.upf.taln.welcome.slas.commons.exceptions;

/**
 * This class represents an application domain exception.
 *
 */
public class WelcomeClientException extends Exception {
	
	private int status;
	private String message;
	
    public WelcomeClientException() {
    }

    public WelcomeClientException(String message) {
        super(message);
    }

    public WelcomeClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public WelcomeClientException(Throwable cause) {
        super(cause);
    }
    
    public WelcomeClientException(int status) {
    	this.status = status;
    	String message;
    	switch (status) {
    		case 404:
    			message = "Status: " + this.status + ". URL not found. Server URL may be incorrect or server is down.";
    			break;
    		case 502:
    			message = "Status: " + this.status + ". Proxy error, probably a timeout error due to a long text. Try to divide you text in several files.";
    			break;
    		case 408:
    			message = "Status: " + this.status + ". Timeout error, probably due to a long text. Try to divide you text in several files.";
    			break;
    		default:
    		case 500:
    			message = "Status: " + this.status + ". Server error.";
    			break;
    	}
    	this.message = message;
    }
    
    public WelcomeClientException(String message, int status) {
        this.status = status;
        String newMessage = "Status: " + this.status + ". Message: " + message;
    	this.message = newMessage;
    }
    
    public WelcomeClientException(String message, Throwable cause, int status) {
    	super(message, cause);
        this.status = status;
    }

    public WelcomeClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    @Override
    public String getMessage() {
    	if (this.message == null) {
    		return super.getMessage();
    	} else {
    		return this.message;
    	}
    }

}
