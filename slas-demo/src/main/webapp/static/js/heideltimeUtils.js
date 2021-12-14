function extractHeideltimeLabelInfo(data){
	if (data && data["result"]){
		var result = data["result"];
		
		if (result["heideltime"]) {
			var heideltimeResult = result["heideltime"];
			heideltimeResult.nodes.forEach(heideltime => {
				if (heideltime.metadata.type && heideltime.metadata.value) {
					var label = heideltime.metadata.type + ":" + heideltime.metadata.value;
					heideltime.metadata["label"] = label;
				} else if (heideltime.metadata.type){
					heideltime.metadata["label"] = heideltime.metadata.type;
				} else if (heideltime.metadata.value) {
					heideltime.metadata["label"] = "UNKNOWN:" + heideltime.metadata.value;
				} else { 
					heideltime.metadata["label"] = "UNKNOWN";
				}
			});
		}
	}
}