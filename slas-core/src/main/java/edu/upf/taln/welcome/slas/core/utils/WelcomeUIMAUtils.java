package edu.upf.taln.welcome.slas.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.io.conll.ConllUReader;

import edu.upf.taln.uima.flow.IFlowOptions;
import edu.upf.taln.uima.flow.utils.FlowUtils;

import edu.upf.taln.welcome.slas.commons.analysis.FlowStepName;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.output.DeepAnalysisOutput;


/**
 *
 * @author rcarlini
 */
public class WelcomeUIMAUtils {
    
    public enum AnalysisType { BASIC, FULL, DEFAULT }
    
    public static IFlowOptions getOptions(AnalysisType type) {
        
        // TODO: Define what components should be enable for BASIC analysis
    	IFlowOptions options;
    	switch(type) {
	    	
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
		
		            flowMap.put(FlowStepName.SSYNTS.name(), true);
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
            JCas jCas = JCasFactory.createJCas();

            try (BufferedReader buffer = new BufferedReader(new StringReader(conll))) {
                ConllUReader reader = new ConllUReader();
                reader.convert(jCas, buffer);
            }

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
    
    public static DeepAnalysisOutput extractOutput(JCas jCas) {
        DeepAnalysisOutput output = new DeepAnalysisOutput();
        
        return output;
    }
}
