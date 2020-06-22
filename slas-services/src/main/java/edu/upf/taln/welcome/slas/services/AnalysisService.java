package edu.upf.taln.welcome.slas.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import edu.upf.taln.welcome.slas.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.io.AnalysisConfigurations;
import edu.upf.taln.welcome.slas.io.AnalysisOutput;
import edu.upf.taln.welcome.slas.io.LanguageConfiguration;
import edu.upf.taln.welcome.slas.io.WelcomeContainer;


/**
 * Analyze text and return results in JSON format
 * 
 * @author jens.grivolla
 * 
 */
@Path("/analyze")
@Produces(MediaType.APPLICATION_JSON)
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
	@Path("/{text}")
	public AnalysisOutput analyzeGET(@PathParam("text") String text) throws WelcomeException{

		AnalysisOutput analysisOutput = new AnalysisOutput();
		analysisOutput.setNumber(2.2);
		
		List<String> list = new ArrayList<>();
		list.add("One");
		list.add("Two");
		list.add("three");
		analysisOutput.setList(list);
		
		if (text == null) {
			analysisOutput.setText("Hello World!");
		} else {
			analysisOutput.setText("Your text: " + text);
		}
		
		return analysisOutput;
	}
	
	@GET
	@Path("/description")
	@Operation(summary = "Retrieves the available configurations.",
		description = "Returns the list of available configurations, it is, the list of languages and available analysis module for each one.",
		responses = {
		        @ApiResponse(description = "The available configurations",
		        			content = @Content(schema = @Schema(implementation = AnalysisConfigurations.class)
		        ))
	})
	public AnalysisConfigurations getAvailableConfigurations() throws WelcomeException{

		List<String> modules = new ArrayList<>();
		modules.add("surface_parsing");
		modules.add("deep_parsing");
		modules.add("dbpedia");

		LanguageConfiguration es_config = new LanguageConfiguration();
		es_config.setLanguage("es");
		es_config.setModules(modules);

		List<LanguageConfiguration> configList = new ArrayList<>();
		configList.add(es_config);

		AnalysisConfigurations configurations = new AnalysisConfigurations();
		configurations.setConfigurations(configList);
		
		return configurations;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public AnalysisOutput analyzePOST(
			@Parameter(description = "Container for analysis input data.", required = true) WelcomeContainer container) throws WelcomeException{

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