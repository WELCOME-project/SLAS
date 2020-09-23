package edu.upf.taln.welcome.slas.commons.output;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upf.taln.utils.pojos.uima.babelnet.BabelnetGraph;
import edu.upf.taln.utils.pojos.uima.concept.ConceptGraph;
import edu.upf.taln.utils.pojos.uima.dbpedia.DbpediaGraph;
import edu.upf.taln.utils.pojos.uima.deep.DeepGraph;
import edu.upf.taln.utils.pojos.uima.ner.NerGraph;
import edu.upf.taln.utils.pojos.uima.predarg.PredargGraph;
import edu.upf.taln.utils.pojos.uima.sentence.SentenceGraph;
import edu.upf.taln.utils.pojos.uima.surface.SurfaceGraph;
import edu.upf.taln.welcome.slas.commons.output.welcome.DlaResult;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WelcomeDemoResult {
	
	@JsonProperty("surface_parsing")
	SurfaceGraph surfaceParsing;
	
	NerGraph ner;
	
	@JsonProperty("concept_extraction")
	ConceptGraph conceptExtraction;
	
	@JsonProperty("babelnet_linking")
	BabelnetGraph babelnetLinking;
	
	@JsonProperty("dbpedia_linking")
	DbpediaGraph dbpediaLinking;
	
	@JsonProperty("deep_parsing")
	DeepGraph deepParsing;
	
	@JsonProperty("predicate_arguments_parsing")
	PredargGraph predargParsing;
	
	@JsonProperty("sentence_ranking")
	SentenceGraph sentenceRanking;
	
	List<String[]> triples;
	
	@JsonProperty("dla_result")
	DlaResult dlaResult;
	
	public List<String[]> getTriples() {
		return triples;
	}
	public void setTriples(List<String[]> triples) {
		this.triples = triples;
	}
	public DeepGraph getDeepParsing() {
		return deepParsing;
	}
	public void setDeepParsing(DeepGraph deepParsing) {
		this.deepParsing = deepParsing;
	}
	public SurfaceGraph getSurfaceParsing() {
		return surfaceParsing;
	}
	public void setSurfaceParsing(SurfaceGraph surfaceParsing) {
		this.surfaceParsing = surfaceParsing;
	}
	public NerGraph getNer() {
		return ner;
	}
	public void setNer(NerGraph ner) {
		this.ner = ner;
	}
	public ConceptGraph getConceptExtraction() {
		return conceptExtraction;
	}
	public void setConceptExtraction(ConceptGraph conceptExtraction) {
		this.conceptExtraction = conceptExtraction;
	}
	public BabelnetGraph getBabelnetLinking() {
		return babelnetLinking;
	}
	public void setBabelnetLinking(BabelnetGraph babelnetLinking) {
		this.babelnetLinking = babelnetLinking;
	}
	public DbpediaGraph getDbpediaLinking() {
		return dbpediaLinking;
	}
	public void setDbpediaLinking(DbpediaGraph dbpediaLinking) {
		this.dbpediaLinking = dbpediaLinking;
	}
	public SentenceGraph getSentenceRanking() {
		return sentenceRanking;
	}
	public void setSentenceRanking(SentenceGraph sentenceRanking) {
		this.sentenceRanking = sentenceRanking;
	}
	public PredargGraph getPredargParsing() {
		return predargParsing;
	}
	public void setPredargParsing(PredargGraph predargParsing) {
		this.predargParsing = predargParsing;
	}
	public DlaResult getDlaResult() {
		return dlaResult;
	}
	public void setDlaResult(DlaResult dlaResult) {
		this.dlaResult = dlaResult;
	}

}
