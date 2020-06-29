package edu.upf.taln.welcome.slas.io;

public class DeepAnalysisInput {
    private WelcomeMetadata metadata;
    private WelcomeData data;

    public WelcomeMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(WelcomeMetadata metadata) {
        this.metadata = metadata;
    }

    public WelcomeData getData() {
        return data;
    }

    public void setData(WelcomeData data) {
        this.data = data;
    }
}
