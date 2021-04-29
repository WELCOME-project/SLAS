function extractMainGeolocationInfo(data){
	if (data && data["result"]){
		var result = data["result"];
		
		if (result["geolocation"]) {
			var geolocationResult = result["geolocation"];
			geolocationResult.nodes.forEach(geolocation => {
				if (!geolocation.metadata.main_lexicalization && geolocation.metadata.lexicalizations) {
					var lexicalization = geolocation.metadata.lexicalizations[0];
					geolocation.metadata["main_lexicalization"] = lexicalization;
				}
				
				if (!geolocation.metadata.main_uri && geolocation.metadata.candidates) {
					var uri = geolocation.metadata.candidates[0].uri;
					geolocation.metadata["main_uri"] = uri; 
				}
			});
		}
	}
}