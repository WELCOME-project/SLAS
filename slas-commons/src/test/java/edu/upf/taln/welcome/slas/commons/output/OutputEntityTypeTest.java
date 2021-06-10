package edu.upf.taln.welcome.slas.commons.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class OutputEntityTypeTest {
    
    /**
     * Test of assignEntityType method of class OutputGenerator.
     * @param pos 
     * @param morph 
     * @param isNamedEntity 
     * @param namedEntity 
     * @param isHeidelTime 
     * @param isGeolocation 
     * @param isWSDNamedEntity 
     * @param isPredicate 
     * @param isConcept 
     */
	@ParameterizedTest(name = "{0}")
	@CsvSource({ 
		"SpeakerTypePRPTest,PRP,Person=1,false,,false,false,false,false,false,Speaker",
		"SpeakerTypePRP$Test,PRP$,Person=1,false,,false,false,false,false,false,Speaker",
		"AddresseeTypePRPTest,PRP,Person=2,false,,false,false,false,false,false,Addressee",
		"AddresseeTypePRP$Test,PRP$,Person=2,false,,false,false,false,false,false,Addressee",
		"NamedEntityTypeTest,NN,,true,Animal,false,false,false,false,false,Animal",
		"HeideltimeTypeTest,NN,,false,,true,false,false,false,false,Temporal",
		"GeolocationTypeTest,NN,,false,,false,true,false,false,false,Location",
		"WSDNamedEntityTypeTest,NN,,false,,false,false,true,false,false,NE",
		"PredicateTypeTest,NN,,false,,false,false,false,true,false,Predicate",
		"ConceptTypeTest,NN,,false,,false,false,false,false,true,Concept",
		"UnknownTypeTest,NN,,false,,false,false,false,false,false,Unknown"
	})
    public void testAssignEntityType(String testName, String pos, String morph, boolean isNamedEntity, String namedEntity, boolean isHeidelTime, 
    		boolean isGeolocation, boolean isWSDNamedEntity, boolean isPredicate, boolean isConcept, String expResult) {
        
        EntityTypeInfo typeInfo = new EntityTypeInfo();
        typeInfo.pos = pos;
        typeInfo.morph = morph;
        typeInfo.isNamedEntity = isNamedEntity;
        typeInfo.namedEntity = namedEntity;
        typeInfo.isHeidelTime = isHeidelTime;
        typeInfo.isGeolocation = isGeolocation;
        typeInfo.isWSDNamedEntity = isWSDNamedEntity;
        typeInfo.isPredicate = isPredicate;
        typeInfo.isConcept = isConcept;
        typeInfo.assignType();
        
        String result = typeInfo.getType();
        
        assertEquals(expResult, result);
    }
}
