package edu.upf.taln.welcome.slas.client;

import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;
import edu.upf.taln.welcome.slas.commons.input.InputMetadata;

/**
 *
 * @author rcarlini
 */
class ClientConfiguration implements IClientConfiguration {
    
    private String serviceURL;
    private String inputPath;
    private String outputPath;
    private boolean skipErrors = true;
    
    private AnalysisType analysisType;
    private String language;
    private OutputLevel outputType;
    
    private InputMetadata metadata;

    @Override
    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    @Override
    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    @Override
    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public boolean isSkipErrors() {
        return skipErrors;
    }

    public void setSkipErrors(boolean skipErrors) {
        this.skipErrors = skipErrors;
    }

    @Override
    public AnalysisType getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public OutputLevel getOutputType() {
        return outputType;
    }

    public void setOutputType(OutputLevel outputType) {
        this.outputType = outputType;
    }

    public InputMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(InputMetadata metadata) {
        this.metadata = metadata;
    }
}
