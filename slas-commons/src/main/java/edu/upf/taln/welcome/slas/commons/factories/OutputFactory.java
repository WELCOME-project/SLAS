package edu.upf.taln.welcome.slas.commons.factories;

import edu.upf.taln.welcome.slas.commons.input.OutputType;
import org.apache.uima.jcas.JCas;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.output.AnalysisOutputImpl;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.OutputGenerator;

public class OutputFactory {
	
	public static IAnalysisOutput extractOutput(JCas jCas, OutputType type) throws WelcomeException {
    	try {
    		
    		IAnalysisOutput output;
    		
    		switch(type) {
	    		case xmi:
	    			output = OutputGenerator.generateXmiOutput(jCas);
	    			
	    			break;
	    		case demo:
	    			 output = OutputGenerator.generateDemoOutput(jCas);
	    			
	    			break;
	    		case welcome:
	    			output = OutputGenerator.generateDlaOutput(jCas);
	    			
	    			break;
	    		case demo_welcome:
	    			output = OutputGenerator.generateDemoOutputWithDla(jCas);
	    			
	    			break;
	    			
	    		case dummy:
	    			output = OutputGenerator.generateDummyResponse(jCas);
	    			
	    			break;
				default:
					output = new AnalysisOutputImpl<>();
					
					break;
    		}

    		return output;

    	} catch (Exception e) {
    		throw new WelcomeException("Error generating output", e);
    	}
    }
}
