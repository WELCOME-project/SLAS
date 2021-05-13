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
import edu.upf.taln.welcome.slas.commons.output.XmiResult;

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
        String text = "I just arrived in Catalonia. In Barcelona, one week ago.";
        text = "Yes, I would like to apply for the First Reception Service.";
        text = "Currently, I live with my friends, I mean I don’t have an own apartment.";
        text = "Currently, I live with my friends, I mean I don’t have an own apartment. \n" +
            "I just arrived in Catalonia. \n" +
            "In Barcelona, one week ago. \n" +
            "I want to make an appointment with the jobcenter.  The employment office. \n" +
            "I already talked to Mr. Codina. I talked to him on Friday. \n" +
            "I want to make an appointment with the job... jobcenter. \n" +
            "I already talked to Mr. Serra. No, it was Mr. Codina. \n" +
            "I already talked to Mr. Sierra… Serra. \n";
        
        InputMetadata metadata = new InputMetadata();
        metadata.setAnalysisType(AnalysisType.FULL);
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
        //System.out.println(jsonStr);
        
        XmiResult output = om.readValue(jsonStr, XmiResult.class);
        System.out.println(output.getTypesystem());
        System.out.println(output.getXmi());
    }
    
}
