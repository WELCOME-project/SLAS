package edu.upf.taln.welcome.slas.commons.output;

import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.dkpro.core.io.xmi.XmiReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.upf.taln.welcome.slas.commons.output.welcome.DlaResult;

/**
 *
 * @author rcarlini
 */
public class OutputGeneratorTest {

	private static JCas getJCasFromXMI(String language, File testFile, File typesystemFile) throws UIMAException, IOException {

        String typeSystemPath = typesystemFile.getPath();
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
    @Disabled("Disabled until implemented!")
    @Test
    public void testGenerateMetadata() {
        System.out.println("generateMetadata");
        AnalysisOutputMetadata expResult = null;
        AnalysisOutputMetadata result = OutputGenerator.generateMetadata(null); //TODO: needs jCAS as argument
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateXmiResult method, of class OutputGenerator.
     */
    @Disabled("Disabled until implemented!")
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
     * @throws org.apache.uima.UIMAException
     * @throws java.io.IOException
     */
    @Test
    public void testGenerateDlaResult() throws UIMAException, IOException {
        System.out.println("generateDlaResult");
        String fileName = "reception_service.xmi";
        //fileName = "one_space.xmi";
        //fileName = "one_space.xmi";
        //fileName = "prototype1_sentences.xmi";
        //fileName = "new_predarg_relations.xmi";
        
        String basePath = "src/test/resources/welcome/";
        File typesystem = new File(basePath, "TypeSystem.xml");
        File xmi = new File(basePath, fileName);
        
        JCas jCas = getJCasFromXMI("en", xmi, typesystem);
        
        DlaResult result = OutputGenerator.generateDlaResult(jCas);
        //List<DlaResult> result = OutputGenerator.generateDlaResultBySentence(jCas);
        
        ObjectWriter writter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String jsonResult = writter.writeValueAsString(result);
        System.out.println(jsonResult);

//        File expected = new File(basePath, "expected_dla.json");
//        String expResult = FileUtils.readFileToString(expected, "utf8");
//        assertEquals(expResult, jsonResult);
    }

    /**
     * Test of generateDemoResult method, of class OutputGenerator.
     * @throws org.apache.uima.UIMAException
     * @throws java.io.IOException
     */
    @Test
    public void testGenerateDemoResult() throws UIMAException, IOException {
        System.out.println("generateDemoResult");
        
        String basePath = "src/test/resources/welcome/";
        File typesystem = new File(basePath, "TypeSystem.xml");
        File xmi = new File(basePath, "one_space.xmi");
        
        JCas jCas = getJCasFromXMI("en", xmi, typesystem);
        
        WelcomeDemoResult result = OutputGenerator.generateDemoResult(jCas);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writter = mapper.writerWithDefaultPrettyPrinter();
        String jsonResult = writter.writeValueAsString(result);
//        System.out.println(jsonResult);

        File expected = new File(basePath, "one_space.json");
        String expResult = FileUtils.readFileToString(expected, "utf8");
        assertEquals(expResult, jsonResult);
    }

    /**
     * Test of generateDlaOutput method, of class OutputGenerator.
     */
    @Disabled("Disabled until implemented!")
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
    public void testGenerateDemoOutput() throws UIMAException, IOException {
        System.out.println("generateDemoOutput");
        
        String basePath = "src/test/resources/welcome/";
        File typesystem = new File(basePath, "TypeSystem.xml");
        File xmi = new File(basePath, "currently.xmi");
        
        JCas jCas = getJCasFromXMI("en", xmi, typesystem);
        
        AnalysisOutputImpl<WelcomeDemoResult, AnalysisOutputMetadata> result = OutputGenerator.generateDemoOutput(jCas);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writter = mapper.writerWithDefaultPrettyPrinter();
        String jsonResult = writter.writeValueAsString(result);
        System.out.println(jsonResult);

        File expected = new File(basePath, "currently.json");
        String expResult = FileUtils.readFileToString(expected, "utf8");
        //assertEquals(expResult, jsonResult);
    }

    /**
     * Test of generateDemoOutputWithDla method, of class OutputGenerator.
     */
    @Disabled("Disabled until implemented!")
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
    @Disabled("Disabled until implemented!")
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
    @Disabled("Disabled until implemented!")
    @Test
    public void testGenerateDummyResponse() throws UIMAException {
        System.out.println("generateDummyResponse");
        JCas jcas = JCasFactory.createText("This is a test sentence.");
        DeepAnalysisOutput expResult = null;
        DeepAnalysisOutput result = OutputGenerator.generateDummyResponse(jcas);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
