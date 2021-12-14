function generateConll(sentences, tokenTypes, tokens){
	var node2Relation = {};
	for(var i = 0; i < tokens.edges.length; i++) {
		var edge = tokens.edges[i];
		var token = tokens.nodes.find((node) => node.id == edge.target);
		node2Relation[token.id] = edge;
		
	}
	
	var conll = "";
	for(var i = 0; i < sentences.nodes.length; i++) {
		var sentence = sentences.nodes[i];
		var sentenceTokens = sentence.metadata.tokens_map[tokenTypes].slice(0);
		sentenceTokens.sort(function(a, b){ 
			var tokenA = tokens.nodes.find((node) => node.id == a);
			var tokenB = tokens.nodes.find((node) => node.id == b);
			return tokenA.metadata.sentenceOrder - tokenB.metadata.sentenceOrder;
		});
		for(var j = 0; j < sentenceTokens.length; j++) {
			var token = tokens.nodes.find((node) => node.id == sentenceTokens[j]);
			conll += token.metadata.sentenceOrder + "\t";
			conll += (token.metadata.form || "_") + "\t";
			conll += (token.metadata.lemma || "_")+ "\t";
			conll += "_" + "\t";
			conll += (token.metadata.pos || "_") + "\t";
			conll += "_" + "\t";
			conll += (token.metadata.feats || "_") + "\t";
			conll += "_" + "\t";
			
			var relation = node2Relation[token.id];
			var parentConllId = 0;
			if(relation.relation !== 'root' && relation.relation !== 'ROOT') {
				var parentId = relation.source;
				var parent = tokens.nodes.find((node) => node.id == parentId);
				parentConllId = parent.metadata.sentenceOrder;
			}
			conll += parentConllId + "\t";
			conll += "_" + "\t";
			conll += relation.relation + "\t";
			conll += "_" + "\t";
			conll += "_" + "\t";
			conll += "_" + "\n";
		}
		conll += "\n";
	}
	
	return conll;
}