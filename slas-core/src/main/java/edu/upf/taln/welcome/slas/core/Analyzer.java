package edu.upf.taln.welcome.slas.core;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInputPlain;
import edu.upf.taln.welcome.slas.commons.output.DeepAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.demo.AnalysisOutput;
import edu.upf.taln.welcome.slas.core.pojos.input.AnalysisConfiguration;
import edu.upf.taln.welcome.slas.core.utils.WelcomeUIMAUtils;
import edu.upf.taln.welcome.slas.core.utils.WelcomeUIMAUtils.AnalysisType;


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

	protected final AnalysisConfiguration readEnvConfiguration() throws UIMAException {
		
		AnalysisConfiguration configuration = new AnalysisConfiguration();
		configuration.setBabelnetConfigPath(System.getenv("BABELNET_CONFIG"));
		configuration.setDbpediaUrl(System.getenv("DBPEDIA_ENDPOINT"));
		configuration.setCandidateConceptsUrl(System.getenv("CONCEPT_URL"));
		configuration.setNerUrl(System.getenv("NER_URL"));
		configuration.setRankingPropertiesFile(System.getenv("DISAMBIGUATION_PROPS"));
		configuration.setCompactDictionaryPath(System.getenv("COMPACT_DICTIONARY"));
		configuration.setEmotionUrl(System.getenv("EMOTION_URL"));
		//configuration.setAspectUrl(System.getenv("ASPECT_URL"));
        
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
    
	/**
	 * Analyzes a given input
     * 
     * @param input
     * 
     * @return the analysis output
     * @throws edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException
	 */
	public AnalysisOutput analyze(DeepAnalysisInput input) throws WelcomeException {

		try {
            String language = "en";
            String conll = input.getData().getConll();
            AnalysisType analysisType = AnalysisType.DEFAULT;
            
            JCas jCas = WelcomeUIMAUtils.createJCasFromConll(conll, language, analysisType);
            
            pipeline.process(jCas);
            
            /*AnalysisEngine writer = AnalysisEngineFactory.createEngine(
    				XmiWriter.class,
    				XmiWriter.PARAM_TARGET_LOCATION, "/home/ivan/git/welcome-slas/dla-service/src/test/resources/output/xmi/fromConll/",
    				XmiWriter.PARAM_OVERWRITE, true);
			writer.process(jCas);*/
            
            AnalysisOutput analysisResult = WelcomeUIMAUtils.extractOutput(jCas, input);
			
			return analysisResult;
		
		} catch (Exception e) { // never crash the service, no matter what happens
			log.error("Error processing document", e);
			throw new WelcomeException("Error processing document", e);
		}
	}
	
	/**
	 * Analyzes a given input
     * 
     * @param input
     * 
     * @return the analysis output
     * @throws edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException
	 */
	public AnalysisOutput analyze(DeepAnalysisInputPlain input) throws WelcomeException {

		try {
            String language = "en";
            String text = input.getData().getConll();
            AnalysisType analysisType = AnalysisType.FULL;
            
            JCas jCas = WelcomeUIMAUtils.createJCas(text, language, analysisType);
			
            pipeline.process(jCas);
            
			/*AnalysisEngine writer = AnalysisEngineFactory.createEngine(
    				XmiWriter.class,
    				XmiWriter.PARAM_TARGET_LOCATION, "/home/ivan/git/welcome-slas/dla-service/src/test/resources/output/xmi/fromText/",
    				XmiWriter.PARAM_OVERWRITE, true);
			writer.process(jCas);*/

            AnalysisOutput analysisResult = WelcomeUIMAUtils.extractOutput(jCas, input);
			
			return analysisResult;
		
		} catch (Exception e) { // never crash the service, no matter what happens
			log.error("Error processing document", e);
			throw new WelcomeException("Error processing document", e);
		}
	}
}
