package edu.upf.taln.welcome.slas.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.factories.OutputFactory;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.utils.AutoDeletingTempFile;
import edu.upf.taln.xr4drama.client.Xr4dramaBackendClient;
import edu.upf.taln.xr4drama.commons.analysis.input.AnalysisInput;
import edu.upf.taln.xr4drama.commons.analysis.input.AnalysisInputData;
import edu.upf.taln.xr4drama.commons.analysis.input.AnalysisInputMetadata;
import edu.upf.taln.xr4drama.commons.analysis.input.AnalysisOptions;
import edu.upf.taln.xr4drama.commons.analysis.input.AnalysisOptionsFactory;
import edu.upf.taln.xr4drama.commons.analysis.input.AnalysisType;
import edu.upf.taln.xr4drama.commons.analysis.input.ModuleOptions;
import edu.upf.taln.xr4drama.commons.analysis.input.OutputType;
import edu.upf.taln.xr4drama.commons.analysis.input.UseCase;
import edu.upf.taln.xr4drama.commons.analysis.output.xmi.XmiOutput;
import edu.upf.taln.xr4drama.commons.exceptions.Xr4dramaClientException;
import edu.upf.taln.xr4drama.commons.exceptions.Xr4dramaException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


/**
 * Analyze text and return results in JSON format
 * 
 * 
 */
@Path("/de")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class DeepAnalysisService {
	
	private static final String SAMPLE_INPUT_XMI = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"xmi\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"de\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Ja, ich möchte mich für den Erstaufnahmeservice bewerben.\"" + 
			"  } \n" + 
			"}";
	
	private static final String SAMPLE_INPUT_TURN0 = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"de\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Hallo, kannst du mich hören?\"" + 
			"  } \n" + 
			"}";


	private static final String SAMPLE_INPUT_TURN1 = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"de\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Ja, ich möchte mich für den Erstaufnahmeservice bewerben.\"" + 
			"  } \n" + 
			"}";
	
	private static final String SAMPLE_INPUT_TURN2 = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"de\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Terrassa ist eine Stadt in der östlichen Zentralregion Kataloniens in der Provinz Barcelona, Comarca Vallès Occidental, deren Kohauptstadt sie zusammen mit Sabadell ist.\"" + 
			"  } \n" + 
			"}";
	/**
	 * Logger for this class and subclasses.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	@Context
	ServletConfig config;
    private final Xr4dramaBackendClient<XmiOutput> client;

	public DeepAnalysisService() throws WelcomeException {
		log.info("getting Analyzer instance");
		client = new Xr4dramaBackendClient<>("https://xr4drama.upf.edu/xr4drama-de-service/api/de", "de", XmiOutput.class);
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
			@Parameter(description = "Container for analysis input data.", required = true) DeepAnalysisInput input) throws WelcomeException, UIMAException {
        
		InputMetadata metadata = input.getMetadata();
		
		AnalysisInputMetadata xr4dramaMeta = null;
		try {
			AnalysisOptions options = AnalysisOptionsFactory.createOptions(AnalysisType.FULL);
			options.setDeepParserOptions(new ModuleOptions(false));
			xr4dramaMeta = new AnalysisInputMetadata(metadata.getLanguage(), UseCase.INCIDENT, AnalysisType.FULL, options, null); 
			xr4dramaMeta.setOutputType(OutputType.xmi);
		} catch (Xr4dramaException e) {
			throw new WelcomeException(e);
		}
		
		AnalysisInputData xr4dramaData = new AnalysisInputData();
		xr4dramaData.setText(input.getData().getText());
		
		AnalysisInput xr4dramaContainer = new AnalysisInput(xr4dramaMeta, xr4dramaData);
		
		XmiOutput output;
		try {
			//output = client.analyze(AnalysisType.PUC1, OutputType.xmi, input.getData().getText());
			log.info("calling xr4drama-services-de backend");
			output = client.analyze(xr4dramaContainer);
		} catch (Xr4dramaClientException e) {
			log.warn("error getting analysis from backend", e);
			throw new WelcomeException(e);
		}
		
		IAnalysisOutput analysisResult;
		// create a temporary file
		try (AutoDeletingTempFile wrapper = new AutoDeletingTempFile()) {
			Files.write(wrapper.getFile(), output.getData().getXmi().getBytes(StandardCharsets.UTF_8));
			
			TypeSystemDescription typesystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
			JCas jCas = JCasFactory.createJCas(wrapper.getFile().toAbsolutePath().toString(), typesystem);
	        
			analysisResult = OutputFactory.extractOutput(jCas, metadata.getOutputType());
	    } catch (IOException e) {
	    	throw new WelcomeException(e);
	    }
		
		return analysisResult;
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