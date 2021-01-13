package edu.upf.taln.welcome.slas.commons.factories;

import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputData;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;

/**
 *
 * @author rcarlini
 */
public class InputFactory {

    public static InputMetadata createMetadata(AnalysisType analysisType, OutputLevel outputLevel, String language) {
        
        InputMetadata meta = new InputMetadata();
        
        meta.setLanguage(language);
        meta.setOutputLevel(outputLevel);
        meta.setAnalysisType(analysisType);
        
        return meta;
    }
    
    public static DeepAnalysisInput create(AnalysisType analysisType, OutputLevel outputLevel, String conll, String language) {
		
		InputData data = new InputData();
		data.setText(conll);
		
        InputMetadata meta = createMetadata(analysisType, outputLevel, language);

        DeepAnalysisInput input = new DeepAnalysisInput();
		input.setData(data);
		input.setMetadata(meta);
		
		return input;
	}
}
