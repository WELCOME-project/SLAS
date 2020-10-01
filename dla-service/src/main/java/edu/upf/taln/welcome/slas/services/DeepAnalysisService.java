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
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.input.CuniInput;
import edu.upf.taln.welcome.slas.commons.input.CuniInput2DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.output.ServiceDescription;
import edu.upf.taln.welcome.slas.commons.output.DeepAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.LanguageConfiguration;
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

	private static final String SAMPLE_CUNI_INPUT_TURN0 = "{\n" + 
			"  \"model\": \"en\",\n" + 
			"  \"acknowledgements\": [\"http://ufal.mff.cuni.cz/udpipe#udpipe_acknowledgements\", \"welcome-ud-2.5-191206\" ],\n" + 
			"    \"result\": \"# newdoc\\n# newpar\\n# sent_id = 1\\n# text = Hello World\\n1\\tHello\\thello\\tINTJ\\tUH\\t_\\t2\\tdiscourse\\t_\\t_\\n2\\tWorld\\tWorld\\tPROPN\\tNNP\\tNumber=Sing\\t0\\troot\\t_\\tSpaceAfter=No\\n\\n\" \n" + 
			"}";    
    
	private static final String SAMPLE_INPUT_TURN0 = "{\n" + 
			"  \"metadata\": {},\n" + 
			"  \"data\": {\n" + 
			"    \"conll\": \"# sent_id = 1\\n" + 
			"# text = Hello, can you hear me?\\n" + 
			"1\\tHello\\thello\\tINTJ\\tUH\\t_\\t5\\tdiscourse\\t_\\tSpaceAfter=No\\n" + 
			"2\\t,\\t,\\tPUNCT\\t,\\t_\\t5\\tpunct\\t_\\t_\\n" + 
			"3\\tcan\\tcan\\tAUX\\tMD\\tVerbForm=Fin\\t5\\taux\\t_\\t_\\n" + 
			"4\\tyou\\tyou\\tPRON\\tPRP\\tCase=Nom|Person=2|PronType=Prs\\t5\\tnsubj\\t_\\t_\\n" + 
			"5\\thear\\thear\\tVERB\\tVB\\tVerbForm=Inf\\t0\\troot\\t_\\t_\\n" + 
			"6\\tme\\tI\\tPRON\\tPRP\\tCase=Acc|Number=Sing|Person=1|PronType=Prs\\t5\\tobj\\t_\\tSpaceAfter=No\\n" + 
			"7\\t?\\t?\\tPUNCT\\t.\\t_\\t5\\tpunct\\t_\\tSpacesAfter=\\\\n\\n\"" + 
			"  } \n" + 
			"}";


	private static final String SAMPLE_INPUT_TURN1 = "{\n" + 
			"  \"metadata\": {},\n" + 
			"  \"data\": {\n" + 
			"    \"conll\": \"# sent_id = 2\\n" + 
			"# text = Yes, I would like to apply for the First Reception Service.\\n" + 
			"1\\tYes\\tyes\\tINTJ\\tUH\\t_\\t5\\tdiscourse\\t_\\tSpaceAfter=No\\n" + 
			"2\\t,\\t,\\tPUNCT\\t,\\t_\\t5\\tpunct\\t_\\t_\\n" + 
			"3\\tI\\tI\\tPRON\\tPRP\\tCase=Nom|Number=Sing|Person=1|PronType=Prs\\t5\\tnsubj\\t_\\t_\\n" + 
			"4\\twould\\twould\\tAUX\\tMD\\tVerbForm=Fin\\t5\\taux\\t_\\t_\\n" + 
			"5\\tlike\\tlike\\tVERB\\tVB\\tVerbForm=Inf\\t0\\troot\\t_\\t_\\n" + 
			"6\\tto\\tto\\tPART\\tTO\\t_\\t7\\tmark\\t_\\t_\\n" + 
			"7\\tapply\\tapply\\tVERB\\tVB\\tVerbForm=Inf\\t5\\txcomp\\t_\\t_\\n" + 
			"8\\tfor\\tfor\\tADP\\tIN\\t_\\t12\\tcase\\t_\\t_\\n" + 
			"9\\tthe\\tthe\\tDET\\tDT\\tDefinite=Def|PronType=Art\\t12\\tdet\\t_\\t_\\n" + 
			"10\\tFirst\\tfirst\\tADJ\\tJJ\\tDegree=Pos|NumType=Ord\\t12\\tamod\\t_\\t_\\n" + 
			"11\\tReception\\treception\\tNOUN\\tNN\\tNumber=Sing\\t12\\tcompound\\t_\\t_\\n" + 
			"12\\tService\\tservice\\tNOUN\\tNN\\tNumber=Sing\\t7\\tobl\\t_\\tSpaceAfter=No\\n" + 
			"13\\t.\\t.\\tPUNCT\\t.\\t_\\t5\\tpunct\\t_\\tSpacesAfter=\\\\n\\n\"" + 
			"  } \n" + 
			"}";
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
		requestBody = @RequestBody(
						content = @Content(mediaType = "application/json",
										schema = @Schema(implementation = CuniInput.class),
										examples = {
											@ExampleObject(name = "Turn 0",
													value = SAMPLE_CUNI_INPUT_TURN0)
										}
						)
					),
		responses = {
		        @ApiResponse(description = "The deep analysis result.",
		        			content = @Content(schema = @Schema(implementation = DeepAnalysisOutput.class)
		        ))
	})
	public DeepAnalysisOutput analyze(
			@Parameter(description = "Container for analysis input data.", required = true) CuniInput input) throws WelcomeException {

        DeepAnalysisInput ourInput = CuniInput2DeepAnalysisInput.convert(input);
        String conll = ourInput.getData().getConll();
        System.out.println(conll);
        
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