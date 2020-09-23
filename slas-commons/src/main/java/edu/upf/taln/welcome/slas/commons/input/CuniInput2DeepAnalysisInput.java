package edu.upf.taln.welcome.slas.commons.input;

import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;

public class CuniInput2DeepAnalysisInput {

	public static DeepAnalysisInput convert(CuniInput originalInput) {
		
		DeepAnalysisInput ourInput = new DeepAnalysisInput();
		InputData data = new InputData();
		data.setConll(originalInput.getResult());
		ourInput.setData(data);
		InputMetadata meta = new InputMetadata();
		meta.setOutputLevel(OutputLevel.welcome);
		meta.setLanguage(originalInput.getModel());
		ourInput.setMetadata(meta);
		
		return ourInput;
	}
}
