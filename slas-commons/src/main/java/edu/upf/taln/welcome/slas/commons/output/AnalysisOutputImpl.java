package edu.upf.taln.welcome.slas.commons.output;

public class AnalysisOutputImpl<T, S> implements IAnalysisOutput {
	
	protected String id;
	protected String text;
	protected String language;
	
	protected T result;
	
	protected S metadata;
	
	public AnalysisOutputImpl(){
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public S getMetadata() {
		return metadata;
	}

	public void setMetadata(S metadata) {
		this.metadata = metadata;
	}
	
}
