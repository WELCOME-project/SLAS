package edu.upf.taln.welcome.slas.core.utils;

import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import java.util.HashMap;
import java.util.Map;

import edu.upf.taln.uima.flow.IFlowOptions;
import edu.upf.taln.welcome.slas.commons.analysis.FlowStepName;
import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;

/**
 *
 * @author rcarlini
 */
public class WelcomeUIMAUtils {

    public static Map<String, Boolean> createMap(boolean defaultValue) {
        Map<String, Boolean> flowMap = new HashMap<>();

        flowMap.put(FlowStepName.PARSING.name(),                		defaultValue);
        
        flowMap.put(FlowStepName.NUMBER_SEQUENCE.name(),				defaultValue);
        flowMap.put(FlowStepName.NUMBER_SEQUENCE_RETOKENIZER.name(),	defaultValue);

        flowMap.put(FlowStepName.HEIDELTIME.name(),             		defaultValue);
        flowMap.put(FlowStepName.HEIDELTIME_RETOKENIZER.name(),			defaultValue);

        flowMap.put(FlowStepName.TAXONOMY.name(),						defaultValue);
        flowMap.put(FlowStepName.TAXONOMY_RETOKENIZER.name(),			defaultValue);

        flowMap.put(FlowStepName.NER.name(),							defaultValue);
        flowMap.put(FlowStepName.NER_RETOKENIZER.name(),				defaultValue);

        flowMap.put(FlowStepName.GEOLOCATION.name(),					defaultValue);
        flowMap.put(FlowStepName.GEOLOCATION_RETOKENIZER.name(),		defaultValue);

        flowMap.put(FlowStepName.SPEECHACT.name(),						defaultValue);

        flowMap.put(FlowStepName.CONCEPT_CANDIDATES.name(),				defaultValue);
        flowMap.put(FlowStepName.CONCEPT_DESAMBIGUATION.name(),			defaultValue);
        flowMap.put(FlowStepName.RETOKENIZER.name(),					defaultValue);

        flowMap.put(FlowStepName.DBPEDIA.name(),						defaultValue);
        flowMap.put(FlowStepName.DBPEDIA_RETOKENIZER.name(),			defaultValue);

        flowMap.put(FlowStepName.SSYNTS.name(),							defaultValue);
        flowMap.put(FlowStepName.DSYNTS.name(),							defaultValue);

        flowMap.put(FlowStepName.EMOTION.name(),						defaultValue);
        
        return flowMap;
    }
    
	public static IFlowOptions getOptions(AnalysisType type) throws WelcomeException {

		// TODO: Define what components should be enable for BASIC analysis
		IFlowOptions options;
		switch (type) {

		case TEST:
		case PREPROCESS:
			options = () -> {
				Map<String, Boolean> flowMap = createMap(false);

				flowMap.put(FlowStepName.PARSING.name(), true);

				return flowMap;
			};
			break;
        
		case PRE_DEEP:
			options = () -> {
				Map<String, Boolean> flowMap = createMap(true);

				flowMap.put(FlowStepName.HEIDELTIME_RETOKENIZER.name(),  false);
				flowMap.put(FlowStepName.TAXONOMY_RETOKENIZER.name(),    false);
				flowMap.put(FlowStepName.GEOLOCATION_RETOKENIZER.name(), false);
                
				flowMap.put(FlowStepName.DSYNTS.name(),  false);
				flowMap.put(FlowStepName.EMOTION.name(), false);

				return flowMap;
			};
            break;
        
		case PRE_DEEP_DEV:
			options = () -> {
				Map<String, Boolean> flowMap = createMap(true);
                
				flowMap.put(FlowStepName.DSYNTS.name(),  false);
				flowMap.put(FlowStepName.EMOTION.name(), false);

				return flowMap;
			};
            break;

        case FULL:
		case PROTO1:
			options = () -> {
				Map<String, Boolean> flowMap = createMap(true);

				flowMap.put(FlowStepName.HEIDELTIME_RETOKENIZER.name(),  false);
				flowMap.put(FlowStepName.TAXONOMY_RETOKENIZER.name(),    false);
				flowMap.put(FlowStepName.GEOLOCATION_RETOKENIZER.name(), false);

                return flowMap;
			};
			break;
            
		case FULL_DEV:
			options = () -> {
				Map<String, Boolean> flowMap = createMap(true);

                return flowMap;
			};
			break;
            
        default:
            throw new WelcomeException("Unrecognized Analysis Type! (" + type.name() + ")");
		}

		return options;
	}

}
