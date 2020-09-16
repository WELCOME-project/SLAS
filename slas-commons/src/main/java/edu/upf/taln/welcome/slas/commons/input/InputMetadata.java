package edu.upf.taln.welcome.slas.commons.input;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author rcarlini
 */
public class InputMetadata {
	public static enum OutputLevel {welcome, demo, demo_welcome, xmi};
	
	String language;
	
	@JsonProperty("output_level")
	OutputLevel outputLevel;

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
	
}
