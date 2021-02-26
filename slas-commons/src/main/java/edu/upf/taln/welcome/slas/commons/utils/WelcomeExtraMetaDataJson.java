package edu.upf.taln.welcome.slas.commons.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upf.taln.uima.flask_wrapper.extra_metadata.IExtraMetaDataUseCaseJson;

public class WelcomeExtraMetaDataJson implements IExtraMetaDataUseCaseJson {
	@JsonProperty("use_case")
	String useCase;
	
	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}

	@Override
	public String getUseCase() {
		return useCase;
	}

}
