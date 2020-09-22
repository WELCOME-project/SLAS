package edu.upf.taln.welcome.slas.commons.output;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class DeepAnalysisOutput {
    @NotNull
    private DlaResult data;

    public DlaResult getData() {
        return data;
    }

    public void setData(DlaResult data) {
        this.data = data;
    }
}
