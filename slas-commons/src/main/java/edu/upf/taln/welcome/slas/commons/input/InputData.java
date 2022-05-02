package edu.upf.taln.welcome.slas.commons.input;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class InputData {
    
    @NotNull
    private String text;
    
    public InputData() {}
    
    public InputData(InputData data) {
    	text = data.getText();
    }

	public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }    
}
