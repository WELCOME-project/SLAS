package edu.upf.taln.welcome.slas.commons.output;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class DlaResult {
    
    private List<SpeechAct> speechActs;
    private List<Entity> entities;
    private List<Relation> relations;

    public List<SpeechAct> getSpeechActs() {
        return speechActs;
    }

    public void setSpeechActs(List<SpeechAct> speechActs) {
        this.speechActs = speechActs;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }
}
