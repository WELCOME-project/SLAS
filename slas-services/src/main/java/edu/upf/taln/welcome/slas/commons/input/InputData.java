package edu.upf.taln.welcome.slas.commons.input;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class InputData {
    
    @NotNull
    private String conll;

    public String getConll() {
        return conll;
    }

    public void setConll(String conll) {
        this.conll = conll;
    }    
}
