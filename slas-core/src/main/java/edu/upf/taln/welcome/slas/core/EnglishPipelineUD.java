package edu.upf.taln.welcome.slas.core;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;

import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.FlowControllerFactory;
import org.apache.uima.flow.FlowControllerDescription;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.dbpedia.spotlight.uima.SpotlightAnnotator;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.matetools.MateLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.matetools.MateMorphTagger;
import de.tudarmstadt.ukp.dkpro.core.nlp4j.Nlp4JDependencyParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordCoreferenceResolver;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.wsd.algorithm.MostFrequentSenseBaseline;
import de.tudarmstadt.ukp.dkpro.wsd.annotator.WSDAnnotatorIndividualBasic;
import de.tudarmstadt.ukp.dkpro.wsd.resource.WSDResourceIndividualBasic;

import edu.stanford.nlp.process.WordToSentenceProcessor.NewlineIsSentenceBreak;

import edu.upf.taln.flask_wrapper.type.WSDSpan;
import edu.upf.taln.parser.deep_parser.core.DeepParser;
import edu.upf.taln.textplanning.uima.TextPlanningAnnotator;
import edu.upf.taln.uima.disambiguation.core.TALNSenseBaseline;
import edu.upf.taln.uima.disambiguation.core.WSDAnnotatorCollectiveContext;
import edu.upf.taln.uima.disambiguation.core.WSDResourceCollectiveCandidate;
import edu.upf.taln.uima.disambiguation.core.inventory.BabelnetSenseInventoryResource;
import edu.upf.taln.uima.disambiguation.core.inventory.CompactDictionarySenseInventoryResource;
import edu.upf.taln.uima.flask_wrapper.ConceptExtractorAnnotator;
import edu.upf.taln.uima.flask_wrapper.EmotionAnnotator;
import edu.upf.taln.uima.flask_wrapper.NERAnnotator;
import edu.upf.taln.uima.flow.AnnotationFlowController;
import edu.upf.taln.uima.retokenize.MultiwordRetokenizer;
import edu.upf.taln.uima.wsd.annotation_extender.core.WSDResultExtender;
import edu.upf.taln.uima.wsd.item_annotator.WSDItemAnnotator;
import edu.upf.taln.welcome.slas.commons.analysis.FlowStepName;
import edu.upf.taln.welcome.slas.core.pojos.input.AnalysisConfiguration;

import it.uniroma1.lcl.jlt.util.Language;

public class EnglishPipelineUD {
	
	private static final String LANGUAGE = "en";
    
    static ArrayList<String> names = new ArrayList<>();
    
	private static AnalysisEngineDescription getPreprocessDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription segmenter = createEngineDescription(
				StanfordSegmenter.class, 
				StanfordSegmenter.PARAM_NEWLINE_IS_SENTENCE_BREAK, NewlineIsSentenceBreak.ALWAYS,
	    		StanfordSegmenter.PARAM_LANGUAGE, LANGUAGE);
        
		AnalysisEngineDescription pos = createEngineDescription(
				StanfordPosTagger.class,
				StanfordPosTagger.PARAM_LANGUAGE, LANGUAGE,
				StanfordPosTagger.PARAM_VARIANT, "taln-ud");
        
		AnalysisEngineDescription lemma = createEngineDescription(
				MateLemmatizer.class,
				MateLemmatizer.PARAM_LANGUAGE, LANGUAGE,
				MateLemmatizer.PARAM_VARIANT, "ewt");
		
		AnalysisEngineDescription morph = createEngineDescription(
				MateMorphTagger.class,
				MateMorphTagger.PARAM_LANGUAGE, LANGUAGE,
				MateMorphTagger.PARAM_VARIANT, "taln-ud");
		
		names.add(FlowStepName.PARSING.name());
		return createEngineDescription(segmenter, pos, lemma, morph);	
	}
	
	private static AnalysisEngineDescription getParsingDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription parserDescription = createEngineDescription(
				Nlp4JDependencyParser.class, 
				Nlp4JDependencyParser.PARAM_LANGUAGE, LANGUAGE,
				Nlp4JDependencyParser.PARAM_VARIANT, "ewt-ud");
        
		names.add(FlowStepName.SSYNTS.name());
        return parserDescription;
	}
	
	private static AnalysisEngineDescription getConceptExtractionDescription(AnalysisConfiguration configuration) throws ResourceInitializationException{
        
		AnalysisEngineDescription spans = createEngineDescription(
				ConceptExtractorAnnotator.class,
				ConceptExtractorAnnotator.PARAM_FLASK_URL, configuration.getCandidateConceptsUrl());
		String[] annotationClasses = new String[] {
				WSDSpan.class.getName(),
				NamedEntity.class.getName()
		};
        
		AnalysisEngineDescription candidates = createEngineDescription(
				WSDItemAnnotator.class, 
				WSDItemAnnotator.PARAM_CLASS_NAMES, annotationClasses);
		
		names.add(FlowStepName.CONCEPT_CANDIDATES.name());
        return createEngineDescription(spans, candidates);
	}
	
	private static AnalysisEngineDescription getFirstSenseDisambiguationDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
		
		ExternalResourceDescription BabelNet = createExternalResourceDescription(BabelnetSenseInventoryResource.class, 
				BabelnetSenseInventoryResource.PARAM_BABELNET_CONFIGPATH, configuration.getBabelnetConfigPath(), 
				BabelnetSenseInventoryResource.PARAM_BABELNET_LANG, LANGUAGE.toUpperCase(), 
				BabelnetSenseInventoryResource.PARAM_BABELNET_DESCLANG, "EN");
	    
	    ExternalResourceDescription mfsBaselineResourceBabelNet = createExternalResourceDescription(WSDResourceIndividualBasic.class,
				WSDResourceIndividualBasic.SENSE_INVENTORY_RESOURCE, BabelNet,
				WSDResourceIndividualBasic.DISAMBIGUATION_METHOD, MostFrequentSenseBaseline.class.getName());
		
		AnalysisEngineDescription senseDisambiguation = createEngineDescription(WSDAnnotatorIndividualBasic.class,
				WSDAnnotatorIndividualBasic.WSD_ALGORITHM_RESOURCE, mfsBaselineResourceBabelNet,
				WSDAnnotatorIndividualBasic.PARAM_MAXIMUM_ITEMS_TO_ATTEMPT, -1);
		
		AnalysisEngineDescription extender = AnalysisEngineFactory.createEngineDescription(WSDResultExtender.class,
				WSDResultExtender.PARAM_BABELNET, BabelNet, 
				WSDResultExtender.PARAM_LANGUAGES, new Language[]{Language.EN, Language.ES, Language.IT, Language.EL});
		
		AnalysisEngineDescription babelnet = createEngineDescription(senseDisambiguation, extender);
        
		names.add(FlowStepName.CONCEPT_DESAMBIGUATION.name());
		return babelnet;
	}
	
	private static AnalysisEngineDescription getTalnDisambiguationDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
		
		ExternalResourceDescription babelnetResource = createExternalResourceDescription(BabelnetSenseInventoryResource.class,
				BabelnetSenseInventoryResource.PARAM_BABELNET_CONFIGPATH, configuration.getBabelnetConfigPath(),
				BabelnetSenseInventoryResource.PARAM_BABELNET_LANG, LANGUAGE.toUpperCase(),
				BabelnetSenseInventoryResource.PARAM_BABELNET_DESCLANG, "EN");
		
		ExternalResourceDescription compactDictionaryResource = createExternalResourceDescription(CompactDictionarySenseInventoryResource.class, 
				CompactDictionarySenseInventoryResource.PARAM_CONFIGURATION_PATH, configuration.getCompactDictionaryPath(), 
				CompactDictionarySenseInventoryResource.PARAM_LANGUAGE, LANGUAGE.toUpperCase() );
	    
		ExternalResourceDescription talnSenseResource = createExternalResourceDescription(WSDResourceCollectiveCandidate.class,
				WSDResourceCollectiveCandidate.SENSE_INVENTORY_RESOURCE, compactDictionaryResource,
				WSDResourceCollectiveCandidate.DISAMBIGUATION_METHOD, TALNSenseBaseline.class.getName(),
				WSDResourceCollectiveCandidate.PARAM_LANGUAGE, LANGUAGE.toUpperCase(),
				WSDResourceCollectiveCandidate.PARAM_PROPERTIES_FILE, configuration.getRankingPropertiesFile());
		
		AnalysisEngineDescription senseDisambiguation = createEngineDescription(WSDAnnotatorCollectiveContext.class,
				WSDAnnotatorCollectiveContext.WSD_ALGORITHM_RESOURCE, talnSenseResource,
				WSDAnnotatorCollectiveContext.PARAM_BEST_ONLY, false,
				WSDAnnotatorCollectiveContext.PARAM_NORMALIZE_CONFIDENCE, false);

		AnalysisEngineDescription extender = AnalysisEngineFactory.createEngineDescription(WSDResultExtender.class,
				WSDResultExtender.PARAM_BABELNET, babelnetResource,
				WSDResultExtender.PARAM_LANGUAGES, new Language[]{Language.EN, Language.ES, Language.IT, Language.EL});

		AnalysisEngineDescription agregate = createEngineDescription(senseDisambiguation, extender);
		
		names.add(FlowStepName.CONCEPT_DESAMBIGUATION.name());
		return agregate;
	}
	
	private static AnalysisEngineDescription getDisambiguatedSensesRetokenizerDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription retokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult");
		
		names.add(FlowStepName.RETOKENIZER.name());
		return retokenizer;
	}
	
	private static AnalysisEngineDescription getNameEntityRetokenizerDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription retokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity");
		
		names.add(FlowStepName.NER_RETOKENIZER.name());
		return retokenizer;
	}
	
	private static AnalysisEngineDescription getDBPediaRetokenizerDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription retokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "org.dbpedia.spotlight.uima.types.TopDBpediaResource");
		
		names.add(FlowStepName.DBPEDIA_RETOKENIZER.name());
		return retokenizer;
	}
	
	private static AnalysisEngineDescription getNERDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
        
		AnalysisEngineDescription flaskNER = AnalysisEngineFactory.createEngineDescription(
				NERAnnotator.class,
				NERAnnotator.PARAM_FLASK_URL, configuration.getNerUrl());
		
		names.add(FlowStepName.NER.name());
		return flaskNER;
	}
	
	private static AnalysisEngineDescription getEmotionDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
        
		AnalysisEngineDescription flaskNER = AnalysisEngineFactory.createEngineDescription(
				EmotionAnnotator.class,
				EmotionAnnotator.PARAM_FLASK_URL, configuration.getEmotionUrl(),
				EmotionAnnotator.PARAM_SKIP_ON_SERVER_ERROR, false, 
				EmotionAnnotator.PARAM_SCOPE, "sentence");
		
		names.add(FlowStepName.EMOTION.name());
		return flaskNER;
	}

    public static AnalysisEngineDescription getPipelineDescription(AnalysisConfiguration configuration) throws UIMAException {
        
		if (configuration == null) {
			// TODO: log empty configuration
			configuration = new AnalysisConfiguration();
        }
		
		ArrayList<AnalysisEngineDescription> components = new ArrayList<>();
        
		components.add(getPreprocessDescription());
				
		components.add(getNERDescription(configuration));
		 
		components.add(getConceptExtractionDescription(configuration));
		
		if (configuration.getBabelnetConfigPath() != null) {
			if(configuration.getRankingPropertiesFile() == null || configuration.getCompactDictionaryPath() == null) {
				components.add(getFirstSenseDisambiguationDescription(configuration));
			} else {
				components.add(getTalnDisambiguationDescription(configuration));
			}
		}
		
		AnalysisEngineDescription dbpedia = createEngineDescription(SpotlightAnnotator.class,
				SpotlightAnnotator.PARAM_ENDPOINT, configuration.getDbpediaUrl(),
				SpotlightAnnotator.PARAM_FUNCTION, "annotate",
				SpotlightAnnotator.PARAM_CONFIDENCE, 0.35f,
				SpotlightAnnotator.PARAM_ALL_CANDIDATES, true);
		components.add(dbpedia);
		names.add(FlowStepName.DBPEDIA.name());
		
		components.add(getDBPediaRetokenizerDescription());
		components.add(getNameEntityRetokenizerDescription());
        components.add(getDisambiguatedSensesRetokenizerDescription());
        
        components.add(getParsingDescription());
		
		AnalysisEngineDescription deepParser = createEngineDescription(
                DeepParser.class,
				DeepParser.PARAM_TYPE, "ud",
				DeepParser.PARAM_RUN_DEEP, true,
				DeepParser.PARAM_RUN_PREDARGS, true,
				DeepParser.PARAM_RUN_TRIPLES, true);
		components.add(deepParser);
		names.add(FlowStepName.DSYNTS.name());
				
		FlowControllerDescription fcd = FlowControllerFactory.createFlowControllerDescription(AnnotationFlowController.class); 
	    AnalysisEngineDescription description = createEngineDescription(components, names, null, null, fcd);
		
		return description;
	}

}
