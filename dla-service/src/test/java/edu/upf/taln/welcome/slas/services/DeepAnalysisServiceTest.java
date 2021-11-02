package edu.upf.taln.welcome.slas.services;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.output.AnalysisOutputImpl;

/**
 *
 * @author rcarlini
 */
public class DeepAnalysisServiceTest {
    
    /**
     * Base test to check outputs
     * @param input
     * @param expected
     * @throws java.lang.Exception
     */
    public void testSample(DeepAnalysisInput input, File expected) throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        DeepAnalysisService instance = new DeepAnalysisService();
        
        String expResult = FileUtils.readFileToString(expected, "utf-8");
        AnalysisOutputImpl output = (AnalysisOutputImpl) instance.analyze(input);
        String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
        assertEquals(expResult, result);
    }
    
}
