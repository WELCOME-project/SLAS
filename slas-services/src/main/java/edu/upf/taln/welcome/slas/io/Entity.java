package edu.upf.taln.welcome.slas.io;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class Entity {
    
    @NotNull
    private String id;

    @NotNull
    private String type;

    @NotNull
    private String anchor;
    private String link;
    private double confidence;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }    
}
