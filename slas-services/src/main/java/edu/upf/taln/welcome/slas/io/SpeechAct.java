package edu.upf.taln.welcome.slas.io;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class SpeechAct {
    @NotNull
    private String id;
    @NotNull
    private String type;
    private String anchor;
    private List<String> entities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getEntities() {
        return entities;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }
}
