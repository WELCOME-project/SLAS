package edu.upf.taln.welcome.slas.services;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusOutput {
	
	public static enum Status {operational, degraded, failed}
	
	private String status;
	private String build;
	
	@JsonProperty("status_description")
	private String statusDescription;
		
	public StatusOutput(String build) {
		this.status = Status.operational.name();
		this.statusDescription = "Everything is alright.";
		this.setBuild(build);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}
	
}
