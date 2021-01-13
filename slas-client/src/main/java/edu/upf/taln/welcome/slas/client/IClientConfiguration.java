package edu.upf.taln.welcome.slas.client;

import edu.upf.taln.welcome.slas.commons.factories.OutputFactory.OutputLevel;
import edu.upf.taln.welcome.slas.commons.input.AnalysisType;


/**
 *
 * @author rcarlini
 */
interface IClientConfiguration {

    public String getServiceURL();

    public String getInputPath();

    public String getOutputPath();

    public boolean isSkipErrors();

    public AnalysisType getAnalysisType();

    public String getLanguage();

    public OutputLevel getOutputType();
}
