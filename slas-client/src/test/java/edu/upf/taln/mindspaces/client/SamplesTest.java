package edu.upf.taln.mindspaces.client;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;
import edu.upf.taln.welcome.slas.client.ClientConfiguration;
import edu.upf.taln.welcome.slas.client.Processor;
import edu.upf.taln.welcome.slas.commons.input.OutputType;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;

/**
 *
 * @author rcarlini
 */
public class SamplesTest {
    
    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testSpaces() throws Exception {
        String url = "http://welcome-dla.p.taln.ml/dla-service/";
        String text = "I just arrived in Catalonia.In Barcelona, one week ago.";
        
        InputMetadata metadata = new InputMetadata();
        metadata.setAnalysisType(AnalysisType.PRE_DEEP);
        metadata.setLanguage("en");
        metadata.setOutputType(OutputType.xmi);
        metadata.setUseCase("catalonia");
        
        ClientConfiguration config = new ClientConfiguration();
        config.setMetadata(metadata);
        config.setServiceURL(url);
        
        Processor processor = new Processor(config);
        IAnalysisOutput result = processor.execute(text);
        
        ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String jsonStr = om.writeValueAsString(result);
        System.out.println(jsonStr);
    }
    
}
