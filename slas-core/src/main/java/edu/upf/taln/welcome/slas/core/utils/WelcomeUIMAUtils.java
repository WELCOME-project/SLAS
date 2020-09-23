package edu.upf.taln.welcome.slas.core.utils;

import java.util.HashMap;
import java.util.Map;

import edu.upf.taln.uima.flow.IFlowOptions;
import edu.upf.taln.welcome.slas.commons.analysis.FlowStepName;


/**
 *
 * @author rcarlini
 */
public class WelcomeUIMAUtils {
    
    public enum AnalysisType { BASIC, FULL, DEFAULT, TEST }
    
    public static IFlowOptions getOptions(AnalysisType type) {
        
        // TODO: Define what components should be enable for BASIC analysis
    	IFlowOptions options;
    	switch(type) {
	    	
    		case TEST:
		        options = () -> {
		            Map<String, Boolean> flowMap = new HashMap<>();
		
		            flowMap.put(FlowStepName.PARSING.name(), false);
		
		            flowMap.put(FlowStepName.NER.name(), false);
		            flowMap.put(FlowStepName.NER_RETOKENIZER.name(), false);
		
		            flowMap.put(FlowStepName.CONCEPT_CANDIDATES.name(), false);
		            flowMap.put(FlowStepName.CONCEPT_DESAMBIGUATION.name(), false);
		            flowMap.put(FlowStepName.RETOKENIZER.name(), false);
		
		            flowMap.put(FlowStepName.DBPEDIA.name(), false);
		            flowMap.put(FlowStepName.DBPEDIA_RETOKENIZER.name(), false);
		
		            flowMap.put(FlowStepName.SSYNTS.name(), false);
		            flowMap.put(FlowStepName.DSYNTS.name(), false);
		
		            flowMap.put(FlowStepName.EMOTION.name(), false);
		
		            return flowMap;
		        };
		        break;
	    	case DEFAULT:
		        options = () -> {
		            Map<String, Boolean> flowMap = new HashMap<>();
		
		            flowMap.put(FlowStepName.PARSING.name(), false);
		
		            flowMap.put(FlowStepName.NER.name(), true);
		            flowMap.put(FlowStepName.NER_RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.CONCEPT_CANDIDATES.name(), true);
		            flowMap.put(FlowStepName.CONCEPT_DESAMBIGUATION.name(), true);
		            flowMap.put(FlowStepName.RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.DBPEDIA.name(), true);
		            flowMap.put(FlowStepName.DBPEDIA_RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.SSYNTS.name(), false);
		            flowMap.put(FlowStepName.DSYNTS.name(), true);
		
		            flowMap.put(FlowStepName.EMOTION.name(), true);
		
		            return flowMap;
		        };
		        break;
	    	default:
	    	case FULL:
		        options = () -> {
		            Map<String, Boolean> flowMap = new HashMap<>();
		
		            flowMap.put(FlowStepName.PARSING.name(), true);
		
		            flowMap.put(FlowStepName.NER.name(), true);
		            flowMap.put(FlowStepName.NER_RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.CONCEPT_CANDIDATES.name(), true);
		            flowMap.put(FlowStepName.CONCEPT_DESAMBIGUATION.name(), true);
		            flowMap.put(FlowStepName.RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.DBPEDIA.name(), true);
		            flowMap.put(FlowStepName.DBPEDIA_RETOKENIZER.name(), true);
		
		            flowMap.put(FlowStepName.SSYNTS.name(), true);
		            flowMap.put(FlowStepName.DSYNTS.name(), true);
		
		            flowMap.put(FlowStepName.EMOTION.name(), true);
		
		            return flowMap;
		        };
		        break;
    	}
        
        return options;
    }
    
}
