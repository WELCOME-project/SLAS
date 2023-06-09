package edu.upf.taln.welcome.slas.core;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createDependencyAndBind;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.conceptMapper.ConceptMapper;
import org.apache.uima.conceptMapper.support.dictionaryResource.DictionaryResource_impl;
import org.apache.uima.conceptMapper.support.tokens.TokenNormalizer;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.FlowControllerFactory;
import org.apache.uima.flow.FlowControllerDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.dbpedia.spotlight.uima.SpotlightAnnotator;
import org.dkpro.core.udpipe.UDPipeParser;
import org.dkpro.core.udpipe.UDPipePosTagger;
import org.dkpro.core.udpipe.UDPipeSegmenter;
import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS_NOUN;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS_VERB;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.unihd.dbs.uima.annotator.heideltime.HeidelTime;
import edu.upf.taln.flask_wrapper.type.WSDSpan;
import edu.upf.taln.parser.deep_parser.core.DeepParser;
import edu.upf.taln.uima.NumberSequenceAnnotator;
import edu.upf.taln.uima.flask_wrapper.ConceptExtractorAnnotator;
import edu.upf.taln.uima.flask_wrapper.EmotionAnnotator;
import edu.upf.taln.uima.flask_wrapper.GeolocationAnnotator;
import edu.upf.taln.uima.flask_wrapper.NERAnnotator;
import edu.upf.taln.uima.flask_wrapper.SpeechActAnnotator;
import edu.upf.taln.uima.flow.AnnotationFlowController;
import edu.upf.taln.uima.retokenize.MultiwordRetokenizer;
import edu.upf.taln.uima.wsd.item_annotator.WSDItemAnnotator;
import edu.upf.taln.uima.wsd_wrapper.WsdAnnotator;
import edu.upf.taln.welcome.slas.commons.analysis.FlowItem;
import edu.upf.taln.welcome.slas.commons.analysis.FlowStepName;
import edu.upf.taln.welcome.slas.core.pojos.input.AnalysisConfiguration;

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

	private static FlowItem getTaxonomyDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
		try {
			File tmpTokenizerDescription = File.createTempFile("prefffix_", "_suffix");
			tmpTokenizerDescription.deleteOnExit();
			AnalysisEngineDescription segmenter = createEngineDescription(
					UDPipeSegmenter.class, 
					UDPipeSegmenter.PARAM_LANGUAGE, LANGUAGE);
			segmenter.toXML(new OutputStreamWriter(new FileOutputStream(tmpTokenizerDescription), StandardCharsets.UTF_8));

			AnalysisEngineDescription conceptMapper = AnalysisEngineFactory.createEngineDescription(
					ConceptMapper.class,
					"TokenizerDescriptorPath", tmpTokenizerDescription.getAbsolutePath(),
					"LanguageID", LANGUAGE,
					ConceptMapper.PARAM_TOKENANNOTATION, "de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token",
					ConceptMapper.PARAM_ANNOTATION_NAME, "edu.upf.taln.common.uima.taxonomy.TaxonomyEntry",
					"SpanFeatureStructure", "uima.tcas.DocumentAnnotation",
					ConceptMapper.PARAM_FEATURE_LIST, new String[]{"label", "entryId", "parentId", "parentLabel"},
					ConceptMapper.PARAM_ATTRIBUTE_LIST, new String[]{"label", "entryId", "parentId", "parentLabel"},
					TokenNormalizer.PARAM_CASE_MATCH, "insensitive"
					);

			// taxonomy path is the path to the taxonomy dictionary (ex.: src/main/resources/annotation_dictionaries/taxonomyDictExample.xml)
			createDependencyAndBind(conceptMapper, "DictionaryFile", DictionaryResource_impl.class, "file:///" + configuration.getTaxonomyDictPath());
			return new FlowItem(conceptMapper,FlowStepName.TAXONOMY.name());

		} catch (SAXException | IOException | InvalidXMLException ex) {
			throw new ResourceInitializationException(ex);
		}   

	}

	private static FlowItem getConceptExtractionDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {

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
	
	private static FlowItem getWsdServiceDescription(AnalysisConfiguration configuration) throws ResourceInitializationException {
		AnalysisEngineDescription wsd = AnalysisEngineFactory.createEngineDescription(
				WsdAnnotator.class,
				WsdAnnotator.PARAM_WSD_URL, configuration.getDisambiguationUrl(),
				WsdAnnotator.PARAM_LANGUAGE, LANGUAGE);

		return new FlowItem(wsd, FlowStepName.CONCEPT_DESAMBIGUATION.name());
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
	
	private static FlowItem getHeideltimeRetokenizerDescription() throws ResourceInitializationException {
		AnalysisEngineDescription retokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "de.unihd.dbs.uima.types.heideltime.Timex3");

		return new FlowItem(retokenizer,FlowStepName.HEIDELTIME_RETOKENIZER.name());
	}
	
	private static FlowItem getGeolocationRetokenizerDescription() throws ResourceInitializationException {

		AnalysisEngineDescription retokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "edu.upf.taln.flask_wrapper.type.GeolocationCandidate");

		return new FlowItem(retokenizer,FlowStepName.GEOLOCATION_RETOKENIZER.name());
	}
	
	private static FlowItem getTaxonomyRetokenizerDescription() throws ResourceInitializationException {

		AnalysisEngineDescription retokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "edu.upf.taln.common.uima.taxonomy.TaxonomyEntry");

		return new FlowItem(retokenizer,FlowStepName.TAXONOMY_RETOKENIZER.name());
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
		
		/*AnalysisEngineDescription intervalTagger = AnalysisEngineFactory.createEngineDescription(
				IntervalTagger.class,
				IntervalTagger.PARAM_LANGUAGE, "english",
				IntervalTagger.PARAM_INTERVALS, true,
				IntervalTagger.PARAM_INTERVAL_CANDIDATES, false);*/
		
		return new FlowItem(createEngineDescription(heideltime/*, intervalTagger*/), FlowStepName.HEIDELTIME.name());
	}
	
	private static FlowItem getNumericSequenceAnnotatorDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription numberSequence = AnalysisEngineFactory.createEngineDescription(
				NumberSequenceAnnotator.class,
				NumberSequenceAnnotator.NUM_POS_TAG, "NUM");
		
		return new FlowItem(numberSequence, FlowStepName.NUMBER_SEQUENCE.name());
	}
	
	private static FlowItem getNumericSequenceRetokenizerDescription() throws ResourceInitializationException {
        
		AnalysisEngineDescription numberSequenceRetokenizer = AnalysisEngineFactory.createEngineDescription(
				MultiwordRetokenizer.class,
				MultiwordRetokenizer.PARAM_SPAN_CLASS_NAME, "edu.upf.taln.uima.number_sequence.types.NumberSequenceAnnotation",
				MultiwordRetokenizer.PARAM_NEW_TOKENS_POS_TYPE, "de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS_NUM",
				MultiwordRetokenizer.PARAM_DEFAULT_POS, "CD",
				MultiwordRetokenizer.PARAM_DEFAULT_CPOS, "NUM");
		
		return new FlowItem(numberSequenceRetokenizer, FlowStepName.NUMBER_SEQUENCE_RETOKENIZER.name());
	}

	public static AnalysisEngineDescription getPipelineDescription(AnalysisConfiguration configuration) throws UIMAException {

		if (configuration == null) {
			// TODO: log empty configuration
			configuration = new AnalysisConfiguration();
		}

		List<FlowItem> flowItems = new ArrayList<FlowItem>();

		flowItems.add(getPreprocessDescription());
		
		flowItems.add(getNumericSequenceAnnotatorDescription());
		flowItems.add(getNumericSequenceRetokenizerDescription());
		
		flowItems.add(getHeideltimeDescription());
				
		flowItems.add(getTaxonomyDescription(configuration));

		flowItems.add(getNERDescription(configuration));

		flowItems.add(getGeolocationDescription(configuration));

		flowItems.add(getSpeechActDescription(configuration));

		flowItems.add(getConceptExtractionDescription(configuration));

		flowItems.add(getWsdServiceDescription(configuration));

		AnalysisEngineDescription dbpedia = createEngineDescription(SpotlightAnnotator.class,
				SpotlightAnnotator.PARAM_ENDPOINT, configuration.getDbpediaUrl(),
				SpotlightAnnotator.PARAM_FUNCTION, "annotate",
				SpotlightAnnotator.PARAM_CONFIDENCE, 0.35f,
				SpotlightAnnotator.PARAM_ALL_CANDIDATES, true);
		flowItems.add(new FlowItem(dbpedia,FlowStepName.DBPEDIA.name()));
		
		flowItems.add(getTaxonomyRetokenizerDescription());
		flowItems.add(getHeideltimeRetokenizerDescription());
		flowItems.add(getGeolocationRetokenizerDescription());
		flowItems.add(getDBPediaRetokenizerDescription());
		flowItems.add(getDisambiguatedSensesRetokenizerDescription());
		flowItems.add(getNameEntityRetokenizerDescription());

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
