package edu.upf.taln.welcome.slas.commons.input;

import com.fasterxml.jackson.annotation.JsonAlias;

public class DeepAnalysisInputPlain {
	@JsonAlias("meta")
    private InputMetadata metadata;
    private InputDataPlain data;

    public InputMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(InputMetadata metadata) {
        this.metadata = metadata;
    }

    public InputDataPlain getData() {
        return data;
    }

    public void setData(InputDataPlain data) {
        this.data = data;
    }
}
