package edu.upf.taln.welcome.slas.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.uima.cas.CAS;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.CasIOUtils;
import org.apache.uima.util.CasLoadMode;

import edu.upf.taln.mindspaces.client.MindspacesBackendClient;
import edu.upf.taln.mindspaces.exceptions.MindspacesClientException;
import edu.upf.taln.mindspaces.factories.MindspacesContainerFactory;
import edu.upf.taln.mindspaces.pojos.input.MindspacesContainer;
import edu.upf.taln.mindspaces.pojos.input.MindspacesContainer.AnalysisType;
import edu.upf.taln.mindspaces.pojos.input.MindspacesData;
import edu.upf.taln.mindspaces.pojos.input.MindspacesMeta;
import edu.upf.taln.mindspaces.pojos.output.OutputType;
import edu.upf.taln.mindspaces.pojos.output.XmiOutputImpl;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.factories.OutputFactory;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.utils.AutoDeletingTempFile;
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
@Path("/ca")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class DeepAnalysisService {
	
	private static final String SAMPLE_INPUT_XMI = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"xmi\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"ca\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Sí, m'agradaria subscriure'm a First Reception Service.\"" + 
			"  } \n" + 
			"}";
	
	private static final String SAMPLE_INPUT_TURN0 = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"ca\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Hola, em sents?\"" + 
			"  } \n" + 
			"}";


	private static final String SAMPLE_INPUT_TURN1 = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"ca\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Sí, m'agradaria subscriure'm a First Reception Service.\"" + 
			"  } \n" + 
			"}";
	
	private static final String SAMPLE_INPUT_TURN2 = "{\n" + 
			"  \"metadata\": {" +
			"    \"output_level\": \"demo_welcome\",\n" +
			"    \"analysis_type\": \"FULL\",\n" + 
			"    \"language\": \"ca\",\n" + 
			"    \"use_case\": \"catalonia\"\n" +
			"  },\n" + 
			"  \"data\": {\n" + 
			"    \"text\": \"Terrassa, és una ciutat de la regió del centre-est de Catalunya, a la província de Barcelona, comarca del Vallès Occidental, de la qual n'és la cocapital juntament amb Sabadell.\"" + 
			"  } \n" + 
			"}";
	/**
	 * Logger for this class and subclasses.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	@Context
	ServletConfig config;
    private final MindspacesBackendClient<XmiOutputImpl> client;

	public DeepAnalysisService() throws WelcomeException {
		log.info("getting Analyzer instance");
		client = new MindspacesBackendClient<>("https://taln.upf.edu/mindspaces/mindspaces-services-ca/", "ca", XmiOutputImpl.class);
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
			@Parameter(description = "Container for analysis input data.", required = true) DeepAnalysisInput input) throws WelcomeException, IOException, UIMAException {
        
		InputMetadata metadata = input.getMetadata();
		
		MindspacesContainer mindspacesContainer = new MindspacesContainer();
		
		MindspacesMeta mindspacesMeta = MindspacesContainerFactory.getMindspacesMeta(AnalysisType.WELCOME, OutputType.xmi, metadata.getLanguage());
		//mindspacesMeta.setCandidatesOptions(new ModuleOptions(false));
		//mindspacesMeta.setBabelnetOptions(new ModuleOptions(false));
		mindspacesContainer.setMeta(mindspacesMeta);
		
		MindspacesData mindspacesData = new MindspacesData();
		mindspacesData.setText(input.getData().getText());
		mindspacesContainer.setData(mindspacesData);
		
		XmiOutputImpl output;
		try {
			//output = client.analyze(AnalysisType.PUC1, OutputType.xmi, input.getData().getText());
			log.info("calling mindspaces-services-ca backend");
			output = client.analyze(mindspacesContainer);
		} catch (MindspacesClientException e) {
			log.warn("error getting analysis from backend", e);
			throw new WelcomeException(e);
		}
		
		// create a temporary file
		IAnalysisOutput analysisResult;
		// create a temporary file
		try (AutoDeletingTempFile wrapper = new AutoDeletingTempFile()) {
			Files.write(wrapper.getFile(), output.getXmi().getBytes(StandardCharsets.UTF_8));
			
			TypeSystemDescription typesystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
			//JCas jCas = JCasFactory.createJCas(wrapper.getFile().toAbsolutePath().toString(), typesystem);
			CAS cas = CasCreationUtils.createCas(typesystem, null, null);
			try (InputStream is = new FileInputStream(wrapper.getFile().toAbsolutePath().toString())) {
				CasIOUtils.load(is, null, cas, CasLoadMode.LENIENT);
			}
			JCas jCas = cas.getJCas();
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