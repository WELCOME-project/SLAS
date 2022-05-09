package edu.upf.taln.welcome.slas.commons.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.TypeSystemUtil;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.wsd.type.Sense;
import de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult;
import de.unihd.dbs.uima.types.heideltime.Timex3;

import edu.upf.taln.flask_wrapper.type.GeolocationCandidate;
import edu.upf.taln.parser.deep_parser.types.DeepToken;
import edu.upf.taln.parser.deep_parser.types.PredArgsDependency;
import edu.upf.taln.parser.deep_parser.types.PredArgsToken;
import edu.upf.taln.uima.flow.pojos.EnginesExecutionData;
import edu.upf.taln.uima.flow.types.EnginesExecution;
import edu.upf.taln.utils.pojos.uima.babelnet.BabelnetGraph;
import edu.upf.taln.utils.pojos.uima.concept.ConceptGraph;
import edu.upf.taln.utils.pojos.uima.dbpedia.DbpediaGraph;
import edu.upf.taln.utils.pojos.uima.deep.DeepGraph;
import edu.upf.taln.utils.pojos.uima.geolocation.GeolocationGraph;
import edu.upf.taln.utils.pojos.uima.heideltime.HeideltimeGraph;
import edu.upf.taln.utils.pojos.uima.ner.NerGraph;
import edu.upf.taln.utils.pojos.uima.predarg.PredargGraph;
import edu.upf.taln.utils.pojos.uima.surface.SurfaceGraph;
import edu.upf.taln.utils.pojos.uima.taxonomy.TaxonomyGraph;
import edu.upf.taln.utils.pojos.uima.token.TokenNode;

import edu.upf.taln.welcome.slas.commons.output.EntityTypeInfo.EntityType;
import edu.upf.taln.welcome.slas.commons.output.welcome.DlaResult;
import edu.upf.taln.welcome.slas.commons.output.welcome.Entity;
import edu.upf.taln.welcome.slas.commons.output.welcome.Entity.TemporalAnalysis;
import edu.upf.taln.welcome.slas.commons.output.welcome.Location;
import edu.upf.taln.welcome.slas.commons.output.welcome.Participant;
import edu.upf.taln.welcome.slas.commons.output.welcome.Relation;
import edu.upf.taln.welcome.slas.commons.output.welcome.SpeechAct;
import edu.upf.taln.welcome.slas.commons.types.OriginalText;

public class OutputGenerator {
        
    protected static AnalysisOutputMetadata generateMetadata(JCas jCas) {
		AnalysisOutputMetadata outputMetadata = new AnalysisOutputMetadata();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		outputMetadata.setDate(dateFormat.format(date));

        try {
            EnginesExecution ee = JCasUtil.selectSingle(jCas, EnginesExecution.class);
            ObjectMapper om = new ObjectMapper();
            EnginesExecutionData executionData = om.readValue(ee.getExecutionJson(), EnginesExecutionData.class);
            outputMetadata.setTimes(executionData.getTimeData());

        } catch (IllegalArgumentException e) {
            //If there are not engine executions, ignore
            //If there are more than one, maybe warn? 
        } catch (Exception e) {
            e.printStackTrace();
        }

		return outputMetadata;
	}

	protected static XmiResult generateXmiResult(JCas jCas) {

		XmiResult result = new XmiResult();
		try {
			String xmi;
			String typeSystem;

			ByteArrayOutputStream xmiBs = new ByteArrayOutputStream();
			XmiCasSerializer.serialize(jCas.getCas(), null, xmiBs, true, null);
			xmi = new String(xmiBs.toByteArray(), StandardCharsets.UTF_8);

			ByteArrayOutputStream typeBs = new ByteArrayOutputStream();
			TypeSystemUtil.typeSystem2TypeSystemDescription(jCas.getTypeSystem()).toXML(typeBs);
			typeSystem = new String(typeBs.toByteArray(), StandardCharsets.UTF_8);


			result.setXmi(xmi);
			result.setTypesystem(typeSystem);


		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
	protected static final Pattern QUOTED_PATTERN = Pattern.compile("^\"([^\"]+)\"$");
    
	public static String removeQuotes(String text) {
		String result = text;
		Matcher matcher = QUOTED_PATTERN.matcher(text);
		if (matcher.find()) {
			result = matcher.group(1);
		}
		return result;
	}
	
	protected static List<String> extractRoles(String feats, String temporalType) {
		
		List<String> roles = new ArrayList<>();
		
		if (feats != null && !feats.isEmpty()) {
	        Map<String, String> featMap = Pattern.compile("\\|")
	            .splitAsStream(feats)
	            .map(feat -> feat.split("=", 2))
	            .collect(Collectors.toMap(a -> a[0], a -> removeQuotes(a[1])));
	
	        if (featMap.containsKey("fnrole")) {
	            roles.add(featMap.get("fnrole"));
	        }
	        if (featMap.containsKey("vnrole")) {
	            roles.add(featMap.get("vnrole"));
	        }
	        if (featMap.containsKey("pbrole")) {
	            roles.add(featMap.get("pbrole"));
	        }
		}
        
        if (temporalType != null) {
        	roles.add("ht:" + temporalType);
        }
        
        return roles;
    }

    private static List<String> extractRoles(PredArgsToken childAnn, Entity entity) {
        
        String feats = childAnn.getFeatures();
        
        String temporalType = null;
        if (entity.getType().equals(EntityType.Temporal.name())) {
        	temporalType = entity.getTemporalAnalysis().getType();
        }
        
        List<String> roles = extractRoles(feats, temporalType);
        
        return roles;
    }

	private static String extractParentRole(PredArgsToken token, String relationLabel) {

        String feats = token.getFeatures();
		
		String role = null;
		if (feats != null && !feats.isEmpty()) {
	        Map<String, String> featMap = Pattern.compile("\\|")
	            .splitAsStream(feats)
	            .map(feat -> feat.split("=", 2))
	            .collect(Collectors.toMap(a -> a[0], a -> removeQuotes(a[1])));
			
			role = featMap.get(relationLabel + "_role");
		}
		return role;
	}
    
    private static List<Relation> extractRelations(Collection<PredArgsDependency> relationCollection, TreeMap<Integer, Entity> tokenMap) {
        
        // cada governador, con argumentos, instancia una relacion
		int i = 1;
        HashMap<PredArgsToken, Relation> relationMap = new HashMap<>();
		for (PredArgsDependency depAnn : relationCollection) {
            PredArgsToken govAnn = (PredArgsToken) depAnn.getGovernor();
            PredArgsToken childAnn = (PredArgsToken) depAnn.getDependent();
            
            if (govAnn != childAnn) {
                Entity govEntity = tokenMap.get(govAnn.hashCode());
                Entity childEntity = tokenMap.get(childAnn.hashCode());

                Relation relation = relationMap.get(govAnn);
                if (relation == null) {
                    ArrayList<String> links = extractLinks(govAnn);

                    relation = new Relation();
                    relation.setId("relation_" + i);
                    relation.setPredicate(govEntity.getId());
                    relation.setParticipants(new ArrayList<>());
                    relation.setLinks(links);
                    relationMap.put(govAnn, relation);
                    i++;
                }
                
                List<String> roles = extractRoles(childAnn, childEntity);
				String parentRole = extractParentRole(govAnn, depAnn.getDependencyType());
				if (parentRole != null) {
					roles.add(parentRole);
				}

                Participant participant = new Participant();
                participant.setRoles(roles);
                participant.setEntity(childEntity.getId());

                ArrayList<Participant> participants = relation.getParticipants();
                participants.add(participant);
            }
        }
        
        ArrayList<Relation> relations = new ArrayList(relationMap.values());
        return relations;
    }

    protected static List<Entity> extractEntitiesFromWSD(JCas jCas) {
		List<Entity> entities = new ArrayList<>();
        
		int i = 1;
		for (WSDResult wsdResult : JCasUtil.select(jCas, WSDResult.class)) {
			Sense bestSense = wsdResult.getBestSense();

			Entity entity = new Entity();
			entity.setId("entity_" + i);
            
            // TODO: Mirar issue de gerardquía de types
			try {
				NamedEntity nameEntity = JCasUtil.selectSingleAt(jCas, NamedEntity.class, wsdResult.getBegin(), wsdResult.getEnd());
				//System.out.println("NE: \"" + nameEntity.getCoveredText() + "\": " + nameEntity.getValue());
				entity.setType(nameEntity.getValue());
                
			} catch(IllegalArgumentException e){
				entity.setType("concept");
			}
			entity.setAnchor(wsdResult.getCoveredText());
            
            List<String> links = new ArrayList<>();
            // TODO: Add also geolocation info
            links.add(bestSense.getId());
			entity.setConfidence(bestSense.getConfidence()); // TODO: Revisar

            entity.setLinks(links);
            
			entities.add(entity);
            
			i++;
		}
        
        return entities;
    }

	private static Entity extractEntity(PredArgsToken token, String entityId,
			HashSet<PredArgsToken> predicateSet) {

		List<String> links = new ArrayList<>();
		List<Location> locations = new ArrayList<>();

		Entity entity = new Entity();
		entity.setId(entityId);
		entity.setAnchor(token.getValue());
		entity.setLinks(links);
		entity.setLocations(locations);

        List<WSDResult> wsdList = JCasUtil.selectCovered(WSDResult.class, token);
		if (!wsdList.isEmpty()) {
			WSDResult wsdAnn = wsdList.get(0);
			Sense bestSense = wsdAnn.getBestSense();

			links.add(bestSense.getId());
			entity.setConfidence(bestSense.getConfidence());
		}

		// Setting type
        EntityTypeInfo typeInfo = new EntityTypeInfo(token, predicateSet);
        String type = typeInfo.getType();
		entity.setType(type);

		List<Timex3> heideltimeList = JCasUtil.selectCovered(Timex3.class, token);
		if (!heideltimeList.isEmpty()) {
			Timex3 time = heideltimeList.get(0);
			TemporalAnalysis temporalAnalysis = new TemporalAnalysis(time);
			entity.setTemporalAnalysis(temporalAnalysis);
		}

        List<GeolocationCandidate> geolocationsList = JCasUtil.selectCovered(GeolocationCandidate.class, token);
		for (GeolocationCandidate candidate : geolocationsList) {
			Location location = new Location();
			if (candidate.getOsmNodeId() != null) {
				location.setLink("osm:" + candidate.getOsmNodeId());
			} else if (candidate.getGeonamesId() != null) {
				location.setLink("geonames:" + candidate.getGeonamesId());
			}
			location.setLatitude(candidate.getLatitude());
			location.setLongitude(candidate.getLongitude());
			locations.add(location);
		}

		return entity;
	}

    private static ArrayList<String> extractLinks(PredArgsToken token) {
        
        ArrayList<String> links = new ArrayList();
        
        String features = token.getFeatures();
        if (features != null && !features.equals("_")) {
            Map<String, String> featMap = Pattern.compile("\\|")
                .splitAsStream(features)
                .map(feat -> feat.split("=", 2))
                .collect(Collectors.toMap(a -> a[0], a -> removeQuotes(a[1])));
            
            String vn = featMap.get("vn");
            if (vn != null) {
                links.add(vn);
            }
            String fn = featMap.get("frame");
            if (fn != null) {
                links.add("fn:" + fn);
            }
        }
        return links;
    }
    
    protected static TreeMap<Integer, Entity> extractEntities(Collection<PredArgsToken> tokenCollection, Collection<PredArgsDependency> relationCollection) {
        
        HashSet<PredArgsToken> predicateSet = new HashSet<>();
        for (PredArgsDependency depAnn : relationCollection) {
            PredArgsToken govAnn = (PredArgsToken) depAnn.getGovernor();
            PredArgsToken childAnn = (PredArgsToken) depAnn.getDependent();
            if (govAnn != childAnn) {
                predicateSet.add(govAnn);
            }
        }
                
		int i = 1;
        TreeMap<Integer, Entity> entities = new TreeMap<>();
		for (PredArgsToken token : tokenCollection) {

            String entityId = "entity_" + i;
            Entity entity = extractEntity(token, entityId, predicateSet);
            
			entities.put(token.hashCode(), entity);            
			i++;
		}
        
        return entities;
    }
    
    private static DlaResult extractDLAResult(Collection<PredArgsToken> tokenCollection, Collection<PredArgsDependency> relationCollection, Collection<edu.upf.taln.flask_wrapper.type.SpeechAct> speechActCollection) {
    	return extractDLAResult(tokenCollection, relationCollection, speechActCollection, null);
    }

    private static DlaResult extractDLAResult(Collection<PredArgsToken> tokenCollection, Collection<PredArgsDependency> relationCollection, Collection<edu.upf.taln.flask_wrapper.type.SpeechAct> speechActCollection, OriginalText originalText) {

        DlaResult result = new DlaResult();
        
        TreeMap<Integer, Entity> tokenMap = extractEntities(tokenCollection, relationCollection);
        List<Entity> entities = new ArrayList(tokenMap.values());
        result.setEntities(entities);
        
        List<Relation> relations = extractRelations(relationCollection, tokenMap);
        result.setRelations(relations);
        
        int j = 1;
        List<SpeechAct> speechActs = new ArrayList<>();
        for (edu.upf.taln.flask_wrapper.type.SpeechAct speechAct : speechActCollection) {
            SpeechAct sa = new SpeechAct();
            sa.setId("speech_act_" + j);
            sa.setType(speechAct.getLabel());
            
            String anchor = speechAct.getCoveredText();
            //If originalText is present a " ." has been added to the text
            if (originalText != null  
            		&& speechAct.getEnd() == originalText.getText().length() + 2 
            		&& anchor.endsWith(" .")) {
            	anchor = anchor.substring(0, anchor.length() - 2);
            }
            sa.setAnchor(anchor);
            
            List<String> entitiesList = new ArrayList<>();
            List<PredArgsToken> matchingPredArgsTokens = JCasUtil.selectCovered(PredArgsToken.class, speechAct);
            for (PredArgsToken matchingPredArgsToken : matchingPredArgsTokens) {
            	Entity matchingEntity = tokenMap.get(matchingPredArgsToken.hashCode());
            	entitiesList.add(matchingEntity.getId());
            }
            
            sa.setEntities(entitiesList);
            
            speechActs.add(sa);
            j++;
        }
        result.setSpeechActs(speechActs);
        return result;
    }
    
	protected static DlaResult generateDlaResult(JCas jCas) {

        Collection<PredArgsToken> tokenCollection = JCasUtil.select(jCas, PredArgsToken.class);
        Collection<PredArgsDependency> relationCollection = JCasUtil.select(jCas, PredArgsDependency.class);
        Collection<edu.upf.taln.flask_wrapper.type.SpeechAct> speechActCollection = JCasUtil.select(jCas, edu.upf.taln.flask_wrapper.type.SpeechAct.class);
        Collection<OriginalText> originalTextCollection = JCasUtil.select(jCas, OriginalText.class);
        OriginalText originalText = null;
        if (!originalTextCollection.isEmpty()) {
        	originalText = originalTextCollection.iterator().next();
        }
        DlaResult result = extractDLAResult(tokenCollection, relationCollection, speechActCollection, originalText);
        
		return result;
	}
    
	protected static List<DlaResult> generateDlaResultBySentence(JCas jCas) {

        List<DlaResult> results = new ArrayList<>();
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            Collection<PredArgsToken> tokenCollection = JCasUtil.selectCovered(PredArgsToken.class, sentence);
            Collection<PredArgsDependency> relationCollection = JCasUtil.selectCovered(PredArgsDependency.class, sentence);
            Collection<edu.upf.taln.flask_wrapper.type.SpeechAct> speechActCollection = JCasUtil.selectCovered(edu.upf.taln.flask_wrapper.type.SpeechAct.class, sentence);

            DlaResult result = extractDLAResult(tokenCollection, relationCollection, speechActCollection);
            
            results.add(result);
        }
		return results;
	}
    
	protected static WelcomeDemoResult generateDemoResult(JCas jCas) {

		Map<Token, TokenNode> token2entity = new HashMap<>();
		Map<DeepToken, TokenNode> deepToken2entity = new HashMap<>();
		Map<PredArgsToken, TokenNode> predargsToken2entity = new HashMap<>();

		WelcomeDemoResult result = new WelcomeDemoResult();

		result.setSurfaceParsing(SurfaceGraph.extract(jCas, token2entity));

		result.setNer(NerGraph.extract(jCas));

		result.setConceptExtraction(ConceptGraph.extract(jCas));

		result.setDbpediaLinking(DbpediaGraph.extract(jCas));

		result.setDeepParsing(DeepGraph.extract(jCas, deepToken2entity));
		result.setPredargParsing(PredargGraph.extract(jCas, predargsToken2entity));
		List<String[]> triples = TriplesDataExtractor.extractTriples(jCas);
		result.setTriples(triples);

		result.setBabelnetLinking(BabelnetGraph.extract(jCas));

		//result.setSentenceRanking(SentenceGraph.extract(jCas, token2entity, deepToken2entity, predargsToken2entity, null, null));
		
		result.setGeolocation(GeolocationGraph.extract(jCas));
		
		result.setHeideltime(HeideltimeGraph.extract(jCas));
		
		result.setTaxonomy(TaxonomyGraph.extract(jCas));

		return result;
	}

	public static DeepAnalysisOutput generateDlaOutput(JCas jCas) {
		DeepAnalysisOutput analysisResult = new DeepAnalysisOutput();

		DlaResult result = generateDlaResult(jCas);
		analysisResult.setData(result);

		return analysisResult;
	}

	public static AnalysisOutputImpl<WelcomeDemoResult, AnalysisOutputMetadata> generateDemoOutput(JCas jCas) {
		AnalysisOutputImpl<WelcomeDemoResult, AnalysisOutputMetadata> analysisResult = new AnalysisOutputImpl<>();

		UUID uuid = UUID.randomUUID();
		analysisResult.setId(uuid.toString());
		analysisResult.setText(jCas.getDocumentText());
		if(jCas.getDocumentLanguage() != null) {
			analysisResult.setLanguage(jCas.getDocumentLanguage());
		}

		AnalysisOutputMetadata outputMetadata = generateMetadata(jCas);
		analysisResult.setMetadata(outputMetadata);

		WelcomeDemoResult result = generateDemoResult(jCas);

		analysisResult.setResult(result);

		return analysisResult;
	}

	public static AnalysisOutputImpl<WelcomeDemoResult, AnalysisOutputMetadata> generateDemoOutputWithDla(JCas jCas) {
		AnalysisOutputImpl<WelcomeDemoResult, AnalysisOutputMetadata> analysisResult = new AnalysisOutputImpl<>();

		UUID uuid = UUID.randomUUID();
		analysisResult.setId(uuid.toString());
		analysisResult.setText(jCas.getDocumentText());
		if(jCas.getDocumentLanguage() != null) {
			analysisResult.setLanguage(jCas.getDocumentLanguage());
		}

		AnalysisOutputMetadata outputMetadata = generateMetadata(jCas);
		analysisResult.setMetadata(outputMetadata);


		WelcomeDemoResult result = generateDemoResult(jCas);
		DlaResult dlaResult = generateDlaResult(jCas);
		result.setDlaResult(dlaResult);
		analysisResult.setResult(result);

		return analysisResult;
	}

	public static XmiResult generateXmiOutput(JCas jCas) {
		XmiResult result = generateXmiResult(jCas);

		return result;
	}

	public static DeepAnalysisOutput generateDummyResponse(JCas jcas) {

		String text = jcas.getDocumentText();

		System.out.println(text);

		int turn = 1;
		if (text.contains("Sebastià")) {
			turn = 7;
		} else if (text.contains("Karim")) {
			turn = 5;
		} else if (text.contains("apply")) {
			turn = 3;
		} else if (text.contains("Hello")) {
			turn = 1;
		} else {
			turn = 0;
		}
		DeepAnalysisOutput output = SampleResponses.generateResponse(turn);
		return output;
	}
}
