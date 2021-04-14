package edu.upf.taln.welcome.slas.commons.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.uima.jcas.JCas;
import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.JCasFactory;

import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiReader;

import edu.upf.taln.welcome.slas.commons.output.welcome.DlaResult;

/**
 *
 * @author rcarlini
 */
public class OutputGeneratorTest {

	private static JCas getJCasFromXMI(String language, File testFile, File typesystemFile) throws UIMAException, IOException {

        String typeSystemPath = typesystemFile.getAbsolutePath();
		String inputFolderPath = testFile.getParentFile().getAbsolutePath();
		String pattern = testFile.getName();
		
		CollectionReader reader = createReader(
                XmiReader.class,
                XmiReader.PARAM_TYPE_SYSTEM_FILE, typeSystemPath,
                XmiReader.PARAM_SOURCE_LOCATION, inputFolderPath,
                XmiReader.PARAM_LANGUAGE, language,
                XmiReader.PARAM_PATTERNS, new String[] { pattern },
                XmiReader.PARAM_OVERRIDE_DOCUMENT_METADATA, true);

	    JCas jcas = JCasFactory.createJCasFromPath(typeSystemPath);
	    reader.getNext(jcas.getCas());
	    
		return jcas;
	}
    
    public OutputGeneratorTest() {
    }

    
    /**
     * Test of generateMetadata method, of class OutputGenerator.
     */
    @Test
    public void testGenerateMetadata() {
        System.out.println("generateMetadata");
        AnalysisOutputMetadata expResult = null;
        AnalysisOutputMetadata result = OutputGenerator.generateMetadata();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateXmiResult method, of class OutputGenerator.
     */
    @Test
    public void testGenerateXmiResult() {
        System.out.println("generateXmiResult");
        JCas jCas = null;
        XmiResult expResult = null;
        XmiResult result = OutputGenerator.generateXmiResult(jCas);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateDlaResult method, of class OutputGenerator.
     */
    @Test
    public void testGenerateDlaResult() throws UIMAException, IOException {
        System.out.println("generateDlaResult");
        JCas jCas = getJCasFromXMI("en", new File("src/test/resources/welcome/reception_service.xmi"), new File("src/test/resources/welcome/TypeSystem.xml"));
        
        //DlaResult expResult = null;
        DlaResult result = OutputGenerator.generateDlaResult(jCas);
        //assertEquals(expResult, result);
        
        ObjectWriter writter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String jsonResult = writter.writeValueAsString(result);
        System.out.println(jsonResult);
    }

    /**
     * Test of generateDemoResult method, of class OutputGenerator.
     */
    @Test
    public void testGenerateDemoResult() {
        System.out.println("generateDemoResult");
        JCas jCas = null;
        WelcomeDemoResult expResult = null;
        WelcomeDemoResult result = OutputGenerator.generateDemoResult(jCas);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateDlaOutput method, of class OutputGenerator.
     */
    @Test
    public void testGenerateDlaOutput() {
        System.out.println("generateDlaOutput");
        JCas jCas = null;
        DeepAnalysisOutput expResult = null;
        DeepAnalysisOutput result = OutputGenerator.generateDlaOutput(jCas);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateDemoOutput method, of class OutputGenerator.
     */
    @Test
    public void testGenerateDemoOutput() {
        System.out.println("generateDemoOutput");
        JCas jCas = null;
        AnalysisOutputImpl<WelcomeDemoResult, AnalysisOutputMetadata> expResult = null;
        AnalysisOutputImpl<WelcomeDemoResult, AnalysisOutputMetadata> result = OutputGenerator.generateDemoOutput(jCas);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateDemoOutputWithDla method, of class OutputGenerator.
     */
    @Test
    public void testGenerateDemoOutputWithDla() {
        System.out.println("generateDemoOutputWithDla");
        JCas jCas = null;
        AnalysisOutputImpl<WelcomeDemoResult, AnalysisOutputMetadata> expResult = null;
        AnalysisOutputImpl<WelcomeDemoResult, AnalysisOutputMetadata> result = OutputGenerator.generateDemoOutputWithDla(jCas);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateXmiOutput method, of class OutputGenerator.
     */
    @Test
    public void testGenerateXmiOutput() {
        System.out.println("generateXmiOutput");
        JCas jCas = null;
        XmiResult expResult = null;
        XmiResult result = OutputGenerator.generateXmiOutput(jCas);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateDummyResponse method, of class OutputGenerator.
     */
    @Test
    public void testGenerateDummyResponse() {
        System.out.println("generateDummyResponse");
        JCas jcas = null;
        DeepAnalysisOutput expResult = null;
        DeepAnalysisOutput result = OutputGenerator.generateDummyResponse(jcas);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
