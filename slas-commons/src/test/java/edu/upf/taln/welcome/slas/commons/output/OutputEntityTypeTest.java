package edu.upf.taln.welcome.slas.commons.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.testing.factory.TokenBuilder;
import org.apache.uima.jcas.JCas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.morph.MorphologicalFeatures;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS_PRON;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.wsd.type.WSDResult;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import edu.upf.taln.flask_wrapper.type.GeolocationCandidate;
import edu.upf.taln.flask_wrapper.type.WSDSpan;
import edu.upf.taln.parser.deep_parser.types.PredArgsToken;

public class OutputEntityTypeTest {
	
	private static JCas jCas;
    
    @BeforeEach
	public void createCas() throws UIMAException {
		jCas = JCasFactory.createJCas();
		TokenBuilder<Token, Sentence> tb = new TokenBuilder<Token, Sentence>(Token.class,
                Sentence.class);
		String text = "Test."; 
		String tokens = "Test ."; 
        tb.buildTokens(jCas, text, tokens);
	}
    
    /**
     * Test of assignEntityType method of class OutputGenerator.
     */
    @SuppressWarnings("unchecked")
	@ParameterizedTest(name = "{0}")
	@MethodSource("entityTypesTestInput")
    public void testAssignEntityType(String testName, PredArgsToken token, Map<String, List<?>> AnnotationLists, String expResult) {
        System.out.println("assignEntityType");
        
        List<Token> tokensList = (List<Token>) AnnotationLists.get("token");
        List<NamedEntity> neList = (List<NamedEntity>) AnnotationLists.get("ne");
        List<Timex3> heideltimeList = (List<Timex3>) AnnotationLists.get("heideltime");
        List<GeolocationCandidate> geolocationsList = (List<GeolocationCandidate>) AnnotationLists.get("geolocation");
        List<WSDResult> wsdList = (List<WSDResult>) AnnotationLists.get("wsd");
        List<WSDSpan> conceptsList = (List<WSDSpan>) AnnotationLists.get("concept");
        
        
        String result = OutputGenerator.assignEntityType(token, tokensList, neList, heideltimeList,
    			geolocationsList, wsdList, conceptsList, null);
        
        assertEquals(expResult, result);
    }
    
	static Stream<Arguments> entityTypesTestInput() throws UIMAException {
		
		/*
		 * Collection<PredArgsDependency> relationCollection
		 */
		
		//Surface and PredArgs tokens with PoS and morphological features.
		PredArgsToken predArgsTokenAnnotation = new PredArgsToken(jCas, 0, 4);
		predArgsTokenAnnotation.addToIndexes();
		Token tokenAnnotation = new Token(jCas, 0, 4);
		POS_PRON prp = new POS_PRON(jCas);
		prp.addToIndexes();
		prp.setPosValue("PRP");
		tokenAnnotation.setPos(prp);
		MorphologicalFeatures morph = new MorphologicalFeatures(jCas);
		morph.setValue("Person=1");
		morph.addToIndexes();
		tokenAnnotation.setMorph(morph);
		tokenAnnotation.addToIndexes();
		
		//Annotations lists
		Map<String, List<?>> lists = new HashMap<>();
		List<Token> tokensList = new ArrayList<>();
		lists.put("token", tokensList);
		List<NamedEntity> neList = new ArrayList<>();
		lists.put("ne", neList);
		List<Timex3> heideltimeList = new ArrayList<>();
		lists.put("heideltime", heideltimeList);
		List<GeolocationCandidate> geolocationsList = new ArrayList<>();
		lists.put("geolocation", geolocationsList);
		List<WSDResult> wsdList = new ArrayList<>();
		lists.put("wsd", wsdList);
		List<WSDSpan> conceptsList = new ArrayList<>();
		lists.put("concept", conceptsList);
		
		return Stream.of(
				Arguments.of("SpeakerType", tokenAnnotation, lists, "Speaker"),
				Arguments.of("AddresseeType", tokenAnnotation, lists, "Addressee"),
				Arguments.of("TimeType", tokenAnnotation, lists, "Time"),
				Arguments.of("LocationType", tokenAnnotation, lists, "Location"),
				Arguments.of("NEType", tokenAnnotation, lists, "NE"),
				Arguments.of("PredicateType", tokenAnnotation, lists, "Predicate"),
				Arguments.of("ConceptType", tokenAnnotation, lists, "Concept"),
				Arguments.of("UnknownType", tokenAnnotation, lists, "Unknown"));
	}
}
