package edu.upf.taln.welcome.slas.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.TypeSystemUtil;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.io.conll.ConllUReader;
import edu.upf.taln.uima.flow.IFlowOptions;
import edu.upf.taln.uima.flow.utils.FlowUtils;
import edu.upf.taln.welcome.slas.commons.analysis.FlowStepName;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.IAnalysisInput;
import edu.upf.taln.welcome.slas.commons.output.DeepAnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.DlaResult;
import edu.upf.taln.welcome.slas.commons.output.demo.AnalysisOutput;
import edu.upf.taln.welcome.slas.commons.output.demo.AnalysisOutputMetadata;
import edu.upf.taln.welcome.slas.commons.output.demo.OutputGenerator;
import edu.upf.taln.welcome.slas.commons.output.demo.WelcomeDemoResult;


/**
 *
 * @author rcarlini
 */
public class WelcomeUIMAUtils {
    
    public enum AnalysisType { BASIC, FULL, DEFAULT, TEST }
    
    public static IFlowOptions getOptions(AnalysisType type) {
        
        // TODO: Define what components should be enable for BASIC analysis
    	IFlowOptions options;
    	switch(type) {
	    	
    		case TEST:
		        options = () -> {
		            Map<String, Boolean> flowMap = new HashMap<>();
		
		            flowMap.put(FlowStepName.PARSING.name(), false);
		
		            flowMap.put(FlowStepName.NER.name(), false);
		            flowMap.put(FlowStepName.NER_RETOKENIZER.name(), false);
		
		            flowMap.put(FlowStepName.CONCEPT_CANDIDATES.name(), false);
		            flowMap.put(FlowStepName.CONCEPT_DESAMBIGUATION.name(), false);
		            flowMap.put(FlowStepName.RETOKENIZER.name(), false);
		
		            flowMap.put(FlowStepName.DBPEDIA.name(), false);
		            flowMap.put(FlowStepName.DBPEDIA_RETOKENIZER.name(), false);
		
		            flowMap.put(FlowStepName.SSYNTS.name(), false);
		            flowMap.put(FlowStepName.DSYNTS.name(), false);
		
		            flowMap.put(FlowStepName.EMOTION.name(), false);
		
		            return flowMap;
		        };
		        break;
	    	case DEFAULT:
		        options = () -> {
		            Map<String, Boolean> flowMap = new HashMap<>();
		
		            flowMap.put(FlowStepName.PARSING.name(), false);
		
		            flowMap.put(FlowStepName.NER.name(), true);
		            flowMap.put(FlowStepName.NER_RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.CONCEPT_CANDIDATES.name(), true);
		            flowMap.put(FlowStepName.CONCEPT_DESAMBIGUATION.name(), true);
		            flowMap.put(FlowStepName.RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.DBPEDIA.name(), true);
		            flowMap.put(FlowStepName.DBPEDIA_RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.SSYNTS.name(), false);
		            flowMap.put(FlowStepName.DSYNTS.name(), true);
		
		            flowMap.put(FlowStepName.EMOTION.name(), true);
		
		            return flowMap;
		        };
		        break;
	    	default:
	    	case FULL:
		        options = () -> {
		            Map<String, Boolean> flowMap = new HashMap<>();
		
		            flowMap.put(FlowStepName.PARSING.name(), true);
		
		            flowMap.put(FlowStepName.NER.name(), true);
		            flowMap.put(FlowStepName.NER_RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.CONCEPT_CANDIDATES.name(), true);
		            flowMap.put(FlowStepName.CONCEPT_DESAMBIGUATION.name(), true);
		            flowMap.put(FlowStepName.RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.DBPEDIA.name(), true);
		            flowMap.put(FlowStepName.DBPEDIA_RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.SSYNTS.name(), true);
		            flowMap.put(FlowStepName.DSYNTS.name(), true);
		
		            flowMap.put(FlowStepName.EMOTION.name(), true);
		
		            return flowMap;
		        };
		        break;
    	}
        
        return options;
    }
    
    public static JCas createJCas(String text, String language, AnalysisType analysisType) throws WelcomeException {

        try {
            JCas jCas = JCasFactory.createJCas();

            jCas.setDocumentText(text);

            DocumentMetaData docMetadata = DocumentMetaData.create(jCas);
            docMetadata.setDocumentId("welcome-document");
            if(language != null) {
                docMetadata.setLanguage(language);
            }
            
            IFlowOptions options = WelcomeUIMAUtils.getOptions(analysisType);
        
			FlowUtils.annotateFlowOptions(jCas, options);
            
            return jCas;
            
        } catch (UIMAException|IOException e) {
            throw new WelcomeException("Error found while creating jCas!", e);
        }
    }
    
    public static JCas createJCasFromConll(String conll, String language, AnalysisType analysisType) throws WelcomeException {

        try {
        	CollectionReader readerDesc = CollectionReaderFactory.createReader(
        			ConllUReader.class,
        			ConllUReader.PARAM_SOURCE_LOCATION, "folder/",
        			ConllUReader.PARAM_PATTERNS, "*.txt",
        			ConllUReader.PARAM_LANGUAGE, language);
        	
            JCas jCas = JCasFactory.createJCas();

            try (BufferedReader buffer = new BufferedReader(new StringReader(conll))) {
                ((ConllUReader)readerDesc).convert(jCas, buffer);
            }

            DocumentMetaData docMetadata = DocumentMetaData.create(jCas);
            docMetadata.setDocumentId("welcome-document");
            if(language != null) {
                docMetadata.setLanguage(language);
            }
            
            IFlowOptions options = WelcomeUIMAUtils.getOptions(analysisType);
        
			FlowUtils.annotateFlowOptions(jCas, options);
            
            return jCas;
            
        } catch (UIMAException|IOException | SecurityException | IllegalArgumentException e) {
            throw new WelcomeException("Error found while creating jCas!", e);
        }
    }
    
    public static AnalysisOutput extractOutput(JCas jCas, IAnalysisInput container) throws WelcomeException {
    	try {
    		
    		//DeepAnalysisOutput output = new DeepAnalysisOutput();
    		AnalysisOutput output;
    		
    		switch(container.getMetadata().getOutputLevel()) {
	    		case xmi:
	    			output = OutputGenerator.generateXmiOutput(container.getMetadata(), jCas);
	    			
	    			break;
	    		case demo:
	    			 output = OutputGenerator.generateDemoOutput(container.getData().getConll(), container.getMetadata(), jCas);
	    			
	    			break;
	    		case welcome:
	    			output = OutputGenerator.generateDlaOutput(container.getMetadata(), jCas);
	    			
	    			break;
	    		case demo_welcome:
	    			output = OutputGenerator.generateDemoOutputWithDla(container.getData().getConll(), container.getMetadata(), jCas);
	    			
	    			break;
				default:
					output = new AnalysisOutput<>();
					
					break;
    		}

    		return output;

    	} catch (Exception e) {
    		throw new WelcomeException("Error generating output", e);
    	}
    }
}
