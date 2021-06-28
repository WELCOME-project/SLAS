package edu.upf.taln.welcome.slas.commons.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.unihd.dbs.uima.types.heideltime.Timex3;
import edu.upf.taln.welcome.slas.commons.output.welcome.Entity.TemporalAnalysis;

public class OutputTemporalAnalysisTest {
    
	@ParameterizedTest(name = "{0}")
	@CsvSource({ 
		"AllParamsTest,DURATION,PT20M,MORE_THAN,DURATION,PT20M,MORE_THAN",
		"NoModifierTest,DURATION,PT20M,,DURATION,PT20M,",
		"NoValueTest,DURATION,,,DURATION,,",
		"NoTypeTest,,,,UNKNOWN,,"
	})
    public void testTemporalAnalysisInicialization(String testName, String type, String value, String modifier, String expType, String expValue, String expModifier) throws UIMAException {
		JCas jcas = JCasFactory.createJCas();
		Timex3 timex = new Timex3(jcas);
		timex.setTimexType(type);
		timex.setTimexValue(value);
		timex.setTimexMod(modifier);
		timex.addToIndexes();
		
		TemporalAnalysis tempAnalysis = new TemporalAnalysis(timex);
		
        assertEquals(expType, tempAnalysis.getType());
        assertEquals(expValue, tempAnalysis.getValue());
        assertEquals(expModifier, tempAnalysis.getModifier());
    }
}
