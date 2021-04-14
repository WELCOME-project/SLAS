package edu.upf.taln.welcome.slas.commons.output.welcome;

import java.util.List;
import java.util.ArrayList;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class Relation {
    
    @NotNull
    private String id;
    
    @NotNull
    private String predicate;
    private ArrayList<Participant> participants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }
}
