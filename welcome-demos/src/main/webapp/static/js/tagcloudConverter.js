function resultToTagCloud(result)
{
	var entitiesTags = [];
	var wordsData = [];	
	$.each(result.nodes, function(nodeNum, node)
	{
		if(!entitiesTags.includes(node.label)) {
			var word = {word: node.label, weight:node.metadata.confidence};
			wordsData.push(word);
			entitiesTags.push(node.label);
		}
	});
	return wordsData;
}