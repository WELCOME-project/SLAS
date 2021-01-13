package edu.upf.taln.welcome.slas.core;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import edu.upf.taln.welcome.slas.core.factories.JCasWelcomeFactory.InputType;

/**
 *
 * @author rcarlini
 */
public class AnalyzerTest {
    
    /**
     * Test of analyze method, of class Analyzer.
     * @throws edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException
     * @throws java.io.IOException
     */
    @Test
    public void testAnalyze() throws WelcomeException, IOException {

        Analyzer analyzer = Analyzer.getInstance();
        ObjectMapper mapper = new ObjectMapper();

        String inputPath = "src/test/resources/sample_input.json";
        DeepAnalysisInput input = mapper.readValue(new File(inputPath), DeepAnalysisInput.class);

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        

        IAnalysisOutput output = analyzer.analyze(InputType.text, AnalysisType.BASIC, input.getData().getText(), OutputLevel.welcome);

        String result = writer.writeValueAsString(output);
        System.out.println(result);
    }    
}
