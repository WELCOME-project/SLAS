package edu.upf.taln.welcome.slas.core;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.FlowControllerFactory;
import org.apache.uima.flow.FlowControllerDescription;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.dbpedia.spotlight.uima.SpotlightAnnotator;
import org.dkpro.core.udpipe.UDPipeParser;
import org.dkpro.core.udpipe.UDPipePosTagger;
import org.dkpro.core.udpipe.UDPipeSegmenter;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS_NOUN;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS_VERB;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.wsd.algorithm.MostFrequentSenseBaseline;
import de.tudarmstadt.ukp.dkpro.wsd.annotator.WSDAnnotatorIndividualBasic;
import de.tudarmstadt.ukp.dkpro.wsd.resource.WSDResourceIndividualBasic;
import de.unihd.dbs.uima.annotator.heideltime.HeidelTime;
import de.unihd.dbs.uima.annotator.intervaltagger.IntervalTagger;
import edu.upf.taln.flask_wrapper.type.WSDSpan;
import edu.upf.taln.parser.deep_parser.core.DeepParser;
import edu.upf.taln.uima.disambiguation.core.TALNSenseBaseline;
import edu.upf.taln.uima.disambiguation.core.WSDAnnotatorCollectiveContext;
import edu.upf.taln.uima.disambiguation.core.WSDResourceCollectiveCandidate;
import edu.upf.taln.uima.disambiguation.core.inventory.BabelnetSenseInventoryResource;
import edu.upf.taln.uima.disambiguation.core.inventory.CompactDictionarySenseInventoryResource;
import edu.upf.taln.uima.flask_wrapper.ConceptExtractorAnnotator;
import edu.upf.taln.uima.flask_wrapper.EmotionAnnotator;
import edu.upf.taln.uima.flask_wrapper.GeolocationAnnotator;
import edu.upf.taln.uima.flask_wrapper.NERAnnotator;
import edu.upf.taln.uima.flask_wrapper.SpeechActAnnotator;
import edu.upf.taln.uima.flow.AnnotationFlowController;
import edu.upf.taln.uima.retokenize.MultiwordRetokenizer;
import edu.upf.taln.uima.wsd.annotation_extender.core.WSDResultExtender;
import edu.upf.taln.uima.wsd.item_annotator.WSDItemAnnotator;
import edu.upf.taln.welcome.slas.commons.analysis.FlowItem;
import edu.upf.taln.welcome.slas.commons.analysis.FlowStepName;
import edu.upf.taln.welcome.slas.core.pojos.input.AnalysisConfiguration;
import it.uniroma1.lcl.jlt.util.Language;

public class EnglishPipelineUD {
	
	private static final String LANGUAGE = "en";
        
	private static FlowItem getPreprocessDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription segmenter = createEngineDescription(
				UDPipeSegmenter.class, 
				//UDPipeSegmenter.PARAM_NEWLINE_IS_SENTENCE_BREAK, NewlineIsSentenceBreak.ALWAYS,
				UDPipeSegmenter.PARAM_LANGUAGE, LANGUAGE);
        
		/***
		 * does PoS, morph, lemma
		 */
		AnalysisEngineDescription posMorphLemma = createEngineDescription(
				UDPipePosTagger.class,
				UDPipePosTagger.PARAM_LANGUAGE, LANGUAGE
				//UDPipePosTagger.PARAM_VARIANT, "taln-ud"
				);
        		
		//return createEngineDescription(segmenter, pos, lemma, morph);
		return new FlowItem(createEngineDescription(segmenter, posMorphLemma),FlowStepName.PARSING.name());
	}
	
	private static FlowItem getParsingDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription parserDescription = createEngineDescription(
				UDPipeParser.class, 
				UDPipeParser.PARAM_LANGUAGE, LANGUAGE
				//UDPipeParser.PARAM_VARIANT, "ewt-ud"
				);
        
        return new FlowItem(parserDescription,FlowStepName.SSYNTS.name());
	}
	
	private static FlowItem getConceptExtractionDescription(AnalysisConfiguration configuration) throws ResourceInitializationException{
        
		AnalysisEngineDescription spans = createEngineDescription(
				ConceptExtractorAnnotator.class,
				ConceptExtractorAnnotator.PARAM_FLASK_URL, configuration.getCandidateConceptsUrl());
		
		String[] annotationClasses = new String[] {
				WSDSpan.class.getName(),
				NamedEntity.class.getName(),
				POS_VERB.class.getName(),
				POS_NOUN.class.getName()
		};
		AnalysisEngineDescription candidates = createEngineDescription(
				WSDItemAnnotator.class, 
				WSDItemAnnotator.PARAM_CLASS_NAMES, annotationClasses);
		
        return new FlowItem (createEngineDescription(spans, candidates),FlowStepName.CONCEPT_CANDIDATES.name());
	}
	
	private static FlowItem getFirstSenseDisambiguationDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
		
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
        
		return new FlowItem(babelnet,FlowStepName.CONCEPT_DESAMBIGUATION.name());
	}
	
	private static FlowItem getTalnDisambiguationDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
		
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
		
		return new FlowItem(agregate,FlowStepName.CONCEPT_DESAMBIGUATION.name());
	}
	
	private static FlowItem getDisambiguatedSensesRetokenizerDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription retokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult");
		
		return new FlowItem(retokenizer,FlowStepName.RETOKENIZER.name());
	}
	
	private static FlowItem getNameEntityRetokenizerDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription retokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity");
		
		return new FlowItem(retokenizer,FlowStepName.NER_RETOKENIZER.name());
	}
	
	private static FlowItem getDBPediaRetokenizerDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription retokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "org.dbpedia.spotlight.uima.types.TopDBpediaResource");
		
		return new FlowItem(retokenizer,FlowStepName.DBPEDIA_RETOKENIZER.name());
	}
	
	private static FlowItem getNERDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
        
		AnalysisEngineDescription flaskNER = AnalysisEngineFactory.createEngineDescription(
				NERAnnotator.class,
				NERAnnotator.PARAM_FLASK_URL, configuration.getNerUrl());
		
		return new FlowItem(flaskNER,FlowStepName.NER.name());
	}
	
	private static FlowItem getEmotionDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
        
		AnalysisEngineDescription flaskNER = AnalysisEngineFactory.createEngineDescription(
				EmotionAnnotator.class,
				EmotionAnnotator.PARAM_FLASK_URL, configuration.getEmotionUrl(),
				EmotionAnnotator.PARAM_SKIP_ON_SERVER_ERROR, false, 
				EmotionAnnotator.PARAM_SCOPE, "sentence");
		
		return new FlowItem(flaskNER,FlowStepName.EMOTION.name());
	}
	
	private static FlowItem getSpeechActDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
        
		AnalysisEngineDescription flaskSpeechAct = AnalysisEngineFactory.createEngineDescription(
				SpeechActAnnotator.class,
				SpeechActAnnotator.PARAM_FLASK_URL, configuration.getSpeechActUrl(),
				SpeechActAnnotator.PARAM_SCOPE, "sentence");
		
		return new FlowItem(flaskSpeechAct, FlowStepName.SPEECHACT.name());
	}
	
	private static FlowItem getGeolocationDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
        
		AnalysisEngineDescription flaskGeolocation = AnalysisEngineFactory.createEngineDescription(
				GeolocationAnnotator.class,
				GeolocationAnnotator.PARAM_FLASK_URL, configuration.getGeolocationUrl());
		
		return new FlowItem(flaskGeolocation, FlowStepName.GEOLOCATION.name());
	}
	
	private static FlowItem getHeideltimeDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription heideltime = AnalysisEngineFactory.createEngineDescription(
				HeidelTime.class,
				HeidelTime.PARAM_LANGUAGE, "english",
				HeidelTime.PARAM_TYPE_TO_PROCESS, "news",
				HeidelTime.PARAM_DATE, true,
				HeidelTime.PARAM_TIME, true,
				HeidelTime.PARAM_DURATION, true,
				HeidelTime.PARAM_SET, true,
				HeidelTime.PARAM_TEMPONYMS, false,
				HeidelTime.PARAM_GROUP, true,
				HeidelTime.PARAM_USE_COARSE_VALUE, true,
				HeidelTime.PARAM_DEBUG, false);
		
		AnalysisEngineDescription intervalTagger = AnalysisEngineFactory.createEngineDescription(
				IntervalTagger.class,
				IntervalTagger.PARAM_LANGUAGE, "english",
				IntervalTagger.PARAM_INTERVALS, true,
				IntervalTagger.PARAM_INTERVAL_CANDIDATES, true);
		
		return new FlowItem(createEngineDescription(heideltime, intervalTagger), FlowStepName.HEIDELTIME.name());
	}

    public static AnalysisEngineDescription getPipelineDescription(AnalysisConfiguration configuration) throws UIMAException {
        
		if (configuration == null) {
			// TODO: log empty configuration
			configuration = new AnalysisConfiguration();
        }
		
		List<FlowItem> flowItems = new ArrayList<FlowItem>();
        
		flowItems.add(getPreprocessDescription());
		
		flowItems.add(getHeideltimeDescription());
				
		flowItems.add(getNERDescription(configuration));
		
		flowItems.add(getGeolocationDescription(configuration));
		
		flowItems.add(getSpeechActDescription(configuration));
		 
		flowItems.add(getConceptExtractionDescription(configuration));
		
		if (configuration.getBabelnetConfigPath() != null) {
			if(configuration.getRankingPropertiesFile() == null || configuration.getCompactDictionaryPath() == null) {
				flowItems.add(getFirstSenseDisambiguationDescription(configuration));
			} else {
				flowItems.add(getTalnDisambiguationDescription(configuration));
			}
		}
		
		AnalysisEngineDescription dbpedia = createEngineDescription(SpotlightAnnotator.class,
				SpotlightAnnotator.PARAM_ENDPOINT, configuration.getDbpediaUrl(),
				SpotlightAnnotator.PARAM_FUNCTION, "annotate",
				SpotlightAnnotator.PARAM_CONFIDENCE, 0.35f,
				SpotlightAnnotator.PARAM_ALL_CANDIDATES, true);
		flowItems.add(new FlowItem(dbpedia,FlowStepName.DBPEDIA.name()));
		
		flowItems.add(getDBPediaRetokenizerDescription());
		flowItems.add(getNameEntityRetokenizerDescription());
		flowItems.add(getDisambiguatedSensesRetokenizerDescription());
        
		flowItems.add(getParsingDescription());
		
		AnalysisEngineDescription deepParser = createEngineDescription(
                DeepParser.class,
				DeepParser.PARAM_TYPE, "ud-welcome",
				DeepParser.PARAM_RUN_DEEP, true,
				DeepParser.PARAM_RUN_PREDARGS, true);
		flowItems.add(new FlowItem(deepParser,FlowStepName.DSYNTS.name()));
				
		FlowControllerDescription fcd = FlowControllerFactory.createFlowControllerDescription(AnnotationFlowController.class); 
	    AnalysisEngineDescription description = createEngineDescription(
	    		flowItems.stream().map(FlowItem::getComponent).collect(Collectors.toList()),
	    		flowItems.stream().map(FlowItem::getName).collect(Collectors.toList()),
	    		null, null, fcd);
		
		return description;
	}

}
