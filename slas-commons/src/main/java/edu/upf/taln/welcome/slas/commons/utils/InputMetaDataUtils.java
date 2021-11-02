package edu.upf.taln.welcome.slas.commons.utils;

import java.util.List;

import org.apache.uima.jcas.JCas;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upf.taln.common.uima.ExtraMetaDataUtils;

import edu.upf.taln.welcome.slas.commons.input.InputMetadata;

public class InputMetaDataUtils  extends ExtraMetaDataUtils<InputMetadata> {
    
    @Override
    public InputMetadata getMetadata(JCas jcas) throws Exception {
        
        List<String> metadataJsons = getMetaDataJson(jcas, InputMetadata.class.getCanonicalName());
        
        InputMetadata metadata = null;
        if (metadataJsons.size() > 1) {
            throw new Exception("More than one ExtraMetaData found!");
            
        } else if (metadataJsons.size() == 1) {
            String jsonStr = metadataJsons.get(0);
            
            ObjectMapper mapper = new ObjectMapper();
            metadata = mapper.readValue(jsonStr, InputMetadata.class);
        }
        return metadata;
    }    
}
