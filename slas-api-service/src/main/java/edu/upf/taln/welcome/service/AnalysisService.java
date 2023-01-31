package edu.upf.taln.welcome.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upf.taln.welcome.slas.client.WelcomeBackendClient;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeClientException;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.utils.LanguageConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * Analyze text and return results in JSON format
 * 
 * 
 */
@Path("/slas")
@Tag(name = "Analysis")
@Produces(MediaType.APPLICATION_JSON)
public class AnalysisService {

	private static final String SAMPLE_INPUT_EN = "{\n" + 
			"   \"meta\":{\n" + 
			"      \"language\":\"eng\",\n" + 
			"      \"analysis_type\":\"FULL\",\n" + 
			"      \"output_type\":\"demo_welcome\",\n" + 
			"      \"use_case\":\"catalonia\"\n" + 
			"   },\n" + 
			"   \"data\":{\n" + 
			"      \"text\":\"Yes, I would like to apply for the First Reception Service.\"\n" + 
			"   }\n" + 
			"}";
	
	private static final String SAMPLE_INPUT_CA = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"cat\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Sí, m'agradaria subscriure'm a First Reception Service.\"" + 
			"  } \n" + 
			"}";
	private static final String SAMPLE_INPUT_DE = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"deu\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Ja, ich möchte mich für den Erstaufnahmeservice bewerben.\"" + 
			"  } \n" + 
			"}";
	private static final String SAMPLE_INPUT_EL = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"ell\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Ναι, θα ήθελα να κάνω αίτηση για την Υπηρεσία Πρώτης Υποδοχής.\"" + 
			"  } \n" + 
			"}";
	private static final String SAMPLE_INPUT_ES = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"spa\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Sí, me gustaria suscribirme a First Reception Service.\"" + 
			"  } \n" + 
			"}";


	/**
	 * Logger for this class and subclasses.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	@Context
	ServletConfig config;
	
	private Map<String, WelcomeBackendClient<IAnalysisOutput>> clientsMap;

	public AnalysisService() throws WelcomeException, JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		ServiceConfiguration config;
		String confJson = System.getenv("LANGUAGE_SERVICES_CONFIGURATION_JSON");
		if (confJson != null) {
			config = om.readValue(confJson, ServiceConfiguration.class);
		}else {
			config = new ServiceConfiguration();
		}
		
		clientsMap = new HashMap<>();
		for (String key: config.getLanguageServicesMap().keySet()) {
			String url = config.getLanguageServicesMap().get(key);
			WelcomeBackendClient<IAnalysisOutput> client = new WelcomeBackendClient<IAnalysisOutput>(url);
			clientsMap.put(key, client);
		}
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
											@ExampleObject(name = "English",
													value = SAMPLE_INPUT_EN),
											@ExampleObject(name = "Catalan",
													value = SAMPLE_INPUT_CA),
											@ExampleObject(name = "German",
													value = SAMPLE_INPUT_DE),
											@ExampleObject(name = "Greek",
													value = SAMPLE_INPUT_EL),
											@ExampleObject(name = "Spanish",
													value = SAMPLE_INPUT_ES)
										}
						)
					),
		responses = {
		        @ApiResponse(description = "The deep analysis result.",
		        			content = @Content(schema = @Schema(implementation = IAnalysisOutput.class)
		        ))
	})
	public IAnalysisOutput analyze(@Parameter(description = "Container for analysis input data.", required = true) DeepAnalysisInput input) throws WelcomeException, WelcomeClientException {

		InputMetadata metadata = input.getMetadata();
		String newLang = LanguageConverter.convertLanguage(metadata.getLanguage());
		metadata.setLanguage(newLang);
		
		WelcomeBackendClient<IAnalysisOutput> analysisClient = clientsMap.get(metadata.getLanguage().toLowerCase());
		if (analysisClient == null) {
			throw new WelcomeException("No client for " + metadata.getLanguage() + " language.");
		}
		
		/*String url = "https://welcome-project.upf.edu/slas-" + metadata.getLanguage().toLowerCase() + "service/api/" + metadata.getLanguage().toLowerCase() + "/";
		WelcomeBackendClient<IAnalysisOutput> analysisClient = new WelcomeBackendClient<IAnalysisOutput>(url);*/
		
		IAnalysisOutput output = analysisClient.analyze(input);
		
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