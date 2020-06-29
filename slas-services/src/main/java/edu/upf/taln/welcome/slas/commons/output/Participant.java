package edu.upf.taln.welcome.slas.commons.output;

import java.util.List;

/**
 *
 * @author rcarlini
 */
public class Participant {
    private String role;
    private List<String> entities;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getEntities() {
        return entities;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }
}
