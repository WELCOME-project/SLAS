package edu.upf.taln.welcome.slas.io;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class Relation {
    
    @NotNull
    private String id;
    
    @NotNull
    private String relation;
    private List<Participant> participants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
