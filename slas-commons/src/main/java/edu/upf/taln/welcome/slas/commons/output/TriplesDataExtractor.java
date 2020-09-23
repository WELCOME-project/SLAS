package edu.upf.taln.welcome.slas.commons.output;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import edu.upf.taln.parser.deep_parser.types.TriplesDependency;
import edu.upf.taln.parser.deep_parser.types.TriplesToken;
import edu.upf.taln.uima.buddy.core.BuddyPayloadUtils;
import edu.upf.taln.uima.buddy.types.BuddyRelation;
import edu.upf.taln.uima.buddy.types.BuddyToken;


public class TriplesDataExtractor {
    
    public static List<String[]> extractBuddyTriples(JCas jCas, String grammarIdx) {
        
        //HashMap<String, String> triples = new HashMap<>();
        List<String[]> triples = new ArrayList<>();
		for(Sentence sentence : JCasUtil.select(jCas, Sentence.class)){
			
			for(BuddyRelation buddyRelation : JCasUtil.selectCovered(BuddyRelation.class, sentence)) {
                
                BuddyToken sourceToken = buddyRelation.getSource();
				if (sourceToken != null && grammarIdx.equals(sourceToken.getGrammar())) {
                    // Is not root and is the grammar we are looking for
					try {
	                    Map<String, String> sourcePayload = BuddyPayloadUtils.deserialize(sourceToken.getPayload());
	                    
	                    BuddyToken targetToken = buddyRelation.getTarget();
	                    Map<String, String> targetPayload = BuddyPayloadUtils.deserialize(targetToken.getPayload());
	                    
	                    triples.add(new String[]{sourcePayload.get("sem"), targetPayload.get("sem")});
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Warning: Unable to parse Buddy payload data.");
					}
				}
			}
		}
        
        return triples;
    }    
    
    public static List<String[]> extractTriples(JCas jCas) {
        
        List<String[]> triples = new ArrayList<>();
		for(Sentence sentence : JCasUtil.select(jCas, Sentence.class)){
			
			for(TriplesDependency buddyRelation : JCasUtil.selectCovered(TriplesDependency.class, sentence)) {
                
				TriplesToken sourceToken = (TriplesToken) buddyRelation.getGovernor();
				TriplesToken targetToken = (TriplesToken) buddyRelation.getDependent();
				if (sourceToken != targetToken) { //Is not ROOT dependency
					triples.add(new String[]{sourceToken.getValue(), targetToken.getValue()});
				}
			}
		}
        
        return triples;
    }
}
