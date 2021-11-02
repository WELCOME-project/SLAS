package edu.upf.taln.welcome.slas.commons.output.welcome;

import java.util.List;

/**
 *
 * @author rcarlini
 */
public class Participant {
    private List<String> roles;
    private String entity;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
