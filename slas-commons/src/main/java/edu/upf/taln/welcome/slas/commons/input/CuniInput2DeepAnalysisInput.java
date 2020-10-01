package edu.upf.taln.welcome.slas.commons.input;

public class CuniInput2DeepAnalysisInput {

	public static DeepAnalysisInput convert(CuniInput originalInput) {
		
		DeepAnalysisInput ourInput = new DeepAnalysisInput();
		InputData data = new InputData();
		data.setConll(originalInput.getResult());
		ourInput.setData(data);
		InputMetadata meta = new InputMetadata();
		meta.setLanguage(originalInput.getModel());
		
		return ourInput;
	}
}
