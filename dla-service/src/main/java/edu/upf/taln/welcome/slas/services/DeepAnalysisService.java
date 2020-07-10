package edu.upf.taln.welcome.slas.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.output.ServiceDescription;
import edu.upf.taln.welcome.slas.commons.output.OutputData;
import edu.upf.taln.welcome.slas.commons.output.DeepAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.Entity;
import edu.upf.taln.welcome.slas.commons.output.LanguageConfiguration;
import edu.upf.taln.welcome.slas.commons.output.Participant;
import edu.upf.taln.welcome.slas.commons.output.Relation;
import edu.upf.taln.welcome.slas.commons.output.SpeechAct;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.utils.SampleResponses;


/**
 * Analyze text and return results in JSON format
 * 
 * 
 */
@Path("/dla")
@Produces(MediaType.APPLICATION_JSON)
public class DeepAnalysisService {

	/**
	 * Logger for this class and subclasses.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	@Context
	ServletConfig config;

	public DeepAnalysisService() throws WelcomeException {
	}
	
	@GET
	@Path("/description")
	@Operation(summary = "Retrieves the available configurations.",
		description = "Returns the list of available configurations, it is, the list of languages and available analysis module for each one.",
		responses = {
		        @ApiResponse(description = "The available configurations",
		        			content = @Content(schema = @Schema(implementation = ServiceDescription.class)
		        ))
	})
	public ServiceDescription getAvailableConfigurations() throws WelcomeException {

		List<String> modules = new ArrayList<>();
		modules.add("surface_parsing");
		modules.add("deep_parsing");
		modules.add("dbpedia");

		LanguageConfiguration es_config = new LanguageConfiguration();
		es_config.setLanguage("es");
		es_config.setModules(modules);

		List<LanguageConfiguration> configList = new ArrayList<>();
		configList.add(es_config);

		ServiceDescription configurations = new ServiceDescription();
		configurations.setConfigurations(configList);
		
		return configurations;
	}
	
	@POST
	@Path("/analyze")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Performs a deep syntactic analysis of the input data.",
		description = "Returns the result of the deep syntatic analysis, it is, a predicate-argument structure.",
		responses = {
		        @ApiResponse(description = "The deep analysis result.",
		        			content = @Content(schema = @Schema(implementation = DeepAnalysisOutput.class)
		        ))
	})
	public DeepAnalysisOutput analyze(
			@Parameter(description = "Container for analysis input data.", required = true) DeepAnalysisInput input) throws WelcomeException {

        String conll = input.getData().getConll();
        
        int turn = 1;
        if (conll.contains("Sebastià")) {
            turn = 7;
        } else if (conll.contains("Karim")) {
            turn = 5;
        } else if (conll.contains("apply")) {
            turn = 3;
        } else if (conll.contains("Hello")) {
            turn = 1;
        } else {
            turn = 0;
        }
        DeepAnalysisOutput output = SampleResponses.generateResponse(turn);
		return output;
	}

}