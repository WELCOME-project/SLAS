package edu.upf.taln.welcome.slas.core.pojos.input;

public class AnalysisConfiguration {
    
    private String candidateConceptsUrl;
    private String dbpediaUrl;
    private String nerUrl;
    private String emotionUrl;
    private String speechActUrl;
    private String geolocationUrl;
    private String taxonomyDictPath;
    private String disambiguationUrl;

    public String getCandidateConceptsUrl() {
        return candidateConceptsUrl;
    }

    public void setCandidateConceptsUrl(String candidateConceptsUrl) {
        this.candidateConceptsUrl = candidateConceptsUrl;
    }

	public String getDbpediaUrl() {
		return dbpediaUrl;
	}

	public void setDbpediaUrl(String dbpediaUrl) {
		this.dbpediaUrl = dbpediaUrl;
	}

	public String getNerUrl() {
		return nerUrl;
	}

	public void setNerUrl(String nerUrl) {
		this.nerUrl = nerUrl;
	}

	public String getEmotionUrl() {
		return emotionUrl;
	}

	public void setEmotionUrl(String emotionUrl) {
		this.emotionUrl = emotionUrl;
	}

	public String getSpeechActUrl() {
		return speechActUrl;
	}

	public void setSpeechActUrl(String speechActUrl) {
		this.speechActUrl = speechActUrl;
	}

	public String getGeolocationUrl() {
		return geolocationUrl;
	}

	public void setGeolocationUrl(String geolocationUrl) {
		this.geolocationUrl = geolocationUrl;
	}

	public String getTaxonomyDictPath() {
		return taxonomyDictPath;
	}

	public void setTaxonomyDictPath(String taxonomyDictPath) {
		this.taxonomyDictPath = taxonomyDictPath;
	}

	public String getDisambiguationUrl() {
		return disambiguationUrl;
	}

	public void setDisambiguationUrl(String disambiguationUrl) {
		this.disambiguationUrl = disambiguationUrl;
	}
    
}
