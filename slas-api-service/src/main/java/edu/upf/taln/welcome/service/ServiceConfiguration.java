package edu.upf.taln.welcome.service;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceConfiguration {
	
	@JsonProperty("language_services_map")
	private Map<String, String> languageServicesMap;

	public Map<String, String> getLanguageServicesMap() {
		return languageServicesMap;
	}

	public void setLanguageServicesMap(Map<String, String> languageServicesMap) {
		this.languageServicesMap = languageServicesMap;
	}

}
