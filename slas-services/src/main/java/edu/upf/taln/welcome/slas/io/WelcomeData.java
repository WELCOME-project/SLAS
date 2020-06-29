package edu.upf.taln.welcome.slas.io;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class WelcomeData {
    
    @NotNull
    private String conll;

    public String getConll() {
        return conll;
    }

    public void setConll(String conll) {
        this.conll = conll;
    }    
}
