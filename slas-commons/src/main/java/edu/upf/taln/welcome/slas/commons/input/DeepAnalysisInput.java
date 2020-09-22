package edu.upf.taln.welcome.slas.commons.input;

import com.fasterxml.jackson.annotation.JsonAlias;

public class DeepAnalysisInput implements IAnalysisInput{
	@JsonAlias("meta")
    private InputMetadata metadata;
    private InputData data;

    public InputMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(InputMetadata metadata) {
        this.metadata = metadata;
    }

    public InputData getData() {
        return data;
    }

    public void setData(InputData data) {
        this.data = data;
    }
}
