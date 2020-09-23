package edu.upf.taln.welcome.slas.commons.output;

import javax.validation.constraints.NotNull;

import edu.upf.taln.welcome.slas.commons.output.welcome.DlaResult;

/**
 *
 * @author rcarlini
 */
public class DeepAnalysisOutput implements IAnalysisOutput{
    @NotNull
    private DlaResult data;

    public DlaResult getData() {
        return data;
    }

    public void setData(DlaResult data) {
        this.data = data;
    }
}
