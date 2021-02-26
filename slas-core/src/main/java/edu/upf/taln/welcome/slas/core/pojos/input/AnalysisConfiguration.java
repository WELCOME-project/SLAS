package edu.upf.taln.welcome.slas.core.pojos.input;

/**
 *
 * @author rcarlini
 */
public class AnalysisConfiguration {
    
    private String babelnetConfigPath;
    private String candidateConceptsUrl;
    private String domainConceptsSolrUrl;
    private String domainConceptsJihadUrl;
    private String dbpediaUrl;
    private String nerUrl;
    //private TalnDisambiguationConfiguration disambiguationConfig;
    //private BuddyConfiguration buddyConfiguration;
    private String rankingPropertiesFile;
    private String compactDictionaryPath;
    private String emotionUrl;
    private String speechActUrl;
    private String geolocationUrl;

    public String getBabelnetConfigPath() {
        return babelnetConfigPath;
    }

    public void setBabelnetConfigPath(String babelnetConfigPath) {
        this.babelnetConfigPath = babelnetConfigPath;
    }

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

	/*public TalnDisambiguationConfiguration getDisambiguationConfig() {
		return disambiguationConfig;
	}

	public void setDisambiguationConfig(TalnDisambiguationConfiguration disambiguationConfig) {
		this.disambiguationConfig = disambiguationConfig;
	}*/

	public String getDomainConceptsSolrUrl() {
		return domainConceptsSolrUrl;
	}

	public void setDomainConceptsSolrUrl(String domainConceptsSolrUrl) {
		this.domainConceptsSolrUrl = domainConceptsSolrUrl;
	}

	public String getDomainConceptsJihadUrl() {
		return domainConceptsJihadUrl;
	}

	public void setDomainConceptsJihadUrl(String domainConceptsJihadUrl) {
		this.domainConceptsJihadUrl = domainConceptsJihadUrl;
	}

	public String getNerUrl() {
		return nerUrl;
	}

	public void setNerUrl(String nerUrl) {
		this.nerUrl = nerUrl;
	}

    /*public BuddyConfiguration getBuddyConfiguration() {
        return buddyConfiguration;
    }

    public void setBuddyConfiguration(BuddyConfiguration buddyConfiguration) {
        this.buddyConfiguration = buddyConfiguration;
    }*/

	public String getRankingPropertiesFile() {
		return rankingPropertiesFile;
	}

	public void setRankingPropertiesFile(String rankingPropertiesFile) {
		this.rankingPropertiesFile = rankingPropertiesFile;
	}

	public String getCompactDictionaryPath() {
		return compactDictionaryPath;
	}

	public void setCompactDictionaryPath(String compactDictionaryPath) {
		this.compactDictionaryPath = compactDictionaryPath;
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
    
}
