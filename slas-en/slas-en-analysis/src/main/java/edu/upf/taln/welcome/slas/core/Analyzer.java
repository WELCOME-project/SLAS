package edu.upf.taln.welcome.slas.core;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.jg.wordstonumbers.WordsToNumbersUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import edu.upf.taln.flask_wrapper.type.SpeechAct;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.factories.OutputFactory;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputData;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.core.factories.JCasWelcomeFactory;
import edu.upf.taln.welcome.slas.core.pojos.input.AnalysisConfiguration;
import edu.upf.taln.welcome.slas.core.taxonomy.Concepts;
import edu.upf.taln.welcome.slas.core.taxonomy.TaxonomyProcessor;


/**
 *
 * @author rcarlini
 */
public class Analyzer {
    
    protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Singleton instance of Analyzer.
	 */
	private static Analyzer instance;
	
	private AnalysisEngine pipeline;

	/**
	 * Return a unique instance of Analyzer (Singleton pattern).
	 * @return a unique instance of QueryManager
	 * @throws V4DesignException 
	 */
	public static Analyzer getInstance() throws WelcomeException {
		if (instance == null) {
			instance = new Analyzer();
		}
		return instance;
	}

	/**
	 * Private constructor (Singleton pattern)
	 * @throws WelcomeException 
	 */
	protected Analyzer() throws WelcomeException {
		try {
            AnalysisConfiguration configuration = readEnvConfiguration();
			setupPipeline(configuration);
            
		} catch (UIMAException e) {
			log.error("Error configuring UIMA pipeline", e);
			throw new WelcomeException("Error configuring UIMA pipeline", e);
		}
	}

    private String updateTaxonomy(String api_url, String defaultPath) throws UIMAException {
        
        try {
            Concepts concepts;
            
            URL url = new URL(api_url);
            try (InputStream inStream = url.openStream()) {
                
                String rdfLang = null;
                if (api_url.endsWith(".ttl")) {
                    rdfLang = "TTL";
                }
                concepts = TaxonomyProcessor.processStream(inStream, rdfLang);
                log.info("Remote taxonomy retrieved successfully!");

            } catch (Exception ex1) {
                
                log.warn("Unable to retrieve the remote taxonomy!"/*, ex1*/);
                log.warn("Attempting loading from default path...");
                
                File localFile = new File(defaultPath);
                URL defaultUrl = localFile.toURI().toURL();
                
                try (InputStream inStream = defaultUrl.openStream()) {
                    
                    String rdfLang = null;
                    if (defaultPath.endsWith(".ttl")) {
                        rdfLang = "TTL";
                    }
                    concepts = TaxonomyProcessor.processStream(inStream, rdfLang);

                } catch (Exception ex2) {
                    log.warn("Unable to retrieve the default path taxonomy!"/*, ex2*/);
                    log.warn("Attempting loading packaged taxonomy...");

                    try (InputStream inStream = this.getClass().getResourceAsStream("/taxonomy/taxonomy.ttl")) {
                        concepts = TaxonomyProcessor.processStream(inStream, "TTL");
                    }
                }
            }

            File tempFile = File.createTempFile("uima_taxonomy_", "");
            tempFile.deleteOnExit();

            ObjectMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.writeValue(tempFile, concepts);

            // TODO: Check the generated file!
            String outputPath = tempFile.getAbsolutePath();                        
            return outputPath;
            
        } catch (Exception ex) {
            throw new UIMAException(ex);
        } 
    }
	
	protected final AnalysisConfiguration readEnvConfiguration() throws UIMAException {
		
        String taxonomyPath = updateTaxonomy(System.getenv("TAXONOMY_SERVICE_URL"), System.getenv("TAXONOMY_PATH"));

		AnalysisConfiguration configuration = new AnalysisConfiguration();
		configuration.setDbpediaUrl(System.getenv("DBPEDIA_ENDPOINT"));
		configuration.setCandidateConceptsUrl(System.getenv("CONCEPT_URL"));
		configuration.setNerUrl(System.getenv("NER_URL"));
		configuration.setDisambiguationUrl(System.getenv("DISAMBIGUATION_URL"));
		configuration.setEmotionUrl(System.getenv("EMOTION_URL"));
		configuration.setSpeechActUrl(System.getenv("SPEECHACT_URL"));
		configuration.setGeolocationUrl(System.getenv("GEOLOCATION_URL"));
		configuration.setTaxonomyDictPath(taxonomyPath);

        return configuration;
	}

	protected final void setupPipeline(AnalysisConfiguration configuration) throws UIMAException {

		try {
			AnalysisEngineDescription aeDescription = EnglishPipelineUD.getPipelineDescription(configuration);
			pipeline = createEngine(aeDescription);
            
		} catch (UIMAException e) {
			e.printStackTrace();
			throw new ResourceInitializationException(e);
		}
    }

	protected static DeepAnalysisInput preprocess(DeepAnalysisInput input) {
		
		DeepAnalysisInput newInput = new DeepAnalysisInput(input);
		InputData data = newInput.getData();
		
		String text = data.getText();
		
		String newText = WordsToNumbersUtil.convertTextualNumbersInDocument(text);
		
		String replaced = newText;
		if (!newText.trim().matches("(?s).*[.?!]$")) {
			replaced = newText.trim() + " .";
		}
		replaced = replaced.replaceAll("Yeah", "Yes");
		replaced = replaced.replaceAll("yeah", "yes");
		data.setText(replaced);
		return newInput;
	}	
	
	/**
	 * Analyzes a given input
     * 
     * @param input
     * 
     * @return the analysis output
     * @throws edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException
	 */
	public IAnalysisOutput analyze(DeepAnalysisInput input) throws WelcomeException {

		try {            
			DeepAnalysisInput newInput = Analyzer.preprocess(input);
            JCas jCas = JCasWelcomeFactory.createJCas(newInput, input.getData().getText());
            
            pipeline.process(jCas);
			postprocess(jCas);
            
            InputMetadata metadata = newInput.getMetadata();
            IAnalysisOutput analysisResult = OutputFactory.extractOutput(jCas, metadata.getOutputType());
			
			return analysisResult;
		
		} catch (Exception e) { // never crash the service, no matter what happens
			log.error("Error processing document", e);
			throw new WelcomeException("Error processing document", e);
		}
	}
	
	protected static void postprocess(JCas jCas) {
		
		for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
			String text = sentence.getCoveredText();
			text = text.trim().toLowerCase();
			if (text.endsWith(" .")) {
				text = text.substring(0, text.length() - 2);
			} else if(text.endsWith(".")) {
				text = text.substring(0, text.length() - 1);
			}

			HashSet<String> hedgeTexts = new HashSet<>(Arrays.asList(new String[]{"hm", "see", "oh", "ah"}));
			HashSet<String> agreeAccept = new HashSet<>(Arrays.asList(new String[]{"it s okay", "it's okay", "it is okay", "okay that s fine", "okay that's fine", "okay that is fine", "okay", "ok", "yes, thank you", "yes thank you", "yes, thanks", "yes thanks"}));
			String speechActLabel = null;
			
			if (hedgeTexts.contains(text)) {
				speechActLabel = "Hedge";
			} else if (agreeAccept.contains(text)) {
				speechActLabel = "Agree/Accept";
			}

			if (speechActLabel != null) {
				List<SpeechAct> speechActs = JCasUtil.selectCovered(SpeechAct.class, sentence);
				for (SpeechAct speechAct : speechActs) {
					speechAct.setLabel(speechActLabel);
				}
			}
		}
	}
}
