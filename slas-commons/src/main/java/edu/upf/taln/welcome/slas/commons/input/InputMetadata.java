package edu.upf.taln.welcome.slas.commons.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;

import edu.upf.taln.uima.flask_wrapper.commons.IUseCase;

/**
 *
 * @author rcarlini
 */
public class InputMetadata implements IUseCase {

	String language;
	
	@JsonProperty("output_level")
	OutputLevel outputLevel;
    
	@JsonProperty("analysis_type")
	private AnalysisType analysisType;
    
	@JsonProperty("use_case")
	private String useCase;

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

    @Override
    public String getUseCase() {
        return useCase;
    }

    public void setUseCase(String useCase) {
        this.useCase = useCase;
    }
}
