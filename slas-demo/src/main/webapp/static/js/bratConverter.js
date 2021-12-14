function getRandomColor() {
  var letters = '0123456789ABCDEF';
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

function hexToRgb(hex) {
    // Expand shorthand form (e.g. "03F") to full form (e.g. "0033FF")
    var shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
    hex = hex.replace(shorthandRegex, function(m, r, g, b) {
        return r + r + g + g + b + b;
    });

    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}

function contrast(r, g, b) {
	return ((r*0.299 + g*0.587 + b*0.114) > 149) ? '#000000' : '#ffffff';
}

function getFontColor(color) {
	var rgbColor = hexToRgb(color);
	return contrast(rgbColor.r, rgbColor.g, rgbColor.b);
}

function getDarkColor() {
	do {
		var color = getRandomColor();
	} while (getFontColor(color) == '#000000');
	return color;
}

class BratConfiguration {
	constructor(nodeAttr, printEdges, edgeAttr, linkAttr, nodesColorScheme, edgesColorScheme, extraNodeAttr, extraNodeName, extraNodeColor) {
		this.nodeAttr = nodeAttr;
		this.printEdges = printEdges; //true | false
		this.edgeAttr = edgeAttr; 
		this.linkAttr = linkAttr;
		this.nodesColorScheme = nodesColorScheme; //random | same || array ex:["#ffffff", "#ac4523"]
		this.edgesColorScheme = edgesColorScheme; //random | same
		this.extraNodeAttr = extraNodeAttr;
		this.extraNodeName = extraNodeName;
		this.extraNodeColor = extraNodeColor;
	}
}

class BratConfigurationError extends Error {
  constructor(message) {
	  super(message);
	  this.name = "BratConfigurationError";
  }
}

function addEntityType(node, config, resultArray, createdEntityTypes, colorIndex) {
	
	if(!createdEntityTypes.includes(node.metadata[config.nodeAttr])) {
		if(config.nodesColorScheme == "random") {
			var color = getRandomColor();
		} else if (Array.isArray(config.nodesColorScheme)) {
			var color = config.nodesColorScheme[colorIndex];
			colorIndex++;
			if(config.nodesColorScheme[colorIndex] === undefined) {
				config.nodesColorScheme = "random";
			}
		} else if (config.nodesColorScheme == "same") {
			var color = "#7fa2ff";
		} else {
			throw new BratConfigurationError("No valid nodes color scheme: " + config.nodesColorScheme);
		}
		var fontColor = getFontColor(color);
		var entityType = {type: node.metadata[config.nodeAttr], labels: [node.metadata[config.nodeAttr]], bgColor: color, fgColor: fontColor, borderColor: "darken"};
		resultArray.push(entityType);
		createdEntityTypes.push(node.metadata[config.nodeAttr]);
	}
	return colorIndex;
}

function addEntityData(node, config, resultArray) {
	var entityData = [];
	entityData.push("E" + node.id);
	entityData.push(node.metadata[config.nodeAttr]);

	var offsets = [];
	var offset = [];
	offset.push(node.metadata["begin"]);
	offset.push(node.metadata["end"]);
	offsets.push(offset)

	entityData.push(offsets);
	if(config.linkAttr !== undefined) {
		entityData.push(node.metadata[config.linkAttr]);
		//"http://live.babelnet.org/synset?word=" + encodeURI(entity.id)
	}
	resultArray.push(entityData);
}

function addExtraEntityType(text, colorType, resultArray, createdEntityTypes) {
	
	if(!createdEntityTypes.includes(text)) {
		if(colorType == "random") {
			var color = getRandomColor();
		} else if (Array.isArray(colorType)) {
			var color = colorType[0];
		} else if (colorType == "same") {
			var color = "#7fa2ff";
		} else {
			throw new BratConfigurationError("No valid nodes color scheme: " + config.nodesColorScheme);
		}
		var fontColor = getFontColor(color);
		var entityType = {type: text, labels: [text], bgColor: color, fgColor: fontColor, borderColor: "darken"};
		resultArray.push(entityType);
		createdEntityTypes.push(text);
	}
}

function addExtraEntityData(node, id, text, config, resultArray) {
	var entityData = [];
	entityData.push("E" + id);
	entityData.push(text);

	var offsets = [];
	var offset = [];
	offset.push(node.metadata["begin"]);
	offset.push(node.metadata["end"]);
	offsets.push(offset)

	entityData.push(offsets);
	if(config.linkAttr !== undefined) {
		entityData.push(node.metadata[config.linkAttr]);
		//"http://live.babelnet.org/synset?word=" + encodeURI(entity.id)
	}
	resultArray.push(entityData);
}

function addRelationType(edge, config, resultArray, createdRelationTypes) {
	
	if(!createdRelationTypes.includes(edge[config.edgeAttr])) {
		if(config.edgesColorScheme == "random") {
			var color = getDarkColor();
		} else if (config.edgesColorScheme == "same") {
			var color = "purple";
		} else {
			throw new BratConfigurationError("No valid vertices color scheme: " + config.edgesColorScheme);
		}
		var relationType = {type: edge[config.edgeAttr], labels: [edge[config.edgeAttr]], dashArray: "3,3", color: color, args: [{role: "from"}, {role: "to"}]};
		resultArray.push(relationType);
		createdRelationTypes.push(edge[config.edgeAttr]);
	}
}

function addRelationData(edge, edgeNum, config, resultArray) {
	var relationData = [];
	relationData.push("R" + edgeNum);
	relationData.push(edge[config.edgeAttr]);

	var rel = [];
	var from = [];
	var to = [];
	from.push("from");
	from.push("E" + edge.source);
	to.push("to");
	to.push("E" + edge.target);
	rel.push(from);
	rel.push(to);

	relationData.push(rel);
	resultArray.push(relationData);
}

function generatePosAndRelBrat(text, json, config)
{
    
    var createdEntityTypes = [];
    var createdRelationTypes = [];
    var colorIndex = 0;
    
    var output = { data: {documentData: {}, collectionData: {}} };
    
    var documentData = { text: text, entities: [], relations: []};
    var collectionData = {entity_types: [], relation_types:[]};
    if (json != null) {
    	
    	if(config.nodeAttr === undefined) {
    		throw new BratConfigurationError("No node attribute specified.");
    	}
		$.each(json.nodes, function(nodeNum, node)
		{
			addEntityData(node, config, documentData.entities);
			colorIndex = addEntityType(node, config, collectionData.entity_types, createdEntityTypes, colorIndex);
			
			if(config.extraNodeAttr !== null && node.metadata[config.extraNodeAttr] === true) {
				addExtraEntityData(node, "extra" + node.id, config.extraNodeName, config, documentData.entities);
				addExtraEntityType(config.extraNodeName, config.extraNodeColor, collectionData.entity_types, createdEntityTypes);
			}
			
		});
    	
		if(config.printEdges) {
			if(config.edgeAttr === undefined) {
				throw new BratConfigurationError("Edges to be printed, but no edge attribute specified.");
	    	}
			$.each(json.edges, function(edgeNum, edge)
				{
				
					addRelationData(edge, edgeNum, config, documentData.relations);
					addRelationType(edge, config, collectionData.relation_types, createdRelationTypes);
					
				});
		}
	}
	
	output.data.documentData = documentData;
	output.data.collectionData = collectionData;
	
	return output;
}

function jsonToBrat(text, json, config)
{
    var output = { data: {documentData: {}, collectionData: {}} };
    if (json !== undefined) {
    	if (json.nodes == undefined || json.edges == undefined) {
	    	var documentData = { text: text, entities: [], relations: []};
	    	output.data.documentData = documentData;
    	} else {
    		output = generatePosAndRelBrat(text, json, config);
    	}
    }
	
	return output;
}