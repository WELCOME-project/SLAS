package edu.upf.taln.welcome.slas.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.LanguageConfiguration;
import edu.upf.taln.welcome.slas.commons.output.ServiceDescription;
import edu.upf.taln.welcome.slas.core.Analyzer;


/**
 * Analyze text and return results in JSON format
 * 
 * 
 */
@Path("/dla")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class DeepAnalysisService {
	
	private static final String SAMPLE_INPUT_XMI = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"xmi\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"en\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Yes, I would like to apply for the First Reception Service.\"" + 
			"  } \n" + 
			"}";
	
	private static final String SAMPLE_INPUT_TURN0 = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"en\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Hello, can you hear me?\"" + 
			"  } \n" + 
			"}";


	private static final String SAMPLE_INPUT_TURN1 = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"en\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Yes, I would like to apply for the First Reception Service.\"" + 
			"  } \n" + 
			"}";
	
	private static final String SAMPLE_INPUT_TURN2 = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"en\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Terrassa, is a city in the east central region of Catalonia, in the province of Barcelona, comarca of Vallès Occidental, of which it is the cocapital along with Sabadell.\"" + 
			"  } \n" + 
			"}";
	/**
	 * Logger for this class and subclasses.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	@Context
	ServletConfig config;
    private final Analyzer analyzer;

	public DeepAnalysisService() throws WelcomeException {
		log.info("getting Analyzer instance");
		analyzer = Analyzer.getInstance();
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
		es_config.setLanguage("en");
		es_config.setModules(modules);

		List<LanguageConfiguration> configList = new ArrayList<>();
		configList.add(es_config);

		ServiceDescription configurations = new ServiceDescription();
		configurations.setConfigurations(configList);
		
		return configurations;
	}
	
	@POST
	@Path("/analyzePlain")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Performs a deep syntactic analysis of the input data.",
		description = "Returns the result of the deep syntatic analysis, it is, a predicate-argument structure.",
		requestBody = @RequestBody(
						content = @Content(mediaType = "application/json",
										schema = @Schema(implementation = DeepAnalysisInput.class),
										examples = {
											@ExampleObject(name = "Turn 0",
													value = SAMPLE_INPUT_TURN0),
											@ExampleObject(name = "Turn 1",
													value = SAMPLE_INPUT_TURN1),
											@ExampleObject(name = "Turn 2",
													value = SAMPLE_INPUT_TURN2),
											@ExampleObject(name = "Xmi",
													value = SAMPLE_INPUT_XMI)
										}
						)
					),
		responses = {
		        @ApiResponse(description = "The deep analysis result.",
		        			content = @Content(schema = @Schema(implementation = IAnalysisOutput.class)
		        ))
	})
	public IAnalysisOutput analyzePlain(
			@Parameter(description = "Container for analysis input data.", required = true) DeepAnalysisInput input) throws WelcomeException {
		return analyze(input);
	}
	
	
	@POST
	@Path("/analyze")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Performs a deep syntactic analysis of the input data.",
		description = "Returns the result of the deep syntatic analysis, it is, a predicate-argument structure.",
		requestBody = @RequestBody(
						content = @Content(mediaType = "application/json",
										schema = @Schema(implementation = DeepAnalysisInput.class),
										examples = {
											@ExampleObject(name = "Turn 0",
													value = SAMPLE_INPUT_TURN0),
											@ExampleObject(name = "Turn 1",
													value = SAMPLE_INPUT_TURN1),
											@ExampleObject(name = "Turn 2",
													value = SAMPLE_INPUT_TURN2),
											@ExampleObject(name = "Xmi",
													value = SAMPLE_INPUT_XMI)
										}
						)
					),
		responses = {
		        @ApiResponse(description = "The deep analysis result.",
		        			content = @Content(schema = @Schema(implementation = IAnalysisOutput.class)
		        ))
	})
	public IAnalysisOutput analyze(
			@Parameter(description = "Container for analysis input data.", required = true) DeepAnalysisInput input) throws WelcomeException {
        
		IAnalysisOutput output = analyzer.analyze(input);
        
		return output;
	}
	
	@GET
	@Path("/status")
	@Operation(summary = "Retrieve the services status.",
		description = "Returns a status description of the service.",
		responses = {
		        @ApiResponse(description = "The services status.",
		        			content = @Content(schema = @Schema(implementation = StatusOutput.class)
		        ))
	})
	public StatusOutput getStatus() throws WelcomeException {
		ServletContext application = config.getServletContext();
		String build;
		try {
			build = new String(application.getResourceAsStream("META-INF/MANIFEST.MF").readAllBytes());
			//build = DeepAnalysisService.class.getPackage().toString();
		} catch (IOException e) {
			throw new WelcomeException();
		}
		return new StatusOutput(build);
	}
	
	@GET
	@Path("/status/log")
	@Operation(summary = "Retrieve the service log.",
		description = "Returns a specific amount of log messages.",
		responses = {
		        @ApiResponse(description = "The log messages.",
		        			content = @Content(schema = @Schema(implementation = StatusLogOutput.class)
		        ))
	})
	public StatusLogOutput getLog(@QueryParam("limit") int limit) throws WelcomeException {
		return new StatusLogOutput();
	}

}