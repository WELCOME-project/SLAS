package edu.upf.taln.welcome.slas.commons.utils;

import java.io.IOException;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upf.taln.common.uima.ExtraMetaData;

public class WelcomeExtraMetadataUtils {
	public static void addUseCase(JCas jCas, String useCase) throws JsonProcessingException {
        
        WelcomeExtraMetadataJson extra = new WelcomeExtraMetadataJson();
        extra.setUseCase(useCase);
        
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(extra);
        String qname = extra.getClass().getCanonicalName();
        
        ExtraMetaData metadataAnn;
        try {
        	metadataAnn = JCasUtil.selectSingle(jCas, ExtraMetaData.class);
        	if (metadataAnn.getQualifiedName().equals(extra.getClass().getCanonicalName())) {
        	}else {
        		//Existent ExtraMetaData with a different json format!
        		//Overriding ExtraMetaData
        		metadataAnn.setQualifiedName(qname);
        	}
        	metadataAnn.setJson(jsonStr);
        	 
        } catch(IllegalArgumentException iae) {
            // No extra metadata 
        	int begin = 0;
        	int end = jCas.getDocumentText().length();
        	metadataAnn = new ExtraMetaData(jCas, begin, end);
            metadataAnn.setJson(jsonStr);
            metadataAnn.setQualifiedName(qname);
            metadataAnn.addToIndexes();
        }
        
    }
    
    public static String getUseCase(JCas jCas) throws JsonProcessingException, IOException {
        
        try {
            ExtraMetaData metadataAnn = JCasUtil.selectSingle(jCas, ExtraMetaData.class);

            ObjectMapper mapper = new ObjectMapper();
            WelcomeExtraMetadataJson extra = mapper.readValue(metadataAnn.getJson(), WelcomeExtraMetadataJson.class);

            return extra.getUseCase();
            
        } catch(IllegalArgumentException iae) {
            // No extra metadata (or worse, more than one!)
            return null;
        }
    }
}
