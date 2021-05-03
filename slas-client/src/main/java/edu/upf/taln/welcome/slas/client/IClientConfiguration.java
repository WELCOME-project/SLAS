package edu.upf.taln.welcome.slas.client;

import edu.upf.taln.welcome.slas.commons.input.OutputType;
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

    public OutputType getOutputType();

    public String getUseCase();
}
