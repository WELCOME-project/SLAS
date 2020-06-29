package edu.upf.taln.welcome.slas.commons.output;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class DeepAnalysisOutput {
    @NotNull
    private OutputData data;

    public OutputData getData() {
        return data;
    }

    public void setData(OutputData data) {
        this.data = data;
    }
}
