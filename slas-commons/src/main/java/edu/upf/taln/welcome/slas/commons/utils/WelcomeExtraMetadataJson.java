package edu.upf.taln.welcome.slas.commons.utils;

import edu.upf.taln.uima.flask_wrapper.extra_metadata.IUseCaseJson;

public class WelcomeExtraMetadataJson implements IUseCaseJson {
	String useCase;
	
	@Override
	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}

	@Override
	public String getUseCase() {
		return useCase;
	}

}
