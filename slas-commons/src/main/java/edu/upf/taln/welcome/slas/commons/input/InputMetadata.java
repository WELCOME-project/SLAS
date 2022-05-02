package edu.upf.taln.welcome.slas.commons.input;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;


import edu.upf.taln.uima.flask_wrapper.commons.IUseCase;

/**
 *
 * @author rcarlini
 */
public class InputMetadata implements IUseCase {

	String language;
	
    @JsonAlias("output_type")
    @JsonProperty("output_level")
	OutputType outputType;
    
	@JsonProperty("analysis_type")
	private AnalysisType analysisType;
    
	@JsonProperty("use_case")
	private String useCase;
	
	public InputMetadata() {}
	
	public InputMetadata(InputMetadata metadata) {
		language = metadata.getLanguage();
		outputType = metadata.getOutputType();
		analysisType = metadata.getAnalysisType();
		useCase = metadata.getUseCase();
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public OutputType getOutputType() {
		return outputType;
	}

	public void setOutputType(OutputType outputType) {
		this.outputType = outputType;
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
