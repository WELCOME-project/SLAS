package edu.upf.taln.welcome.slas.commons.output.demo;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.TypeSystemUtil;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.wsd.type.Sense;
import de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult;
import edu.upf.taln.parser.deep_parser.types.DeepToken;
import edu.upf.taln.parser.deep_parser.types.PredArgsToken;
import edu.upf.taln.utils.pojos.uima.babelnet.BabelnetGraph;
import edu.upf.taln.utils.pojos.uima.concept.ConceptGraph;
import edu.upf.taln.utils.pojos.uima.dbpedia.DbpediaGraph;
import edu.upf.taln.utils.pojos.uima.deep.DeepGraph;
import edu.upf.taln.utils.pojos.uima.ner.NerGraph;
import edu.upf.taln.utils.pojos.uima.predarg.PredargGraph;
import edu.upf.taln.utils.pojos.uima.surface.SurfaceGraph;
import edu.upf.taln.utils.pojos.uima.token.TokenNode;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.output.DlaResult;
import edu.upf.taln.welcome.slas.commons.output.Entity;
import edu.upf.taln.welcome.slas.commons.output.XmiResult;

public class OutputGenerator {
	
	
	
	protected static AnalysisOutputMetadata generateMetadata() {
		AnalysisOutputMetadata outputMetadata = new AnalysisOutputMetadata();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		outputMetadata.setDate(dateFormat.format(date));
		
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
	
	protected static DlaResult generateDlaResult(JCas jCas) {
		List<Entity> entities = new ArrayList<>();
		
		int i = 1;
		for (WSDResult wsdResult : JCasUtil.select(jCas, WSDResult.class)) {
			Sense bestSense = wsdResult.getBestSense();

			Entity entity = new Entity();
	        entity.setId("entity_" + i);
	        //entity.setType("Predicate");
	        entity.setAnchor(wsdResult.getCoveredText());
	        entity.setLink(bestSense.getId());
	        entity.setConfidence(bestSense.getConfidence());
	        entities.add(entity);
	        
	        i++;
		}
        
        DlaResult result = new DlaResult();
        result.setEntities(entities);
        return result;
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
		
		return result;
	}
	
	public static AnalysisOutput<DlaResult, AnalysisOutputMetadata> generateDlaOutput(InputMetadata metadata, JCas jCas) {
		AnalysisOutput<DlaResult, AnalysisOutputMetadata> analysisResult = new AnalysisOutput<DlaResult, AnalysisOutputMetadata>();
		
		AnalysisOutputMetadata outputMetadata = generateMetadata();
		analysisResult.setMetadata(outputMetadata);
		
		DlaResult result = generateDlaResult(jCas);
        analysisResult.setResult(result);
		
		return analysisResult;
	}
	
	public static AnalysisOutput<WelcomeDemoResult, AnalysisOutputMetadata> generateDemoOutput(String text, InputMetadata metadata, JCas jCas) {
		AnalysisOutput<WelcomeDemoResult, AnalysisOutputMetadata> analysisResult = new AnalysisOutput<WelcomeDemoResult, AnalysisOutputMetadata>();
		
		UUID uuid = UUID.randomUUID();
		analysisResult.setId(uuid.toString());
		analysisResult.setText(text);
		if(metadata.getLanguage() != null) {
			analysisResult.setLanguage(metadata.getLanguage());
		}
		
		AnalysisOutputMetadata outputMetadata = generateMetadata();
		analysisResult.setMetadata(outputMetadata);
		
		
		WelcomeDemoResult result = generateDemoResult(jCas);
		
		analysisResult.setResult(result);
		
		return analysisResult;
	}
	
	public static AnalysisOutput<WelcomeDemoResult, AnalysisOutputMetadata> generateDemoOutputWithDla(String text, InputMetadata metadata, JCas jCas) {
		AnalysisOutput<WelcomeDemoResult, AnalysisOutputMetadata> analysisResult = new AnalysisOutput<WelcomeDemoResult, AnalysisOutputMetadata>();
		
		UUID uuid = UUID.randomUUID();
		analysisResult.setId(uuid.toString());
		analysisResult.setText(text);
		if(metadata.getLanguage() != null) {
			analysisResult.setLanguage(metadata.getLanguage());
		}
		
		AnalysisOutputMetadata outputMetadata = generateMetadata();
		analysisResult.setMetadata(outputMetadata);
		
		
		WelcomeDemoResult result = generateDemoResult(jCas);
		DlaResult dlaResult = generateDlaResult(jCas);
		result.setDlaResult(dlaResult);
		analysisResult.setResult(result);
		
		return analysisResult;
	}
	
	public static AnalysisOutput<XmiResult, AnalysisOutputMetadata> generateXmiOutput(InputMetadata metadata, JCas jCas) {
		AnalysisOutput<XmiResult, AnalysisOutputMetadata> analysisResult = new AnalysisOutput<XmiResult, AnalysisOutputMetadata>();
		
		UUID uuid = UUID.randomUUID();
		analysisResult.setId(uuid.toString());
		if(metadata.getLanguage() != null) {
			analysisResult.setLanguage(metadata.getLanguage());
		}
		
		AnalysisOutputMetadata outputMetadata = generateMetadata();
		analysisResult.setMetadata(outputMetadata);
		
		XmiResult result = generateXmiResult(jCas);
		analysisResult.setResult(result);
		
		return analysisResult;
	}
}
