/**
 * Base output JavaScript functions
 */
head.js(
    //JQuery and Boostrap already loaded in head section

    // External libraries
    './static/external/js/jquery.svg.min.js',
    './static/external/js/jquery.svgdom.min.js',

    // brat helper modules
    './static/external/js/configuration.js',
    './static/external/js/util.js',
    './static/external/js/annotation_log.js',
    './static/external/js/webfont.js',

    // brat modules
    './static/external/js/dispatcher.js',
    './static/external/js/url_monitor.js',

    './static/external/js/visualizer.js',
    
    //TALN WIDGETS
    './static/js/taln.widgets.js'
);

//Services URL's
/*var enURL = "../v4design-services-en/analyze";
var esURL = "../v4design-services-es/analyze";
var elURL = "../v4design-services-el/analyze";
var deURL = "../v4design-services-de/analyze";*/
var analyzeURL = "../dla-service/api/dla/analyze";
var descriptionURL = "../dla-service/api/dla/description";

function spinButton() {
    var button = $("#submitButton");
    if(!button.prop('disabled')){
	    button.data('original-text', button.html());
	    button.prop('disabled', true);
	    button.html(button.data('loading-text'));
    }
}

function unspinButton() {
    var button = $("#submitButton");
    button.prop('disabled', false);
    button.html(button.data('original-text'));
}

var surfaceData = null;
var deepData = null;
var predargData = null;
//var triplesData = null;
var babelnetData = null;
var dbpediaData = null;
var nerData = null;
var coreferenceData = null;
var tagcloudData = null;
var emotionData = null;

function translateCall(type, url, input, sourceLanguage, targetLanguage, topic, loadingCallback, endLoadingCallback) {
	typeof loadingCallback === 'function' && loadingCallback();
    var jqxhr = $.ajax({
    	headers: { 
            "Accept": "application/json",
            "Content-Type": "application/json" 
        },
        type: type,
        dataType: "json",
        contentType: "application/json",
        url: url,
        data: JSON.stringify({
	        	meta: {
	                source_language: sourceLanguage,
	                analysisType: 1
	        	},
	        	data: {
	        		text: input,
	        		targetLanguage: targetLanguage
	        	}
	        })
		}).done(function (result) {
	    	if(result["result"]["translation"] != null){
	    		submitForm(type, analyzeURL, result["result"]["translation"], topic, "en", loadingCallback, endLoadingCallback);
	    	} else {
	    		typeof endLoadingCallback === 'function' && endLoadingCallback();
	    	}
	    })
	    .fail(function(jqXHR, textStatus, errorThrown) {
	    	typeof unspinButton === 'function' && unspinButton();
	    	
	        var msg = '';
	        if (jqXHR.status === 0) {
	            msg = 'Not connect.\n Verify Network.';
	        } else if (jqXHR.status == 404) {
	            msg = 'Requested page not found. [404]';
	        } else if (jqXHR.status == 500) {
	            msg = 'Internal Server Error [500].';
	        } else if (textStatus === 'parsererror') {
	            msg = 'Requested JSON parse failed.';
	        } else if (textStatus === 'timeout') {
	            msg = 'Time out error.';
	        } else if (textStatus === 'abort') {
	            msg = 'Ajax request aborted.';
	        } else {
	            msg = 'Uncaught Error.\n' + jqXHR.responseText;
	        }
	        alert(msg);
	    })
	    /*.always(function(data) {
	        typeof endLoadingCallback === 'function' && endLoadingCallback();
	    });*/
}

function submitForm(type, url, input, topicVal, language, loadingCallback, endLoadingCallback) {
    typeof loadingCallback === 'function' && loadingCallback();
    var jqxhr = $.ajax({
	    	headers: { 
	            "Accept": "application/json",
	            "Content-Type": "application/json" 
	        },
            type: type,
            dataType: "json",
            contentType: "application/json",
            url: url,
            data: JSON.stringify({
	            	meta: {
	            		/*preprocessOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		candidatesOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		babelnetOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		dependencyParserOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		nerOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		dbpediaOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		deepParserOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		retokenizerOptions : {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		summarizationOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
		        		correferenceOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		nerRetokenizerOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		dbpediaRetokenizerOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
	            		emotionOptions: {
	            			processRequested: true,
	            			outputRequested: true
	            		},
		                source_language: language,
		                topic: topicVal*/
		        	},
		        	data: {
		        		conll: input
		        	}
	            })
        })
        .done(function(result) {
        	ajaxCallback(result);
            
        	var result64 = Base64.encode(JSON.stringify(result));
        	$('#download').attr("href", "data:text/plain;charset=utf-8;base64," + result64);
        	$('#downloadDiv').removeClass("hidden");
            $('#inputCollapse').collapse();
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            var msg = '';
            if (jqXHR.status === 0) {
                msg = 'Not connect.\n Verify Network.';
            } else if (jqXHR.status == 404) {
                msg = 'Requested page not found. [404]';
            } else if (jqXHR.status == 500) {
                msg = 'Internal Server Error [500].';
            } else if (textStatus === 'parsererror') {
                msg = 'Requested JSON parse failed.';
            } else if (textStatus === 'timeout') {
                msg = 'Time out error.';
            } else if (textStatus === 'abort') {
                msg = 'Ajax request aborted.';
            } else {
                msg = 'Uncaught Error.\n' + jqXHR.responseText;
            }
            alert(msg);
        })
        .always(function(data) {
            typeof endLoadingCallback === 'function' && endLoadingCallback();
        });
}

function ajaxCallback(result){
	let views = [
		"dbpedia_linking",
		"ner",
		"babelnet_linking",
		"coreference_resolver",
        "surface_parsing",
        "deep_parsing",
        "predicate_arguments_parsing",
        "emotion_detection",
        "triples",
        "sentence_ranking",
        "tag_cloud"
    ];
	result["views"] = views;
    
    $("#result").empty();
    addVisualizations($("#result"), result, result["views"]);

}

head.ready(function() {

	$('#inputCollapse').on('hidden.bs.collapse', function () {
		$('#collapseArrow').html("Show ▼");
	});
	$('#inputCollapse').on('shown.bs.collapse', function () {
		$('#collapseArrow').html("Hide ▲");
	});

    $("#form").submit(function(event) {
        event.preventDefault();

        if (this.checkValidity() === true) {
            var inputText = $("#inputText").val();
            
            var language = "en";
            language = $("#languageSelector option:selected").val();
            
            var topic = null; 
            if($("#topic").val().trim() !== "") {
            	topic = $("#topic").val();
            } 
            
            var translate = false;
            if($("#translateCheck").is(':checked')) {
            	translate = true;
            }
            
            if (translate) {
            	translateCall("POST", translateURL, inputText, language, "en", topic, spinButton, unspinButton);
            } else {
                submitForm("POST", analyzeURL, inputText, topic, language, spinButton, unspinButton);
            }
            
            
        } else {
            event.stopPropagation();
            form.classList.add('was-validated');
        }

    });
    
    $("#jsonForm").submit(function(event) {
        event.preventDefault();

        if (this.checkValidity() === true) {
        	var file = $('#jsonFile').prop('files')[0];
        	if (file) {
        	    var reader = new FileReader();
        	    reader.readAsText(file, "UTF-8");
        	    reader.onload = function (evt) {
        	    	var jsObject = JSON.parse(evt.target.result);
        	    	
        	    	ajaxCallback(jsObject);
        	    }
        	    reader.onerror = function (evt) {
        	        document.getElementById("fileContents").innerHTML = "error reading file";
        	    }
        	    
        	}
        	
        } else {
            event.stopPropagation();
            form.classList.add('was-validated');
        }

    });
});
