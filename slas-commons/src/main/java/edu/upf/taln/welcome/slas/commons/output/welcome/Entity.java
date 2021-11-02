package edu.upf.taln.welcome.slas.commons.output.welcome;

import java.util.List;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.unihd.dbs.uima.types.heideltime.Timex3;

/**
 *
 * @author rcarlini
 */
public class Entity {
	
	public static class TemporalAnalysis {
		public static final String UNKNOWN = "UNKNOWN";
		
		protected String type;
        protected String value;
        protected String modifier;
        
        public TemporalAnalysis() {}
        public TemporalAnalysis(String type, String value, String modifier) {
        	if (type != null) {
				this.type = type;
			} else {
				this.type = UNKNOWN;
			}
        	this.value = value;
        	this.modifier = modifier;
        }
        public TemporalAnalysis(Timex3 time) {
			if (time.getTimexType() != null) {
				type = time.getTimexType();
			} else {
				type = UNKNOWN;
			}
			if (time.getTimexValue() != null) {
				value = time.getTimexValue();
			}
			if (time.getTimexMod() != null) {
				modifier = time.getTimexMod();
			}
        }
        
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getModifier() {
			return modifier;
		}
		public void setModifier(String modifier) {
			this.modifier = modifier;
		}
        

	}
    
    @NotNull
    private String id;

    @NotNull
    private String type;

    @NotNull
    private String anchor;
    private List<String> links;
    private List<Location> locations;
    private double confidence;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("temporal_analysis")
    private TemporalAnalysis temporalAnalysis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public TemporalAnalysis getTemporalAnalysis() {
		return temporalAnalysis;
	}

	public void setTemporalAnalysis(TemporalAnalysis temporalAnalysis) {
		this.temporalAnalysis = temporalAnalysis;
	}
	
}
