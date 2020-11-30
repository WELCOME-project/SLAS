package edu.upf.taln.welcome.slas.core.factories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import org.dkpro.core.io.conll.ConllUReader;
import edu.upf.taln.uima.flow.IFlowOptions;
import edu.upf.taln.uima.flow.utils.FlowUtils;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.core.utils.WelcomeUIMAUtils;
import edu.upf.taln.welcome.slas.core.utils.WelcomeUIMAUtils.AnalysisType;

public class JCasWelcomeFactory {
	public static enum InputType {conll, text}; 
	
	public static JCas createJCas(InputType type, String text, String language, AnalysisType analysisType) throws WelcomeException {

        try {
        	JCas jCas = JCasFactory.createJCas();
        	
        	switch (type) {
	        	case conll:
	        		CollectionReader readerDesc = CollectionReaderFactory.createReader(
	            			ConllUReader.class,
	            			ConllUReader.PARAM_SOURCE_LOCATION, "folder/",
	            			ConllUReader.PARAM_PATTERNS, "*.txt",
	            			ConllUReader.PARAM_LANGUAGE, language);
	            	
	                
	
	                try (BufferedReader buffer = new BufferedReader(new StringReader(text))) {
	                    ((ConllUReader)readerDesc).convert(jCas, buffer);
	                }
	                
	        		break;
	        	default:
	        	case text:
	        		jCas.setDocumentText(text);
	        		
	        		break;
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
}
