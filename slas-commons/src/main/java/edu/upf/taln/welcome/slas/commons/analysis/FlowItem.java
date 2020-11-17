package edu.upf.taln.welcome.slas.commons.analysis;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;

public class FlowItem {

	public FlowItem(AnalysisEngineDescription component, String name) {
		super();
		this.component = component;
		this.name = name;
	}
	AnalysisEngineDescription component;
	String name;
	public AnalysisEngineDescription getComponent() {
		return component;
	}
	public void setComponent(AnalysisEngineDescription component) {
		this.component = component;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
