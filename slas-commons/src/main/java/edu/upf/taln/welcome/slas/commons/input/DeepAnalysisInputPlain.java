package edu.upf.taln.welcome.slas.commons.input;

import com.fasterxml.jackson.annotation.JsonAlias;

public class DeepAnalysisInputPlain implements IAnalysisInput {
	@JsonAlias("meta")
    private InputMetadata metadata;
    private InputDataPlain data;

    public InputMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(InputMetadata metadata) {
        this.metadata = metadata;
    }

    public InputData getData() {
    	InputData input = new InputData();
    	input.setConll(data.getText());
        return input;
    }

    public void setData(InputDataPlain data) {
        this.data = data;
    }
}
