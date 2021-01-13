package edu.upf.taln.welcome.slas.commons.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;

/**
 *
 * @author rcarlini
 */
public class InputMetadata {

	String language;
	
	@JsonProperty("output_level")
	OutputLevel outputLevel;
    
	@JsonProperty("analysis_type")
	private AnalysisType analysisType;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public OutputLevel getOutputLevel() {
		return outputLevel;
	}

	public void setOutputLevel(OutputLevel outputLevel) {
		this.outputLevel = outputLevel;
	}

	public AnalysisType getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(AnalysisType analysisType) {
		this.analysisType = analysisType;
	}	
}
