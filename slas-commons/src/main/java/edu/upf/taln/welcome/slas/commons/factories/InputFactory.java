package edu.upf.taln.welcome.slas.commons.factories;

import edu.upf.taln.welcome.slas.commons.input.OutputType;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputData;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;

/**
 *
 * @author rcarlini
 */
public class InputFactory {

    public static InputMetadata createMetadata(AnalysisType analysisType, OutputType outputLevel, String language, String useCase) {
        
        InputMetadata meta = new InputMetadata();
        
        meta.setLanguage(language);
        meta.setOutputType(outputLevel);
        meta.setAnalysisType(analysisType);
        meta.setUseCase(useCase);
        
        return meta;
    }
    
    public static DeepAnalysisInput create(AnalysisType analysisType, OutputType outputLevel, String text, String language, String useCase) {
		
		InputData data = new InputData();
		data.setText(text);
		
        InputMetadata meta = createMetadata(analysisType, outputLevel, language, useCase);

        DeepAnalysisInput input = new DeepAnalysisInput();
		input.setData(data);
		input.setMetadata(meta);
		
		return input;
	}
}
