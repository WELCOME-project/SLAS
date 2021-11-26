package edu.upf.taln.welcome.slas.commons.output;

import java.util.Map;

public class AnalysisOutputMetadata {
	
	String date;
	Map<String, String> times;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public Map<String, String> getTimes() {
		return times;
	}

	public void setTimes(Map<String, String> times) {
		this.times = times;
	}
	
}
