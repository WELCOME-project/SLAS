package edu.upf.taln.welcome.slas.core;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import edu.upf.taln.flask_wrapper.type.SpeechAct;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.output.IAnalysisOutput;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.AssertTrue;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.CAS;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.CasIOUtils;
import org.junit.Assert;

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
        
        IAnalysisOutput output = analyzer.analyze(input);

        String result = writer.writeValueAsString(output);
        System.out.println(result);
    }
    
    /**
     * Test of analyze method, of class Analyzer.
     * @throws edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException
     * @throws java.io.IOException
     */
    @Test
    public void testAnalyzePreprocess() throws WelcomeException, IOException {

        Analyzer analyzer = Analyzer.getInstance();
        ObjectMapper mapper = new ObjectMapper();

        String inputPath = "src/test/resources/only_preprocess.json";
        DeepAnalysisInput input = mapper.readValue(new File(inputPath), DeepAnalysisInput.class);

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        
        IAnalysisOutput output = analyzer.analyze(input);

        String result = writer.writeValueAsString(output);
        System.out.println(result);
    }    

    /**
     * Test of analyze method, of class Analyzer.
     * @throws edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException
     * @throws java.io.IOException
     */
    @Test
    public void testAnalyzeTaxonomy() throws WelcomeException, IOException {

        Analyzer analyzer = Analyzer.getInstance();
        ObjectMapper mapper = new ObjectMapper();

        String inputPath = "src/test/resources/sample_input_taxonomy.json";
        DeepAnalysisInput input = mapper.readValue(new File(inputPath), DeepAnalysisInput.class);

        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        
        IAnalysisOutput output = analyzer.analyze(input);

        String result = writer.writeValueAsString(output);
        System.out.println(result);
    }    

	@Test
	public void testPostprocess() throws UIMAException, IOException {

		JCas jCas = JCasFactory.createJCas();
		
		String path = "src/test/resources/speechact_fix/document.xmi";
		InputStream casInputStream = new FileInputStream(path);
		CasIOUtils.load(casInputStream, jCas.getCas());
		
		Analyzer.postprocess(jCas);
		
		List<String> expected = Arrays.asList(new String[]{"Hedge", "Acknowledge (Backchannel)", "Hedge", "Agree/Accept"});
		
		List<Sentence> sentences = new ArrayList(JCasUtil.select(jCas, Sentence.class));
		for (int idx=0; idx<sentences.size(); idx++) {
			Sentence sentence = sentences.get(idx);
			
			List<SpeechAct> speechActs = JCasUtil.selectCovered(SpeechAct.class, sentence);
			Assert.assertEquals(1, speechActs.size());
			
			SpeechAct speechAct = speechActs.get(0);
			Assert.assertEquals(expected.get(idx), speechAct.getLabel());
		}
	}
}
