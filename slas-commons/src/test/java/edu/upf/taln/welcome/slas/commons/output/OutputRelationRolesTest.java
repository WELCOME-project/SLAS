package edu.upf.taln.welcome.slas.commons.output;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.uima.UIMAException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OutputRelationRolesTest {
    
    /**
     * Test of extractRoles method of class OutputGenerator.
     */
	@ParameterizedTest(name = "{0}")
	@MethodSource("rolesTestInput")
    public void testObtainRolesTest(String testName, String morphFeatures, String temporalType, List<String> expResult) {
        
		List<String> result = OutputGenerator.extractRoles(morphFeatures, temporalType);
        
        assertThat(result).containsAll(expResult);
    }
	
	static Stream<Arguments> rolesTestInput() throws UIMAException {
		return Stream.of(
				Arguments.of("EmptyRolesTest", null, null, new ArrayList<>()),
				Arguments.of("TemporalRolesTest", null, "Duration", Arrays.asList("ht:Duration")),
				Arguments.of("FeaturesRolesTest", "fnrole=A1|vnrole=B2|pbrole=C3", null, Arrays.asList("A1","C3","B2")),
				Arguments.of("EmptyFeaturesRolesTest", "", null, new ArrayList<>()));
	}
}
