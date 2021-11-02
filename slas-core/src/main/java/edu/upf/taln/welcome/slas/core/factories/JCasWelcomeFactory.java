package edu.upf.taln.welcome.slas.core.factories;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.unihd.dbs.uima.types.heideltime.Dct;
import edu.upf.taln.uima.flow.IFlowOptions;
import edu.upf.taln.uima.flow.utils.FlowUtils;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputData;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.commons.utils.InputMetaDataUtils;
import edu.upf.taln.welcome.slas.core.utils.WelcomeUIMAUtils;

public class JCasWelcomeFactory {
    
	public static enum InputType {conll, text}; 
	
	public static JCas createJCas(DeepAnalysisInput input) throws WelcomeException {

        try {
        	
            InputMetadata metadata = input.getMetadata();
            InputData data = input.getData();
            String text = data.getText();
            String language = metadata.getLanguage();
            AnalysisType analysisType = metadata.getAnalysisType();
            
        	JCas jCas = JCasFactory.createJCas();
        	if (text == null || text.equals("")) {
        		throw new WelcomeException("Error creating jCas: Input text is null or empty.");
        	}
            jCas.setDocumentText(text);
        	            
            // metadata.setUseCase("catalonia");
            
            // Adds ExtraMetaData
            InputMetaDataUtils utils = new InputMetaDataUtils();
            utils.addMetaData(jCas, metadata);
                    	
            DocumentMetaData docMetadata = DocumentMetaData.create(jCas);
            docMetadata.setDocumentId("welcome-document");
            if(language != null) {
                docMetadata.setLanguage(language);
            }
            
            //Setting current date in the cas as a Dct annotation
            SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd");
            Date date = new Date(System.currentTimeMillis());
            Dct documentDate = new Dct(jCas);
            documentDate.setValue(formatter.format(date));
            documentDate.addToIndexes();
            
            IFlowOptions options = WelcomeUIMAUtils.getOptions(analysisType);
			FlowUtils.annotateFlowOptions(jCas, options);
            
            return jCas;
            
        } catch (UIMAException|IOException e) {
            throw new WelcomeException("Error found while creating jCas!", e);
        }
    }
}
