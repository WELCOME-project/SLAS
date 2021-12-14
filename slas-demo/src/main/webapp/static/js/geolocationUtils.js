function extractMainGeolocationInfo(data){
	if (data && data["result"]){
		var result = data["result"];
		
		if (result["geolocation"]) {
			var geolocationResult = result["geolocation"];
			geolocationResult.nodes.forEach(geolocation => {
				if (!geolocation.metadata.main_link && geolocation.metadata.candidates) {
					var link = geolocation.metadata.candidates[0].link;
					geolocation.metadata["main_link"] = link;
				}
				
				if (!geolocation.metadata.main_uri && geolocation.metadata.candidates) {
					var uri = geolocation.metadata.candidates[0].uri;
					geolocation.metadata["main_uri"] = uri; 
				}
			});
		}
	}
}