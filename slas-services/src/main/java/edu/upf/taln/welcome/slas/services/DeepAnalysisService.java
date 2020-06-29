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


/**
 * Analyze text and return results in JSON format
 * 
 * @author jens.grivolla
 * 
 */
@Path("/analyze")
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
	
    private DeepAnalysisOutput createFakeOutput() {

        
        Entity entity1 = new Entity();
        entity1.setId("entity_1");
        entity1.setType("Predicate");
        entity1.setAnchor("apply");
        entity1.setLink("bn:00082707v");

        Entity entity2 = new Entity();
        entity2.setId("entity_2");
        entity2.setType("DialogueUser");
        entity2.setAnchor("I");
        entity2.setConfidence(0.6);

        Entity entity3 = new Entity();
        entity3.setId("entity_3");
        entity3.setType("Concept");
        entity3.setAnchor("Service");
        entity3.setLink("bn:00070651n");
        entity3.setConfidence(0.8);

        Entity entity4 = new Entity();
        entity4.setId("entity_4");
        entity4.setType("Concept");
        entity4.setAnchor("First Reception Service");
        entity4.setConfidence(0.7);

        Participant participant1 = new Participant();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity2.getId());

            participant1.setRole("Agent");
            participant1.setEntities(entities);
        }
        
        Participant participant2 = new Participant();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity3.getId());
            entities.add(entity4.getId());

            participant2.setRole("Object");
            participant2.setEntities(entities);
        }
        
        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);
        
        Relation relation1 = new Relation();
        relation1.setId("relation1");
        relation1.setRelation("entity_1");
        relation1.setParticipants(participants);
        
        SpeechAct speechAct1 = new SpeechAct();
        speechAct1.setId("act_1");
        speechAct1.setAnchor("Hello");
        speechAct1.setType("Greeting");
        
        SpeechAct speechAct2 = new SpeechAct();
        {        
            List<String> entities = new ArrayList<>();
            entities.add(entity1.getId());
            entities.add(entity2.getId());
            entities.add(entity3.getId());
            entities.add(entity4.getId());
            entities.add(relation1.getId());

            speechAct2.setId("act_3");
            speechAct2.setAnchor("Hello");
            speechAct2.setType("Request for service");
            speechAct2.setEntities(entities);
        }

        List<Entity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);
        entities.add(entity3);
        entities.add(entity4);
        
        List<Relation> relations = new ArrayList<>();
        relations.add(relation1);
        
        List<SpeechAct> speechActs = new ArrayList<>();
        speechActs.add(speechAct1);
        speechActs.add(speechAct2);
        
        
        OutputData data = new OutputData();
        data.setUserId(1);
        data.setDialogueSession(1);
        data.setDialogueTurn(1);
        data.setEntities(entities);
        data.setRelations(relations);
        data.setSpeechActs(speechActs);
        
        DeepAnalysisOutput output = new DeepAnalysisOutput();
        output.setData(data);
        return output;
    }
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Performs a deep syntactic analysis of the input data.",
		description = "Returns the result of the deep syntatic analysis, it is, a predicate-argument structure.",
		responses = {
		        @ApiResponse(description = "The deep analysis result.",
		        			content = @Content(schema = @Schema(implementation = DeepAnalysisOutput.class)
		        ))
	})
	public DeepAnalysisOutput analyze(
			@Parameter(description = "Container for analysis input data.", required = true) DeepAnalysisInput container) throws WelcomeException {

        DeepAnalysisOutput output = createFakeOutput();
		return output;
	}

}