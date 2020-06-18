package edu.upf.taln.welcome.slas.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.upf.taln.welcome.slas.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.io.AnalysisOutput;
import edu.upf.taln.welcome.slas.io.WelcomeContainer;


/**
 * Analyze text and return results in JSON format
 * 
 * @author jens.grivolla
 * 
 */
@WebListener
@Path("/analyze")
public class AnalysisService {

	/**
	 * Logger for this class and subclasses.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	@Context
	ServletConfig config;

	public AnalysisService() throws WelcomeException {
	}

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public AnalysisOutput analyzeGET() throws WelcomeException{

		AnalysisOutput analysisOutput = new AnalysisOutput();
		analysisOutput.setText("Hello World!");
		analysisOutput.setNumber(2.2);
		
		List<String> list = new ArrayList<>();
		list.add("One");
		list.add("Two");
		list.add("three");
		analysisOutput.setList(list);
		
		return analysisOutput;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public AnalysisOutput analyzePOST(WelcomeContainer container) throws WelcomeException{

		AnalysisOutput analysisOutput = new AnalysisOutput();
		analysisOutput.setText("Hello World!");
		analysisOutput.setNumber(2.2);
		
		List<String> list = new ArrayList<>();
		list.add("One");
		list.add("Two");
		list.add("three");
		analysisOutput.setList(list);
		
		return analysisOutput;
	}

}