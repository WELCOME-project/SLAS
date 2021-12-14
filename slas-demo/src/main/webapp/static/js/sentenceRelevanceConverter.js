function resultToSentenceRelevance(text, json) {
	var data = {text: "", spans: []};
	data.text = text;
	for (var i = 0; i < json.nodes.length; i++) {
		var sentenceData = {start: 0, end: 0, relevance: ""};
		sentenceData.start = json.nodes[i].metadata.begin;
		sentenceData.end = json.nodes[i].metadata.end;
		sentenceData.relevance = json.nodes[i].metadata.relevance;
		data.spans.push(sentenceData);
	}
	
	return data;
}