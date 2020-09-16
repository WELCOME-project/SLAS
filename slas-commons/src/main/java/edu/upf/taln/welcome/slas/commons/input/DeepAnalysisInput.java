package edu.upf.taln.welcome.slas.commons.input;

import com.fasterxml.jackson.annotation.JsonAlias;

public class DeepAnalysisInput {
	@JsonAlias("metadata")
    private InputMetadata meta;
    private InputData data;

    public InputMetadata getMeta() {
        return meta;
    }

    public void setMeta(InputMetadata meta) {
        this.meta = meta;
    }

    public InputData getData() {
        return data;
    }

    public void setData(InputData data) {
        this.data = data;
    }
}
