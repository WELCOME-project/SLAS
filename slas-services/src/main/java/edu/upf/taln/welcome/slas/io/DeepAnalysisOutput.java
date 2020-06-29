package edu.upf.taln.welcome.slas.io;

import javax.validation.constraints.NotNull;

/**
 *
 * @author rcarlini
 */
public class DeepAnalysisOutput {
    @NotNull
    private DeepAnalysisData data;

    public DeepAnalysisData getData() {
        return data;
    }

    public void setData(DeepAnalysisData data) {
        this.data = data;
    }
}
