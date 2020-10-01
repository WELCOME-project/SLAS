package edu.upf.taln.welcome.slas.commons.input;

import java.util.List;

public class CuniInput {
	String model;
	List<String> acknowledgements;
	String result;
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public List<String> getAcknowledgements() {
		return acknowledgements;
	}
	public void setAcknowledgements(List<String> acknowledgements) {
		this.acknowledgements = acknowledgements;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
