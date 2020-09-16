package edu.upf.taln.welcome.slas.commons.input;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class InputDataPlain {
    
    @NotNull
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }    
}
