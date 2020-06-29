package edu.upf.taln.welcome.slas.io;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class DeepAnalysisData {
    @NotNull
    private int userId;
    
    @NotNull
    private int dialogueSession;
    
    @NotNull
    private int dialogueTurn;
    
    private List<SpeechAct> speechActs;
    private List<Entity> entities;
    private List<Relation> relations;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDialogueSession() {
        return dialogueSession;
    }

    public void setDialogueSession(int dialogueSession) {
        this.dialogueSession = dialogueSession;
    }

    public int getDialogueTurn() {
        return dialogueTurn;
    }

    public void setDialogueTurn(int dialogueTurn) {
        this.dialogueTurn = dialogueTurn;
    }

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
